package com.mocktest.mocktest.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mocktest.mocktest.entities.User;
import com.mocktest.mocktest.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Save / Register user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Get user by id
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
      public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User findByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
}

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
