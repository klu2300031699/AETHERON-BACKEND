package com.example.demo;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository repository;

    // CREATE
    public UserDetails createUser(UserDetails user, String confirmPassword) {
        if (!user.getPassword().equals(confirmPassword)) {
            throw new RuntimeException("Password and Confirm Password do not match");
        }
        // Check for duplicate email
        if (repository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        // Check for duplicate mobile number
        if (repository.findByMobileNumber(user.getMobileNumber()) != null) {
            throw new RuntimeException("Mobile number already exists");
        }
        // Set default role as "user"
        user.setRole("user");
        return repository.save(user);
    }

    // READ ALL
    public List<UserDetails> getAllUsers() {
        return repository.findAll();
    }

    // READ BY ID
    public UserDetails getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // UPDATE
    public UserDetails updateUser(Long id, UserDetails updatedUser) {
        UserDetails existingUser = getUserById(id);

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setMobileNumber(updatedUser.getMobileNumber());
        existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

        return repository.save(existingUser);
    }

    // DELETE
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    // LOGIN
    public UserDetails loginUser(String emailOrMobile, String password) {
        // Check for hardcoded admin login
        if ("gnanesh@gmail.com".equals(emailOrMobile) && "Gnanesh@1561".equals(password)) {
            UserDetails admin = new UserDetails();
            admin.setId(0L);
            admin.setFirstName("Gnanesh");
            admin.setLastName("Admin");
            admin.setEmail("gnanesh@gmail.com");
            admin.setMobileNumber("0000000000");
            admin.setRole("admin");
            return admin;
        }
        
        UserDetails user = repository.findByEmail(emailOrMobile);
        if (user == null) {
            user = repository.findByMobileNumber(emailOrMobile);
        }
        
        if (user == null || !user.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect email/mobile or password");
        }
        
        return user;
    }

    // Helper method to check if email exists
    public boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }

    // Helper method to save user (for admin registration)
    public UserDetails saveUser(UserDetails user) {
        // Check for duplicate email
        if (repository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        // Check for duplicate mobile number if provided
        if (user.getMobileNumber() != null && !user.getMobileNumber().isEmpty()) {
            if (repository.findByMobileNumber(user.getMobileNumber()) != null) {
                throw new RuntimeException("Mobile number already exists");
            }
        }
        return repository.save(user);
    }
}
