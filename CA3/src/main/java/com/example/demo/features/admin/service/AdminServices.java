package com.example.demo.features.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.features.admin.model.Admin;
import com.example.demo.features.admin.repository.AdminRepository;

@Component
public class AdminServices {

    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> getAll() {
        return (List<Admin>) adminRepository.findAll();
    }

    public Admin getAdmin(int id) {
        Optional<Admin> optional = adminRepository.findById(id);
        return optional.orElse(null);
    }

    public void update(Admin admin, int id) {
        admin.setAdminId(id);
        adminRepository.save(admin);
    }

    public void delete(int id) {
        adminRepository.deleteById(id);
    }

    public void addAdmin(Admin admin) {
        adminRepository.save(admin);
    }
}
