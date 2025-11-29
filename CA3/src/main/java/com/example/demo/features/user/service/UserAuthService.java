package com.example.demo.features.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.core.auth.AbstractAuthService;
import com.example.demo.features.user.model.User;
import com.example.demo.features.user.repository.UserRepository;

@Service
public class UserAuthService extends AbstractAuthService<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected User getUserByEmail(String email) {
        return userRepository.findUserByUemail(email);
    }

    @Override
    protected String getStoredPassword(User user) {
        return user.getUpassword();
    }
}
