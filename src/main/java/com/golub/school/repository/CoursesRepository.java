package com.golub.school.repository;

import com.golub.school.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Long> {
    Courses findCoursesById(Long id);
    List<Courses> findCoursesByLanguage(String language);

    List<Courses> findAllByLanguage(String language);
}
