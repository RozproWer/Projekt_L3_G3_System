package com.giga.htask.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tasks")
public class Task {
    private int id;
    private String title;
    private DoctorPatient doctorPatient;
    private List<Message> messages;
    private String description;
    private Timestamp createdOn;
    private Timestamp finishedOn;
    private String status;

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
    @Column(name = "description", nullable = true, length = 2048)
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
    @Column(name = "finished_on", nullable = true)
    public Timestamp getFinishedOn() {
        return finishedOn;
    }

    public void setFinishedOn(Timestamp finishedOn) {
        this.finishedOn = finishedOn;
    }

    @Basic
    @Column(name = "status", nullable = false, length = 128)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //create a one-to-many relationship between tasks and messages
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<Message> getMessages() {
        return messages;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && doctorPatient == task.doctorPatient && Objects.equals(title, task.title) && Objects.equals(description, task.description) && Objects.equals(createdOn, task.createdOn) && Objects.equals(finishedOn, task.finishedOn) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, doctorPatient, description, createdOn, finishedOn, status);
    }
}
