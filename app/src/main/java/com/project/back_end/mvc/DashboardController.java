package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.project.back_end.services.CommonService;

@Controller
public class DashboardController {

    @Autowired
    private CommonService commonService;

    // 3. Admin Dashboard Route
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        String validationMessage = commonService.validateToken(token, "admin");
        if (validationMessage == null || validationMessage.isEmpty()) {
            return "admin/adminDashboard";
        }
        return "redirect:/";
    }

    // 4. Doctor Dashboard Route
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        String validationMessage = commonService.validateToken(token, "doctor");
        if (validationMessage == null || validationMessage.isEmpty()) {
            return "doctor/doctorDashboard";
        }
        return "redirect:/";
    }
}
//public class DashboardController {

// 1. Set Up the MVC Controller Class:
//    - Annotate the class with `@Controller` to indicate that it serves as an MVC controller returning view names (not JSON).
//    - This class handles routing to admin and doctor dashboard pages based on token validation.


// 2. Autowire the Shared Service:
//    - Inject the common `Service` class, which provides the token validation logic used to authorize access to dashboards.


// 3. Define the `adminDashboard` Method:
//    - Handles HTTP GET requests to `/adminDashboard/{token}`.
//    - Accepts an admin's token as a path variable.
//    - Validates the token using the shared service for the `"admin"` role.
//    - If the token is valid (i.e., no errors returned), forwards the user to the `"admin/adminDashboard"` view.
//    - If invalid, redirects to the root URL, likely the login or home page.


// 4. Define the `doctorDashboard` Method:
//    - Handles HTTP GET requests to `/doctorDashboard/{token}`.
//    - Accepts a doctor's token as a path variable.
//    - Validates the token using the shared service for the `"doctor"` role.
//    - If the token is valid, forwards the user to the `"doctor/doctorDashboard"` view.
//    - If the token is invalid, redirects to the root URL.


//}
