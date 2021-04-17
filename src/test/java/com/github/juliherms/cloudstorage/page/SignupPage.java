package com.github.juliherms.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * This class responsible to mapping ids and fields from Signup Page
 * @author jlv
 *
 */
public class SignupPage {

	@FindBy(id = "login-link")
	private WebElement loginLink;

	@FindBy(id = "backToLoginPageLink")
	private WebElement backToLoginPageLink;

	@FindBy(id = "success-msg")
	private WebElement successMsg;

	@FindBy(id = "error-msg")
	private WebElement errorMsg;

	@FindBy(id = "inputFirstName")
	private WebElement inputFirstName;

	@FindBy(id = "inputLastName")
	private WebElement inputLastName;

	@FindBy(id = "inputUsername")
	private WebElement inputUsername;

	@FindBy(id = "inputPassword")
	private WebElement inputPassword;

	@FindBy(id = "submit-button")
	private WebElement submitButton;

	public SignupPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	public void signup(String firstname, String lastname, String username, String password) {
		inputFirstName.sendKeys(firstname);
		inputLastName.sendKeys(lastname);
		inputUsername.sendKeys(username);
		inputPassword.sendKeys(password);
		submitButton.click();
	}

	public String getSuccessMessage() {
		return successMsg.getText();
	}

	public void clickLoginLink() {
		loginLink.click();
	}
}
