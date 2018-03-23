package com.example;

import com.example.model.User;
import com.example.repository.UserRepository;
import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ModelUserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenUserRepository_whenSaveAndRetreiveEntity_thenOK() {
        User user = new User();
        user.setEmail("testing1@gmail.com");
        user.setLastName("testlastname1");
        user.setName("testname1");
        User userEntity = userRepository
                .save(user);
        User foundEntity = userRepository
                .findById(userEntity.getId());

        assertNotNull(foundEntity);
        assertEquals(userEntity.getId(), foundEntity.getId());
    }
}
