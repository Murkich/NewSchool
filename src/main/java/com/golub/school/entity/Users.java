package com.golub.school.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private long id;

    @Size(max=60)
    @NotBlank(message = "Введите имя")
    private String name;

    @Size(max=60)
    @NotBlank(message = "Введите фамилию")
    private String surname;

    @Size(max = 15, min = 10, message = "Некорректный номер")
    private String telephone;

    @Email(message = "Некорректный e-mail")
    @NotBlank(message = "Введите e-mail")
    @Column(name = "email")
    private String username;

    @Size(min=6, max=255, message="Пароль должен содержать более 5 символов!")
    private String password;

    @Transient
    private String passwordConfirm;

    @Transient
    private String newPassword;

    private String country;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Неверна заполнена дата рождения")
    private Date birthday;

    private String work;

    private String education;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name="user_role", joinColumns = @JoinColumn(name="id_user"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

    @OneToMany(mappedBy = "users")
    private Set<Card> card;

    @OneToOne
    @JoinColumn(name="id_avatar")
    private Avatar avatar;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="orders",
            joinColumns = {@JoinColumn(name="iduser", referencedColumnName="id_user")},
            inverseJoinColumns = {@JoinColumn(name = "idcourse", referencedColumnName="id")})
    private List<Courses> courses;
    @ManyToMany()
    @JoinTable(name="mentors",
            joinColumns = {@JoinColumn(name="iduser", referencedColumnName="id_user")},
            inverseJoinColumns = {@JoinColumn(name = "idcourses", referencedColumnName="id")})
    private List<Courses> mentorCourses;

    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return isActive();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return getRole(); }
    public boolean isAdmin() {
        return role.contains(Role.ADMIN);
    }
    public boolean isUser() {
        return role.contains(Role.USER);
    }
    public boolean isMentor() {
        return role.contains(Role.MENTOR);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users)) return false;
        Users user = (Users) o;
        return  Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(telephone, user.telephone) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(courses, user.courses);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, telephone, username, password, courses);
    }
}
