package com.golub.school.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    private Date birthday;

    private String work;

    private String education;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name="user_role", joinColumns = @JoinColumn(name="id_user"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

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
                Objects.equals(password, user.password);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, telephone, username, password);
    }
}
