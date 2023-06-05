package com.golub.school.repository;

import com.golub.school.entity.Role;
import com.golub.school.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
    Users findUsersById(Long id);
    List<Users> findUsersBySurname(String surname);
    List<Users> findUsersBySurnameContaining(String surname);
    List<Users> findUsersByName(String name);
    List<Users> findUsersByNameContaining(String name);
    List<Users> findUsersByRoleIn(Set<Role> role);
}