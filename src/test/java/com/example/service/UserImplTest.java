package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserImplTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepositoryMock;

    @Test
    public void findUserByEmailTest(){
        User user = new User();
        user.setName("kala");
        user.setLastName("gee");
        user.setEmail("sksk@gmail.com");

        when(userRepositoryMock.findByEmail("sksk@gmail.com")).thenReturn(user);

        assertThat(userService.findUserByEmail("sksk@gmail.com")).isEqualTo(user);
    }
}
