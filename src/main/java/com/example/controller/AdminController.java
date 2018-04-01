package com.example.controller;

import com.example.model.File;
import com.example.model.User;
import com.example.service.FileService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

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
    public ModelAndView adminUsers(){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getCurrentUser();
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        ArrayList<ArrayList<String>> aObject;
// Create the 2D array list
        aObject = new ArrayList<ArrayList<String>>();

// Add an element to the first dimension
        List<User> userList = userService.findAllUser();
        for(int i=0;i<userList.size();i++){
            aObject.add(new ArrayList<String>());
        }
        for(int j=0;j<userList.size();j++){
//            aObject.get(j).add(new String("Quarks"));
            int userId = userList.get(j).getId();
            String firstName = userList.get(j).getName();
            String lastName = userList.get(j).getLastName();
            String email = userList.get(j).getEmail();
            int fileNumber = fileService.findFileNumberByUserId(userId);

            aObject.get(j).add(""+userId);
            aObject.get(j).add(firstName);
            aObject.get(j).add(lastName);
            aObject.get(j).add(email);
            aObject.get(j).add(""+fileNumber);
            aObject.get(j).add("delete/"+email);
        }
        modelAndView.addObject("files",aObject);
        modelAndView.setViewName("/admin/users");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value="/admin/files", method = RequestMethod.GET)
    public ModelAndView adminFile(){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getCurrentUser();
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        Integer northeastSales = 17089;
        Integer westSales = 10603;
        Integer midwestSales = 5223;
        Integer southSales = 10111;

        modelAndView.addObject("northeastSales", northeastSales);
        modelAndView.addObject("southSales", southSales);
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
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = {"/admin/delete/{email:.+}"} , method = RequestMethod.GET)
    public ModelAndView deleteUser(@PathVariable String email){

        ModelAndView modelAndView = new ModelAndView();
System.out.println(email);
        userService.deleteUser(email);
System.out.println(email);
        modelAndView.setViewName("redirect:/admin/users");


        return modelAndView;
    }
}
