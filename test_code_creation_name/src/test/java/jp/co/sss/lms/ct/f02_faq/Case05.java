package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

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
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		webDriver.findElement(By.linkText("機能")).click();
		webDriver.findElement(By.linkText("ヘルプ")).click();
		assertEquals(Constants.CONTEXT_PATH + "/help", webDriver.getCurrentUrl());
		getEvidence(new Object() {});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		webDriver.findElement(By.linkText("よくある質問")).click();
		Object[] windowHandles = webDriver.getWindowHandles().toArray();
		webDriver.switchTo().window((String) windowHandles[1]);
		assertEquals(Constants.CONTEXT_PATH + "/faq", webDriver.getCurrentUrl());
		getEvidence(new Object() {});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		//"東京ITスクール"をキーワードに検索
		WebElement keywordForm = webDriver.findElement(By.id("form"));
		String keyword = "東京ITスクール";
		keywordForm.clear();
		keywordForm.sendKeys(keyword);
		webDriver.findElement(By.xpath("//input[@type='submit' and @value='検索']")).click();
		/*URLが検索ワード受付を反映して変化し、
		 * フォームに入力内容が維持されていることを確認
		 */
		assertTrue(webDriver.getCurrentUrl().contains(Constants.CONTEXT_PATH + "/faq?keyword="));
		//自画面遷移後のため、Elementを再取得
		keywordForm = webDriver.findElement(By.id("form"));
		assertEquals(keyword, keywordForm.getAttribute("value"));
		String suffix = "form";
		getEvidence(new Object(){}, suffix);

		/* 1.検索結果がキーワードを含むことを確認
		 * 2.Q&Aをクリックして展開
		 * 3.撮影
		 */
		int resultCount = 0;
		int correctAnswerCount = 0;
		//検索結果、Q&Aは何個生成されたか？
		List<WebElement> results = new ArrayList<>();
		results = webDriver.findElements(By.xpath("//dl[contains(@id,'question-h')]"));
		resultCount = results.size();
		//その内、キーワードを含むA.は何個あるか？
		List<WebElement> answers = new ArrayList<>();
		answers = webDriver
				.findElements(By.xpath("//dd[contains(@id,'answer-h')]"
						+ "/span[contains(text(),'" + keyword + "')]"));
		correctAnswerCount = answers.size();
		//検索結果数 = キーワードを含むA.数なら機能OKと判断
		assertEquals(resultCount, correctAnswerCount);
		/*最下部にスクロール、A.を展開して撮影
		 * 展開する毎に、fotterが被ってクリック処理で例外となるため、
		 * 展開前後で都度最下部へ移動する
		 */
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		for (WebElement result : results) {
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			result.click();
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		}
		suffix = "Q&A";
		getEvidence(new Object(){}, suffix);
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		//test05で最下部に移動したので、最上部に戻る
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		js.executeScript("window.scrollTo(0, 0);");
		webDriver.findElement(By.xpath("//input[@type='button' and @value='クリア']")).click();
		assertEquals("", webDriver.findElement(By.id("form")).getAttribute("value"));
		getEvidence(new Object(){});
	}

}
