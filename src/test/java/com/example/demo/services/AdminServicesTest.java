package com.example.demo.services;

import com.example.demo.entities.Admin;
import com.example.demo.repositories.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AdminServicesTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServices adminServices;

    private Admin admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        admin = new Admin();
        admin.setAdminId(1);
        admin.setAdminName("Alice");
        admin.setAdminEmail("alice@example.com");
        admin.setAdminPassword("pass");
        admin.setAdminNumber("1234567890");
    }

    @Test
    @DisplayName("getAll should return list of admins")
    void testGetAll() {
        when(adminRepository.findAll()).thenReturn(Arrays.asList(admin));
        List<Admin> result = adminServices.getAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAdminName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("getAdmin should return admin by id")
    void testGetAdmin() {
        when(adminRepository.findById(1)).thenReturn(Optional.of(admin));
        Admin result = adminServices.getAdmin(1);
        assertThat(result.getAdminEmail()).isEqualTo("alice@example.com");
    }

    @Test
    @DisplayName("addAdmin should call save on repository")
    void testAddAdmin() {
        adminServices.addAdmin(admin);
        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    @DisplayName("delete should call deleteById on repository")
    void testDelete() {
        adminServices.delete(1);
        verify(adminRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("update should save when matching id found")
    void testUpdate() {
        Admin updated = new Admin();
        updated.setAdminId(1);
        updated.setAdminName("Updated");
        when(adminRepository.findAll()).thenReturn(Arrays.asList(admin));
        adminServices.update(updated, 1);
        verify(adminRepository, times(1)).save(updated);
    }

    @Test
    @DisplayName("validateAdminCredentials should return true for valid credentials")
    void testValidateAdminCredentials_Valid() {
        when(adminRepository.findByAdminEmail("alice@example.com")).thenReturn(admin);
        boolean result = adminServices.validateAdminCredentials("alice@example.com", "pass");
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("validateAdminCredentials should return false for invalid credentials")
    void testValidateAdminCredentials_Invalid() {
        when(adminRepository.findByAdminEmail("alice@example.com")).thenReturn(admin);
        boolean result = adminServices.validateAdminCredentials("alice@example.com", "wrongpass");
        assertThat(result).isFalse();
    }
}
