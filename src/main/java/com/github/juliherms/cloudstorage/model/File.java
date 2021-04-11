package com.github.juliherms.cloudstorage.model;

import java.util.Date;

/**
 * This class responsible to represents file access in the system
 * @author jlv
 *
 */
public class File {

	private Integer fileId;
	private String filename;
	private String contentType;
	private Long fileSize;
	private Integer userId;
	private byte[] fileData;
	private Date uploadTime;

	public File() {

	}

	public File(Integer fileId, String filename, String contentType, Long fileSize, Integer userId, byte[] fileData,
			Date uploadTime) {
		
		this.fileId = fileId;
		this.filename = filename;
		this.contentType = contentType;
		this.fileSize = fileSize;
		this.userId = userId;
		this.fileData = fileData;
		this.uploadTime = uploadTime;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
}
