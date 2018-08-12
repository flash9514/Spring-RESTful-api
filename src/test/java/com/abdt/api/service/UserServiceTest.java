package com.abdt.api.service;

import com.abdt.api.domain.User;
import com.abdt.api.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @TestConfiguration
    static class UserServiceTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserService();
        }
    }

    @Autowired
    private UserService userService;

    // avoid using real UserRepository
    @MockBean
    UserRepository userRepository;

    User alex = new User("alex@example.com", "alex", "USER", null, null);
    List<User> allUsers = Arrays.asList(alex);

    // imitate behavior
    @Before
    public void setUp() {

        Mockito.when(userRepository.findByUsername(alex.getUsername()))
                .thenReturn(alex);

        Mockito.when(userRepository.findAll())
                .thenReturn(allUsers);

        Mockito.when(userRepository.save(alex))
                .thenReturn(alex);

        Mockito.when(userRepository.findById(alex.getId()))
                .thenReturn(java.util.Optional.ofNullable(alex));

    }

    @Test
    public void whenValidName_thenUserShouldBeFound() {
        String name = "alex@example.com";
        UserDetails found = userService.loadUserByUsername(name);

        assertThat(found.getUsername())
                .isEqualTo(name);
    }

    @Test
    public void whenFindAllUsers_thenReturnUsers() {
        List<User> found = userService.findAllUsers();

        assertThat(found)
                .isEqualTo(allUsers);
    }

    @Test
    public void whenUserId_thenReturnUser() {

        User found = userService.findById(alex.getId());

        assertThat(found)
                .isNotEqualTo(null);
    }

    @Test
    public void whenSaveUser_thenReturnNewUser() {

        userService.saveUser(alex);

        assertThat(userService.isUserExist(alex.getUsername()))
                .isEqualTo(true);
    }

    @Test
    public void whenUpdateUser_thenReturnNewUser() {

        alex.setUsername("alex2@example.ru");

        userService.updateUser(alex);
        User found = userService.findById(alex.getId());

        assertThat(found.getUsername())
                .isEqualTo("alex2@example.ru");
    }
}
