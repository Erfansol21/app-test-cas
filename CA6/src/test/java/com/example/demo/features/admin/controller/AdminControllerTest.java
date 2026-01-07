package com.example.demo.features.admin.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import com.example.demo.features.admin.model.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.features.admin.repository.AdminRepository;
import com.example.demo.features.order.repository.OrderRepository;
import com.example.demo.features.product.repository.ProductRepository;
import com.example.demo.features.user.repository.UserRepository;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void adminLogin_success_redirectsToAdminServices() throws Exception {
        Admin mockAdmin = new Admin();
        mockAdmin.setAdminEmail("admin@test.com");
        mockAdmin.setAdminPassword("1234"); // must match test input

        when(adminRepository.findByAdminEmail("admin@test.com")).thenReturn(mockAdmin);

        mockMvc.perform(post("/adminLogin")
                        .param("email", "admin@test.com")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
    }

    @Test
    void adminServicesPage_returnsAdminPageWithModel() throws Exception {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(adminRepository.findAll()).thenReturn(Collections.emptyList());
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/services"))
                .andExpect(status().isOk())
                .andExpect(view().name("Admin_Page"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("admins"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void addAdminPage_returnsAddAdminView() throws Exception {
        mockMvc.perform(get("/addAdmin"))
                .andExpect(status().isOk())
                .andExpect(view().name("Add_Admin"));
    }

    @Test
    void addingAdmin_redirectsToAdminServices() throws Exception {
        when(adminRepository.save(any())).thenReturn(new Admin());

        mockMvc.perform(post("/addingAdmin")
                        .param("adminEmail", "admin@example.com")
                        .param("adminPassword", "1234")
                        .param("adminName", "TestAdmin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
    }

    @Test
    void deleteAdmin_redirectsToAdminServices() throws Exception {
        doNothing().when(adminRepository).deleteById(anyInt());

        mockMvc.perform(get("/deleteAdmin/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
    }
}
