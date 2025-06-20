package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 1. REST API controller
@RequestMapping("${api.path}prescription") // base path defined via application properties
public class PrescriptionController {

    // 2. Inject required services
    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;
    private final Service sharedService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService,
                                  AppointmentService appointmentService,
                                  Service sharedService) {
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
        this.sharedService = sharedService;
    }

    // 3. Save a new prescription
    @PostMapping("/save/{token}")
    public ResponseEntity<String> savePrescription(@RequestBody Prescription prescription,
                                                   @PathVariable String token) {
        ResponseEntity<String> validation = sharedService.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) return validation;

        try {
            Long appointmentId = prescription.getAppointmentId();
            appointmentService.changeStatus(appointmentId, 1); // Mark as completed (e.g., status = 1)
            prescriptionService.savePrescription(prescription);
            return ResponseEntity.ok("Prescription saved and appointment marked as completed.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving prescription.");
        }
    }

    // 4. Retrieve prescription by appointment ID
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(@PathVariable Long appointmentId,
                                             @PathVariable String token) {
        ResponseEntity<String> validation = sharedService.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) return validation;

        try {
            List<Prescription> prescriptions = prescriptionService.getByAppointmentId(appointmentId);
            return prescriptions.isEmpty()
                    ? ResponseEntity.status(404).body("No prescription found for this appointment.")
                    : ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch prescription data.");
        }
    }
}



//public class PrescriptionController {
    
// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("${api.path}prescription")` to set the base path for all prescription-related endpoints.
//    - This controller manages creating and retrieving prescriptions tied to appointments.


// 2. Autowire Dependencies:
//    - Inject `PrescriptionService` to handle logic related to saving and fetching prescriptions.
//    - Inject the shared `Service` class for token validation and role-based access control.
//    - Inject `AppointmentService` to update appointment status after a prescription is issued.


// 3. Define the `savePrescription` Method:
//    - Handles HTTP POST requests to save a new prescription for a given appointment.
//    - Accepts a validated `Prescription` object in the request body and a doctor’s token as a path variable.
//    - Validates the token for the `"doctor"` role.
//    - If the token is valid, updates the status of the corresponding appointment to reflect that a prescription has been added.
//    - Delegates the saving logic to `PrescriptionService` and returns a response indicating success or failure.


// 4. Define the `getPrescription` Method:
//    - Handles HTTP GET requests to retrieve a prescription by its associated appointment ID.
//    - Accepts the appointment ID and a doctor’s token as path variables.
//    - Validates the token for the `"doctor"` role using the shared service.
//    - If the token is valid, fetches the prescription using the `PrescriptionService`.
//    - Returns the prescription details or an appropriate error message if validation fails.


//}
