## Admin User Stories

User Story 1
Title:
As an Admin, I want to log into the portal with my username and password, so that I can manage the platform securely.
Acceptance Criteria:
- Admin is prompted for username and password upon visiting the portal.
- Only valid credentials allow access to the dashboard.
- Failed logins are logged, and an alert is triggered after 3 unsuccessful attempts.
Priority: High
Story Points: 3
Notes:
- Passwords must be encrypted; consider multi-factor authentication in future.

User Story 2
Title:
As an Admin, I want to log out of the portal, so that I can protect system access when I’m away.
Acceptance Criteria:
- Admin can l- og out manually via a visible logout option.
- Session is terminated securely and user is redirected to login page.
- System automatically logs out after a defined period of inactivity.
Priority: High
Story Points: 2
Notes:
- Implement session timeout policy (e.g., 15 minutes of inactivity).

User Story 3
Title:
As an Admin, I want to add doctors to the portal, so that patients can book appointments with verified professionals.
Acceptance Criteria:
- Admin can input doctor details - including name, specialty, availability, and credentials.
- Doctor profile is validated and stored in the database.
- Added doctor appears in the appointment booking module.
Priority: High
Story Points: 5
Notes:
- Duplicate email or license number should trigger a validation error.

User Story 4
Title:
As an Admin, I want to delete a doctor's profile, so that outdated or incorrect entries are removed from the system.
Acceptance Criteria:
- Admin can search and select a doctor’s profile for deletion.
- System prompts confirmation before deletion.
- Deleted doctor is removed from the visible roster and scheduling module.
Priority: Medium
Story Points: 3
Notes:
- Consider soft delete with status flag before permanent deletion.

User Story 5
Title:
As an Admin, I want to run a stored procedure in the MySQL CLI to get monthly appointment statistics, so that I can track system usage and trends.
Acceptance Criteria:
- Stored procedure returns number of appointments grouped by month.
- Admin receives output in a readable tabular format.
- Errors or anomalies in execution are logged and notified to admin.
Priority: Medium
Story Points: 8
Notes:
- Result should be exportable as CSV or viewable in dashboard analytics.


## Patient User Stories 

User Story 1
Title:
As a Patient, I want to view a list of doctors without logging in, so that I can explore options before registering.
Acceptance Criteria:
- Public-facing doctor directory is accessible without authentication.
- Doctor profiles display name, specialty, and availability summary.
- “Book Now” redirects unauthenticated users to the registration page.
Priority: Medium
Story Points: 3
Notes:
- Limit profile details for privacy; exclude contact info unless logged in.

User Story 2
Title:
As a Patient, I want to sign up using my email and password, so that I can book appointments.
Acceptance Criteria:
- Registration form captures email, password, and basic information.
- System checks for existing accounts and enforces password complexity.
- Successful registration redirects to the login page or dashboard.
Priority: High
Story Points: 3
Notes:
- Include captcha or email verification for added security.

User Story 3
Title:
As a Patient, I want to log into the portal, so that I can manage my bookings.
Acceptance Criteria:
- Login form accepts email and password.
- Invalid credentials prompt error messages without exposing sensitive info.
- Successful login leads to personalized appointment dashboard.
Priority: High
Story Points: 3
Notes:
- Session tokens should expire after inactivity; protect against brute-force attacks.

User Story 4
Title:
As a Patient, I want to log out of the portal, so that I can secure my account.
Acceptance Criteria:
- Logout option is visible from all dashboard views.
- Session is securely terminated and user is redirected to homepage.
- Token or session reuse is blocked after logout.
Priority: High
Story Points: 2
Notes:
- Consider confirming logout with a toast message or alert.

User Story 5
Title:
As a Patient, I want to book an hour-long appointment with a doctor, so that I can consult them about my health concerns.
Acceptance Criteria:
- Available doctors and time slots are visible after login.
- Appointment duration is fixed at one hour per booking.
- Booking is confirmed with a summary and notification.
Priority: High
Story Points: 5
Notes:
- Prevent double-booking; support - ing and cancellation within rules.

User Story 6
Title:
As a Patient, I want to view my upcoming appointments, so that I can prepare accordingly.
Acceptance Criteria:
- Dashboard shows a chronological list of all scheduled appointments.
- Each entry includes date, time, doctor, and location/mode.
- Appointments within the next 24 hours are highlighted or tagged.
Priority: Medium
Story Points: 3
Notes:
- Add links to join virtual consultations if applicable.



## Doctor User Stories 


User Story 1
Title:
As a Doctor, I want to log into the portal to manage my appointments, so that I can access and update my schedule.
Acceptance Criteria:
- Doctor is prompted for username and password on login screen.
- System authenticates credentials securely and grants access to dashboard.
- Login session is established with token-based authentication.
Priority: High
Story Points: 3
Notes:
- Consider session timeout and two-factor authentication for security.

User Story 2
Title:
As a Doctor, I want to log out of the portal, so that I can protect my data from unauthorized access.
Acceptance Criteria:
- Logout option is clearly accessible from the dashboard.
- Logging out invalidates the session token.
- User is redirected to the login page upon logout.
Priority: High
Story Points: 2
Notes:
- Auto-logout after inactivity is a helpful safety net.

User Story 3
Title:
As a Doctor, I want to view my appointment calendar, so that I can stay organized and prepared for consultations.
Acceptance Criteria:
- Calendar displays appointments by date and time.
- Appointments are color-coded or filtered based on status (upcoming, completed, canceled).
- Doctors can click an appointment to view details.
Priority: Medium
Story Points: 5
Notes:
- Include week and month views to enhance usability.

User Story 4
Title:
As a Doctor, I want to mark my unavailability, so that patients only see available slots when booking appointments.
Acceptance Criteria:
- Doctor can select and block time ranges on the calendar.
- Blocked time is immediately reflected in patient booking view.
- System prevents appointment bookings during blocked slots.
Priority: High
Story Points: 5
Notes:
- Optional: allow recurring unavailability (e.g., every Friday afternoon).

User Story 5
Title:
As a Doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information.
Acceptance Criteria:
- Doctors can edit fields like specialty, phone number, and office address.
- Changes are validated and saved to the database.
- Updated information is reflected in the patient-facing profile.
Priority: Medium
Story Points: 3
Notes:
- Allow uploading a profile photo and brief bio for transparency.

User Story 6
Title:
As a Doctor, I want to view patient details for upcoming appointments, so that I can be prepared for consultations.
Acceptance Criteria:
- Doctor can click on an appointment to view patient name, medical history, and previous prescriptions.
- Sensitive data is masked unless doctor is assigned to the patient.
- Patient records load quickly and are read-only unless in consultation.
Priority: High
Story Points: 5
Notes:
- Include option to export or print consultation notes if needed.
















