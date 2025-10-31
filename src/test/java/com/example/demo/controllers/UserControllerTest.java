package com.example.demo.controllers;

import com.example.demo.entities.User;
import com.example.demo.services.UserServices;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServices services;

    @Test
    void testAddUserRedirects() throws Exception {
        mockMvc.perform(post("/addingUser")
                .param("uname", "John")
                .param("uemail", "john@example.com")
                .param("upassword", "1234")
                .param("unumber", "1234567890"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
        Mockito.verify(services).addUser(Mockito.any(User.class));
    }

    @Test
    void testUpdateUserRedirects() throws Exception {
        mockMvc.perform(get("/updatingUser/1")
                .param("uname", "Jane")
                .param("uemail", "jane@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
        Mockito.verify(services).updateUser(Mockito.any(User.class), Mockito.eq(1));
    }

    @Test
    void testDeleteUserRedirects() throws Exception {
        mockMvc.perform(get("/deleteUser/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
        Mockito.verify(services).deleteUser(1);
    }
}
