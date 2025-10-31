package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServices userServices;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setU_id(1);
        user.setUname("John");
        user.setUemail("john@example.com");
        user.setUpassword("1234");
        user.setUnumber(1234567890L);
    }

    @Test
    @DisplayName("getAllUser should return all users")
    void testGetAllUser() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        List<User> result = userServices.getAllUser();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUemail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("getUser should return user by id")
    void testGetUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        User result = userServices.getUser(1);
        assertThat(result).isNotNull();
        assertThat(result.getUname()).isEqualTo("John");
    }

    @Test
    @DisplayName("getUserByEmail should return user when found")
    void testGetUserByEmail() {
        when(userRepository.findUserByUemail("john@example.com")).thenReturn(user);
        User result = userServices.getUserByEmail("john@example.com");
        assertThat(result).isNotNull();
        assertThat(result.getUemail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("updateUser should set id and save user")
    void testUpdateUser() {
        userServices.updateUser(user, 2);
        assertThat(user.getU_id()).isEqualTo(2);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("deleteUser should call deleteById")
    void testDeleteUser() {
        userServices.deleteUser(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("addUser should save user")
    void testAddUser() {
        userServices.addUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("validateLoginCredentials should return true for valid user")
    void testValidateLoginCredentialsValid() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        boolean result = userServices.validateLoginCredentials("john@example.com", "1234");
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("validateLoginCredentials should return false for invalid user")
    void testValidateLoginCredentialsInvalid() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        boolean result = userServices.validateLoginCredentials("john@example.com", "wrong");
        assertThat(result).isFalse();
    }
}
