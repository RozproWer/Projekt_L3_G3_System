package entity1;

import com.example.recepcja.entities.Specjalizacja;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "messages", schema = "htask")
public class Messages {
    private int id;
    private int userId;
    private int taskId;
    private Timestamp createdOn;
    private String message;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "id")
    private Users users;
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @ManyToOne(targetEntity = Tasks.class)
    @JoinColumn(name = "id")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Messages messages = (Messages) o;
        return id == messages.id && userId == messages.userId && taskId == messages.taskId && Objects.equals(createdOn, messages.createdOn) && Objects.equals(message, messages.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, taskId, createdOn, message);
    }
}
