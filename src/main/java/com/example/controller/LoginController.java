package com.example.controller;

import javax.validation.Valid;

//import com.example.model.Product;
import com.example.model.File;
import com.example.model.SolrSearch;
import com.example.repository.ProductRepository;
import com.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.User;
import com.example.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {


	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ProductRepository productRepository;

	@RequestMapping(value={"/login"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");

//		this.repository.save(new Product(1, "Nintendo Entertainment System"));
//		this.repository.save(new Product(2, "Sega Megadrive, KALA"));
//		this.repository.save(new Product(3, "Sony Playstation, kdk , kala, kks"));
//		productRepository.deleteAll();
		// fetch all

		System.out.println("Products found by findAll():");
		System.out.println("----------------------------");
//		for (SolrSearch product : this.productRepository.findByName( "File")) {
//			System.out.println(product.getOriginalName());
//		}
		int d = Integer.MIN_VALUE;
		System.out.println(d);
		return modelAndView;
	}

	@RequestMapping(value = {"/"} , method = RequestMethod.GET)
	public ModelAndView home(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}
	@RequestMapping(value = "/profileInfo",method = RequestMethod.GET)
	public ModelAndView profileInfo(){
		ModelAndView modelAndView = new ModelAndView();

		User user = userService.getCurrentUser();

		modelAndView.addObject("firstname",user.getName());
		modelAndView.addObject("lastname",user.getLastName());
		modelAndView.addObject("email",user.getEmail());
		List<File> files = fileService.findFileByuser_id();
		System.out.println(files.size());
		modelAndView.addObject("filenumber",files.size());
		modelAndView.setViewName("profileInfo");
		return modelAndView;
	}
	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.user",
							"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");
			
		}
		return modelAndView;
	}

	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView editProfile(){
		ModelAndView modelAndView = new ModelAndView();
		User user = userService.getCurrentUser();
		modelAndView.addObject("firstname",user.getName());
		modelAndView.addObject("lastname",user.getLastName());
		modelAndView.addObject("email",user.getEmail());
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//		System.out.println(user.getPassword());
//		System.out.println(bCryptPasswordEncoder.matches("1234562",user.getPassword()));
//		System.out.println(bCryptPasswordEncoder.equals(user.getPassword()));
		modelAndView.addObject("user",new User());
		modelAndView.addObject("numberImage",fileService.findFileNumberByuser_idAndtype(".jpg"));
		modelAndView.addObject("numberAudio",fileService.findFileNumberByuser_idAndtype(".mp3"));
		modelAndView.addObject("numberVideo",fileService.findFileNumberByuser_idAndtype(".mp4"));
		modelAndView.setViewName("editProfile");
		return modelAndView;
	}

	@RequestMapping(value = "/saveEditProfileChanges", method = RequestMethod.POST)
	public ModelAndView saveEditProfile(@RequestParam String name,String lastname,String currentpassword,String password, @Valid User user,BindingResult bindingResult){
		ModelAndView modelAndView = new ModelAndView();
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		User currentUser = userService.getCurrentUser();
		modelAndView.addObject("email",currentUser.getEmail());
//		if(newpassword.length()<=5){
//			bindingResult.rejectValue("newpassword","error.user","Password must be a least 6 characters long");
//		}
		if(bindingResult.hasErrors()){
			modelAndView.setViewName("editProfile");
		}else if ((bCryptPasswordEncoder.matches(currentpassword,currentUser.getPassword()))) {
			System.out.println("11111111111111111111111111");
			userService.editUser(user,password);
			System.out.println("22222222222222222222");
			modelAndView.addObject("successMessage", "User has been Updated successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("editProfile");

       } else {
           bindingResult.rejectValue("password","error.user","*Current Password is wrong");
           modelAndView.setViewName("editProfile");
       }

		return modelAndView;
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView userHome(){
		ModelAndView modelAndView = new ModelAndView();
		User user = userService.getCurrentUser();
		if(user.getRoleID()==1) {
			modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
			modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		}
		else {
			modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
			modelAndView.addObject("userMessage", "Content Available Only for Users with User Role");
			modelAndView.addObject("numberImage",fileService.findFileNumberByuser_idAndtype(".jpg"));
			modelAndView.addObject("numberAudio",fileService.findFileNumberByuser_idAndtype(".mp3"));
			modelAndView.addObject("numberVideo",fileService.findFileNumberByuser_idAndtype(".mp4"));
		}
		modelAndView.setViewName("home");
		return modelAndView;
	}


}
