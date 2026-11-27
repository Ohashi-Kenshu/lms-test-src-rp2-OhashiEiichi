package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.Constants;

/**
 * 結合テスト 勤怠管理機能
 * ケース10
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
public class Case10 {

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
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		loginWithTheUsed();
		assertEquals(Constants.CONTEXT_PATH + "/course/detail", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		WebElement attendance = webDriver.findElement(By.id("nav-content"));
		attendance.click();
		pageLoadTimeout(5);
	    Alert alert = webDriver.switchTo().alert();
	    alert.accept();
		assertEquals(Constants.CONTEXT_PATH + "/attendance/detail", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() {
		WebElement punchIn = webDriver.findElement(By.xpath("//input[contains(@name, 'punchIn')]"));
		punchIn.click();
		pageLoadTimeout(5);
		Alert alert = webDriver.switchTo().alert();
	    alert.accept();
	    //エラーメッセージが出現していればOK
	    WebElement errorMsg = webDriver.findElement(By.xpath("//div[contains(@id,'main')]/ul"));
	    assertEquals( 
	    		"* 研修のない日に出退勤を記録することはできません。直接編集してください。",
	    		errorMsg.getText());
		assertEquals(Constants.CONTEXT_PATH + "/attendance/detail", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() {
		WebElement punchOut = webDriver.findElement(By.xpath("//input[contains(@name, 'punchOut')]"));
		punchOut.click();
		pageLoadTimeout(5);
		Alert alert = webDriver.switchTo().alert();
	    alert.accept();
	    WebElement errorMsg = webDriver.findElement(By.xpath("//div[contains(@id,'main')]/ul"));
	    assertEquals(
	    		"* 研修のない日に出退勤を記録することはできません。直接編集してください。",
	    		errorMsg.getText());
		assertEquals(Constants.CONTEXT_PATH + "/attendance/detail", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

}
