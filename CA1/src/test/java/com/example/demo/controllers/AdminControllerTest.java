package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.loginCredentials.*;
import com.example.demo.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UserServices userServices;
    @MockBean private AdminServices adminServices;
    @MockBean private ProductServices productServices;
    @MockBean private OrderServices orderServices;

    private Admin admin;
    private User user;
    private Product product;
    private Orders order;

    @BeforeEach
    void setup() {
        admin = new Admin();
        admin.setAdminId(1);
        admin.setAdminEmail("admin@example.com");
        admin.setAdminPassword("pass");

        user = new User();
        user.setU_id(1);
        user.setUemail("user@example.com");
        user.setUpassword("1234");
        user.setUname("John");

        product = new Product();
        product.setPid(1);
        product.setPname("Pizza");
        product.setPprice(10.0);

        order = new Orders();
        order.setoId(1);
        order.setoPrice(10.0);
        order.setoQuantity(2);
        order.setUser(user);
    }

    @Test
    void testAdminLoginSuccess() throws Exception {
        when(adminServices.validateAdminCredentials("admin@example.com", "pass")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/adminLogin")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "admin@example.com")
                .param("password", "pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
    }

    @Test
    void testAdminLoginFailure() throws Exception {
        when(adminServices.validateAdminCredentials(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/adminLogin")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "wrong@example.com")
                .param("password", "bad"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("Login"));
    }

    @Test
    void testReturnBackLoadsAllEntities() throws Exception {
        when(userServices.getAllUser()).thenReturn(List.of(user));
        when(adminServices.getAll()).thenReturn(List.of(admin));
        when(productServices.getAllProducts()).thenReturn(List.of(product));
        when(orderServices.getOrders()).thenReturn(List.of(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/services"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users", "admins", "products", "orders"))
                .andExpect(view().name("Admin_Page"));
    }

    @Test
    void testAddAdminRedirects() throws Exception {
        doNothing().when(adminServices).addAdmin(any(Admin.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/addingAdmin")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("adminEmail", "admin@example.com")
                .param("adminPassword", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
    }

    @Test
    void testDeleteAdminRedirects() throws Exception {
        doNothing().when(adminServices).delete(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/deleteAdmin/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
    }

    @Test
    void testAddAdminPageView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/addAdmin"))
                .andExpect(status().isOk())
                .andExpect(view().name("Add_Admin"));
    }

    @Test
    void testAddProductPageView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/addProduct"))
                .andExpect(status().isOk())
                .andExpect(view().name("Add_Product"));
    }
}
