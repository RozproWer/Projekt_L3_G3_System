package com.giga.model;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users_settings", schema = "htask")
public class UsersSettings {
    private int userId;
    private boolean isDarkMode;

    @Id
    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "id")
    private Users users;
    public int getUserId() {
        return users.getId();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "is_dark_mode", nullable = false)
    public boolean isDarkMode() {
        return isDarkMode;
    }

    public void setDarkMode(boolean darkMode) {
        isDarkMode = darkMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersSettings that = (UsersSettings) o;
        return userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
