package entity1;

import com.example.recepcja.entities.Specjalizacja;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "tasks", schema = "htask")
public class Tasks {
    private int id;
    private String title;
    private int doctorId;
    private int userId;
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

    @Basic
    @Column(name = "title", nullable = false, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tasks tasks = (Tasks) o;
        return id == tasks.id && doctorId == tasks.doctorId && Objects.equals(title, tasks.title) && Objects.equals(description, tasks.description) && Objects.equals(createdOn, tasks.createdOn) && Objects.equals(finishedOn, tasks.finishedOn) && Objects.equals(status, tasks.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, doctorId, description, createdOn, finishedOn, status);
    }
}
