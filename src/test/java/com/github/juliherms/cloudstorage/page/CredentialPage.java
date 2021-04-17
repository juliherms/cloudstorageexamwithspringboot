package com.github.juliherms.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialPage {

	@FindBy(id = "add-credential-button")
	private WebElement addCredentialButton;

	@FindBy(id = "credential-url")
	private WebElement urlInput;

	@FindBy(id = "credential-username")
	private WebElement usernameInput;

	@FindBy(id = "credential-password")
	private WebElement passwordInput;

	@FindBy(id = "credentialSubmit")
	private WebElement submitCredentialButton;

	@FindBy(className = "url-print")
	private WebElement urlPrint;

	@FindBy(className = "username-print")
	private WebElement usernamePrint;

	@FindBy(className = "password-print")
	private WebElement passwordPrint;

	@FindBy(className = "credential-edit-button")
	private WebElement editButton;

	@FindBy(className = "credential-delete-button")
	private WebElement deleteButton;

	@FindBy(id = "no-credentials")
	private WebElement noCredentialsMsg;

	@FindBy(id = "credential-success-msg")
	private WebElement credentialSuccessMsg;

	public CredentialPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	public void clickAddCredentialButton() {
		addCredentialButton.click();
	}

	public void submitCredential(String url, String username, String password) {
		urlInput.sendKeys(url);
		usernameInput.sendKeys(username);
		passwordInput.sendKeys((password));
		submitCredentialButton.submit();
	}

	public void updateCredential(String url, String username, String password) {
		urlInput.clear();
		usernameInput.clear();
		passwordInput.clear();
		urlInput.sendKeys(url);
		usernameInput.sendKeys(username);
		passwordInput.sendKeys((password));
		submitCredentialButton.submit();
	}

	public String getUrlPrint() {
		return urlPrint.getText();
	}

	public String getUsernamePrint() {
		return usernamePrint.getText();
	}

	public String getPasswordPrint() {
		return passwordPrint.getText();
	}

	public void clickEditButton() {
		this.editButton.click();
	}

	public void clickDeleteButton() {
		this.deleteButton.click();
	}

	public String getPasswordInput() {
		return passwordInput.getAttribute("value");
	}

	public String getCredentialSuccessMsg() {
		return credentialSuccessMsg.getText();
	}

	public String getNoCredentialsMsg() {
		return noCredentialsMsg.getText();
	}
}