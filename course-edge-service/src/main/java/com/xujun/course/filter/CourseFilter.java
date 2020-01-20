package com.xujun.course.filter;

import com.xujun.thrift.user.dto.UserDTO;
import com.xujun.user.client.LoginFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Order(1)
@WebFilter(filterName="CourseFilter", urlPatterns="/*")
public class CourseFilter extends LoginFilter {

    @Value("${zuul.service}")
    private String zuulService;



    @Override
    protected String UserEdgeServiceAddress() {
        return zuulService;
    }

    @Override
    protected void login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, UserDTO userDTO) {
         httpServletRequest.setAttribute("user", userDTO);
    }
}
