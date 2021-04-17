package com.github.juliherms.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * This class responsible to mapping ids and fields from Login Page
 * @author jlv
 *
 */
public class LoginPage {
	
	@FindBy(id = "inputUsername")
	private WebElement inputUsername;

	@FindBy(id = "inputPassword")
	private WebElement inputPassword;

	@FindBy(id = "submit-button")
	private WebElement submitButton;

	@FindBy(id = "signup-link")
	private WebElement signupLink;

	@FindBy(id = "signup-success-msg")
	private WebElement signupSuccessMsg;

	public LoginPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	public void login(String username, String password) {
		inputUsername.sendKeys(username);
		inputPassword.sendKeys(password);
		submitButton.click();
	}

	public void clickSignupLink() {
		signupLink.click();
	}

	public String getSignupSuccessMessage() {
		return signupSuccessMsg.getAttribute("innerHTML");
	}
}