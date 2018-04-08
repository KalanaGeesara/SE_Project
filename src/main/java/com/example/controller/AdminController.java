package com.example.controller;

import com.example.model.File;
import com.example.model.User;
import com.example.service.AdminService;
import com.example.service.FileService;
import com.example.service.SolrService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private SolrService solrService;

    @Autowired
    private AdminService adminService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value="/adminHome", method = RequestMethod.GET)
    public ModelAndView adminHome(){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getCurrentUser();
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");

        modelAndView.setViewName("/home");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value="/admin/users", method = RequestMethod.GET)
    public ModelAndView adminUsers(@RequestParam(required=false) String dd){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getCurrentUser();
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");

        if (dd!=null){
            ArrayList<ArrayList<String>> aObject;
            aObject = new ArrayList<ArrayList<String>>();
            User users = userService.findUserByEmail(dd);
            List<User> userList = new ArrayList<>();
            userList.add(users);
            aObject = adminService.getUserForAdmin(userList);
            modelAndView.addObject("searchUser",aObject);
        }
        ArrayList<ArrayList<String>> aObject;
// Create the 2D array list
        aObject = new ArrayList<ArrayList<String>>();

// Add an element to the first dimension
        List<User> userList = userService.findAllUser();
        aObject = adminService.getUserForAdmin(userList);
        modelAndView.addObject("files",aObject);
        modelAndView.setViewName("/admin/users");
        return modelAndView;
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value="/admin/files", method = RequestMethod.GET)
    public ModelAndView adminFiles(@RequestParam(required=false) String dd){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getCurrentUser();
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        System.out.println(dd);

        if(dd!=null){
            ArrayList<ArrayList<String>> aObject;
            aObject = new ArrayList<ArrayList<String>>();
            List<File> fileList = fileService.findFileByUserIdGiven(Integer.parseInt(dd));
            aObject = adminService.getFilesForAdmin(fileList);
            modelAndView.addObject("filesByUser",aObject);
//            modelAndView.addObject("dd","hhhdh");
        }
        ArrayList<ArrayList<String>> aObject;
// Create the 2D array list
        aObject = new ArrayList<ArrayList<String>>();
// Add an element to the first dimension
        List<File> fileList = fileService.findAllFiles();
        aObject =adminService.getFilesForAdmin(fileList);
        modelAndView.addObject("files",aObject);
        modelAndView.setViewName("/admin/files");
        return modelAndView;
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value="/admin/filesOverview", method = RequestMethod.GET)
    public ModelAndView adminFile(){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getCurrentUser();
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        Integer northeastSales = fileService.findAllFilesByType(".jpg").size();
        Integer westSales = fileService.findAllFilesByType(".mp3").size();
        Integer midwestSales = fileService.findAllFilesByType(".mp4").size();

        modelAndView.addObject("northeastSales", northeastSales);
        modelAndView.addObject("midwestSales", midwestSales);
        modelAndView.addObject("westSales", westSales);

        //now add sales by lure type
        List<Integer> inshoreSales = Arrays.asList(4074, 3455, 4112);
        List<Integer> nearshoreSales = Arrays.asList(3222, 3011, 3788);
        List<Integer> offshoreSales = Arrays.asList(7811, 7098, 6455);

        modelAndView.addObject("inshoreSales", inshoreSales);
        modelAndView.addObject("nearshoreSales", nearshoreSales);
        modelAndView.addObject("offshoreSales", offshoreSales);


        modelAndView.setViewName("admin/chart");
        return modelAndView;
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = {"/admin/user/delete/{email:.+}"} , method = RequestMethod.GET)
    public ModelAndView deleteUser(@PathVariable String email){

        ModelAndView modelAndView = new ModelAndView();
        userService.deleteUser(email);
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = {"/admin/file/delete/{filename:.+}"} , method = RequestMethod.GET)
    public ModelAndView deleteFile(@PathVariable String filename){

        ModelAndView modelAndView = new ModelAndView();
        List<File> file = fileService.fildFileByfile_name_metadata(filename);
        fileService.deleteFile(filename);
        solrService.deleteFromSolr(file.get(0).getFile_id());
        modelAndView.setViewName("redirect:/admin/files");


        return modelAndView;
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = {"/admin/file/search"} , method = RequestMethod.POST)
    public ModelAndView searchFile(@RequestParam String search){
        System.out.println("ddddddddddddddddddd");
System.out.println(search);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("dd",search);
        modelAndView.setViewName("redirect:/admin/files");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = {"/admin/user/search"} , method = RequestMethod.POST)
    public ModelAndView searchUser(@RequestParam String search){
        System.out.println("ddddddddddddddddddd");
        System.out.println(search);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("dd",search);
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }
}
