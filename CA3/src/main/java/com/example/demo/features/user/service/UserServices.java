package com.example.demo.features.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.features.user.model.User;
import com.example.demo.features.user.repository.UserRepository;

@Component
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUser() {
        return (List<User>) userRepository.findAll();
    }

    public User getUser(int id) {
        Optional<User> optional = userRepository.findById(id);
        return optional.orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByUemail(email);
    }

    public void updateUser(User user, int id) {
        user.setU_id(id);
        userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }
}
