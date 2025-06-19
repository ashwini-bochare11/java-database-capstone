MySQL Database Design
This relational schema handles structured data like patient profiles, doctor info, appointment schedules, and admin management.
1. patients
| Column Name | Data Type | Constraints | 
| patient_id | INT | PRIMARY KEY, AUTO_INCREMENT | 
| first_name | VARCHAR(50) | NOT NULL | 
| last_name | VARCHAR(50) | NOT NULL | 
| email | VARCHAR(100) | UNIQUE, NOT NULL | 
| phone | VARCHAR(15) | NOT NULL | 
| date_of_birth | DATE | NOT NULL | 
| gender | ENUM('M','F') |  | 
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 

2. doctors
| Column Name | Data Type | Constraints | 
| doctor_id | INT | PRIMARY KEY, AUTO_INCREMENT | 
| full_name | VARCHAR(100) | NOT NULL | 
| specialty | VARCHAR(50) | NOT NULL | 
| email | VARCHAR(100) | UNIQUE, NOT NULL | 
| phone | VARCHAR(15) | NOT NULL | 
| availability | TEXT |  | 
| profile_updated | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 


3. appointments
| Column Name | Data Type | Constraints | 
| appointment_id | INT | PRIMARY KEY, AUTO_INCREMENT | 
| patient_id | INT | FOREIGN KEY REFERENCES patients(patient_id) | 
| doctor_id | INT | FOREIGN KEY REFERENCES doctors(doctor_id) | 
| appointment_date | DATETIME | NOT NULL | 
| status | ENUM('scheduled','completed','cancelled') | DEFAULT 'scheduled' | 
| notes | TEXT |  | 


4. admins
| Column Name | Data Type | Constraints | 
| admin_id | INT | PRIMARY KEY, AUTO_INCREMENT | 
| username | VARCHAR(50) | UNIQUE, NOT NULL | 
| password_hash | VARCHAR(255) | NOT NULL | 
| email | VARCHAR(100) | UNIQUE, NOT NULL | 
| role | ENUM('admin', 'superadmin') | DEFAULT 'admin' | 



MongoDB Collection Design

We’ll store prescriptions in MongoDB for greater flexibility and nesting.
Collection: prescriptions
Each document represents a prescription linked to a doctor and a patient, with multiple medications.
{
  "prescription_id": "RX-20240601-001",
  "patient_id": 101,
  "doctor_id": 12,
  "prescribed_on": "2024-06-01T11:00:00Z",
  "medications": [
    {
      "name": "Amoxicillin",
      "dosage": "500mg",
      "frequency": "3 times a day",
      "duration": "5 days"
    },
    {
      "name": "Paracetamol",
      "dosage": "650mg",
      "frequency": "2 times a day",
      "duration": "3 days"
    }
  ],
  "notes": "Patient advised to rest and drink fluids. Follow-up in one week.",
  "uploaded_by": "Dr. Leena Shah"
}


MySQL Database Design:
Table: patients
- patient_id: INT, Primary Key, AUTO_INCREMENT
- first_name: VARCHAR(50), NOT NULL
- last_name: VARCHAR(50), NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- phone: VARCHAR(15), NOT NULL
- gender: ENUM('M','F','O')
- dob: DATE, NOT NULL
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP
<!-- We enforce email uniqueness for account login; phone/email format validated in application code -->

Table: doctors
- doctor_id: INT, Primary K- ey, AUTO_INCREMENT
- full_name: VARCHAR(100), NOT NULL
- specialization: VARCHAR(100), NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- phone: VARCHAR(15), NOT NULL
- clinic_location_id: INT, Foreign Key → clinic_locations(location_id)
- availability: TEXT
- profile_updated: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP
<!-- Each doctor is linked to one clinic; availability is stored in a human-readable format or parsed JSON string -->

Table: appointments
- appointment_id: INT, Primary Key, AUTO_INCREMENT
- patient_id: INT, Foreign Key → patients(patient_id), ON DELETE CASCADE
- doctor_id: INT, Foreign Key → doctors(doctor_id)
- appointment_time: DATETIME, NOT NULL
- status: ENUM('scheduled', 'completed', 'cancelled') DEFAULT 'scheduled'
- notes: TEXT
<!-- If a patient is deleted, all their appointments are also removed; we assume appointments shouldn't survive without the patient -->

Table: admins
- admin_id: INT, Primary Key, AUTO_INCREMENT
- username: VARCHAR(50), UNIQUE, NOT NULL
- password_hash: VARCHAR(255), NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- role: ENUM('admin', 'superadmin') DEFAULT 'admin'
<!-- Password is securely hashed; roles define system privileges -->


MongoDB Collection Design:
We use MongoDB to manage activity logs, which capture unstructured or semi-structured operational data such as system events, patient interactions, and doctor messages. These are well-suited to NoSQL due to the variability in event types and payloads.
Collection: logs

{
  "_id": "ObjectId('6675ba2123d3f6001c73f9aa')",
  "eventType": "patient_checkin",
  "timestamp": "2025-06-19T10:30:00Z",
  "patientId": 101,
  "metadata": {
    "device": "self-kiosk",
    "location": "Reception Zone A",
    "notes": "Checked in for appointment ID 212"
  },
  "tags": ["checkin", "patient", "walk-in"],
  "handledBy": {
    "adminId": 7,
    "name": "Priya Kapoor"
  }
}

