package com.vedkdu.smartpresence.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vedkdu.smartpresence.dao.UserDao;
import com.vedkdu.smartpresence.entity.User;
import com.vedkdu.smartpresence.model.LoginRequest;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	public User loginUser(LoginRequest request) {
		return dao.loginUser(request);
	}

	public User registerUser(User user) {
		return dao.registerUser(user);
	}

	public User getUserByName(String username) {
		return dao.getUserByName(username);
	}

	public List<User> getAllUser() {
		return dao.getAllUser();
	}

	public User updateUser(User user) {
		return dao.updateUser(user);
	}

	public String deleteUserById(String username) {
		return dao.deleteUserById(username);
	}

	public List<User> getAllAdmins() {
		return dao.getAllAdmins();
	}
	
	public List<User> getAllFaculties() {
		return dao.getAllFaculties();
	}

}