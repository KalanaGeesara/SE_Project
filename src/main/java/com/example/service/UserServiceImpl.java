package com.example.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        user.setRoleID(2);
        Role userRole = roleRepository.findByRole("USER");
        System.out.println(userRole);
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}
	@Override
	public void editUser(User user,String newPassword){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = findUserByEmail(auth.getName());
		user.setEmail(currentUser.getEmail());
		user.setId(currentUser.getId());
		System.out.println(currentUser.getId());
		user.setRoles(currentUser.getRoles());
		System.out.println(currentUser.getRoles());
		user.setActive(currentUser.getActive());
		System.out.println(currentUser.getActive());
		user.setRoleID(currentUser.getRoleID());
		System.out.println(currentUser.getRoleID());
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		System.out.println(user.getPassword());
		userRepository.save(user);
	}

	@Override
	public User getCurrentUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		return user;
	}

}
