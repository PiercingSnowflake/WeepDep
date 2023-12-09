package com.example.weepdep.service;

import com.example.weepdep.model.User;
import com.example.weepdep.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // No password encryption for simplicity
        return userRepository.save(newUser);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

	public User getUserById(String userId) {
	        return userRepository.findById(userId).orElse(null);
	}
}
