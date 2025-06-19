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



