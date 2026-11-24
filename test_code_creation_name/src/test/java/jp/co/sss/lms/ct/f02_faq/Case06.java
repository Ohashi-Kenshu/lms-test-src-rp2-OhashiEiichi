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
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
		//画面遷移を確認
		assertEquals(Constants.CONTEXT_PATH + "/course/detail", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		webDriver.findElement(By.linkText("機能")).click();
		webDriver.findElement(By.linkText("ヘルプ")).click();
		assertEquals(Constants.CONTEXT_PATH + "/help", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		webDriver.findElement(By.linkText("よくある質問")).click();
		Object[] windowHandles = webDriver.getWindowHandles().toArray();
		webDriver.switchTo().window((String) windowHandles[1]);
		assertEquals(Constants.CONTEXT_PATH + "/faq", webDriver.getCurrentUrl());
		getEvidence(new Object(){});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() throws Exception {
		int allResultsConut = 0;
		int searchedResultsCount = 0;
		
		/*まず、初期表示状態の結果リストと、結果数・Q.リストを記録
		 * また、参照が古いことによる例外回避のため、文字列リストで保存
		 */
		List<WebElement> allResults = new ArrayList<>();
		allResults = webDriver.findElements(
				By.xpath("//dl[contains(@id,'question-h')]"
						+ "/dt/span[position() = 2]"));
		List<String> allQuestions = new ArrayList<>();
		for(WebElement allResult : allResults) {
			allQuestions.add(allResult.getText());
		}

		//カテゴリ検索押下
		allResultsConut = allResults.size();
		webDriver.findElement(
				By.xpath("//a[contains(text(), '【人材開発支援助成金】')]"))
				.click();
		
		/*検索後の結果数が、初期表示の結果数以下であり、
		 * 全ての結果が、初期表示の結果のどれかと一致すればOK
		 */
		List<WebElement> searchedResults = new ArrayList<>();
		searchedResults = webDriver.findElements(
				By.xpath("//dl[contains(@id,'question-h')]"
						+ "/dt/span[position() = 2]"));
		searchedResultsCount = searchedResults.size();
		boolean matchExisting = false;
		for (WebElement searchedResult : searchedResults) {
			boolean isExisting = false;
			for (String allQuestion : allQuestions) {
				if(allQuestion.equals(searchedResult.getText())) {
					isExisting = true;
					break;
				} else {
					continue;
				}
			}
			if(isExisting) {
				matchExisting = true;
				continue;
			} else {
				matchExisting = false;
				break;
			}
		}
		assertTrue(matchExisting);
		assertTrue(allResultsConut >= searchedResultsCount);
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		getEvidence(new Object(){});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		int resultsCount = 0;
		int hiddenAnswersCount = 0;
		int displayedAnswersCount = 0;
		
		/*検索後の初期表示で、結果の数＝未展開A.の数であり、
		 * 展開済A.＝0であることを確認
		 */
		List<WebElement> results = new ArrayList<>();
		results = webDriver.findElements(
				By.xpath("//dl[contains(@id,'question-h')]"));
		resultsCount = results.size();
		List<WebElement> hiddenAnswers = new ArrayList<>();
		hiddenAnswers = webDriver.findElements(
				By.xpath("//dd[contains(@id,'answer-h') "
						+ "and contains(@class,'dn')]"));
		hiddenAnswersCount = hiddenAnswers.size();
		List<WebElement> displayedAnswers = new ArrayList<>();
		displayedAnswers = webDriver.findElements(
				By.xpath("//dd[contains(@id,'answer-h') "
						+ "and contains(@class,'fs18')]"));
		displayedAnswersCount = displayedAnswers.size();
		assertEquals(0, displayedAnswersCount);
		assertEquals(resultsCount, hiddenAnswersCount);
		
		//Javascriptにより、各A.を展開
		JavascriptExecutor js = (JavascriptExecutor)webDriver;
		for (WebElement result : results) {
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			result.click();
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		}
		
		//結果の数＝展開済A.の数であり、未展開A.数=0を確認
		displayedAnswers = webDriver.findElements(
				By.xpath("//dd[contains(@id,'answer-h') "
						+ "and contains(@class,'fs18')]"));
		displayedAnswersCount = displayedAnswers.size();
		hiddenAnswers = webDriver.findElements(
				By.xpath("//dd[contains(@id,'answer-h') "
						+ "and contains(@class,'dn')]"));
		hiddenAnswersCount = hiddenAnswers.size();
		assertEquals(0, hiddenAnswersCount);
		assertEquals(resultsCount, displayedAnswersCount);
		getEvidence(new Object(){});
	}

}
