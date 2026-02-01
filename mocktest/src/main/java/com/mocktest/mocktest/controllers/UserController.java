package com.mocktest.mocktest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.mocktest.mocktest.entities.User;
import com.mocktest.mocktest.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // ================= REGISTER =================

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userForm", new User());
        return "common/register";
    }

    @PostMapping("/register")
    public String processRegister(
            @ModelAttribute("userForm") User form,
            Model model) {

        if (userService.emailExists(form.getEmail())) {
            model.addAttribute("emailError", "Email already registered!");
            return "common/register";
        }

        User user = new User();
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setPassword(form.getPassword()); // encrypt later

        userService.saveUser(user);

        model.addAttribute("successMessage", "Registration successful! Please login.");
        return "common/login";
    }

    // ================= LOGIN =================

    // Show login page
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "common/login";
    }
   @PostMapping("/login")
public String processLogin(
        @ModelAttribute("user") User form,
        Model model,
        HttpSession session) {

    User dbUser = userService.findByEmail(form.getEmail());

    if (dbUser == null || !dbUser.getPassword().equals(form.getPassword())) {
        model.addAttribute("loginError", "Invalid email or password");
        return "common/login";
    }

    // ✅ CREATE SESSION
    session.setAttribute("loggedInUser", dbUser);
   
    // Optional: set session max inactive interval manually
    session.setMaxInactiveInterval(60 * 60 * 24 * 30); // 30 days

    return "redirect:/dashboard";
}
    
}
