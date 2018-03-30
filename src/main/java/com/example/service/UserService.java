package com.example.service;

import com.example.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
	public void editUser(User user, String newPassword);
	public User getCurrentUser();
}
