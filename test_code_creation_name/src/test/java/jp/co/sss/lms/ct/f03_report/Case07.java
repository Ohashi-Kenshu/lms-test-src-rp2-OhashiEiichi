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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.Constants;

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {
	//Case04と05の比較のため、日付を保持
	private String buttonValueBefore = "";

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
		//画面遷移を確認
		assertEquals(Constants.CONTEXT_PATH + "/course/detail", webDriver.getCurrentUrl());
		getEvidence(new Object() {});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		//未提出のtrを検知、親要素にさかのぼって同行の日付と詳細ボタンを取得
		WebElement unSubmit = webDriver.findElement(By.xpath(
				"//tr/td[position() = 3]"
				+ "/span[contains(text(),'未提出')]"));
		WebElement detailButton = unSubmit.findElement(By.xpath(
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
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		WebElement submitButton = webDriver.findElement(By.xpath(
				"//tr/td/form"
				+ "/input[position() = 5"
				+ "and contains(@value,'日報')]"));
		buttonValueBefore = submitButton.getAttribute("value");
		submitButton.click();
		assertEquals(Constants.CONTEXT_PATH + "/report/regist"
				,webDriver.getCurrentUrl());
		getEvidence(new Object() {});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		WebElement textarea = webDriver.findElement(By.xpath(
				"//div[contains(@class,'form-group')]"
				+ "/div[contains(@class,'col-lg-10')]"
				+ "/textarea"));
		textarea.sendKeys("頑張った");
		WebElement submit = webDriver.findElement(By.xpath(
				"//div[contains(@class,'form-group')]"
				+ "/div/button[contains(text(),'提出する')]"));
		submit.click();
		//提出後はクエリパラメータが追加されるため、末尾の含有のみで判定
		assertTrue(webDriver.getCurrentUrl().contains("/section/detail?"));
		WebElement submitButtonAfter = webDriver.findElement(By.xpath(
				"//tr/td/form"
				+ "/input[position() = 6"
				+ "and contains(@value,'日報')]"));
		assertNotEquals(buttonValueBefore, submitButtonAfter.getAttribute("value"));
		getEvidence(new Object() {});
	}

}
