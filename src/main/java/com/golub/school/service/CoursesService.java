package com.golub.school.service;

import com.golub.school.entity.Courses;
import com.golub.school.repository.CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursesService {
    @Autowired
    private CoursesRepository coursesRepository;

    public List<Courses> findCoursesByLanguage(String language) {return coursesRepository.findCoursesByLanguage(language); }
    public Courses findCoursesById(Long id) {return coursesRepository.findCoursesById(id);}
    public List<Courses> getAllCourse()
    {
        return coursesRepository.findAll();
    }
}
