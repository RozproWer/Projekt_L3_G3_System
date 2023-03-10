package entity1;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "patients_doctors", schema = "htask")
public class PatientsDoctors {
    private int id;
    private int patientId;
    private int doctorId;
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
//

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
        PatientsDoctors that = (PatientsDoctors) o;
        return id == that.id && patientId == that.patientId && doctorId == that.doctorId && Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, doctorId, createdOn);
    }
}
