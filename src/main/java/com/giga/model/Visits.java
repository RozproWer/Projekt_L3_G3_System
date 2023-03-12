package com.giga.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "visits", schema = "htask")
public class Visits {
    private int id;
    private int patientId;
    private int doctorId;
    private String title;
    private String description;
    private Timestamp createdOn;
    private Timestamp appointmentOn;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "id")
    private Users users;
    public int getDoctorId() {
        return users.getId();
    }

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "id")
    private Users users1;
    public int getUserId() {
        return users1.getId();
    }
//    @Basic
//    @Column(name = "patient_id", nullable = false)
//    public int getPatientId() {
//        return patientId;
//    }
//
//    @Basic
//    @Column(name = "doctor_id", nullable = false)
//    public int getDoctorId() {
//        return doctorId;
//    }


    @Basic
    @Column(name = "title", nullable = false, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "description", nullable = false, length = 2048)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "created_on", nullable = false)
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Basic
    @Column(name = "appointment_on", nullable = false)
    public Timestamp getAppointmentOn() {
        return appointmentOn;
    }

    public void setAppointmentOn(Timestamp appointmentOn) {
        this.appointmentOn = appointmentOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visits visits = (Visits) o;
        return id == visits.id && patientId == visits.patientId && doctorId == visits.doctorId && Objects.equals(title, visits.title) && Objects.equals(description, visits.description) && Objects.equals(createdOn, visits.createdOn) && Objects.equals(appointmentOn, visits.appointmentOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, doctorId, title, description, createdOn, appointmentOn);
    }
}
