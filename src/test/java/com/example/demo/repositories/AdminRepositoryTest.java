package com.example.demo.repositories;

import com.example.demo.entities.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void testSaveAndFindByAdminEmail() {
        // Arrange
        Admin admin = new Admin();
        admin.setAdminName("Test Admin");
        admin.setAdminEmail("testadmin@example.com");
        admin.setAdminPassword("password123");
        admin.setAdminNumber("1234567890");

        adminRepository.save(admin);

        // Act
        Admin found = adminRepository.findByAdminEmail("testadmin@example.com");

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getAdminEmail()).isEqualTo("testadmin@example.com");
        assertThat(found.getAdminName()).isEqualTo("Test Admin");
    }

    @Test
    void testFindByAdminEmail_NotFound() {
        // Act
        Admin found = adminRepository.findByAdminEmail("nonexistent@example.com");

        // Assert
        assertThat(found).isNull();
    }
}
