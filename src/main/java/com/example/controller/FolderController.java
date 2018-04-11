package com.example.controller;

import com.example.model.File;
import com.example.model.User;
import com.example.service.FileService;
import com.example.service.FolderService;
import com.example.service.FolderinfoService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FolderController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private FolderinfoService folderinfoService;

    @GetMapping("folderManagement")
    public ModelAndView testGet(@RequestParam(required=false) String folderName){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getCurrentUser();
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
//		List<String> arr = new ArrayList<>();
//		arr.add("a");
//		arr.add("b");
//		arr.add("c");
//		modelAndView.addObject("allTypes",arr);

        List<String> arr = folderService.getFolderNames();
        if(folderName!=null){
            if(!arr.contains(folderName)){
                folderService.saveFolder(folderName);
                modelAndView.addObject("message","Folder Successfully Added");
            }
            else{
                modelAndView.addObject("message","This folder Name Already exist");
            }
        }
        List<String> arr2 = folderService.getFolderNames();
        ArrayList<ArrayList<String>> aObject;
// Create the 2D array list
        aObject = new ArrayList<ArrayList<String>>();
        for(int i=0;i<arr2.size();i++){
            aObject.add(new ArrayList<String>());
        }
        for(int j=0;j<arr2.size();j++){
//            aObject.get(j).add(new String("Quarks"));
            String foldername = arr2.get(j);
            String folderlink = "folder/"+foldername;
            aObject.get(j).add(folderlink);
            aObject.get(j).add(foldername);
        }
        modelAndView.addObject("folders",aObject);
        modelAndView.addObject("numberImage",fileService.findFileNumberByuser_idAndtype(".jpg"));
        modelAndView.addObject("numberAudio",fileService.findFileNumberByuser_idAndtype(".mp3"));
        modelAndView.addObject("numberVideo",fileService.findFileNumberByuser_idAndtype(".mp4"));
        modelAndView.setViewName("folderManagement");
        return modelAndView;
    }

    @RequestMapping(value = {"/remove/{filename:.+}/{folderName}"} , method = RequestMethod.GET)
    public ModelAndView removeFileFromFolder(@PathVariable String filename, @PathVariable String folderName) {

        ModelAndView modelAndView = new ModelAndView();
        folderinfoService.removeFile(filename);
        String redirect = "redirect:/folder/"+folderName;
        modelAndView.setViewName(redirect);
        return modelAndView;
    }
    @RequestMapping(value = "/aa",method = RequestMethod.POST)
    public ModelAndView testPost(@RequestParam String folderName){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("folderName",folderName);
        modelAndView.setViewName("redirect:/folderManagement");
        return modelAndView;
    }

    @RequestMapping(value = {"/folder/{foldername:.+}"} , method = RequestMethod.GET)
    public ModelAndView infoFile(@PathVariable String foldername){
        System.out.println("################################################");
        System.out.println(foldername);
        System.out.println("################################################");
        ModelAndView modelAndView = new ModelAndView();
        List<File> fileList = fileService.findFileByuser_id();
        List<String> fileNames = new ArrayList<>();
        for(int i=0; i<fileList.size();i++){
            fileNames.add(fileList.get(i).getFileName());
        }
        ArrayList<ArrayList<String>> aObject;
        aObject = folderinfoService.getFiles(foldername);
        modelAndView.addObject("folderName",foldername);
        modelAndView.addObject("Files",fileNames);
        modelAndView.addObject("FileInfos",aObject);
        modelAndView.addObject("numberImage",fileService.findFileNumberByuser_idAndtype(".jpg"));
        modelAndView.addObject("numberAudio",fileService.findFileNumberByuser_idAndtype(".mp3"));
        modelAndView.addObject("numberVideo",fileService.findFileNumberByuser_idAndtype(".mp4"));
        modelAndView.setViewName("folder");
        return modelAndView;
    }

    @RequestMapping(value = {"/folder/addFile"}, method = RequestMethod.POST)
    public ModelAndView addFileToFolder(@RequestParam String addFileName, String folderName){
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(addFileName);
        System.out.println(folderName);
        folderinfoService.saveFileToFolder(folderName,addFileName);
        String s ="redirect:/folder/"+folderName;
        modelAndView.setViewName(s);
        return modelAndView;
    }

}
