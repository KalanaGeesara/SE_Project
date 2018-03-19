package com.example.controller;

import com.example.model.File;
import com.example.service.FileService;
import com.example.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MetadataController {

    @Autowired
    private FileService fileService;

    @Autowired
    private StorageService storageService;
//    @RequestMapping(value = "/metadata", method = RequestMethod.GET)
    @GetMapping("/viewMetadata")
    public String listUploadedFiles(Model model) throws IOException {

//        model.addAttribute("files", storageService.loadAll().map(
//                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                        "serveFile", path.getFileName().toString()).build().toString())
//                .collect(Collectors.toList()));
        List<String> imagefileNames = new ArrayList<String>();
        List<String> audiofileNames = new ArrayList<String>();
        List<String> videofileNames = new ArrayList<String>();
        for(File i:fileService.findFileByuser_id()){
            if(i.getType().equals(".jpg")){
                imagefileNames.add(i.getFileName());
            }
            if(i.getType().equals(".mp3")){
                audiofileNames.add(i.getFileName());
            }
            if(i.getType().equals(".mp4")){
                videofileNames.add(i.getFileName());
            }
        }
        model.addAttribute("imagefiles", storageService.loadAll2(imagefileNames).map(
                path -> "metadata/"+path.getFileName().toString())
                .collect(Collectors.toList()));

        model.addAttribute("audiofiles", storageService.loadAll2(audiofileNames).map(
                path -> "metadata/"+path.getFileName().toString())
                .collect(Collectors.toList()));
        model.addAttribute("vidoefiles", storageService.loadAll2(videofileNames).map(
                path -> "metadata/"+path.getFileName().toString())
                .collect(Collectors.toList()));
        List<File> imageFile = fileService.findFileByuser_idAndtype(".jpg");
        List<File> audioFile = fileService.findFileByuser_idAndtype(".mp3");
        List<File> videoFile = fileService.findFileByuser_idAndtype(".mp4");
        model.addAttribute("numberImage",imageFile.size());
        model.addAttribute("numberAudio",audioFile.size());
        model.addAttribute("numberVideo",videoFile.size());
        return "viewMetadata";
    }

//    @GetMapping("/metadata/{filename:.+}")
//    @ResponseBody
//    public String viewMetadata(@PathVariable String filename, Model model){
//        List<File> file = fileService.findFileByfile_name(filename);
//
//        model.addAttribute("metadatas",file.get(0).getMetadata());
//        return "displayMetadata";
//    }
@RequestMapping(value = {"/metadata/{filename:.+}"} , method = RequestMethod.GET)
public ModelAndView serveFile2(@PathVariable String filename){

    ModelAndView modelAndView = new ModelAndView();
    List<File> file = fileService.fildFileByfile_name_metadata(filename);
    System.out.println(filename);
    System.out.println(file.get(0).getMetadata());

    if(file.get(0).getType().equals(".jpg")){
        System.out.println(file.get(0).getType());
        modelAndView.addObject("JPG","True");

    }
    modelAndView.addObject("filename",filename);
    List<String> metas= Arrays.asList(file.get(0).getMetadata().split(","));
    System.out.println(metas);
    modelAndView.addObject("metadatas",metas);
    List<File> imageFile = fileService.findFileByuser_idAndtype(".jpg");
    List<File> audioFile = fileService.findFileByuser_idAndtype(".mp3");
    List<File> videoFile = fileService.findFileByuser_idAndtype(".mp4");
    modelAndView.addObject("numberImage",imageFile.size());
    modelAndView.addObject("numberAudio",audioFile.size());
    modelAndView.addObject("numberVideo",videoFile.size());
    modelAndView.setViewName("displayMetadata");
    return modelAndView;
    }
    @RequestMapping(method = RequestMethod.GET,value = "metadata"+"/"+"{filename:.+}/jpg")
    @ResponseBody
    public ResponseEntity<?> showJpgImage(@PathVariable String filename){
        try {
            System.out.println("xxxxxxxxxxxxxxxxx"+filename);
            Resource file = storageService.loadAsResource(filename);

            return ResponseEntity.ok()
                    .contentLength(file.contentLength())
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(file.getInputStream()));
        }catch (IOException e){
            return ResponseEntity.badRequest()
                    .body("Couldn;t find "+filename+" => "+e.getMessage());
        }
    }
}
