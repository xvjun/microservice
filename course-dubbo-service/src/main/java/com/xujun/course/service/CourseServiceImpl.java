package com.xujun.course.service;

import com.xujun.course.dto.CourseDTO;
import com.xujun.course.mapper.CourseMapper;
import com.xujun.course.thrift.ServicePeovider;
import com.xujun.thrift.user.UserInfo;
import com.xujun.thrift.user.dto.TeacherDTO;
import org.apache.dubbo.config.annotation.Service;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "1.0.0")
public class CourseServiceImpl implements ICourseService{

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ServicePeovider servicePeovider;

    @Override
    public List<CourseDTO> courseList() {
        List<CourseDTO> courseDTOS = courseMapper.listCourse();
        if (courseDTOS != null) {
            for (CourseDTO courseDTO : courseDTOS) {
                Integer teacherId = courseMapper.getCourseTeacher(courseDTO.getId());
                try {
                    UserInfo userInfo = servicePeovider.getUserService().getTeacherById(teacherId);
                    courseDTO.setTeacher(trans2Teacher(userInfo));
                } catch (TException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return courseDTOS;
    }

    private TeacherDTO trans2Teacher(UserInfo userInfo) {
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(userInfo, teacherDTO);
        return teacherDTO;
    }
}
