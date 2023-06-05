package com.golub.school.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Введите номер карты")
    @Size(max=16,min=16, message = "Введите ккорректный номер карты")
    private String number;

    @NotBlank(message = "Введите месяц")
    @Size(max=2,min=2, message = "Введите корректный месяц")
    private String month;

    @NotBlank(message = "Введите год")
    @Size(max=2,min=2, message = "Введите корректный год")
    private String year;

    @NotBlank(message = "Введите трехзначный код")
    @Size(max=3, min=3, message = "Код должен быть трехзначным")
    private String cvc;

    @NotBlank(message = "Введите имя")
    @Size(max=60)
    private String name;

    @NotBlank(message = "Введите фамилию")
    @Size(max=60)
    private String surname;

    @ManyToOne
    @JoinColumn(name="iduser", nullable = false)
    private Users users;
}
