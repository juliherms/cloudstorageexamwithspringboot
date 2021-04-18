package com.github.juliherms.cloudstorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.github.juliherms.cloudstorage.model.Note;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.github.juliherms.cloudstorage.page.CredentialPage;
import com.github.juliherms.cloudstorage.page.HomePage;
import com.github.juliherms.cloudstorage.page.LoginPage;
import com.github.juliherms.cloudstorage.page.NotePage;
import com.github.juliherms.cloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * This class responsible to set initial setup to test case
 * 
 * @author jlv
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudstorageApplicationTests {

	@LocalServerPort
	private int port;

	public String baseURL;

	private WebDriver driver;
	private HomePage homePage;
	private NotePage notePage;
	private LoginPage loginPage;
	private CredentialPage credentialPage;
	private SignupPage signupPage;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		baseURL = baseURL = "http://localhost:" + port;
		this.homePage = new HomePage(driver);
		this.notePage = new NotePage(driver);
		this.loginPage = new LoginPage(driver);
		this.credentialPage = new CredentialPage(driver);
		this.signupPage = new SignupPage(driver);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	/**
	 * This method responsible to test access page home without login
	 */
	@Test
	@Order(1)
	public void testUnauthorizedUserSuccess() {

		//step 1 - access direct home without login
		driver.get(baseURL + "/home");
		assertEquals("Login", driver.getTitle());

		//step 2 - access default web page without login
		driver.get(baseURL);
		assertEquals("Login", driver.getTitle());

		//step 3 - access login web page
		driver.get(baseURL + "/login");
		assertEquals("Login", driver.getTitle());
	}

	/**
	 * This method responsible to test a login with a new created user
	 */
	@Test
	@Order(2)
	public void testAuthorizedUserSuccess() {

		//step 1 - create a new user
		createUser();

		//step 2 - login with a new user
		loginUser();

		//step 3 - user was redirect to page home
		assertEquals("Home", driver.getTitle());
	}

	/**
	 * This method responsible to test add note
	 */
	@Test
	@Order(3)
	public void testAddNoteSuccess() {

		//step 1 - create and login user - pre req
		createUser();
		loginUser();

		//step 2 - navigate to notes tab
		driver.get(baseURL + "/home");
		WebDriverWait wait = new WebDriverWait(driver, 2000);
		wait.until(driver -> driver.findElement(By.id("logout")));

		//step 3 - navigate to create a note
		homePage.navToNoteTab();

		wait.until(driver -> driver.findElement(By.id("add-note-button")));

		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor)driver).executeScript("showNoteModal();");
		}

		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).click();
		createNote();

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("logout")));

		//step 4 - check note was saved
		homePage.navToNoteTab();

		WebDriverWait wait2 = new WebDriverWait(driver, 2000);
		wait2.until(ExpectedConditions.elementToBeClickable(By.id("add-note-button")));

		String noteTitle = notePage.getNoteTitle();

		//Step 5 - check return
		assertEquals("Note teste 1", notePage.getNoteTitle());
		assertEquals("Note description test", notePage.getNoteDescription());

	}

	@Test
	@Order(4)
	public void testEditNoteSuccess() {

		//step 1 - create and login user - pre req
		createUser();
		loginUser();

		//step 2 - navigate to notes tab
		driver.get(baseURL + "/home");
		WebDriverWait wait = new WebDriverWait(driver, 2000);
		wait.until(driver -> driver.findElement(By.id("logout")));

		//step 3 - navigate to create a note
		homePage.navToNoteTab();

		wait.until(driver -> driver.findElement(By.id("add-note-button")));

		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor)driver).executeScript("showNoteModal();");
		}

		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).click();
		createNote();

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("logout")));

		//step 4 - check note was saved
		homePage.navToNoteTab();

		WebDriverWait wait2 = new WebDriverWait(driver, 2000);
		wait2.until(ExpectedConditions.elementToBeClickable(By.id("add-note-button")));

		String noteTitle = notePage.getNoteTitle();

		//Step 5 - check return
		assertEquals("Note teste 1", notePage.getNoteTitle());
		assertEquals("Note description test", notePage.getNoteDescription());

		//Step 6 - edit note
		notePage.clickEditNoteButton();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).click();
		editNote("Note test 2", "Note description test 2");

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("logout")));

		homePage.navToNoteTab();



		WebDriverWait wait3 = new WebDriverWait(driver, 2000);
		wait2.until(ExpectedConditions.elementToBeClickable(By.id("add-note-button")));


		String a = notePage.getNoteTitle();

