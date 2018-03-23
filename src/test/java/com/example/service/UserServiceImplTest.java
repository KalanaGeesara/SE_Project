package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceImplTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private UserRepository userRepository;
//        @Autowired
//        private StorageService storageService;

//        storageService.init();

        @Test
        public void testSaveUser() {
            User user = getUser();
            User savedUser = entityManager.persist(user);
            User getUser = userRepository.findById(savedUser.getId());

            assertThat(getUser.getEmail()).isEqualTo(savedUser.getEmail());
        }
        private User getUser(){
            User user = new User();
            user.setName("kalana");
            user.setEmail("kalana@gmail.com");
            user.setLastName("Geesara");
            return user;
        }
    }

