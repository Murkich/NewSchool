package com.golub.school;

import static org.assertj.core.api.Assertions.assertThat;

import com.golub.school.entity.Role;
import com.golub.school.entity.Users;
import com.golub.school.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Collections;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UsersRepository repo;

    @Test
    public void testCreateUser() {
        Users user = new Users();
        user.setUsername("golub@gmail.com");
        user.setPassword("Dasha_12345");
        user.setPasswordConfirm("Dasha_12345");
        user.setName("Дарья");
        user.setSurname("Голуб");
        user.setTelephone("+375333557464");
        user.setActive(true);
        user.setRole(Collections.singleton(Role.valueOf("USER")));

        Users savedUser = repo.save(user);

        Users existUser = entityManager.find(Users.class, savedUser.getId());

        assertThat(user.getUsername()).isEqualTo(existUser.getUsername());

    }
}