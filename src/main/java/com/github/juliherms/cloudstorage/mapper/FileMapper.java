package com.github.juliherms.cloudstorage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.github.juliherms.cloudstorage.model.File;

/**
 * This class responsible to access database
 * 
 * @author jlv
 *
 */
@Mapper
public interface FileMapper {

	
	/**
	 * This method responsible to insert file
	 * 
	 * @param file
	 * @return
	 */
	@Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData} )")
	@Options(useGeneratedKeys = true, keyProperty = "fileId")
	int insert(File file);

	/**
	 * This method responsible to list files from userId
	 * 
	 * @param userId
	 * @return
	 */
	@Select("SELECT fileid, filename, filesize FROM FILES WHERE userid = #{userId}")
	List<File> getFilesByUserId(Integer userId);

	/**
	 * This method responsible to find file by user and id
	 * 
	 * @param fileId
	 * @param userId
	 * @return
	 */
	@Select("SELECT * FROM FILES WHERE fileid = #{fileId} AND userid = #{userId}")
	File getFileById(Integer fileId, Integer userId);

	/**
	 * This method responsible to find file by filename, userid and filesize
	 * 
	 * @param filename
	 * @param userId
	 * @param filesize
	 * @return
	 */
	@Select("SELECT * FROM FILES WHERE filename = #{filename} AND userid = #{userId} AND filesize = #{filesize}")
	File getFileByFilenameAndUserId(String filename, Integer userId, Long filesize);

	/**
	 * this method responsible to delete file by user and id
	 * @param fileId
	 * @param userId
	 * @return
	 */
	@Delete("DELETE FROM FILES WHERE fileid = #{fileId}  AND userid = #{userId}")
	int delete(Integer fileId, Integer userId);
}
