package com.mocktest.mocktest.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
@Controller
public class PageController {
  @GetMapping("/")
   public String Homepage() {
      return "common/home";
   }
   @GetMapping("/dashboard")
   public String showDashboard(Model model, HttpSession session) {

    if (session.getAttribute("loggedInUser") == null) {
        return "redirect:/login";
    }

    
    return "common/dashboard";
}

   
}

