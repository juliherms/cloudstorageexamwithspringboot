package com.github.juliherms.cloudstorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

		driver.get("http://localhost:" + port + "/home");
		WebDriverWait wait = new WebDriverWait(driver, 2000);
		wait.until(driver -> driver.findElement(By.id("logout")));

		homePage.navToNoteTab();

		wait.until(driver -> driver.findElement(By.id("new-note")));

		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor)driver).executeScript("showNoteModal();");
		}

		createNote();

		driver.get("http://localhost:" + port + "/home");
		wait.until(driver -> driver.findElement(By.id("logout")));

		homePage.navToNoteTab();

	}

	@Test
	@Order(4)
	public void testEditNoteSuccess() {
		driver.get(baseURL + "/login");
		loginPage.login("admin", "admin");
		homePage.navToNoteTab();

		notePage.clickEditNoteButton();
		String editedTitle = "Testing";
		String editedDescription = "Description teste one, "
				+ "Description test two";
		notePage.updateNote(editedTitle, editedDescription);
		String editedNoteTitlePrint = notePage.getNoteTitle();
		String editedNoteDescriptionPrint = notePage.getNoteDescription();
		String noteUpdateSuccessMsg = notePage.getNoteSuccessMsg();
		assertEquals("Note was updated.", noteUpdateSuccessMsg);
		assertEquals(editedTitle, editedNoteTitlePrint);
		assertEquals(editedDescription, editedNoteDescriptionPrint);
	}

	@Test
	@Order(5)
	public void testDeleteNoteSuccess() {
		driver.get(baseURL + "/login");
		loginPage.login("admin", "admin");
		homePage.navToNoteTab();
		notePage.clickDeleteNoteButton();
		String noNotes = notePage.getNoNotesMessage();
		String noteDeleteSuccessMsg = notePage.getNoteSuccessMsg();
		assertEquals("Note was deleted.", noteDeleteSuccessMsg);
		assertEquals("There is no notes currently, please add some...", noNotes);
	}

	@Test
	@Order(6)
	public void testAddCredentialSuccess() {
		driver.get(baseURL + "/login");
		loginPage.login("admin", "admin");
		homePage.navToCredentialTab();
		String url = "www.google.com";
		String username = "userAdmin";
		String password = "P0wsrd01";
		credentialPage.clickAddCredentialButton();
		credentialPage.submitCredential(url, username, password);
		String urlPrint = credentialPage.getUrlPrint();
		String usernamePrint = credentialPage.getUsernamePrint();
		String passwordPrint = credentialPage.getPasswordPrint();
		String credentialSaveSuccessMsg = credentialPage.getCredentialSuccessMsg();
		assertEquals(url, urlPrint);
		assertEquals(username, usernamePrint);
		assertNotEquals(password, passwordPrint);
		assertEquals("Credential was saved.", credentialSaveSuccessMsg);
	}

	@Test
	@Order(7)
	public void testEditCredentialSucess() {
		driver.get(baseURL + "/login");
		loginPage.login("admin", "admin");
		homePage.navToCredentialTab();
		String password = "1234";
		credentialPage.clickEditButton();
		String passwordInputValue = credentialPage.getPasswordInput();
		assertEquals(password, passwordInputValue);
		String updatedUrl = "www.pudim.com";
		String updatedUsername = "vasconcelos@gmail.com";
		String updatedPassword = "test";
		credentialPage.updateCredential(updatedUrl, updatedUsername, updatedPassword);
		String urlPrint = credentialPage.getUrlPrint();
		String usernamePrint = credentialPage.getUsernamePrint();
		String passwordPrint = credentialPage.getPasswordPrint();
		String credentialUpdateSuccessMsg = credentialPage.getCredentialSuccessMsg();
		assertEquals(updatedUrl, urlPrint);
		assertEquals(updatedUsername, usernamePrint);
		assertNotEquals(updatedPassword, passwordPrint);
		assertEquals("Credential was updated.", credentialUpdateSuccessMsg);
	}

	@Test
	@Order(8)
	public void testDeleteCredentialSuccess() {
		driver.get(baseURL + "/login");
		loginPage.login("admin", "admin");
		homePage.navToCredentialTab();
		credentialPage.clickDeleteButton();
		String noCredentialsMsg = credentialPage.getNoCredentialsMsg();
		String deleteSuccessMsg = credentialPage.getCredentialSuccessMsg();
		assertEquals("There is no credentials currently, please add some...", noCredentialsMsg);
		assertEquals("Credential was deleted.", deleteSuccessMsg);
		homePage.logout();
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

	private void createNote() {
		notePage = new NotePage(driver);
		notePage.submitNote("Note teste 1","Note description test");
	}
}
