package com.example.demo.features.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.core.auth.AbstractAuthService;
import com.example.demo.features.admin.model.Admin;
import com.example.demo.features.admin.repository.AdminRepository;

@Service
public class AdminAuthService extends AbstractAuthService<Admin> {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    protected Admin getUserByEmail(String email) {
        return adminRepository.findByAdminEmail(email);
    }

    @Override
    protected String getStoredPassword(Admin admin) {
        return admin.getAdminPassword();
    }
}
