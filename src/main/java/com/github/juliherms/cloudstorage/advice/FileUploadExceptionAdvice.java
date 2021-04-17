package com.github.juliherms.cloudstorage.advice;

import java.util.Locale;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This class responsible to intercept exceptions for file upload
 * 
 * @author jlv
 *
 */
@ControllerAdvice
public class FileUploadExceptionAdvice {

	private final MessageSource messageSource;
	private final String ERROR_MESSAGE = "fileErrorMessage";

	public FileUploadExceptionAdvice(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Method responsible to check valid input max file
	 * 
	 * @param ra
	 * @return
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxSizeException(RedirectAttributes ra) {

		ra.addFlashAttribute(ERROR_MESSAGE,
				messageSource.getMessage("files-tab.file-upload-size-error-msg", null, Locale.getDefault()));
		return "redirect:/home#nav-files";
	}

	/**
	 * Method responsible to check valid input max file
	 * 
	 * @param ra
	 * @return
	 */
	@ExceptionHandler(SizeLimitExceededException.class)
	public String handleFileSizeException(RedirectAttributes ra) {
		ra.addFlashAttribute(ERROR_MESSAGE,
				messageSource.getMessage("files-tab.file-upload-size-error-msg", null, Locale.getDefault()));
		return "redirect:/home#nav-files";
	}

}
