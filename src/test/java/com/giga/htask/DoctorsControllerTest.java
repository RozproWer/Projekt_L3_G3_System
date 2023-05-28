package com.giga.htask;

import com.giga.htask.controllers.content.doctors.DoctorsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.MockitoAnnotations;

public class DoctorsControllerTest {

    @Test
    public void testAddDoctor() {
        // Create an instance of DoctorsController
        DoctorsController doctorsController = new DoctorsController();

        // Set up any required test data or stubs
        String name = "John";
        String surname = "Doe";
        String email = "john.doe@example.com";
        String telephone = "1234567890";
        String address = "123 Main St";
        String pesel = "12345678901";
        String specialization = "General Medicine";

        // Set values for the text fields in the controller
        doctorsController.nameField.setText(name);
        doctorsController.surnameField.setText(surname);
        doctorsController.emailField.setText(email);
        doctorsController.telephoneField.setText(telephone);
        doctorsController.addressField.setText(address);
        doctorsController.peselField.setText(pesel);
        doctorsController.specializationField.setText(specialization);

        // Call the addDoctor() method
        doctorsController.addDoctor();

        boolean doctorAdded = doctorsController.doctorsTable.getItems().stream()
                .anyMatch(doctor -> doctor.getName().equals(name) &&
                        doctor.getSurname().equals(surname) &&
                        doctor.getEmail().equals(email) &&
                        doctor.getTelephone().equals(telephone) &&
                        doctor.getAddress().equals(address) &&
                        doctor.getPesel().equals(pesel) &&
                        doctor.getSpecialization().equals(specialization));
        assertTrue(doctorAdded, "Doctor should be added to the doctorsTable");
    }

}