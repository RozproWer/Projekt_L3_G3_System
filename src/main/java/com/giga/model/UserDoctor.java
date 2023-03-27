package com.giga.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Hibernate mapped entity User class
 *
 * @author GigaNByte
 * @since 1.0
 */

//Hibernate requires each entity to have a unique identifier

@Entity
@Table(name = "users_doctors")
public class UserDoctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User user;


    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Column(name = "specialization", nullable = true, length = 255)
    private String specialization;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDoctor that = (UserDoctor) o;
        return id == that.id &&
                Objects.equals(user, that.user) &&
                Objects.equals(description, that.description) &&
                Objects.equals(specialization, that.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, description, specialization);
    }

}
