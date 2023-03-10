package entity1;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "htask")
public class Users {
    private int id;
    private int name;
    private int surname;
    private String pesel;
    private String address;
    private String telephone;
    private String email;
    private String role;
    private String password;
    private Timestamp createdOn;

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
    @Column(name = "name", nullable = false)
    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    @Basic
    @Column(name = "surname", nullable = false)
    public int getSurname() {
        return surname;
    }

    public void setSurname(int surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "pesel", nullable = false, length = 11)
    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    @Basic
    @Column(name = "address", nullable = false, length = 255)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "telephone", nullable = true, length = 15)
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "role", nullable = false, length = 127)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "created_on", nullable = false)
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return id == users.id && name == users.name && surname == users.surname && Objects.equals(pesel, users.pesel) && Objects.equals(address, users.address) && Objects.equals(telephone, users.telephone) && Objects.equals(email, users.email) && Objects.equals(role, users.role) && Objects.equals(password, users.password) && Objects.equals(createdOn, users.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, pesel, address, telephone, email, role, password, createdOn);
    }
}
