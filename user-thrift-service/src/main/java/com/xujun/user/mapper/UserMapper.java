package com.xujun.user.mapper;

import com.xujun.thrift.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select id,username,password,realName,mobile,email from pe_user where id=#{id}")
    UserInfo getUserById(@Param("id") int id);

    @Select("select id,username,password,realName,mobile,email from pe_user where username=#{username}")
    UserInfo getUserByName(@Param("username") String username);

    @Insert("insert into pe_user (username,password,realName,mobile,email) values (#{u.username},#{u.password},#{u.realName},#{u.mobile},#{u.email})")
    void registerUser(@Param("u") UserInfo userInfo);
}
