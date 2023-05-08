package com.giga.htask.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class Message {
    private int id;
    private int taskId;
    private DoctorPatient doctorPatient;
    private User sender;
    private Timestamp createdOn;
    private String message;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "doctor_patient_id", nullable = false)
    public DoctorPatient getDoctorPatient() {
        return doctorPatient;
    }

    public void setDoctorPatient(DoctorPatient doctorPatient) {
        this.doctorPatient = doctorPatient;
    }

    @ManyToOne(targetEntity = Task.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "task", nullable = false)
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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
    @Column(name = "message", nullable = false, length = 1024)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sender_id", nullable = false)
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id && doctorPatient == message.doctorPatient && taskId == message.taskId && Objects.equals(createdOn, message.createdOn) && Objects.equals(this.message, message.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctorPatient, taskId, createdOn, message);
    }
}
