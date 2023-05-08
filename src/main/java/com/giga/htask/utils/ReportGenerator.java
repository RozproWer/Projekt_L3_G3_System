package com.giga.htask.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import com.giga.htask.model.Context;
import com.giga.htask.model.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportGenerator {

    private static final String FILE_NAME_PREFIX = "doctor_report_";
    private static final String FILE_EXTENSION = ".pdf";
    private static final String FILE_PATH = "./reports/";

    public void generateNewUserReport(User user) {
        try {
            // Create a new PDF document
            Document document = new Document();
            // Set the file name based on the user's ID and the current timestamp
            String fileName = FILE_PATH + FILE_NAME_PREFIX + user.getId() + "_" + System.currentTimeMillis() + FILE_EXTENSION;
            // Open a FileOutputStream for the file
            FileOutputStream outputStream = new FileOutputStream(fileName);
            // Create a PdfWriter that will write to the output stream
            PdfWriter.getInstance(document, outputStream);
            // Open the document for writing
            document.open();
            generateReportHeader(document,"User Details Report");
            // Add a paragraph for each field in the User object
            document.add(new Paragraph("Role: " + user.getRole()));
            document.add(new Paragraph("Name: " + user.getName()));
            document.add(new Paragraph("Surname: " + user.getSurname()));
            document.add(new Paragraph("PESEL: " + user.getPesel()));
            document.add(new Paragraph("Address: " + user.getAddress()));
            document.add(new Paragraph("Telephone: " + user.getTelephone()));
            document.add(new Paragraph("Email: " + user.getEmail()));
            document.add(new Paragraph("Password: " + user.getPassword()));
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

    private void generateReportHeader(Document document,String documentTitle) {
        try {
            // Get the current user from the context
            User currentUser = Context.getInstance().getLoggedUser();

            // Add the application name as a large title
            Paragraph title = new Paragraph("Hospital Tasks App Report");
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Add the current date, user name, surname and role on the left of the document
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100f);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // Current date
            PdfPCell dateCell = new PdfPCell(new Phrase("Date: " + new Timestamp(System.currentTimeMillis())));
            dateCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(dateCell);

            // User info
            PdfPCell userInfoCell = new PdfPCell();
            userInfoCell.setBorder(Rectangle.NO_BORDER);
            Paragraph userInfo = new Paragraph(currentUser.getName() + " " + currentUser.getSurname() + ", " + currentUser.getRole());
            userInfo.setSpacingAfter(5f);
            userInfoCell.addElement(userInfo);
            table.addCell(userInfoCell);

            document.add(table);

            // Add some space below the header for main content
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));

        } catch (DocumentException e) {
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