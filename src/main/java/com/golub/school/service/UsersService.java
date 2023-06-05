package com.golub.school.service;

import com.golub.school.entity.Role;
import com.golub.school.entity.Users;
import com.golub.school.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class UsersService implements UserDetailsService {
    @Autowired
    private UsersRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("Пользователь не найден");

        return user;
    }
    public boolean findByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }
    public Users findUserById(Long id) {return  userRepository.findUsersById(id); }
    public void saveUser(Users user) {
        userRepository.save(user);
    }
    public List<Users> getAllUsers() { return userRepository.findAll(); }
    public List<Users> findUsersByName(String name) { return userRepository.findUsersByName(name); }
    public List<Users> findUsersByNameContaining(String name) { return userRepository.findUsersByNameContaining(name); }
    public List<Users> findUsersBySurname(String name) { return userRepository.findUsersBySurname(name); }
    public List<Users> findUsersBySurnameContaining(String name) { return userRepository.findUsersBySurnameContaining(name); }
    public List<Users> findUsersByRole(String role) { return userRepository.findUsersByRoleIn(Set.of(Role.valueOf(role))); }
}