package com.example.controllers;

import com.example.controller.MetadataController;
import com.example.model.File;
import com.example.service.FileService;
import com.example.service.StorageService;

import com.example.storage.StorageProperties;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MetadataController.class)
@AutoConfigureMockMvc(secure=false)
public class metadataControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private FileService fileServiceMock;
    @MockBean
    private StorageService storageServiceMock;

    @Test
    public void testDisplayMetadata() throws Exception {
        List<File> fileArray;
        fileArray = new ArrayList<File>();
        File file = new File();
        file.setMetadata("jcncjcns,kcsscscn,kkskscks,ckcskcscscs");
        file.setType(".mp3");
        fileArray.add(file);
        assertThat(this.fileServiceMock).isNotNull();
        when(fileServiceMock.fildFileByfile_name_metadata("aa")).thenReturn(fileArray);
        fileArray.get(0).getType();
        List<String> a = Arrays.asList(("jcncjcns,kcsscscn,kkskscks,ckcskcscscs").split(","));
        MvcResult result= mockMvc.perform(get("/metadata/{filename:.+}", "aa"))
                .andExpect(status().isOk())
                .andExpect(view().name("displayMetadata"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("product"))
//                .andExpect(model().attribute("", hasProperty("id", is(1))))
//                .andExpect(model().attribute("product", hasProperty("productId", is("235268845711068308"))))
//                .andExpect(model().attribute("product", hasProperty("description", is("Spring Framework Guru Shirt"))))
//                .andExpect(model().attribute("product", hasProperty("price", is(new BigDecimal("18.95")))))
//                .andExpect(model().attribute("product", hasProperty("imageUrl", is("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg"))))
                .andExpect(model().attribute("metadatas",a))
                .andDo(print())
                .andReturn();


        MockHttpServletResponse mockResponse=result.getResponse();
        assertThat(mockResponse.getContentType()).isEqualTo("text/html;charset=UTF-8");

        Collection<String> responseHeaders = mockResponse.getHeaderNames();
        assertNotNull(responseHeaders);
        assertEquals(1, responseHeaders.size());
        assertEquals("Check for Content-Type header", "Content-Type", responseHeaders.iterator().next());
        String responseAsString=mockResponse.getContentAsString();
        assertTrue(responseAsString.contains("Test"));

//        verify(productServiceMock, times(1)).getProductById(1);
//        verifyNoMoreInteractions(productServiceMock);
    }

    @Test
    public void testViewMetadata() throws Exception{
//        this.mockMvc.perform(get("/login"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andDo(print());
//        when(storageServiceMock.loadAll2("cc"))
        List<String> dan = Arrays.asList("7headlineimage.jpg", "test.jpg", "Yellow", "Green", "Blue", "Violet", "Orange", "Blue");
        List<String> a = Arrays.asList(("4index1.jpg, 4index2.jpg, 4index3.jpg, metadata/4index4.jpg").split(","));
        List<String> imagefileNames = Arrays.asList(("4index1.jpg, metadata/4index2.jpg, metadata/4index3.jpg, metadata/4index4.jpg").split(","));
        final Path rootLocation = Paths.get("upload-dir");
        Stream<Path> b =  Files.walk(rootLocation, 1)
                .filter(path -> !path.equals(rootLocation))
                .filter(path -> dan.contains(rootLocation.relativize(path).getFileName().toString()))
                .map(path -> rootLocation.relativize(path));

        when(storageServiceMock.loadAll()).thenReturn(b);
//        when(storageServiceMock.loadAll2(imagefileNames)).thenReturn(b);
        mockMvc.perform(MockMvcRequestBuilders.get("/viewMetadataTest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("viewMetadata"))
                .andExpect(MockMvcResultMatchers.view().name("viewMetadata"))
//                .andExpect(content().string(Matchers.containsString("MyTube Media Library")))
                .andDo(print());
    }
}
