package com.giga.htask.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "visits")
public class Visit {
    private int id;
    private DoctorPatient doctorPatient;
    private String title;
    private String description;
    private Timestamp createdOn;
    private Timestamp appointmentOn;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "doctor_patient_id", nullable = false)
    public DoctorPatient getDoctorPatient() {
        return doctorPatient;
    }

    public void setDoctorPatient(DoctorPatient doctorPatient) {
        this.doctorPatient = doctorPatient;
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
    @PrePersist
    protected void onCreate() {
        createdOn = new Timestamp(System.currentTimeMillis());
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
        return id == visit.id && doctorPatient == visit.doctorPatient && Objects.equals(title, visit.title) && Objects.equals(description, visit.description) && Objects.equals(createdOn, visit.createdOn) && Objects.equals(appointmentOn, visit.appointmentOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctorPatient, title, description, createdOn, appointmentOn);
    }
}
