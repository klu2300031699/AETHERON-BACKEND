package com.example.demo;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "https://*.vercel.app", "https://teachytechie.com", "https://www.teachytechie.com"}, allowCredentials = "true")
public class UserDetailsController {

    @Autowired
    private UserDetailsService service;

    // CREATE
    @PostMapping("/create")
    public org.springframework.http.ResponseEntity<?> createUser(
            @RequestBody UserRequest request, HttpSession session) {
        try {
            UserDetails user = new UserDetails();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setMobileNumber(request.getMobileNumber());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            UserDetails created = service.createUser(user, request.getConfirmPassword());
            
            // Automatically create session for the newly registered user
            session.setAttribute("userId", created.getId());
            session.setAttribute("userEmail", created.getEmail());
            session.setAttribute("userRole", created.getRole());
            session.setAttribute("userFirstName", created.getFirstName());
            session.setAttribute("userMobileNumber", created.getMobileNumber());
            session.setMaxInactiveInterval(24 * 60 * 60); // 24 hours
            
            return org.springframework.http.ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return org.springframework.http.ResponseEntity
                .status(org.springframework.http.HttpStatus.CONFLICT)
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    // READ ALL
    @GetMapping("/all")
    public List<UserDetails> getAllUsers() {
        return service.getAllUsers();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public UserDetails getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public UserDetails updateUser(
            @PathVariable Long id,
            @RequestBody UserDetails user) {
        return service.updateUser(id, user);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return "User deleted successfully";
    }

    // LOGIN
    @PostMapping("/login")
    public org.springframework.http.ResponseEntity<?> loginUser(@RequestBody LoginRequest request, HttpSession session) {
        try {
            UserDetails user = service.loginUser(request.getEmailOrMobile(), request.getPassword());
            // Store user in session
            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userFirstName", user.getFirstName());
            session.setAttribute("userMobileNumber", user.getMobileNumber());
            session.setMaxInactiveInterval(24 * 60 * 60); // 24 hours
            
            return org.springframework.http.ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return org.springframework.http.ResponseEntity
                .status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    // CHECK SESSION
    @GetMapping("/session")
    public org.springframework.http.ResponseEntity<?> checkSession(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            UserDetails user = new UserDetails();
            user.setId(userId);
            user.setEmail((String) session.getAttribute("userEmail"));
            user.setRole((String) session.getAttribute("userRole"));
            user.setFirstName((String) session.getAttribute("userFirstName"));
            user.setMobileNumber((String) session.getAttribute("userMobileNumber"));
            return org.springframework.http.ResponseEntity.ok(user);
        }
        return org.springframework.http.ResponseEntity
            .status(org.springframework.http.HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("No active session"));
    }
    
    // LOGOUT
    @PostMapping("/logout")
    public org.springframework.http.ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return org.springframework.http.ResponseEntity.ok("Logged out successfully");
    }

    // REGISTER (for admin to add users)
    @PostMapping("/register")
    public org.springframework.http.ResponseEntity<?> registerUser(@RequestBody UserDetails userDetails) {
        try {
            // Check if user already exists
            if (service.emailExists(userDetails.getEmail())) {
                return org.springframework.http.ResponseEntity
                    .status(org.springframework.http.HttpStatus.CONFLICT)
                    .body("Email already exists");
            }
            
            // Set default role if not provided
            if (userDetails.getRole() == null || userDetails.getRole().isEmpty()) {
                userDetails.setRole("user");
            }
            
            // Save the user
            UserDetails savedUser = service.saveUser(userDetails);
            return org.springframework.http.ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity
                .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating user: " + e.getMessage());
        }
    }
}

// DTO class
class UserRequest {
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private String password;
    private String confirmPassword;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}

// Login DTO class
class LoginRequest {
    private String emailOrMobile;
    private String password;

    public String getEmailOrMobile() { return emailOrMobile; }
    public void setEmailOrMobile(String emailOrMobile) { this.emailOrMobile = emailOrMobile; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

// Error Response DTO class
class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
