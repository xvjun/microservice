package com.xujun.user.client;


import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.xujun.thrift.user.dto.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public abstract class LoginFilter implements Filter {

    private static Cache<String, UserDTO> cache = CacheBuilder.newBuilder().maximumSize(10000)
            .expireAfterWrite(3, TimeUnit.MINUTES).build();

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String token = httpServletRequest.getParameter("token");
        UserDTO userDTO = null;
        if(StringUtils.isBlank(token)){
            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equals("token")){
                        token = cookie.getValue();
                    }
                }
            }
        }else{
            userDTO = cache.getIfPresent(token);
            if(userDTO == null){
                userDTO = requestUserInfo(token);
            }
        }
        if(userDTO == null){
            httpServletResponse.sendRedirect("http://127.0.0.1:8082/user/login");
            return;
        }

        cache.put(token, userDTO);

        login(httpServletRequest,httpServletResponse,userDTO);

        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }

    protected abstract void login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, UserDTO userDTO);

    private UserDTO requestUserInfo(String token){
        String url = "http://127.0.0.1:8082/user/authentication";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.addHeader("token", token);
        InputStream inputStream = null;
        try {
            HttpResponse httpResponse = client.execute(post);
            if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                throw new RuntimeException("request user info  failed! StatusLine:" + httpResponse.getStatusLine());
            }
            inputStream = httpResponse.getEntity().getContent();
            byte[] temp = new byte[1024];
            StringBuilder stringBuilder = new StringBuilder();
            int len = 0;
            while ((len = inputStream.read(temp)) > 0){
                stringBuilder.append(new String(temp, 0, len));
            }
            UserDTO userDTO = JSON.parseObject(stringBuilder.toString(), UserDTO.class);
            return userDTO;
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void destroy() {

    }
}
