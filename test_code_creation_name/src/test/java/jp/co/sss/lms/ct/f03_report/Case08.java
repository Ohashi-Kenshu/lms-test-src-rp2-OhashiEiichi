package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.Constants;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {
	/*test07において、test03で選択した受講日と、
	 * test05での変更内容確認に使用
	 */
	private static String date = "";
	private static String report ="頑張った";
	
	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo("http://localhost:" + Constants.PORT + "/lms");
		assertEquals(Constants.CONTEXT_PATH + "/", webDriver.getCurrentUrl());
		getEvidence(new Object() {});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		loginWithTheUsed();
		assertEquals(Constants.CONTEXT_PATH + "/course/detail", webDriver.getCurrentUrl());
		getEvidence(new Object() {});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		WebElement submitted = webDriver.findElement(By.xpath(
				"//tr/td[position() = 3]"
				+ "/span[contains(text(),'提出済み')]"));
		//test07で、同日を特定するために使用
		date = submitted.findElement(By.xpath(
				"../../td[position() = 1]")).getText();
		WebElement detailButton = submitted.findElement(By.xpath(
				"../../td[contains(@class,'w20per')]"
						+ "/form/input[contains(@class,'btn btn-default')]"));
		detailButton.click();
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains("/section/detail"));
		assertEquals(Constants.CONTEXT_PATH + "/section/detail"
				,webDriver.getCurrentUrl());
		getEvidence(new Object() {});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		WebElement submitButton = webDriver.findElement(By.xpath(
				"//tr/td/form"
				+ "/input[position() = 6"
				+ "and contains(@value,'日報')]"));
		submitButton.click();
		assertEquals(Constants.CONTEXT_PATH + "/report/regist"
				,webDriver.getCurrentUrl());
		getEvidence(new Object() {});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		WebElement textarea = webDriver.findElement(By.xpath(
				"//div[contains(@class,'form-group')]"
				+ "/div[contains(@class,'col-lg-10')]"
				+ "/textarea"));
		textarea.clear();
		textarea.sendKeys(report);
		WebElement submit = webDriver.findElement(By.xpath(
				"//div[contains(@class,'form-group')]"
				+ "/div/button[contains(text(),'提出する')]"));
		submit.click();
		assertTrue(webDriver.getCurrentUrl().contains("/section/detail?"));
		getEvidence(new Object() {});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		WebElement welcomeUser = webDriver.findElement(By.partialLinkText("ようこそ"));
		welcomeUser.click();
		assertEquals(Constants.CONTEXT_PATH + "/user/detail"
				,webDriver.getCurrentUrl());
		getEvidence(new Object() {});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		WebElement detailButton = webDriver.findElement(By.xpath(
				"//td[contains(text(),'" + date  + "')]"
				+ "/../td/form/input[contains(@value,'詳細')]"));
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		detailButton.click();
		assertEquals(Constants.CONTEXT_PATH + "/report/detail"
				,webDriver.getCurrentUrl());
		WebElement reportBody = webDriver.findElement(By.xpath(
				"//h3[contains(text(),'報告レポート')]"
				+ "/../table/tbody/tr/td"));
		assertEquals(report, reportBody.getText());
		getEvidence(new Object() {});
	}

}
