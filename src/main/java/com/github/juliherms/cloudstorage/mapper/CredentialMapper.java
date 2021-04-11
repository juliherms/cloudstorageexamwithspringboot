package com.github.juliherms.cloudstorage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.juliherms.cloudstorage.model.Credential;

/**
 * This class responsible to access credential
 * 
 * @author jlv
 *
 */
@Mapper
public interface CredentialMapper {

	/**
	 * Method responsible to insert credential in the database
	 * 
	 * @param credential
	 * @return
	 */
	@Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
	@Options(useGeneratedKeys = true, keyProperty = "credentialId")
	int insert(Credential credential);

	/**
	 * Method responsible to list credentials by user
	 * 
	 * @param userId
	 * @return
	 */
	@Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
	List<Credential> getCredentialsByUserId(Integer userId);

	/**
	 * Method responsible to find credential by id
	 * 
	 * @param credentialId
	 * @return
	 */
	@Select("SELECT * FROM CREDENTIALS WHERE credentialid=#{credentialId}")
	Credential getCredentialById(Integer credentialId);

	/**
	 * Method responsible to find credential by url,username and userId
	 * 
	 * @param url
	 * @param username
	 * @param userId
	 * @return
	 */
	@Select("SELECT * FROM CREDENTIALS WHERE url=#{url} AND username=#{username} AND userid=#{userId}")
	Credential getCredential(String url, String username, Integer userId);

	/**
	 * Method responsible to update credential by id
	 * @param credential
	 * @return
	 */
	@Update("UPDATE CREDENTIALS set url=#{url}, username=#{username}, key=#{key}, password=#{password}, userid=#{userId} WHERE credentialid=#{credentialId}")
	int update(Credential credential);
	
	/**
	 * Method responsible to delete credential by id
	 * @param credentialId
	 * @param userId
	 * @return
	 */
	@Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid = #{userId}")
    int deleteById(Integer credentialId, Integer userId);

}
