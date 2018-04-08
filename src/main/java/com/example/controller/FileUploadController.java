package com.example.controller;

import com.example.model.File;
import com.example.model.User;
//import com.example.service.FileService;
import com.example.repository.FileRepository;
import com.example.service.FileService;
import com.example.service.SolrService;
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
    private SolrService solrService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/imageView")
    public String viewImageFiles(Model model) throws IOException {
        List<String> fileNames = fileService.findFilesByType(".jpg");
        //get the file locations of image files to another list
        List<String> fileLocation = storageService.loadAll2(fileNames).map(path -> "files/"+path.getFileName().toString()).collect(Collectors.toList());
        ArrayList<ArrayList<String>> aObject;
// Create the 2D array list
        aObject = new ArrayList<ArrayList<String>>();
        aObject = fileService.getFilePaths(fileLocation);
        model.addAttribute("numberImage",fileService.findFileNumberByuser_idAndtype(".jpg"));
        model.addAttribute("numberAudio",fileService.findFileNumberByuser_idAndtype(".mp3"));
        model.addAttribute("numberVideo",fileService.findFileNumberByuser_idAndtype(".mp4"));
        model.addAttribute("files",aObject);
        return "imageView";
    }

    @GetMapping("/audioView")
    public String viewAudioFiles(Model model) throws IOException {
        List<String> fileNames = fileService.findFilesByType(".mp3");
        //get the file locations of image files to another list
        List<String> fileLocation = storageService.loadAll2(fileNames).map(path -> "files/"+path.getFileName().toString()).collect(Collectors.toList());
        ArrayList<ArrayList<String>> aObject;
        aObject = fileService.getFilePaths(fileLocation);
        model.addAttribute("numberImage",fileService.findFileNumberByuser_idAndtype(".jpg"));
        model.addAttribute("numberAudio",fileService.findFileNumberByuser_idAndtype(".mp3"));
        model.addAttribute("numberVideo",fileService.findFileNumberByuser_idAndtype(".mp4"));
        model.addAttribute("files",aObject);
        return "audioView";
    }

    @RequestMapping(value = "**/uploadForm", method = RequestMethod.GET)
    public ModelAndView fileUpload(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        User user = userService.getCurrentUser();
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ") " + user.getRoles() + " ");
        modelAndView.addObject("numberImage",fileService.findFileNumberByuser_idAndtype(".jpg"));
        modelAndView.addObject("numberAudio",fileService.findFileNumberByuser_idAndtype(".mp3"));
        modelAndView.addObject("numberVideo",fileService.findFileNumberByuser_idAndtype(".mp4"));
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
        int user_id = file.get(0).getUserId();
        User currentUser = userService.getCurrentUser();
        if(user_id==currentUser.getId()){
            String currentAuthor = file.get(0).getAuthor();
            String originalFileName = file.get(0).getFileName();
            String currentFileName = file.get(0).getName();
            String keywords = file.get(0).getKeywords();
            String fileType = file.get(0).getType();
            if(fileType.equals(".jpg")){
                modelAndView.addObject("fileType","images");
                modelAndView.addObject("fileTypeLink","/imageView");
            }
            else if(fileType.equals(".mp3")){
                modelAndView.addObject("fileType","audio");
                modelAndView.addObject("fileTypeLink","/audioView");
            }
            else if(fileType.equals(".mp4")){
                modelAndView.addObject("fileType","video");
                modelAndView.addObject("fileTypeLink","/videoView");
            }
            modelAndView.addObject("file",new File());
            modelAndView.addObject("filename",originalFileName);
            modelAndView.addObject("newfilename",currentFileName);
            modelAndView.addObject("fileauthor",currentAuthor);
            modelAndView.addObject("keywords",keywords);

            modelAndView.addObject("numberImage",fileService.findFileNumberByuser_idAndtype(".jpg"));
            modelAndView.addObject("numberAudio",fileService.findFileNumberByuser_idAndtype(".mp3"));
            modelAndView.addObject("numberVideo",fileService.findFileNumberByuser_idAndtype(".mp4"));
            modelAndView.setViewName("editFile");
        }else {
            modelAndView.addObject("authenticatedMessage","You performed an unautherized action! Please Login AGAIN ");
            modelAndView.setViewName("login");
        }

        return modelAndView;
    }
    @RequestMapping(value = {"/delete/{filename:.+}"} , method = RequestMethod.GET)
    public ModelAndView deleteFile(@PathVariable String filename) throws IOException {

        ModelAndView modelAndView = new ModelAndView();

        List<File> file = fileService.fildFileByfile_name_metadata(filename);
        int user_id = file.get(0).getUserId();
        User currentUser = userService.getCurrentUser();
        if(user_id==currentUser.getId()){
            fileService.deleteFile(filename);
            solrService.deleteFromSolr(file.get(0).getFile_id());
            storageService.deleteFile(filename);
            if (file.get(0).getType().equals(".jpg")){
                modelAndView.setViewName("redirect:/imageView");
            }
            else if (file.get(0).getType().equals(".mp3")){
                modelAndView.setViewName("redirect:/audioView");
            }
            else if (file.get(0).getType().equals(".mp4")){
                modelAndView.setViewName("redirect:/videoView");
            }
        }else {
            modelAndView.addObject("authenticatedMessage","You performed an unautherized action! Please Login AGAIN ");
            modelAndView.setViewName("login");
        }

        return modelAndView;
    }

    @RequestMapping(value = {"/info/{filename:.+}"} , method = RequestMethod.GET)
    public ModelAndView infoFile(@PathVariable String filename){
        System.out.println("################################################");
        System.out.println(filename);
        System.out.println("################################################");
        ModelAndView modelAndView = new ModelAndView();
        List<File> fileList = fileService.findFileBySpaceFreename(filename);
        File file = fileList.get(0);
        System.out.println(file);
        modelAndView.addObject("firstname",file.getFileName());
        modelAndView.addObject("lastname",file.getName());
        modelAndView.addObject("email",file.getAuthor());
        modelAndView.addObject("filenumber",file.getKeywords());
        modelAndView.setViewName("fileInfo");
        return modelAndView;
    }
    @RequestMapping(value = {"/saveEditFileChanges"},method = RequestMethod.POST)
    public ModelAndView saveEditChanges(@RequestParam String originalname, String name, String author,String keywords,String privacy, @Valid File file, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        String extension = "";

        int i = originalname.lastIndexOf('.');
        if (i > 0) {
            extension = originalname.substring(i+1);
        }

        if(extension.equals("jpg")){
            modelAndView.addObject("fileType","images");
            modelAndView.addObject("fileTypeLink","/imageView");
        }
        else if(extension.equals("mp3")){
            modelAndView.addObject("fileType","audio");
            modelAndView.addObject("fileTypeLink","/audioView");
        }
        else if(extension.equals("mp4")){
            modelAndView.addObject("fileType","video");
            modelAndView.addObject("fileTypeLink","/videoView");
        }
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
            fileService.editFile(originalname, name, author, keywords,privacy);
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
    public String handleFileUpload(@RequestParam("file") MultipartFile file, String privacy,
                                   RedirectAttributes redirectAttributes) throws IOException, TikaException, SAXException{
        String extension = "";
System.out.println(privacy);
        int i = file.getOriginalFilename().lastIndexOf('.');
        if (i > 0) {
            extension = file.getOriginalFilename().substring(i+1);
        }
        extension.toLowerCase();
        if(extension.equals("jpg") || extension.equals("mp3")) {
            String filename = file.getOriginalFilename();
            List<File> fileExist = fileService.findFileByfile_name(filename);
            extension = "."+extension;
            if(!fileExist.isEmpty()){
                redirectAttributes.addFlashAttribute("message","You have already uploaded " + filename);
            }
            else {
                User user = userService.getCurrentUser();
                int users_id = user.getId();
                storageService.store(file , users_id);
                fileService.saveFile(file,extension,privacy);
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded " + file.getOriginalFilename() + "!");
            }
        }
        else {
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