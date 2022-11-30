package com.golub.school.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="courses")
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Введите название курса")
    private String name;

    @NotBlank(message = "Введите язык")
    private String language;

    @NotBlank(message = "Введите цену")
    private double price;

    @NotBlank(message = "Введите количество мест на курс")
    private int seats;

    private int freeseats;

    @NotBlank(message = "Введите начало курса")
    private Date begindate;

    @NotBlank(message = "Введите окончание курса")
    private Date enddate;

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
