package com.golub.school.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="courses")
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Введите название курса")
    private String name;

    @NotBlank(message = "Введите язык")
    private String language;

    @Positive(message = "Цена должна быть больше 0")
    private double price;

    @Positive(message = "Количество мест должно быть больше 0")
    private int seats;

    private int freeseats;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Неправильно введена дата")
    @NotNull(message = "Укажите начало курса")
    private java.util.Date begindate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Неправильно введена дата")
    @NotNull(message = "Укажите окончание курса")
    private java.util.Date enddate;

    @ManyToMany(mappedBy = "courses")
    private List<Users> users;

    @ManyToMany(mappedBy = "mentorCourses")
    private List<Users> mentors;

    public Courses(Long id, String name, String language, double price, int seats, int freeseats, Date begindate, Date enddate) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.price = price;
        this.seats = seats;
        this.freeseats = freeseats;
        this.begindate = begindate;
        this.enddate = enddate;
    }
}