/*
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).click();
		notePage.updateNote("Note teste 2","Note description test 2");

		WebDriverWait wait3 = new WebDriverWait(driver, 2000);
		wait3.until(driver -> driver.findElement(By.id("logout")));



		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("logout")));

		homePage.navToNoteTab();

		WebDriverWait wait4 = new WebDriverWait(driver, 2000);
		wait4.until(ExpectedConditions.elementToBeClickable(By.id("add-note-button")));

		assertEquals("Note teste 2", notePage.getNoteTitle());
		assertEquals("Note description test 2", notePage.getNoteDescription());
*/

	}

	@Test
	@Order(5)
	public void testDeleteNoteSuccess() {

		//step 1 - create and login user - pre req
		createUser();
		loginUser();

		//step 2 - navigate to notes tab
		driver.get(baseURL + "/home");
		WebDriverWait wait = new WebDriverWait(driver, 2000);
		wait.until(driver -> driver.findElement(By.id("logout")));

		//step 3 - navigate to create a note
		homePage.navToNoteTab();
		wait.until(driver -> driver.findElement(By.id("add-note-button")));

		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor)driver).executeScript("showNoteModal();");
		}

		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).click();
		createNote();

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("logout")));

		//step 4 - check note was saved
		homePage.navToNoteTab();

		WebDriverWait wait2 = new WebDriverWait(driver, 2000);
		wait2.until(ExpectedConditions.elementToBeClickable(By.id("add-note-button")));

		String noteTitle = notePage.getNoteTitle();

		//Step 5 - check return
		assertEquals("Note teste 1", notePage.getNoteTitle());
		assertEquals("Note description test", notePage.getNoteDescription());

		//Step 6 - navigate to notes tab and delete note
		homePage.navToNoteTab();
		notePage.clickDeleteNoteButton();
		String noNotes = notePage.getNoNotesMessage();
		String noteDeleteSuccessMsg = notePage.getNoteSuccessMsg();
		assertEquals("Note was deleted.", noteDeleteSuccessMsg);
		assertEquals("Notes is Empty", noNotes);
	}

	@Test
	@Order(6)
	public void testAddCredentialSuccess() {

		//step 1 - create and login user - pre req
		createUser();
		loginUser();

		//step 2 - navigate to credentials tab
		driver.get(baseURL + "/home");
		WebDriverWait wait = new WebDriverWait(driver, 2000);
		wait.until(driver -> driver.findElement(By.id("logout")));
		homePage.navToCredentialTab();
		wait.until(driver -> driver.findElement(By.id("add-credential-button")));

		//step 3 - create credential
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor)driver).executeScript("showCredentialModal();");
		}

		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).click();
		createCredential();

		String credentialSaveSuccessMsg = credentialPage.getCredentialSuccessMsg();

		//step 4 - check message success
		assertEquals("Credential was saved.", credentialSaveSuccessMsg);


	}

	@Test
	@Order(7)
	public void testEditCredentialSucess() {

		//step 1 - create and login user - pre req
		createUser();
		loginUser();

		//step 2 - navigate to credentials tab
		driver.get(baseURL + "/home");
		WebDriverWait wait = new WebDriverWait(driver, 2000);
		wait.until(driver -> driver.findElement(By.id("logout")));
		homePage.navToCredentialTab();
		wait.until(driver -> driver.findElement(By.id("add-credential-button")));

		//step 3 - create credential
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor)driver).executeScript("showCredentialModal();");
		}

		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).click();
		createCredential();

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("logout")));

		//step 4 - check note was saved
		homePage.navToCredentialTab();

		WebDriverWait wait2 = new WebDriverWait(driver, 2000);
		wait2.until(ExpectedConditions.elementToBeClickable(By.id("add-credential-button")));

	}

	@Test
	@Order(8)
	public void testDeleteCredentialSuccess() {


		//step 1 - create and login user - pre req
		createUser();
		loginUser();

		//step 2 - navigate to credentials tab
		driver.get(baseURL + "/home");
		WebDriverWait wait = new WebDriverWait(driver, 2000);
		wait.until(driver -> driver.findElement(By.id("logout")));
		homePage.navToCredentialTab();
		wait.until(driver -> driver.findElement(By.id("add-credential-button")));

		//step 3 - create credential
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor)driver).executeScript("showCredentialModal();");
		}

		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).click();
		createCredential();

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("logout")));

		//step 4 - check credential was saved
		homePage.navToCredentialTab();
		WebDriverWait wait2 = new WebDriverWait(driver, 2000);
		wait2.until(ExpectedConditions.elementToBeClickable(By.id("add-credential-button")));

		//Step 5 - navigate to credentials tab and delete credential
		credentialPage.clickDeleteButton();
		String noCredentials = credentialPage.getNoCredentialsMsg();
		String credentialDeleteSuccessMsg = credentialPage.getCredentialSuccessMsg();
		assertEquals("Credentials is Empty", noCredentials);
	}

	private void createUser() {
		driver.get(baseURL + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.signup("fred","vasconcelos","fred","fred");
	}

	private void loginUser() {
		driver.get(baseURL + "/login");
		loginPage = new LoginPage(driver);
		loginPage.login("fred","fred");
	}

	private void createCredential(){
		credentialPage = new CredentialPage(driver);
		credentialPage.submitCredential("www.google.com","fred","aoogeire");
	}

	private void createNote() {
		notePage = new NotePage(driver);
		notePage.submitNote("Note teste 1","Note description test");
	}

	private void editNote(String title, String description) {
		notePage = new NotePage(driver);
		notePage.clearNote();
		notePage.submitNote(title,description);
	}
}
