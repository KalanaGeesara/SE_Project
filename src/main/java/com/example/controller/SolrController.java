package com.example.controller;

import com.example.model.File;
//import com.example.model.Product;
import com.example.model.SolrSearch;
import com.example.model.User;
import com.example.repository.FileRepository;
import com.example.repository.ProductRepository;
import com.example.service.FileService;
import com.example.service.StorageService;
import com.example.service.UserService;
import com.example.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SolrController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @PostMapping("/search")
    public String showSearchResult(@RequestParam(name = "searchBox") String myString, Model model) throws IOException {

//        model.addAttribute("files", storageService.loadAll().map(
//                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                        "serveFile", path.getFileName().toString()).build().toString())
//                .collect(Collectors.toList()));
        List<String> productNames = new ArrayList<String>();
        System.out.println("Products found by findAll():");
        System.out.println("----------------------------");
        for (SolrSearch solrSearch : this.productRepository.findByName( myString)){
//            List<File> file = fileRepository.findByFileName(product);
//            productNames.add(file.get(0).getFileName().toString());
            productNames.add(solrSearch.getOriginalName());
            System.out.println(solrSearch.getOriginalName());
            System.out.println(solrSearch);
        }
//        List<String> fileNames = new ArrayList<String>();
//        for(File i:fileService.findFileByuser_id()){
//            fileNames.add(i.getFileName());
//        }
        model.addAttribute("files", storageService.loadAll2(productNames).map(
                path -> "files/"+path.getFileName().toString())
                .collect(Collectors.toList()));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if(user!= null) {
            model.addAttribute("message", "logged_in");
        }
System.out.println("bhbhbhjbhjbhjbhjbhjbhbhjbh");
System.out.println(myString);
        List<File> imageFile = fileService.findFileByuser_idAndtype(".jpg");
        List<File> audioFile = fileService.findFileByuser_idAndtype(".mp3");
        List<File> videoFile = fileService.findFileByuser_idAndtype(".mp4");
        model.addAttribute("numberImage",imageFile.size());
        model.addAttribute("numberAudio",audioFile.size());
        model.addAttribute("numberVideo",videoFile.size());
        return "searchResult";
    }
}
