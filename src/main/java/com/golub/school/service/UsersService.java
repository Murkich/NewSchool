package com.golub.school.service;

import com.golub.school.entity.Users;
import com.golub.school.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    public Users findById(Long id) {return  userRepository.findUsersById(id); }
    public void saveUser(Users user) {
        userRepository.save(user);
    }
}