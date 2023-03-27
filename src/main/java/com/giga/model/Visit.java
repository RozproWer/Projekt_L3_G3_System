package com.giga.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "visits")
public class Visit {
    private int id;
    private User patient;
    private User doctor;
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

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "doctor_id", nullable = false)
    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "patient_id", nullable = false)
    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }


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
        Visit visit = (Visit) o;
        return id == visit.id &&  doctor == visit.doctor && patient == visit.patient && Objects.equals(title, visit.title) && Objects.equals(description, visit.description) && Objects.equals(createdOn, visit.createdOn) && Objects.equals(appointmentOn, visit.appointmentOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctor, patient,title, description, createdOn, appointmentOn);
    }
}
