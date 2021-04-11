package com.github.juliherms.cloudstorage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.juliherms.cloudstorage.mapper.FileMapper;
import com.github.juliherms.cloudstorage.model.File;

/**
 * This class responsible to business logic for file
 * @author jlv
 *
 */
@Service
public class FileService {

	private final FileMapper fileMapper;

	public FileService(FileMapper fileMapper) {
		this.fileMapper = fileMapper;
	}
	
	/**
	 * Method responsible to upload file
	 * @param uploadFile
	 * @return
	 */
	public int upload(File uploadFile) {
        return fileMapper.insert(uploadFile);
    }

	/**
	 * Method responsible to list files by user
	 * @param userId
	 * @return
	 */
    public List<File> getFilesByUserId(Integer userId) {
        return fileMapper.getFilesByUserId(userId);
    }
    
    /**
     * Method responsible to delete file
     * @param fileId
     * @param userId
     * @return
     */
    public int delete(Integer fileId, Integer userId) {
        return fileMapper.delete(fileId, userId);
    }

    /**
     * Method responsible to find file by id
     * @param fileId
     * @param userId
     * @return
     */
    public File getFileById(Integer fileId, Integer userId) {
        return fileMapper.getFileById(fileId, userId);
    }

    /**
     * Method responsible to verify exist file
     * @param filename
     * @param userId
     * @param filesize
     * @return
     */
    public boolean fileExists(String filename, Integer userId, Long filesize) {
        File file = fileMapper.getFileByFilenameAndUserId(filename, userId, filesize);
        return file != null;
    }
}
