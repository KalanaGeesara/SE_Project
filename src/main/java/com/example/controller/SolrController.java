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
        List<String> productImageNames = new ArrayList<String>();
        List<String> productAudioNames = new ArrayList<String>();
        List<String> productVideoNames = new ArrayList<String>();
        System.out.println("Products found by findAll():");
        System.out.println("----------------------------");
        User user = userService.getCurrentUser();
        for (SolrSearch solrSearch : this.productRepository.findByName( myString)){
//            List<File> file = fileRepository.findByFileName(product);
//            productNames.add(file.get(0).getFileName().toString());
            String extension = "";
System.out.println(solrSearch.getOriginalName());
            int i = solrSearch.getOriginalName().lastIndexOf('.');
            if (i > 0) {
                extension = solrSearch.getOriginalName().substring(i+1);
            }

            extension.toLowerCase();
System.out.println(user);
            if(user!=null){
                List<File> file = fileService.fildFileByfile_name_metadata(solrSearch.getOriginalName());
                if(!file.isEmpty()){
                    if((file.get(0).getUserId()==user.getId()) || (file.get(0).getPrivacy().equals("public"))){
                        if(extension.equals("jpg")){
                            productImageNames.add(solrSearch.getOriginalName());
                        }else if(extension.equals("mp3")){
                            productAudioNames.add(solrSearch.getOriginalName());
                        }else if(extension.equals("mp4")){
                            productVideoNames.add(solrSearch.getOriginalName());
                        }
                    }
                }
            }

            else {
                List<File> file = fileService.fildFileByfile_name_metadata(solrSearch.getOriginalName());
                System.out.println(file);
                if(!file.isEmpty()){
                    System.out.println("0000000000000000000000000000000000000");
                    System.out.println(file.get(0).getPrivacy());
                    if(file.get(0).getPrivacy().equals("public")){
                        if(extension.equals("jpg")){
                            productImageNames.add(solrSearch.getOriginalName());
                            System.out.println("1111111111111111111111111111");
                        }else if(extension.equals("mp3")){
                            productAudioNames.add(solrSearch.getOriginalName());
                            System.out.println("22222222222222222222222222222");
                        }else if(extension.equals("mp4")){
                            productVideoNames.add(solrSearch.getOriginalName());
                        }
                    }
                }
            }
System.out.println("5555555555555555555555555555555555555");
            System.out.println(solrSearch.getOriginalName());
            System.out.println(solrSearch);
        }
//        List<String> fileNames = new ArrayList<String>();
//        for(File i:fileService.findFileByuser_id()){
//            fileNames.add(i.getFileName());
//        }
        model.addAttribute("imagefiles", storageService.loadAll2(productImageNames).map(
                path -> "files/"+path.getFileName().toString())
                .collect(Collectors.toList()));
        model.addAttribute("audiofiles", storageService.loadAll2(productAudioNames).map(
                path -> "files/"+path.getFileName().toString())
                .collect(Collectors.toList()));
        model.addAttribute("videofiles", storageService.loadAll2(productVideoNames).map(
                path -> "files/"+path.getFileName().toString())
                .collect(Collectors.toList()));


        if(user!= null) {
            model.addAttribute("message", "logged_in");
            model.addAttribute("numberImage",fileService.findFileNumberByuser_idAndtype(".jpg"));
            model.addAttribute("numberAudio",fileService.findFileNumberByuser_idAndtype(".mp3"));
            model.addAttribute("numberVideo",fileService.findFileNumberByuser_idAndtype(".mp4"));
        }
System.out.println("bhbhbhjbhjbhjbhjbhjbhbhjbh");
System.out.println(myString);

        return "searchResult";
    }
}
