package com.github.juliherms.cloudstorage.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.juliherms.cloudstorage.model.File;
import com.github.juliherms.cloudstorage.model.User;
import com.github.juliherms.cloudstorage.service.FileService;
import com.github.juliherms.cloudstorage.service.UserService;

/**
 * This class responsible to code behind for files page
 * 
 * @author jlv
 *
 */
@Controller
@RequestMapping("/files")
public class FileController {

	private final FileService fileService;
	private final UserService userService;
	private final String ERROR_MESSAGE = "fileErrorMessage";
	private final String SUCCESS_MESSAGE = "fileSuccessMessage";

	private final Logger logger = LoggerFactory.getLogger(FileController.class);
	private final MessageSource messageSource;

	public FileController(FileService fileService, UserService userService, MessageSource messageSource) {
		this.fileService = fileService;
		this.userService = userService;
		this.messageSource = messageSource;
	}

	/**
	 * Method responsible to save and upload file
	 * 
	 * @param fileUpload
	 * @param authentication
	 * @param ra
	 * @return
	 */
	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication,
			RedirectAttributes ra) {

		User user = getUserAuthenticated(authentication.getName());

		Integer userId = user.getUserId();
		String errorMessage;
		String successMessage;

		if (!fileUpload.isEmpty()) {

			// check exists file
			String filename = StringUtils.cleanPath(Objects.requireNonNull(fileUpload.getOriginalFilename()));
			Long filesize = fileUpload.getSize();

			if (fileService.fileExists(filename, userId, filesize)) {
				errorMessage = messageSource.getMessage("files-tab.file-already-exist-msg", null, Locale.getDefault());
				ra.addFlashAttribute(ERROR_MESSAGE, errorMessage);
				logger.error("Files error: " + errorMessage + " userId = " + userId);
				return "redirect:/home#nav-files";
			}

			File uploadFile;
			int fileId = -1;

			try {

				uploadFile = new File(null, filename, fileUpload.getContentType(), filesize, user.getUserId(),
						fileUpload.getBytes());
				fileId = fileService.upload(uploadFile);

			} catch (IOException e) {

				logger.error(e.getMessage());

			}

			if (fileId == -1) {
				errorMessage = messageSource.getMessage("files-tab.file-upload-error-msg", null, Locale.getDefault());
				ra.addFlashAttribute(ERROR_MESSAGE, errorMessage);
				logger.error("Files error: " + errorMessage + " userId = " + userId);

			} else {

				successMessage = messageSource.getMessage("files-tab.file-upload-success-msg", null,
						Locale.getDefault());
				ra.addFlashAttribute(SUCCESS_MESSAGE, successMessage);
				ra.addFlashAttribute("files", fileService.getFilesByUserId(user.getUserId()));
			}

		} else {
			errorMessage = messageSource.getMessage("files-tab.file-is-empty-msg", null, Locale.getDefault());
			ra.addFlashAttribute(ERROR_MESSAGE, errorMessage);
			logger.error("Files error: " + errorMessage + " userId = " + userId);
		}
		return "redirect:/home#nav-files";
	}

	/**
	 * Method responsible to delete file
	 * 
	 * @param id
	 * @param authentication
	 * @param ra
	 * @return
	 */
	@GetMapping("/delete/{id}")
	public String deleteFile(@PathVariable("id") Integer id, Authentication authentication, RedirectAttributes ra) {

		User user = getUserAuthenticated(authentication.getName());
		Integer userId = user.getUserId();

		int deleted = fileService.delete(id, userId);

		if (deleted == 0) {

			logger.error("File with id = " + id + " was not deleted");
			ra.addFlashAttribute(ERROR_MESSAGE,
					messageSource.getMessage("files-tab.file-delete-error-msg", null, Locale.getDefault()));

		} else {

			ra.addFlashAttribute(SUCCESS_MESSAGE,
					messageSource.getMessage("files-tab.file-delete-success-msg", null, Locale.getDefault()));

		}

		ra.addFlashAttribute("files", fileService.getFilesByUserId(userId));

		return "redirect:/home#nav-files";
	}

	/**
	 * Method responsible to get file by id
	 * 
	 * @param id
	 * @param authentication
	 * @param response
	 * @param ra
	 */
	@GetMapping("/download/{id}")
	public void getFile(@PathVariable("id") Integer id, Authentication authentication, HttpServletResponse response,
			RedirectAttributes ra) {

		User user = getUserAuthenticated(authentication.getName());

		Integer userId = user.getUserId();
		File file = fileService.getFileById(id, userId);

		if (file == null) {
			ra.addFlashAttribute(ERROR_MESSAGE,
					messageSource.getMessage("files-tab.file-download-error-msg", null, Locale.getDefault()));
		} else {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getFilename());
			try (ServletOutputStream outputStream = response.getOutputStream()) {
				outputStream.write(file.getFileData());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * Method responsible to find User authenticated
	 * 
	 * @param username
	 * @return
	 */
	private User getUserAuthenticated(String username) {
		return userService.getUser(username);
	}

}
