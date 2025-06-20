package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.models.AvailableTime;
import com.project.back_end.repo.*;
import com.project.back_end.TokenService;
import com.project.back_end.DTO.AppointmentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service // 1. Mark this class as a Spring-managed service
public class Service {

    private final AdminRepository adminRepo;
    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;
    private final TokenService tokenService;
    private final PatientService patientService;

    // 2. Constructor Injection
    public Service(AdminRepository adminRepo,
                   DoctorRepository doctorRepo,
                   PatientRepository patientRepo,
                   TokenService tokenService,
                   PatientService patientService) {
        this.adminRepo = adminRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.tokenService = tokenService;
        this.patientService = patientService;
    }

    // 3. Validate token for any user role
    public ResponseEntity<String> validateToken(String token, String role) {
        if (tokenService.validateToken(token, role)) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    // 4. Validate Admin login
    public ResponseEntity<String> validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepo.findByUsername(username);
            if (admin == null || !admin.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
            String token = tokenService.generateToken(username);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }

    // 5. Filter doctors
    public List<Doctor> filterDoctor(String name, String time, String specialty) {
        if (name != null && time != null && specialty != null) {
            return doctorRepo.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCaseAndAvailableTimes_StartTime(
                    name, specialty, LocalTime.parse(time));
        } else if (name != null && specialty != null) {
            return doctorRepo.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        } else if (time != null && specialty != null) {
            return doctorRepo.findByAvailableTimes_StartTimeAndSpecialtyIgnoreCase(
                    LocalTime.parse(time), specialty);
        } else if (specialty != null) {
            return doctorRepo.findBySpecialtyIgnoreCase(specialty);
        } else {
            return doctorRepo.findAll();
        }
    }

    // 6. Validate appointment time
    public int validateAppointment(Long doctorId, LocalDate date, String startTime) {
        Optional<Doctor> optional = doctorRepo.findById(doctorId);
        if (optional.isEmpty()) {
            return -1;
        }
        Doctor doctor = optional.get();
        LocalTime requestedTime = LocalTime.parse(startTime);
        List<AvailableTime> timeSlots = doctor.getAvailableTimesForDate(date);

        for (AvailableTime slot : timeSlots) {
            if (slot.getStartTime().equals(requestedTime)) {
                return 1; // Time is available
            }
        }
        return 0; // Time not found
    }

    // 7. Validate uniqueness of patient
    public boolean validatePatient(String email, String phone) {
        return patientRepo.findByEmailOrPhone(email, phone) == null;
    }

    // 8. Validate patient login
    public ResponseEntity<String> validatePatientLogin(String email, String password) {
        try {
            Patient patient = patientRepo.findByEmail(email);
            if (patient == null || !patient.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
            String token = tokenService.generateToken(email);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }

    // 9. Filter patient appointments based on condition and/or doctor
    public List<AppointmentDTO> filterPatient(String token, String condition, String doctorName) {
        Patient patient = patientService.getPatientDetails(token);
        if (patient == null) {
            return List.of(); // Or throw an exception if preferred
        }
        Long patientId = patient.getId();

        if (condition != null && doctorName != null) {
            return patientService.filterByDoctorAndCondition(doctorName, patientId, condition);
        } else if (condition != null) {
            return patientService.filterByCondition(patientId, condition);
        } else if (doctorName != null) {
            return patientService.filterByDoctor(doctorName, patientId);
        } else {
            return patientService.getPatientAppointment(patientId);
        }
    }
}

//public class Service {
// 1. **@Service Annotation**
// The @Service annotation marks this class as a service component in Spring. This allows Spring to automatically detect it through component scanning
// and manage its lifecycle, enabling it to be injected into controllers or other services using @Autowired or constructor injection.

// 2. **Constructor Injection for Dependencies**
// The constructor injects all required dependencies (TokenService, Repositories, and other Services). This approach promotes loose coupling, improves testability,
// and ensures that all required dependencies are provided at object creation time.

// 3. **validateToken Method**
// This method checks if the provided JWT token is valid for a specific user. It uses the TokenService to perform the validation.
// If the token is invalid or expired, it returns a 401 Unauthorized response with an appropriate error message. This ensures security by preventing
// unauthorized access to protected resources.

// 4. **validateAdmin Method**
// This method validates the login credentials for an admin user.
// - It first searches the admin repository using the provided username.
// - If an admin is found, it checks if the password matches.
// - If the password is correct, it generates and returns a JWT token (using the admin’s username) with a 200 OK status.
// - If the password is incorrect, it returns a 401 Unauthorized status with an error message.
// - If no admin is found, it also returns a 401 Unauthorized.
// - If any unexpected error occurs during the process, a 500 Internal Server Error response is returned.
// This method ensures that only valid admin users can access secured parts of the system.

// 5. **filterDoctor Method**
// This method provides filtering functionality for doctors based on name, specialty, and available time slots.
// - It supports various combinations of the three filters.
// - If none of the filters are provided, it returns all available doctors.
// This flexible filtering mechanism allows the frontend or consumers of the API to search and narrow down doctors based on user criteria.

// 6. **validateAppointment Method**
// This method validates if the requested appointment time for a doctor is available.
// - It first checks if the doctor exists in the repository.
// - Then, it retrieves the list of available time slots for the doctor on the specified date.
// - It compares the requested appointment time with the start times of these slots.
// - If a match is found, it returns 1 (valid appointment time).
// - If no matching time slot is found, it returns 0 (invalid).
// - If the doctor doesn’t exist, it returns -1.
// This logic prevents overlapping or invalid appointment bookings.

// 7. **validatePatient Method**
// This method checks whether a patient with the same email or phone number already exists in the system.
// - If a match is found, it returns false (indicating the patient is not valid for new registration).
// - If no match is found, it returns true.
// This helps enforce uniqueness constraints on patient records and prevent duplicate entries.

// 8. **validatePatientLogin Method**
// This method handles login validation for patient users.
// - It looks up the patient by email.
// - If found, it checks whether the provided password matches the stored one.
// - On successful validation, it generates a JWT token and returns it with a 200 OK status.
// - If the password is incorrect or the patient doesn't exist, it returns a 401 Unauthorized with a relevant error.
// - If an exception occurs, it returns a 500 Internal Server Error.
// This method ensures only legitimate patients can log in and access their data securely.

// 9. **filterPatient Method**
// This method filters a patient's appointment history based on condition and doctor name.
// - It extracts the email from the JWT token to identify the patient.
// - Depending on which filters (condition, doctor name) are provided, it delegates the filtering logic to PatientService.
// - If no filters are provided, it retrieves all appointments for the patient.
// This flexible method supports patient-specific querying and enhances user experience on the client side.


//}
