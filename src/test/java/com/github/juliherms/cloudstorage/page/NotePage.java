package com.github.juliherms.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotePage {

	@FindBy(id = "add-note-button")
	private WebElement addNoteButton;

	@FindBy(id = "note-title")
	private WebElement noteTitleInput;

	@FindBy(id = "note-description")
	private WebElement noteDescriptionInput;

	@FindBy(id = "noteSubmit")
	private WebElement noteSubmitButton;

	@FindBy(className = "note-title-print")
	private WebElement noteTitlePrint;

	@FindBy(className = "note-description-print")
	private WebElement noteDescriptionPrint;

	@FindBy(className = "note-edit-button")
	private WebElement noteEditButton;

	@FindBy(className = "note-delete-button")
	private WebElement noteDeleteButton;

	@FindBy(id = "no-notes")
	private WebElement noNotes;

	@FindBy(id = "note-success-msg")
	private WebElement noteSuccessMsg;

	public NotePage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	public void submitNote(String title, String description) {
		noteTitleInput.sendKeys(title);
		noteDescriptionInput.sendKeys(description);
		noteSubmitButton.submit();
	}

	public void updateNote(String title, String description) {
		noteTitleInput.clear();
		noteDescriptionInput.clear();
		noteTitleInput.sendKeys(title);
		noteDescriptionInput.sendKeys(description);
		noteSubmitButton.submit();
	}

	public String getNoteTitle() {
		return noteTitlePrint.getText();
	}

	public String getNoteDescription() {
		return noteDescriptionPrint.getText();
	}

	public void clickAddNoteButton() {
		addNoteButton.click();
	}

	public void clickEditNoteButton() {
		noteEditButton.click();
	}

	public void clickDeleteNoteButton() {
		noteDeleteButton.click();
	}

	public String getNoNotesMessage() {
		return noNotes.getText();
	}

	public String getNoteSuccessMsg() {
		return noteSuccessMsg.getAttribute("innerHTML");
	}
}
