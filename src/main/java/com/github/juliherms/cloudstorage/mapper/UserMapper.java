package com.github.juliherms.cloudstorage.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.github.juliherms.cloudstorage.model.User;

/**
 * This interface responsible to access database repository
 * 
 * @author jlv
 *
 */
@Mapper
public interface UserMapper {

	@Select("SELECT * FROM USERS WHERE username = #{username}")
	User getUser(String username);

	@Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
	@Options(useGeneratedKeys = true, keyProperty = "userId")
	int insert(User user);
}
