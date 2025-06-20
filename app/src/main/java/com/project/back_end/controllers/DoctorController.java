package com.project.back_end.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.project.back_end.services.Service;
import com.project.back_end.services.DoctorService;
import com.project.back_end.models.Doctor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private Service sharedService;

    // 3. Get availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        if (!sharedService.isValidToken(user, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }

        boolean available = doctorService.isDoctorAvailable(doctorId, date);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 4. Get all doctors
    @GetMapping
    public ResponseEntity<?> getDoctors() {
        return ResponseEntity.ok(Map.of("doctors", doctorService.getAllDoctors()));
    }

    // 5. Save new doctor
    @PostMapping("/register/{token}")
    public ResponseEntity<?> saveDoctor(@Valid @RequestBody Doctor doctor, @PathVariable String token) {
        if (!sharedService.isAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access.");
        }

        if (doctorService.exists(doctor)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor already exists.");
        }

        doctorService.saveDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body("Doctor registered.");
    }

    // 6. Doctor login
    @PostMapping("/login")
    public ResponseEntity<?> doctorLogin(@Valid @RequestBody Login login) {
        return ResponseEntity.ok(doctorService.authenticate(login));
    }

    // 7. Update doctor
    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateDoctor(@Valid @RequestBody Doctor doctor, @PathVariable String token) {
        if (!sharedService.isAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized.");
        }

        if (!doctorService.existsById(doctor.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }

        doctorService.updateDoctor(doctor);
        return ResponseEntity.ok("Doctor updated.");
    }

    // 8. Delete doctor
    @DeleteMapping("/delete/{id}/{token}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id, @PathVariable String token) {
        if (!sharedService.isAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized.");
        }

        if (!doctorService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }

        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor deleted.");
    }

    // 9. Filter doctors
    @GetMapping("/filter/{name}/{time}/{specialty}")
    public ResponseEntity<?> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String specialty) {

        List<Doctor> filtered = sharedService.filterDoctors(name, time, specialty);
        return ResponseEntity.ok(filtered);
    }
}


//public class DoctorController {

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST controller that serves JSON responses.
//    - Use `@RequestMapping("${api.path}doctor")` to prefix all endpoints with a configurable API path followed by "doctor".
//    - This class manages doctor-related functionalities such as registration, login, updates, and availability.


// 2. Autowire Dependencies:
//    - Inject `DoctorService` for handling the core logic related to doctors (e.g., CRUD operations, authentication).
//    - Inject the shared `Service` class for general-purpose features like token validation and filtering.


// 3. Define the `getDoctorAvailability` Method:
//    - Handles HTTP GET requests to check a specific doctor’s availability on a given date.
//    - Requires `user` type, `doctorId`, `date`, and `token` as path variables.
//    - First validates the token against the user type.
//    - If the token is invalid, returns an error response; otherwise, returns the availability status for the doctor.


// 4. Define the `getDoctor` Method:
//    - Handles HTTP GET requests to retrieve a list of all doctors.
//    - Returns the list within a response map under the key `"doctors"` with HTTP 200 OK status.


// 5. Define the `saveDoctor` Method:
//    - Handles HTTP POST requests to register a new doctor.
//    - Accepts a validated `Doctor` object in the request body and a token for authorization.
//    - Validates the token for the `"admin"` role before proceeding.
//    - If the doctor already exists, returns a conflict response; otherwise, adds the doctor and returns a success message.


// 6. Define the `doctorLogin` Method:
//    - Handles HTTP POST requests for doctor login.
//    - Accepts a validated `Login` DTO containing credentials.
//    - Delegates authentication to the `DoctorService` and returns login status and token information.


// 7. Define the `updateDoctor` Method:
//    - Handles HTTP PUT requests to update an existing doctor's information.
//    - Accepts a validated `Doctor` object and a token for authorization.
//    - Token must belong to an `"admin"`.
//    - If the doctor exists, updates the record and returns success; otherwise, returns not found or error messages.


// 8. Define the `deleteDoctor` Method:
//    - Handles HTTP DELETE requests to remove a doctor by ID.
//    - Requires both doctor ID and an admin token as path variables.
//    - If the doctor exists, deletes the record and returns a success message; otherwise, responds with a not found or error message.


// 9. Define the `filter` Method:
//    - Handles HTTP GET requests to filter doctors based on name, time, and specialty.
//    - Accepts `name`, `time`, and `speciality` as path variables.
//    - Calls the shared `Service` to perform filtering logic and returns matching doctors in the response.


//}
