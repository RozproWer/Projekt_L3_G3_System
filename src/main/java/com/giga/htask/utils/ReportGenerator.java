package com.giga.htask.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import com.giga.htask.model.Context;
import com.giga.htask.model.Task;
import com.giga.htask.model.User;
import com.giga.htask.model.Visit;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;

public class ReportGenerator {


    private static final String FILE_EXTENSION = ".pdf";
    private static final String FILE_PATH = "./reports/";

    public void generateNewUserReport(User user) {
        try {
            // Create a new PDF document
            Document document = new Document();
            // Set the file name based on the user's ID and the current timestamp
            String fileName = FILE_PATH + "new_user_raport_" + user.getId() + "_" + System.currentTimeMillis() + FILE_EXTENSION;
            // Open a FileOutputStream for the file
            FileOutputStream outputStream = new FileOutputStream(fileName);
            // Create a PdfWriter that will write to the output stream
            PdfWriter.getInstance(document, outputStream);
            // Open the document for writing
            document.open();
            generateReportHeader(document, "New User Details Report");
            document.add(new Paragraph("\n"));
            // Add a paragraph for each field in the User object
            PdfPTable userDetailsTable = new PdfPTable(2);
            userDetailsTable.setWidthPercentage(100f);
            userDetailsTable.setWidths(new float[]{1, 3});
            userDetailsTable.addCell("Role");
            userDetailsTable.addCell(user.getRole());
            userDetailsTable.addCell("Name");
            userDetailsTable.addCell(user.getName());
            userDetailsTable.addCell("Surname");
            userDetailsTable.addCell(user.getSurname());
            userDetailsTable.addCell("PESEL");
            userDetailsTable.addCell(user.getPesel());
            userDetailsTable.addCell("Address");
            userDetailsTable.addCell(user.getAddress());
            userDetailsTable.addCell("Telephone");
            userDetailsTable.addCell(user.getTelephone());
            userDetailsTable.addCell("Email");
            userDetailsTable.addCell(user.getEmail());
            userDetailsTable.addCell("Password");
            userDetailsTable.addCell(user.getPassword());
            // Close the document
            document.close();
            // Close the output stream
            outputStream.close();
            // Open the file in the default PDF viewer,Cannot resolve symbol 'Desktop'
            openPDFFile(fileName);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private void generateReportHeader(Document document, String documentTitle) {
        try {
            // Get the current user from the context
            User currentUser = Context.getInstance().getLoggedUser();

            // Add the application name as a large title
            Paragraph title = new Paragraph("Hospital Tasks App Report");

            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100f);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            PdfPCell dateLabelCell = new PdfPCell(new Phrase("Date:"));
            dateLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Center the text vertically
            table.addCell(dateLabelCell);

            PdfPCell dateValueCell = new PdfPCell(new Phrase(new Timestamp(System.currentTimeMillis()).toString()));
            dateValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Center the text vertically
            table.addCell(dateValueCell);

            PdfPCell createdByLabelCell = new PdfPCell(new Phrase("Created by:"));
            createdByLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Center the text vertically
            table.addCell(createdByLabelCell);

            PdfPCell userInfoCell = new PdfPCell();
            Paragraph userInfo = new Paragraph(currentUser.getName() + " " + currentUser.getSurname() + ", " + currentUser.getRole());
            userInfo.setSpacingAfter(5f);
            userInfoCell.addElement(userInfo);
            userInfoCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Center the text vertically
            table.addCell(userInfoCell);

            document.add(table);

            // Add some space below the header for main content
            document.add(new Paragraph("\n"));
            document.add(new Paragraph(documentTitle + ":"));
            document.add(new Paragraph("\n"));

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public void generateUserReport(User user) {
        try {
            // Create a new PDF document
            Document document = new Document();
            // Set the file name based on the user's ID and the current timestamp
            String fileName = FILE_PATH + "user_raport_" + user.getId() + "_" + System.currentTimeMillis() + FILE_EXTENSION;
            // Open a FileOutputStream for the file
            FileOutputStream outputStream = new FileOutputStream(fileName);
            // Create a PdfWriter that will write to the output stream
            PdfWriter.getInstance(document, outputStream);
            // Open the document for writing
            document.open();
            generateReportHeader(document, "User Details Report");

            // Add user details
            PdfPTable userDetailsTable = new PdfPTable(2);
            userDetailsTable.setWidthPercentage(100f);
            userDetailsTable.setWidths(new float[]{1, 3});
            userDetailsTable.addCell("Role");
            userDetailsTable.addCell(user.getRole());
            userDetailsTable.addCell("Name");
            userDetailsTable.addCell(user.getName());
            userDetailsTable.addCell("Surname");
            userDetailsTable.addCell(user.getSurname());
            userDetailsTable.addCell("PESEL");
            userDetailsTable.addCell(user.getPesel());
            userDetailsTable.addCell("Address");
            userDetailsTable.addCell(user.getAddress());
            userDetailsTable.addCell("Telephone");
            userDetailsTable.addCell(user.getTelephone());
            userDetailsTable.addCell("Email");
            userDetailsTable.addCell(user.getEmail());
            if (user.getRole().equals("administrator")) {
                userDetailsTable.addCell("Password");
                userDetailsTable.addCell(user.getPassword());
            }

            document.add(userDetailsTable);

            // Add user tasks
            document.add(new Paragraph("\nTasks:\n"));
            document.add(new Paragraph("\n"));

            PdfPTable taskTable = new PdfPTable(8); // Number of columns
            taskTable.setWidthPercentage(100f);
            float[] columnWidths = {1, 3, 2, 3, 2, 2, 2, 2}; // Adjust the widths as needed
            taskTable.setWidths(columnWidths);

            taskTable.addCell("Task ID");
            taskTable.addCell("Title");
            taskTable.addCell("Finished On");
            taskTable.addCell("Status");
            taskTable.addCell("Doctor Name");
            taskTable.addCell("Doctor Surname");
            taskTable.addCell("Patient Name");
            taskTable.addCell("Patient Surname");

            for (Task task : Context.getInstance().getTasksTable(user.getId())) {
                // Add table rows for each task
                taskTable.addCell(String.valueOf(task.getId()));
                taskTable.addCell(task.getTitle());
                taskTable.addCell(task.getFinishedOn() != null ? task.getFinishedOn().toString() : "");
                taskTable.addCell(task.getStatus());
                taskTable.addCell(task.getDoctorPatient().getDoctor().getName());
                taskTable.addCell(task.getDoctorPatient().getDoctor().getSurname());
                taskTable.addCell(task.getDoctorPatient().getPatient().getName());
                taskTable.addCell(task.getDoctorPatient().getPatient().getSurname());
            }

            document.add(taskTable);

            // Add visits
            ObservableList<Visit> upcomingVisits = Context.getInstance().getVisitsTable(user.getId(), "upcoming");
            ObservableList<Visit> pastVisits = Context.getInstance().getVisitsTable(user.getId(), "past");

            // Add upcoming visits
            document.add(new Paragraph("\nUpcoming Visits:\n"));
            document.add(new Paragraph("\n"));

            PdfPTable upcomingVisitsTable = new PdfPTable(7);
            upcomingVisitsTable.setWidthPercentage(100f);
            float[] columnWidths2 = {1, 3, 2, 3, 2, 2, 2}; // Adjust the widths as needed
            upcomingVisitsTable.setWidths(columnWidths2);

            upcomingVisitsTable.addCell("Visit ID");
            upcomingVisitsTable.addCell("Title");
            upcomingVisitsTable.addCell("Appointment on");
            upcomingVisitsTable.addCell("Doctor Name");
            upcomingVisitsTable.addCell("Doctor Surname");
            upcomingVisitsTable.addCell("Patient Name");
            upcomingVisitsTable.addCell("Patient Surname");

            for (Visit visit : upcomingVisits) {
                upcomingVisitsTable.addCell(String.valueOf(visit.getId()));
                upcomingVisitsTable.addCell(visit.getTitle());
                upcomingVisitsTable.addCell(visit.getAppointmentOn().toString());
                upcomingVisitsTable.addCell(visit.getDoctorPatient().getDoctor().getName());
                upcomingVisitsTable.addCell(visit.getDoctorPatient().getDoctor().getSurname());
                upcomingVisitsTable.addCell(visit.getDoctorPatient().getPatient().getName());
                upcomingVisitsTable.addCell(visit.getDoctorPatient().getPatient().getSurname());
            }

            document.add(upcomingVisitsTable);

            // Add past visits
            document.add(new Paragraph("\nPast Visits:\n"));
            document.add(new Paragraph("\n"));

            PdfPTable pastVisitsTable = new PdfPTable(7);
            pastVisitsTable.setWidthPercentage(100f);
            pastVisitsTable.setWidths(columnWidths2);

            pastVisitsTable.addCell("Visit ID");
            pastVisitsTable.addCell("Title");
            pastVisitsTable.addCell("Appointment on");
            pastVisitsTable.addCell("Doctor Name");
            pastVisitsTable.addCell("Doctor Surname");
            pastVisitsTable.addCell("Patient Name");
            pastVisitsTable.addCell("Patient Surname");

            for (Visit visit : pastVisits) {
                pastVisitsTable.addCell(String.valueOf(visit.getId()));
                pastVisitsTable.addCell(visit.getTitle());
                pastVisitsTable.addCell(visit.getAppointmentOn().toString());
                pastVisitsTable.addCell(visit.getDoctorPatient().getDoctor().getName());
                pastVisitsTable.addCell(visit.getDoctorPatient().getDoctor().getSurname());
                pastVisitsTable.addCell(visit.getDoctorPatient().getPatient().getName());
                pastVisitsTable.addCell(visit.getDoctorPatient().getPatient().getSurname());
            }

            document.add(pastVisitsTable);

            // Add assigned patients or doctors based on the role
            if (user.getRole().equals("doctor")) {
                document.add(new Paragraph("\nAssigned Patients:\n"));
                document.add(new Paragraph("\n"));

                PdfPTable patientsTable = new PdfPTable(4);
                patientsTable.setWidthPercentage(100f);
                float[] columnWidths3 = {1, 2, 3, 3}; // Adjust the widths as needed
                patientsTable.setWidths(columnWidths3);

                patientsTable.addCell("Patient ID");
                patientsTable.addCell("Name");
                patientsTable.addCell("Surname");
                patientsTable.addCell("PESEL");

                for (User assignedUser : Context.getInstance().getAssignedPatients(user.getId())) {
                    patientsTable.addCell(String.valueOf(assignedUser.getId()));
                    patientsTable.addCell(assignedUser.getName());
                    patientsTable.addCell(assignedUser.getSurname());
                    patientsTable.addCell(assignedUser.getPesel());
                }
                document.add(patientsTable);

            } else if (user.getRole().equals("patient")) {
                document.add(new Paragraph("\nAssigned Doctors:\n"));
                document.add(new Paragraph("\n"));

                PdfPTable doctorsTable = new PdfPTable(4);
                doctorsTable.setWidthPercentage(100f);
                float[] columnWidths3 = {1, 2, 2, 3}; // Adjust the widths as needed
                doctorsTable.setWidths(columnWidths3);

                doctorsTable.addCell("Doctor ID");
                doctorsTable.addCell("Name");
                doctorsTable.addCell("Surname");
                doctorsTable.addCell("Specialization");

                for (User assignedUser : Context.getInstance().getAssignedDoctors(user.getId())) {
                    doctorsTable.addCell(String.valueOf(assignedUser.getId()));
                    doctorsTable.addCell(assignedUser.getName());
                    doctorsTable.addCell(assignedUser.getSurname());
                    doctorsTable.addCell(assignedUser.getSpecialization());
                }
                document.add(doctorsTable);
            }

            // Close the document
            document.close();
            // Close the output stream
            outputStream.close();
            // Open the file in the default PDF viewer
            openPDFFile(fileName);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public void openPDFFile(String fileName) {
        try {
            // Get the default desktop class
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            // Open the PDF file with the default viewer
            desktop.open(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}