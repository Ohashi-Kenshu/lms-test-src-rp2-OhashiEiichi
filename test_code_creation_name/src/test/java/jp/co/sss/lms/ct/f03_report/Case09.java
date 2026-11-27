package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

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

import jp.co.sss.lms.ct.util.Constants;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		WebElement welcomeUser = webDriver.findElement(By.partialLinkText("ようこそ"));
		welcomeUser.click();
		assertEquals(Constants.CONTEXT_PATH + "/user/detail"
				,webDriver.getCurrentUrl());
		getEvidence(new Object() {});	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		WebElement detailButton = webDriver.findElement(By.xpath(
				"//th[contains(text(),'レポート')]/../.."
				+"/tr[position() = 2]"
				+ "/td[position() = 5]/form[position() = 2]"
				+ "/input[contains(@value,'修正する')]"));
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		detailButton.click();
		assertEquals(Constants.CONTEXT_PATH + "/report/regist"
				,webDriver.getCurrentUrl());
		getEvidence(new Object() {});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		WebElement textarea = webDriver.findElement(By.id("intFieldName_0"));
		textarea.clear();
		WebElement select = webDriver.findElement(By.id("intFieldValue_0"));
		select.click();
		WebElement optionOne = webDriver.findElement(By.name("intFieldValue_$[i]"));
		optionOne.click();
		WebElement submit = webDriver.findElement(By.xpath(
				"//div[contains(@class,'form-group')]"
				+ "/div/button[contains(text(),'提出する')]"));
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		submit.click();
		assertTrue(webDriver.getCurrentUrl().contains("/report/complete"));
		getEvidence(new Object() {});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		WebElement textarea = webDriver.findElement(By.id("intFieldName_0"));
		textarea.clear();
		textarea.sendKeys("パソコン教室");
		WebElement select = webDriver.findElement(By.id("intFieldValue_0"));
		select.click();
		WebElement optionOne = webDriver.findElement(By.name("intFieldValue_$[i]"));
		WebElement optionZero = optionOne.findElement(By.xpath("../option[position()=1]"));
		optionZero.click();
		WebElement submit = webDriver.findElement(By.xpath(
				"//div[contains(@class,'form-group')]"
				+ "/div/button[contains(text(),'提出する')]"));
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		submit.click();
		assertTrue(webDriver.getCurrentUrl().contains("/report/complete"));
		getEvidence(new Object() {});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		WebElement textarea = webDriver.findElement(By.id("content_0"));
		textarea.clear();
		textarea.sendKeys("💮");
		WebElement submit = webDriver.findElement(By.xpath(
				"//div[contains(@class,'form-group')]"
				+ "/div/button[contains(text(),'提出する')]"));
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		submit.click();
		assertTrue(webDriver.getCurrentUrl().contains("/report/complete"));
		getEvidence(new Object() {});
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		WebElement textarea = webDriver.findElement(By.id("content_0"));
		textarea.clear();
		textarea.sendKeys("0");
		WebElement submit = webDriver.findElement(By.xpath(
				"//div[contains(@class,'form-group')]"
				+ "/div/button[contains(text(),'提出する')]"));
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		submit.click();
		assertTrue(webDriver.getCurrentUrl().contains("/report/complete"));
		getEvidence(new Object() {});
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		WebElement achieve = webDriver.findElement(By.id("content_0"));
		achieve.clear();
		WebElement opinion = webDriver.findElement(By.id("content_1"));
		opinion.clear();
		WebElement submit = webDriver.findElement(By.xpath(
				"//div[contains(@class,'form-group')]"
				+ "/div/button[contains(text(),'提出する')]"));
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		submit.click();
		assertTrue(webDriver.getCurrentUrl().contains("/report/complete"));
		getEvidence(new Object() {});
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		WebElement opinion = webDriver.findElement(By.id("content_1"));
		opinion.clear();
		opinion.sendKeys(Constants.CHAR_2001);
		WebElement reflection = webDriver.findElement(By.id("content_2"));
		reflection.clear();
		reflection.sendKeys(Constants.CHAR_2001);
		WebElement submit = webDriver.findElement(By.xpath(
				"//div[contains(@class,'form-group')]"
				+ "/div/button[contains(text(),'提出する')]"));
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		submit.click();
		assertTrue(webDriver.getCurrentUrl().contains("/report/complete"));
		getEvidence(new Object() {});
	}

}
