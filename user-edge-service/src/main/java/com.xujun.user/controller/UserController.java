package com.xujun.user.controller;

import com.xujun.thrift.user.UserInfo;
import com.xujun.thrift.user.dto.UserDTO;
import com.xujun.user.redis.RedisClient;
import com.xujun.user.response.LoginResponse;
import com.xujun.user.response.Response;
import com.xujun.user.thrift.ServicePeovider;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ServicePeovider servicePeovider;

    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Response login(@RequestParam("username") String username,@RequestParam("password") String password){

        //1.验证用户名密码
        UserInfo userInfo;
        try {
            userInfo  = servicePeovider.getUserService().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if(userInfo == null){
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if(!userInfo.getPassword().equalsIgnoreCase(MD5(password))){
            return Response.USERNAME_PASSWORD_INVALID;
        }
//        2.生成token
        String token = getToken();
//        3.缓存用户
        redisClient.set(token,toDTO(userInfo),3600);
        return new LoginResponse(token);

    }

    @RequestMapping(value = "/sendVerifyCode", method = RequestMethod.POST)
    @ResponseBody
    public Response sendVerifyCode(@RequestParam(value = "mobile", required = false) String mobile,
                                   @RequestParam(value = "email", required = false) String email){
        String message = "Verify code is : ";
        String code = randomCode("1234567890",6);
        boolean result;
        try {
            if(StringUtils.isNotBlank(mobile)){
                result = servicePeovider.getMessageService().sendMobileMessage(mobile, message + code);
                redisClient.set(mobile, code);
            }else if(StringUtils.isNotBlank(email)){
                result = servicePeovider.getMessageService().sendEmailMessage(email, message + code);
                redisClient.set(email, code);
            }else{
                return Response.MOBILE_OR_EMAIL_REQUIRED;
            }
            if(!result){
                return Response.SEND_VERIFY_CODE_FAILED;
            }
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }

        return Response.SUCCESS;
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public Response register(@RequestParam("username") String username,@RequestParam("password") String password,
                             @RequestParam(value = "mobile", required = false) String mobile,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam("verifyCode") String verifyCode){
        boolean flag = false;
        if(StringUtils.isBlank(mobile) && StringUtils.isBlank(email)){
            return Response.MOBILE_OR_EMAIL_REQUIRED;
        }
        if(StringUtils.isNotBlank(mobile)){
            String redisCode = redisClient.get(mobile);
            if(redisCode != null){
                if(verifyCode.equals(redisCode)){
                    flag = true;
                }
            }
        }
        if(flag == false || StringUtils.isNotBlank(email)){
            String redisCode = redisClient.get(email);
            if(redisCode != null){
                if(verifyCode.equals(redisCode)){
                    flag = true;
                }
            }
        }

        if(!flag){return Response.VERIFY_CODE_INVALID;}
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(MD5(password));
        userInfo.setEmail(email);
        userInfo.setMobile(mobile);
        try {
            servicePeovider.getUserService().regiserUser(userInfo);
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }
        return Response.SUCCESS;
    }

    @RequestMapping(value = "/authentication",method = RequestMethod.POST)
    @ResponseBody
    public UserDTO authentication(@RequestHeader("token") String token){
        return redisClient.get(token);
    }

    private UserDTO toDTO(UserInfo userInfo){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userInfo,userDTO);
        return userDTO;
    }

    private String getToken(){
        return randomCode("1234567890qwertyuioplkjhgfdsazxcvbnm",32);
    }

    private String randomCode(String s,int size){
        StringBuilder result = new StringBuilder(32);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int loc = random.nextInt(s.length());
            result.append(s.charAt(loc));
        }
        return result.toString();
    }

    private String MD5(String password){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(password.getBytes("utf-8"));
            return HexUtils.toHexString(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
