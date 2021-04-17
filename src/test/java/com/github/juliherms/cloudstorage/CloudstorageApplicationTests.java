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

	private WebDriver driver;
	private HomePage homePage;
	private NotePage notePage;
	private LoginPage loginPage;
	private CredentialPage credentialPage;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		this.homePage = new HomePage(driver);
		this.notePage = new NotePage(driver);
		this.loginPage = new LoginPage(driver);
		this.credentialPage = new CredentialPage(driver);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	/**
	 * Tests that "/" and "/home" urls redirects to "/login" page and unauthorized
	 * user can only access "/login" and "/signup" pages
	 */
	@Test
	@Order(1)
	public void unauthorizedUserTest() {
		driver.get("http://localhost:" + this.port + "/home");
		assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port);
		assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/signup");
		assertEquals("Sign Up", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());
	}

	/**
	 * Tests user signup, login, if "/home" page access is available after login,
	 * logout and if "/home" page access is restricted after logout
	 */

	@Test
	@Order(2)
	public void authorizedUserTest() {
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = new HomePage(driver);
		// signup new user
		driver.get("http://localhost:" + this.port);
		loginPage.clickSignupLink();
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("fred", "vasconcelos", "fred", "fred");
		String signupSuccessMessage = loginPage.getSignupSuccessMessage();
		assertEquals("Login", driver.getTitle());
		assertEquals("You successfully signed up!", signupSuccessMessage);

		// login and verify that the home page is accessible
		loginPage.login("fred", "fred");
		assertEquals("Home", driver.getTitle());

		// sign out returns to Login page
		homePage.logout();
		assertEquals("Login", driver.getTitle());

		// verify that the home page is no longer accessible
		driver.get("http://localhost:" + this.port + "/home");
		assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port);
		assertEquals("Login", driver.getTitle());
	}

	/**
	 * Note CRUD test A default user was created with the help of DataLoader.java
	 * class
	 */

	@Test
	@Order(3)
	public void addNoteTest() {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage.login("admin", "admin");
		homePage.navToNoteTab();
		String title = "note title";
		String description = "note description";
		notePage.clickAddNoteButton();
		notePage.submitNote(title, description);
		String noteTitlePrint = notePage.getNoteTitle();
		String noteDescriptionPrint = notePage.getNoteDescription();
		String noteSaveSuccessMsg = notePage.getNoteSuccessMsg();
		assertEquals("Note was saved.", noteSaveSuccessMsg);
		assertEquals(title, noteTitlePrint);
		assertEquals(description, noteDescriptionPrint);
	}

	@Test
	@Order(4)
	public void editNoteTest() {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage.login("admin", "admin");
		homePage.navToNoteTab();

		notePage.clickEditNoteButton();
		String editedTitle = "Testing";
		String editedDescription = "Your tech lead trusts you to do a good job, "
				+ "but testing is important whether you're an excel number-cruncher or a full-stack coding superstar!";
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
	public void deleteNoteTest() {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage.login("admin", "admin");
		homePage.navToNoteTab();
		notePage.clickDeleteNoteButton();
		String noNotes = notePage.getNoNotesMessage();
		String noteDeleteSuccessMsg = notePage.getNoteSuccessMsg();
		assertEquals("Note was deleted.", noteDeleteSuccessMsg);
		assertEquals("There is no notes currently, please add some...", noNotes);
	}

	/**
	 * Credential CRUD test A default user was created with the help of
	 * DataLoader.java class
	 */

	@Test
	@Order(6)
	public void addCredentialTest() {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage.login("admin", "admin");
		homePage.navToCredentialTab();
		String url = "https://classroom.udacity.com";
		String username = "islamgaliev@mail.ru";
		String password = "WhyAlwaysMe202104";
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
	public void editCredentialTest() {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage.login("admin", "admin");
		homePage.navToCredentialTab();
		String password = "WhyAlwaysMe202104";
		credentialPage.clickEditButton();
		String passwordInputValue = credentialPage.getPasswordInput();
		assertEquals(password, passwordInputValue);
		String updatedUrl = "https://classroom.udacity.com/me";
		String updatedUsername = "islamgaliev@gmail.com";
		String updatedPassword = "CallMeBaby202005";
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
	public void deleteCredentialTest() {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage.login("admin", "admin");
		homePage.navToCredentialTab();
		credentialPage.clickDeleteButton();
		String noCredentialsMsg = credentialPage.getNoCredentialsMsg();
		String deleteSuccessMsg = credentialPage.getCredentialSuccessMsg();
		assertEquals("There is no credentials currently, please add some...", noCredentialsMsg);
		assertEquals("Credential was deleted.", deleteSuccessMsg);
		homePage.logout();
	}
}
