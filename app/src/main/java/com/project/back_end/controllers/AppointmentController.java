package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController // 1. Declare as a REST controller
@RequestMapping("/appointments")
public class AppointmentController {

    // 2. Autowire dependencies
    private final AppointmentService appointmentService;
    private final Service sharedService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, Service sharedService) {
        this.appointmentService = appointmentService;
        this.sharedService = sharedService;
    }

    // 3. Get appointments for doctor with optional filtering
    @GetMapping("/{token}/{date}/{patientName}")
    public ResponseEntity<?> getAppointments(@PathVariable String token,
                                             @PathVariable String date,
                                             @PathVariable(required = false) String patientName) {
        ResponseEntity<String> validation = sharedService.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        try {
            LocalDate parsedDate = LocalDate.parse(date);
            Long doctorId = sharedService.getDoctorIdFromToken(token);
            return ResponseEntity.ok(appointmentService.getAppointments(doctorId, parsedDate, patientName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date or request format.");
        }
    }

    // 4. Book appointment (POST)
    @PostMapping("/book/{token}")
    public ResponseEntity<String> bookAppointment(@RequestBody Appointment appointment,
                                                  @PathVariable String token) {
        ResponseEntity<String> validation = sharedService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        int availability = sharedService.validateAppointment(
                appointment.getDoctor().getId(),
                appointment.getAppointmentTime().toLocalDate(),
                appointment.getAppointmentTime().toLocalTime().toString()
        );

        if (availability == -1) return ResponseEntity.badRequest().body("Invalid doctor ID.");
        if (availability == 0) return ResponseEntity.status(409).body("Appointment slot unavailable.");

        int result = appointmentService.bookAppointment(appointment);
        return (result == 1)
                ? ResponseEntity.ok("Appointment booked successfully.")
                : ResponseEntity.status(500).body("Failed to book appointment.");
    }

    // 5. Update existing appointment (PUT)
    @PutMapping("/update/{token}")
    public ResponseEntity<String> updateAppointment(@RequestBody Appointment updatedAppt,
                                                    @PathVariable String token) {
        ResponseEntity<String> validation = sharedService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        Long patientId = sharedService.getPatientIdFromToken(token);
        String result = appointmentService.updateAppointment(updatedAppt.getId(), updatedAppt, patientId);

        return result.equalsIgnoreCase("Appointment updated successfully")
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }

    // 6. Cancel appointment (DELETE)
    @DeleteMapping("/cancel/{id}/{token}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id,
                                                    @PathVariable String token) {
        ResponseEntity<String> validation = sharedService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        Long patientId = sharedService.getPatientIdFromToken(token);
        String result = appointmentService.cancelAppointment(id, patientId);

        return result.equals("Appointment canceled")
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(403).body(result);
    }
}

//public class AppointmentController {

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("/appointments")` to set a base path for all appointment-related endpoints.
//    - This centralizes all routes that deal with booking, updating, retrieving, and canceling appointments.


// 2. Autowire Dependencies:
//    - Inject `AppointmentService` for handling the business logic specific to appointments.
//    - Inject the general `Service` class, which provides shared functionality like token validation and appointment checks.


// 3. Define the `getAppointments` Method:
//    - Handles HTTP GET requests to fetch appointments based on date and patient name.
//    - Takes the appointment date, patient name, and token as path variables.
//    - First validates the token for role `"doctor"` using the `Service`.
//    - If the token is valid, returns appointments for the given patient on the specified date.
//    - If the token is invalid or expired, responds with the appropriate message and status code.


// 4. Define the `bookAppointment` Method:
//    - Handles HTTP POST requests to create a new appointment.
//    - Accepts a validated `Appointment` object in the request body and a token as a path variable.
//    - Validates the token for the `"patient"` role.
//    - Uses service logic to validate the appointment data (e.g., check for doctor availability and time conflicts).
//    - Returns success if booked, or appropriate error messages if the doctor ID is invalid or the slot is already taken.


// 5. Define the `updateAppointment` Method:
//    - Handles HTTP PUT requests to modify an existing appointment.
//    - Accepts a validated `Appointment` object and a token as input.
//    - Validates the token for `"patient"` role.
//    - Delegates the update logic to the `AppointmentService`.
//    - Returns an appropriate success or failure response based on the update result.


// 6. Define the `cancelAppointment` Method:
//    - Handles HTTP DELETE requests to cancel a specific appointment.
//    - Accepts the appointment ID and a token as path variables.
//    - Validates the token for `"patient"` role to ensure the user is authorized to cancel the appointment.
//    - Calls `AppointmentService` to handle the cancellation process and returns the result.


//}
