package com.example.demo.repositories;

import com.example.demo.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findUserByUemail should return user with given email")
    void testFindUserByUemail() {
        User user = new User();
        user.setUname("John Doe");
        user.setUemail("john@example.com");
        user.setUpassword("securepass");
        user.setUnumber(1234567890L);
        userRepository.save(user);

        User found = userRepository.findUserByUemail("john@example.com");

        assertThat(found).isNotNull();
        assertThat(found.getUname()).isEqualTo("John Doe");
        assertThat(found.getUemail()).isEqualTo("john@example.com");
    }
}
