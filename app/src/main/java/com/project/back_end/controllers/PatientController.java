package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 1. Define as REST API controller
@RequestMapping("/patient")
public class PatientController {

    // 2. Dependencies
    private final PatientService patientService;
    private final Service sharedService;

    @Autowired
    public PatientController(PatientService patientService, Service sharedService) {
        this.patientService = patientService;
        this.sharedService = sharedService;
    }

    // 3. Get patient profile by token
    @GetMapping("/profile/{token}")
    public ResponseEntity<?> getPatient(@PathVariable String token) {
        ResponseEntity<String> validation = sharedService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) return validation;

        Patient patient = patientService.getPatientDetails(token);
        return (patient != null)
                ? ResponseEntity.ok(patient)
                : ResponseEntity.status(404).body("Patient not found.");
    }

    // 4. Register a new patient
    @PostMapping("/register")
    public ResponseEntity<String> createPatient(@RequestBody Patient patient) {
        boolean isUnique = sharedService.validatePatient(patient.getEmail(), patient.getPhone());
        if (!isUnique) {
            return ResponseEntity.status(409).body("Patient already exists.");
        }

        int result = patientService.createPatient(patient);
        return (result == 1)
                ? ResponseEntity.ok("Patient registered successfully.")
                : ResponseEntity.status(500).body("Failed to register patient.");
    }

    // 5. Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        return sharedService.validatePatientLogin(login.getEmail(), login.getPassword());
    }

    // 6. Get all appointments for a patient (admin or patient access)
    @GetMapping("/appointments/{id}/{role}/{token}")
    public ResponseEntity<?> getPatientAppointment(@PathVariable Long id,
                                                   @PathVariable String role,
                                                   @PathVariable String token) {
        ResponseEntity<String> validation = sharedService.validateToken(token, role);
        if (!validation.getStatusCode().is2xxSuccessful()) return validation;

        List<AppointmentDTO> appointments = patientService.getPatientAppointment(id);
        return ResponseEntity.ok(appointments);
    }

    // 7. Filter appointments using optional doctor name and condition
    @GetMapping("/appointments/filter")
    public ResponseEntity<?> filterPatientAppointment(@RequestParam(required = false) String condition,
                                                      @RequestParam(required = false) String name,
                                                      @RequestParam String token) {
        ResponseEntity<String> validation = sharedService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) return validation;

        List<AppointmentDTO> filtered = sharedService.filterPatient(token, condition, name);
        return ResponseEntity.ok(filtered);
    }
}


//public class PatientController {

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller for patient-related operations.
//    - Use `@RequestMapping("/patient")` to prefix all endpoints with `/patient`, grouping all patient functionalities under a common route.


// 2. Autowire Dependencies:
//    - Inject `PatientService` to handle patient-specific logic such as creation, retrieval, and appointments.
//    - Inject the shared `Service` class for tasks like token validation and login authentication.


// 3. Define the `getPatient` Method:
//    - Handles HTTP GET requests to retrieve patient details using a token.
//    - Validates the token for the `"patient"` role using the shared service.
//    - If the token is valid, returns patient information; otherwise, returns an appropriate error message.


// 4. Define the `createPatient` Method:
//    - Handles HTTP POST requests for patient registration.
//    - Accepts a validated `Patient` object in the request body.
//    - First checks if the patient already exists using the shared service.
//    - If validation passes, attempts to create the patient and returns success or error messages based on the outcome.


// 5. Define the `login` Method:
//    - Handles HTTP POST requests for patient login.
//    - Accepts a `Login` DTO containing email/username and password.
//    - Delegates authentication to the `validatePatientLogin` method in the shared service.
//    - Returns a response with a token or an error message depending on login success.


// 6. Define the `getPatientAppointment` Method:
//    - Handles HTTP GET requests to fetch appointment details for a specific patient.
//    - Requires the patient ID, token, and user role as path variables.
//    - Validates the token using the shared service.
//    - If valid, retrieves the patient's appointment data from `PatientService`; otherwise, returns a validation error.


// 7. Define the `filterPatientAppointment` Method:
//    - Handles HTTP GET requests to filter a patient's appointments based on specific conditions.
//    - Accepts filtering parameters: `condition`, `name`, and a token.
//    - Token must be valid for a `"patient"` role.
//    - If valid, delegates filtering logic to the shared service and returns the filtered result.



//}


