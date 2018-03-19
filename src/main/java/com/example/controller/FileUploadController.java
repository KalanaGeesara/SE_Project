package com.example.controller;

import com.example.model.File;
import com.example.model.User;
//import com.example.service.FileService;
import com.example.repository.FileRepository;
import com.example.service.FileService;
import com.example.service.UserService;
import com.example.storage.StorageFileNotFoundException;
import com.example.service.StorageService;
//import org.apache.solr.client.solrj.SolrServerException;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/imageView")
    public String listUploadedFiles(Model model) throws IOException {
        List<String> fileNames = new ArrayList<String>();
        for(File i:fileService.findFileByuser_id()){                    //get all the files uploaded by logged in user

            // add only image files to a list
            if(i.getType().equals(".jpg")){
                fileNames.add(i.getFileName());
            }
        }
        //get the file locations of image files to another list
        List<String> fileLocation = storageService.loadAll2(fileNames).map(path -> "files/"+path.getFileName().toString()).collect(Collectors.toList());
        ArrayList<ArrayList<String>> aObject;
// Create the 2D array list
        aObject = new ArrayList<ArrayList<String>>();

// Add an element to the first dimension

        for(int i=0;i<fileLocation.size();i++){
            aObject.add(new ArrayList<String>());
        }
        for(int j=0;j<fileLocation.size();j++){
//            aObject.get(j).add(new String("Quarks"));
            String s = fileLocation.get(j);
            String [] part = s.split("/");
            String filePart = "files/"+part[1];
            String editPart = "edit/"+part[1];
            aObject.get(j).add(filePart);
            aObject.get(j).add(editPart);
        }
        List<File> imageFile = fileService.findFileByuser_idAndtype(".jpg");
        List<File> audioFile = fileService.findFileByuser_idAndtype(".mp3");
        List<File> videoFile = fileService.findFileByuser_idAndtype(".mp4");
        model.addAttribute("numberImage",imageFile.size());
        model.addAttribute("numberAudio",audioFile.size());
        model.addAttribute("numberVideo",videoFile.size());
        model.addAttribute("files",aObject);
        return "imageView";
    }

    @RequestMapping(value = "**/uploadForm", method = RequestMethod.GET)
    public ModelAndView fileUpload(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ") " + user.getRoles() + " ");
        List<File> imageFile = fileService.findFileByuser_idAndtype(".jpg");
        List<File> audioFile = fileService.findFileByuser_idAndtype(".mp3");
        List<File> videoFile = fileService.findFileByuser_idAndtype(".mp4");
        modelAndView.addObject("numberImage",imageFile.size());
        modelAndView.addObject("numberAudio",audioFile.size());
        modelAndView.addObject("numberVideo",videoFile.size());
        modelAndView.setViewName("uploadForm");
        return modelAndView;
    }
@RequestMapping(method = RequestMethod.GET,value = "images"+"/"+"{filename:.+}/raw")
@ResponseBody
public ResponseEntity<?> oneRawImage(@PathVariable String filename){
        try {
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
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = {"/edit/{filename:.+}"} , method = RequestMethod.GET)
    public ModelAndView editFile(@PathVariable String filename){

        ModelAndView modelAndView = new ModelAndView();
        List<File> file = fileService.fildFileByfile_name_metadata(filename);
        String currentAuthor = file.get(0).getAuthor();
        String originalFileName = file.get(0).getFileName();
        String currentFileName = file.get(0).getName();
        String keywords = file.get(0).getKeywords();
        modelAndView.addObject("file",new File());
        modelAndView.addObject("filename",originalFileName);
        modelAndView.addObject("newfilename",currentFileName);
        modelAndView.addObject("fileauthor",currentAuthor);
        modelAndView.addObject("keywords",keywords);
        List<File> imageFile = fileService.findFileByuser_idAndtype(".jpg");
        List<File> audioFile = fileService.findFileByuser_idAndtype(".mp3");
        List<File> videoFile = fileService.findFileByuser_idAndtype(".mp4");
        modelAndView.addObject("numberImage",imageFile.size());
        modelAndView.addObject("numberAudio",audioFile.size());
        modelAndView.addObject("numberVideo",videoFile.size());
        modelAndView.setViewName("editFile");
        return modelAndView;
    }

    @RequestMapping(value = {"/saveEditFileChanges"},method = RequestMethod.POST)
    public ModelAndView saveEditChanges(@RequestParam String originalname, String name, String author,String keywords, @Valid File file, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(originalname);
        System.out.println(name);
        System.out.println(author);
        if (name == "") {
            bindingResult
                    .rejectValue("name", "error.file",
                            "Name cannot be empty");
        }
        if (author == "") {
            bindingResult
                    .rejectValue("author", "error.file",
                            "author cannot be empty");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("filename",originalname);
            modelAndView.addObject("newfilename",name);
            modelAndView.addObject("fileauthor",author);
            modelAndView.addObject("keywords",keywords);
            modelAndView.setViewName("editFile");
        }
        else {
            fileService.editFile(originalname, name, author, keywords);
            modelAndView.addObject("file", new File());
            modelAndView.addObject("filename",originalname);
            modelAndView.addObject("newfilename",name);
            modelAndView.addObject("fileauthor",author);
            modelAndView.addObject("keywords",keywords);
            modelAndView.addObject("successMessage", "Changes added Successfully");
            modelAndView.setViewName("editFile");

        }
        return modelAndView;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException, TikaException, SAXException{
        String extension = "";

        int i = file.getOriginalFilename().lastIndexOf('.');
        if (i > 0) {
            extension = file.getOriginalFilename().substring(i+1);
        }
        extension.toLowerCase();
        System.out.println(extension);
        if(extension.equals("jpg") || extension.equals("JPG") || extension.equals("mp3")) {
            String filename = file.getOriginalFilename();
            List<File> fileExist = fileService.findFileByfile_name(filename);
            System.out.println(fileExist);
            extension = "."+extension;
//            System.out.println(fileExist.get(0).getFileName());
//            System.out.println(fileExist.get(0).getFile_path());
//            System.out.println(fileExist.get(0).getFile_id());
//            System.out.println(fileExist.get(0).getName());
//            System.out.println("msddfknsdfkfkfkdfkdndkdndkgdnkdngdknd");
//            System.out.println(fileExist.get(1).getFileName());
//            System.out.println(fileExist.get(1).getFile_path());
//            System.out.println(fileExist.get(1).getFile_id());
//            System.out.println(fileExist.get(1).getName());
            System.out.println("0");
            if(!fileExist.isEmpty()){
                System.out.println("1");
                redirectAttributes.addFlashAttribute("message","You have already uploaded " + filename);
            }
            else {
                System.out.println("2");
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User user = userService.findUserByEmail(auth.getName());

                int users_id = user.getId();
                storageService.store(file , users_id);

                fileService.saveFile(file,extension);
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded " + file.getOriginalFilename() + "!");
            }
        }
// else if(extension.equals("mp3")){
//            String filename = file.getOriginalFilename();
//            List<File> fileExist = fileService.findFileByfile_name(filename);
//            System.out.println(fileExist);
//            extension = "."+extension;
//            System.out.println("Fuuuuuuuuuuuuuuuuuuuuuuuuuuck");
//        }
        else {
            System.out.println("3");
            redirectAttributes.addFlashAttribute("message",
                    "Could not upload " + file.getOriginalFilename() + "!... Please upload a file of type jpg,mp3");
        }
        return "redirect:/uploadForm";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}