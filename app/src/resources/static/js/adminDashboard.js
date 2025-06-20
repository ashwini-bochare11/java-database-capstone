// js/adminDashboard.js

import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorService.js";
import { createDoctorCard } from "./components/doctorCard.js";
import { openModal, closeModal } from "./components/modal.js";

// Load doctor cards when page is ready
document.addEventListener("DOMContentLoaded", () => {
  const addDocBtn = document.getElementById("addDocBtn");
  if (addDocBtn) {
    addDocBtn.addEventListener("click", () => openModal("addDoctor"));
  }

  loadDoctorCards();

  // Attach filter listeners
  const searchBar = document.getElementById("searchBar");
  const timeFilter = document.getElementById("timeFilter");
  const specialtyFilter = document.getElementById("specialtyFilter");

  if (searchBar) searchBar.addEventListener("input", filterDoctorsOnChange);
  if (timeFilter) timeFilter.addEventListener("change", filterDoctorsOnChange);
  if (specialtyFilter) specialtyFilter.addEventListener("change", filterDoctorsOnChange);
});

// Load and render all doctor cards
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error loading doctors:", error);
  }
}

// Filter and render doctors based on input
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar")?.value || null;
  const time = document.getElementById("timeFilter")?.value || null;
  const specialty = document.getElementById("specialtyFilter")?.value || null;

  try {
    const doctors = await filterDoctors(name, time, specialty);
    if (doctors && doctors.length) {
      renderDoctorCards(doctors);
    } else {
      document.getElementById("content").innerHTML = "<p>No doctors found with the given filters.</p>";
    }
  } catch (err) {
    console.error("Filter error:", err);
    alert("Failed to filter doctors. Please try again.");
  }
}

// Render doctor cards into the DOM
function renderDoctorCards(doctors) {
  const content = document.getElementById("content");
  if (!content) return;
  content.innerHTML = "";
  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    content.appendChild(card);
  });
}

// Add new doctor from modal form
window.adminAddDoctor = async function () {
  const name = document.getElementById("doctorName")?.value;
  const email = document.getElementById("doctorEmail")?.value;
  const phone = document.getElementById("doctorPhone")?.value;
  const password = document.getElementById("doctorPassword")?.value;
  const specialty = document.getElementById("doctorSpecialty")?.value;
  const time = document.getElementById("doctorTime")?.value;

  const token = localStorage.getItem("token");
  if (!token) {
    alert("Authorization token is missing. Please log in again.");
    return;
  }

  const doctor = { name, email, phone, password, specialty, time };

  try {
    const result = await saveDoctor(doctor, token);
    if (result.success) {
      alert(result.message || "Doctor added successfully");
      closeModal("addDoctor");
      loadDoctorCards();
    } else {
      alert(result.message || "Failed to add doctor.");
    }
  } catch (error) {
    console.error("Save doctor failed:", error);
    alert("An error occurred while saving the doctor.");
  }
};



/*
  This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form


  Attach a click listener to the "Add Doctor" button
  When clicked, it opens a modal form using openModal('addDoctor')


  When the DOM is fully loaded:
    - Call loadDoctorCards() to fetch and display all doctors


  Function: loadDoctorCards
  Purpose: Fetch all doctors and display them as cards

    Call getDoctors() from the service layer
    Clear the current content area
    For each doctor returned:
    - Create a doctor card using createDoctorCard()
    - Append it to the content div

    Handle any fetch errors by logging them


  Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  On any input change, call filterDoctorsOnChange()


  Function: filterDoctorsOnChange
  Purpose: Filter doctors based on name, available time, and specialty

    Read values from the search bar and filters
    Normalize empty values to null
    Call filterDoctors(name, time, specialty) from the service

    If doctors are found:
    - Render them using createDoctorCard()
    If no doctors match the filter:
    - Show a message: "No doctors found with the given filters."

    Catch and display any errors with an alert


  Function: renderDoctorCards
  Purpose: A helper function to render a list of doctors passed to it

    Clear the content area
    Loop through the doctors and append each card to the content area


  Function: adminAddDoctor
  Purpose: Collect form data and add a new doctor to the system

    Collect input values from the modal form
    - Includes name, email, phone, password, specialty, and available times

    Retrieve the authentication token from localStorage
    - If no token is found, show an alert and stop execution

    Build a doctor object with the form values

    Call saveDoctor(doctor, token) from the service

    If save is successful:
    - Show a success message
    - Close the modal and reload the page

    If saving fails, show an error message
*/
