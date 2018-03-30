package com.example.controllers;

import com.example.controller.LoginController;
import com.example.model.File;
import com.example.model.User;
import com.example.repository.ProductRepository;
import com.example.service.FileService;
import com.example.service.UserService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = LoginController.class)
public class loginControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userServiceMock;

    @MockBean
    private FileService fileServiceMock;

    @MockBean
    private ProductRepository productRepositoryMock;

    @Before
    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController()).build();
          mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testLogin() throws Exception{
//        this.mockMvc.perform(get("/login"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andDo(print());
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("login"))
                .andExpect(MockMvcResultMatchers.view().name("login"))
//                .andExpect(content().string(Matchers.containsString("MyTube Media Library")))
                .andDo(print());

    }

    @Test
    public void testRegistration() throws Exception{
//        this.mockMvc.perform(get("/login"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andDo(print());
//        mockMvc.perform(MockMvcRequestBuilders.get("/registration"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("registration"))
//                .andExpect(MockMvcResultMatchers.view().name("registration"))
////                .andExpect(content().string(Matchers.containsString("MyTube Media Library")))
//                .andDo(print());
        mockMvc.perform(MockMvcRequestBuilders.post("/registration"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.view().name("registration"))
//                .andExpect(content().string(Matchers.containsString("MyTube Media Library")))
//                .andExpect(model().attribute("user"))
                .andExpect(model().hasErrors())
                .andDo(print());
    }
//    @Test
//    public void testRegistration() throws Exception{
//        this.mockMvc.perform(get("/login"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andDo(print());
//    }
@Test
public void testEditProfile() throws Exception{
//        this.mockMvc.perform(get("/login"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andDo(print());
    User user = new User();
    user.setLastName("kcmc");
    user.setName("mkmckcm");

    File file1 = new File();
    File file2 = new File();
    List<File> fileArray = new ArrayList<File>();
    fileArray.add(file1);
    fileArray.add(file2);

//
    when(userServiceMock.getCurrentUser()).thenReturn(user);
    when(fileServiceMock.findFileByuser_idAndtype(".jpg")).thenReturn(fileArray);
    mockMvc.perform(MockMvcRequestBuilders.get("/editProfile"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("editProfile"))
            .andExpect(MockMvcResultMatchers.view().name("editProfile"))
//                .andExpect(content().string(Matchers.containsString("MyTube Media Library")))
            .andDo(print());
}

@Test
  public void testHome() throws Exception{
        User user = new User();
    when(userServiceMock.getCurrentUser()).thenReturn(user);
    mockMvc.perform(MockMvcRequestBuilders.get("/home"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("home"))
            .andExpect(MockMvcResultMatchers.view().name("home"))
//                .andExpect(content().string(Matchers.containsString("MyTube Media Library")))
            .andDo(print());
//    Authentication auth = new UsernamePasswordAuthenticationToken("kala@gmail.com", "1234567");
//    SecurityContext securityContext = SecurityContextHolder.getContext();
//    securityContext.setAuthentication(auth);
//    System.out.println(auth.getCredentials());
//    System.out.println(auth.getName());
}
}
