package com.abdt.api.web;

import com.abdt.api.SecurityConfiguration;
import com.abdt.api.domain.User;
import com.abdt.api.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void givenUsers_whenGetUsers_thenReturnArray() throws Exception {
        User alex = new User("alex@example.com","alex", "ADMIN", null, null);

        List<User> allUsers = Arrays.asList(alex);

        given(userService.findAllUsers()).willReturn(allUsers);

        mvc.perform(get("/user")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUserId_whenGetUser_thenReturnUser() throws Exception {
        User alex = new User("alex@example.com", "alex", "ADMIN", null, null);

        given(userService.findById(alex.getId())).willReturn(alex);

        mvc.perform(get("/user/{id}")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUserInfo_whenPostUser_thenReturnUser() throws Exception {

        mvc.perform(post("/user")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUserInfo_whenPutUser_thenReturnNewUser() throws Exception {

        mvc.perform(put("/user/{id}")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUserId_whenDeleteUser_thenReturnOnlyText() throws Exception {

        mvc.perform(delete("/user/{id}")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }
}
