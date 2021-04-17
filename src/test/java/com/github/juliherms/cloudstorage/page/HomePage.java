package com.github.juliherms.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * This class responsible to mapping ids and fields from Home Page
 * @author jlv
 *
 */
public class HomePage {

	@FindBy(id = "logout-button")
	private WebElement logoutButton;

	@FindBy(id = "nav-files-tab")
	private WebElement filesTab;

	@FindBy(id = "nav-notes-tab")
	private WebElement notesTab;

	@FindBy(id = "nav-credentials-tab")
	private WebElement credentialsTab;

	public HomePage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	public void logout() {
		logoutButton.click();
	}

	public void navToNoteTab() {
		notesTab.click();
	}

	public void navToCredentialTab() {
		credentialsTab.click();
	}
}