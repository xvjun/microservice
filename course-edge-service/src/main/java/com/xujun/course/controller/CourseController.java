package com.xujun.course.controller;

import com.xujun.course.dto.CourseDTO;
import com.xujun.course.service.ICourseService;
import com.xujun.thrift.user.dto.UserDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Reference(version = "1.0.0")
    private ICourseService courseService;

    @RequestMapping(value = "/courseList", method = RequestMethod.GET)
    @ResponseBody
    public List<CourseDTO> courseList(HttpServletRequest request){
        UserDTO userDTO = (UserDTO) request.getAttribute("user");
        System.out.println(userDTO.toString());
        return courseService.courseList();
    }

}
