package com.example.demo.features.user.controller;

import com.example.demo.features.user.model.User;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.features.user.repository.UserRepository;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void addUser_redirectsToAdminServices() throws Exception {
        // Mock save to return a dummy user
        when(userRepository.save(any())).thenReturn(new User());

        mockMvc.perform(post("/addingUser")
                        .param("uname", "TestUser")
                        .param("uemail", "test@example.com")
                        .param("upassword", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
    }

    @Test
    void updateUser_redirectsToAdminServices() throws Exception {
        mockMvc.perform(get("/updatingUser/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
    }

    @Test
    void deleteUser_redirectsToAdminServices() throws Exception {
        doNothing().when(userRepository).deleteById(anyInt());

        mockMvc.perform(get("/deleteUser/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
    }
}
