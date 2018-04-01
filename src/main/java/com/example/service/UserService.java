package com.example.service;

import com.example.model.User;

import java.util.List;

public interface UserService {
	public User findUserByEmail(String email);
	public List<User> findAllUser();
	public void saveUser(User user);
	public void editUser(User user, String newPassword);
	public void deleteUser(String email);
	public User getCurrentUser();
}
