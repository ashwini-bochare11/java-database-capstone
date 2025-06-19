import { showBookingOverlay } from './loggedPatient.js';
import { deleteDoctor } from './doctorServices.js';
import { getPatientDetails } from './patientServices.js';

export function createDoctorCard(doctor) {
  const card = document.createElement('div');
  card.className = 'doctor-card';

  const role = localStorage.getItem('role');

  // Doctor Info
  const info = document.createElement('div');
  info.className = 'doctor-info';

  info.innerHTML = `
    <h3>${doctor.name}</h3>
    <p><strong>Specialty:</strong> ${doctor.specialty}</p>
    <p><strong>Email:</strong> ${doctor.email}</p>
    <p><strong>Available Times:</strong> ${doctor.availableTimes.join(', ')}</p>
  `;

  // Action Buttons
  const actions = document.createElement('div');
  actions.className = 'doctor-actions';

  // === ADMIN ===
  if (role === 'admin') {
    const delBtn = document.createElement('button');
    delBtn.textContent = 'Delete';
    delBtn.className = 'delete-btn';

    delBtn.addEventListener('click', async () => {
      const token = localStorage.getItem('token');
      if (confirm(`Delete Dr. ${doctor.name}?`)) {
        const res = await deleteDoctor(doctor.id, token);
        if (res.status === 'success') {
          card.remove();
          alert('Doctor deleted.');
        } else {
          alert('Error deleting doctor.');
        }
      }
    });

    actions.appendChild(delBtn);
  }

  // === PATIENT NOT LOGGED IN ===
  if (role === null || !localStorage.getItem('token')) {
    const bookBtn = document.createElement('button');
    bookBtn.textContent = 'Book Now';
    bookBtn.addEventListener('click', () => alert('Please log in as a patient to book.'));
    actions.appendChild(bookBtn);
  }

  // === PATIENT LOGGED IN ===
  if (role === 'patient' && localStorage.getItem('token')) {
    const bookBtn = document.createElement('button');
    bookBtn.textContent = 'Book Now';

    bookBtn.addEventListener('click', async () => {
      const token = localStorage.getItem('token');
      const patient = await getPatientDetails(token);

      if (patient) {
        showBookingOverlay({ doctor, patient });
      } else {
        alert('Session expired. Please log in again.');
        window.location.href = '/login';
      }
    });

    actions.appendChild(bookBtn);
  }

  // Assemble the card
  card.appendChild(info);
  card.appendChild(actions);

  return card;
}

/*
Import the overlay function for booking appointments from loggedPatient.js

  Import the deleteDoctor API function to remove doctors (admin role) from docotrServices.js

  Import function to fetch patient details (used during booking) from patientServices.js

  Function to create and return a DOM element for a single doctor card
    Create the main container for the doctor card
    Retrieve the current user role from localStorage
    Create a div to hold doctor information
    Create and set the doctor’s name
    Create and set the doctor's specialization
    Create and set the doctor's email
    Create and list available appointment times
    Append all info elements to the doctor info container
    Create a container for card action buttons
    === ADMIN ROLE ACTIONS ===
      Create a delete button
      Add click handler for delete button
     Get the admin token from localStorage
        Call API to delete the doctor
        Show result and remove card if successful
      Add delete button to actions container
   
    === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
      Create a book now button
      Alert patient to log in before booking
      Add button to actions container
  
    === LOGGED-IN PATIENT ROLE ACTIONS === 
      Create a book now button
      Handle booking logic for logged-in patient   
        Redirect if token not available
        Fetch patient data with token
        Show booking overlay UI with doctor and patient info
      Add button to actions container
   
  Append doctor info and action buttons to the car
  Return the complete doctor card element
*/
