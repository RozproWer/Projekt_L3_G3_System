
package com.giga.htask.model;

import com.giga.htask.HibernateConnection;
import com.giga.htask.utils.ReportGenerator;
import com.giga.htask.utils.SortedFilteredObservableList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

//TODO: EM now we have user roles so i dont know how to refactor Context class to handle it
// i thought about about creating different Context like ContextAdmin, ContextUser, ContextGuest classes but its is not as agile as possible

/**
 * Singleton class that handles queries and stores state shared across all fxml tab controllers in app
 *
 * @author GigaNByte
 * @since 1.0
 */
public class Context {
    private SessionFactory sessionFactory = HibernateConnection.getSessionFactory();
    public ReportGenerator reportGenerator = new ReportGenerator();
    private User loggedUser;

    private Boolean isDarkMode = null; //for caching so we dont have to query database every time (every scene load)

    private final static Context instance = new Context();


    /**
     * @return singleton instance of Context object
     */
    public static Context getInstance() {
        return instance;
    }

    /*Auth*/
    public User login(String email, String password) {


        Session session = sessionFactory.openSession();
        session.beginTransaction();
        loggedUser = (User) session.createQuery("FROM User WHERE email = :email")
                .setParameter("email", email)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();

        if (loggedUser == null || !loggedUser.getPassword().equals(password)) {
            return null;
        }

        return loggedUser;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    /*Settings*/
    public Boolean isDarkMode() {
        if (loggedUser == null) {
            return false;
        }

        if(isDarkMode == null) {
            //load from database
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            loggedUser = (User) session.createQuery("FROM User WHERE email = :email")
                    .setParameter("email", loggedUser.getEmail())
                    .uniqueResult();
            isDarkMode = loggedUser.getUserSettings().isDarkMode();
            session.getTransaction().commit();
            session.close();
            return isDarkMode;
        }

        return isDarkMode;
    }

    public void setDarkMode(Boolean darkMode) {
        isDarkMode = darkMode; // for "cache" so we dont have to query database every time

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        loggedUser.getUserSettings().setDarkMode(darkMode);

        session.saveOrUpdate(loggedUser);
        session.getTransaction().commit();
        session.close();
    }

    /*Doctors and patients have shared observable list*/
    private ObservableList<User> usersTable = FXCollections.observableArrayList();
    public ObservableList<User> getUserTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<User> allUsers = session.createQuery("FROM User ").list();
        session.getTransaction().commit();
        session.close();
        usersTable.setAll(allUsers);
        filteredDoctorsTable = new FilteredList<User>(usersTable, p -> p.getRole().equals("doctor"));
        filteredPatientsTable = new FilteredList<User>(usersTable, p -> p.getRole().equals("patient"));
        return usersTable;
    }
    private void refreshUserTableIfNeeded(Boolean refresh) {
        if (usersTable.isEmpty() || refresh) {
            getUserTable();
        }
    }
    public void refreshUsersTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        usersTable.clear();
        usersTable.addAll(session.createQuery("FROM User").list());
        session.getTransaction().commit();
        session.close();
    }

    private FilteredList<User> filteredDoctorsTable = new FilteredList<>(usersTable, p -> p.getRole().equals("doctor"));
    private SortedList<User> sortedDoctorsTable =  new SortedList<>(filteredDoctorsTable);
    public FilteredList<User> getFilteredDoctorsTable() {
        return filteredDoctorsTable;
    }
    public SortedList<User> getSortedDoctorsTable() {
        refreshUserTableIfNeeded(false);
        sortedDoctorsTable = new SortedList<User>(filteredDoctorsTable);
        return sortedDoctorsTable;
    }
    public SortedList<User> getSortedDoctorsTable(Boolean refresh) {
        refreshUserTableIfNeeded(refresh);
        sortedDoctorsTable = new SortedList<User>(filteredDoctorsTable);
        return sortedDoctorsTable;
    }

    private FilteredList<User> filteredPatientsTable = new FilteredList<>(usersTable, p -> p.getRole().equals("patient"));
    private SortedList<User> sortedPatientsTable =  new SortedList<>(filteredPatientsTable);
    public SortedList<User> getSortedPatientsTable() {
        refreshUserTableIfNeeded(false);
        sortedPatientsTable = new SortedList<User>(filteredPatientsTable);
        return sortedPatientsTable;
    }
    public SortedList<User> getSortedPatientsTable(Boolean refresh) {
        refreshUserTableIfNeeded(refresh);
        sortedPatientsTable = new SortedList<User>(filteredPatientsTable);
        return sortedPatientsTable;
    }
    public FilteredList<User> getFilteredPatientsTable() {
        return filteredPatientsTable;
    }


    /*Tasks and Visits Lists needs to be generated everytime new tab is created because there are depending on userId*/
    public ObservableList<Task> getTasksTable(Integer userId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT DISTINCT t FROM Task t WHERE t.doctorPatient IN ( SELECT dp FROM DoctorPatient dp WHERE dp.patient.id = :userId OR dp.doctor.id = :userId)");
        query.setParameter("userId", userId);
        List userTasks = query.getResultList();
        session.getTransaction().commit();
        session.close();
        ObservableList<Task> tasksTable = FXCollections.observableArrayList();
        tasksTable.setAll(userTasks);
        return tasksTable;
    }
    public ObservableList<Visit> getVisitsTable(Integer userId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT DISTINCT v FROM Visit v WHERE v.doctorPatient IN ( SELECT dp FROM DoctorPatient dp WHERE dp.patient.id = :userId OR dp.doctor.id = :userId)");
        query.setParameter("userId", userId);
        List userVisits = query.getResultList();
        session.getTransaction().commit();
        session.close();
        ObservableList<Visit> visitsTable = FXCollections.observableArrayList();
        visitsTable.setAll(userVisits);
        return visitsTable;
    }
    public SortedFilteredObservableList<Task> getSortedFilteredObservableTasksTable(Integer userId) {
        return new SortedFilteredObservableList<Task>(getTasksTable(userId), p -> true);
    }
    public SortedFilteredObservableList<Visit> getSortedFilteredObservableVisitsTable(Integer userId) {
        return new SortedFilteredObservableList<Visit>(getVisitsTable(userId), p -> true);
    }

    /**
     * Saves or updates an entity object in the database.
     *
     * @param entityObject the entity object to save or update
     * @return true if the operation was successful, false otherwise
     */
    public boolean saveOrUpdateEntity(Object entityObject) {
        boolean success = false;
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(entityObject);
            session.getTransaction().commit();
            success = true;
        } catch (PersistenceException e) {
            System.err.println("\u001B[31m" + "Error: " + e.getMessage() + "\u001B[0m");
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return success;
    }

    /**
     * deletes Entity by id (no delete propagation)
     *
     * @param entityClass class object of entity to be deleted
     * @param id          id of entity
     * @param <T>         class name of entity to be deleted
     */
    public <T> boolean deleteEntityById(Class<T> entityClass, Integer id) {
        boolean success = false;
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            T entity = session.find(entityClass, id);

            if (entity == null) {
                throw new PersistenceException("Entity with id " + id + " does not exist");
            }
            session.remove(entity);
            session.getTransaction().commit();
            success = true;
        }catch (PersistenceException e) {
            System.err.println("\u001B[31m" + "Error: " + e.getMessage() + "\u001B[0m");
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return success;
    }


    public <T> T getEntityById(Class<T> entityClass, Integer userId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        T entity = session.find(entityClass, userId);
        session.getTransaction().commit();
        session.close();
        return entity;
    }

    public DoctorPatient getDoctorPatientByDoctorAndPatientId(Integer doctorId, Integer patientId){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT dp FROM DoctorPatient dp WHERE dp.doctor.id = :doctorId AND dp.patient.id = :patientId");
        query.setParameter("doctorId", doctorId);
        query.setParameter("patientId", patientId);
        DoctorPatient doctorPatient = (DoctorPatient) query.getSingleResult();
        session.getTransaction().commit();
        session.close();
        return doctorPatient;
    }


    public ObservableList getDoctorPatientsTable(Integer doctorId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT dp.patient FROM DoctorPatient dp WHERE dp.doctor.id = :doctorId");
        query.setParameter("doctorId", doctorId);
        List patients = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return FXCollections.observableArrayList(patients);
    }
    public ObservableList getPatientDoctorsTable(Integer patientId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT dp.doctor FROM DoctorPatient dp WHERE dp.patient.id = :patientId");
        query.setParameter("patientId", patientId);
        List doctors = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return FXCollections.observableArrayList(doctors);
    }

    public boolean addDoctorPatientAssociation(DoctorPatient doctorPatient) {
        boolean success = false;
        //create query to get all doctorPatient associations
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM DoctorPatient");
        List<DoctorPatient> doctorPatientList = query.getResultList();
        session.getTransaction().commit();
        session.close();

        // Check if the association already exists
        for (DoctorPatient dp : doctorPatientList) {
            if (dp.getPatient().getId() == doctorPatient.getPatient().getId() && dp.getDoctor().getId() == doctorPatient.getDoctor().getId()) {
                System.err.println("Doctor-Patient association already exists.");
                return false;
            }
        }
        return saveOrUpdateEntity(doctorPatient);
    }



    public List<Message> getMessagesBetweenUsers(int userId1, int userId2) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery(
                "FROM Message " +
                        "WHERE (sender.id = :userId1 AND doctorPatient.patient.id = :userId2) " + //toto
                        "OR (sender.id = :userId2 AND doctorPatient.patient.id = :userId1)"
        );
        query.setParameter("userId1", userId1);
        query.setParameter("userId2", userId2);
        List<Message> messages = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return messages;
    }

    public Integer getDoctorPatientId(int doctorId, int patientId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery(
                "SELECT dp.id " +
                        "FROM DoctorPatient dp " +
                        "WHERE dp.doctor.id = :doctorId " +
                        "AND dp.patient.id = :patientId"
        );
        query.setParameter("doctorId", doctorId);
        query.setParameter("patientId", patientId);
        Integer doctorPatientId = (Integer) query.getSingleResult();
        session.getTransaction().commit();
        session.close();
        return doctorPatientId;
    }

    public ObservableList getUnassignedDoctors(Integer patientId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT DISTINCT dp.doctor FROM DoctorPatient dp WHERE dp.doctor NOT IN (SELECT dp2.doctor FROM DoctorPatient dp2 WHERE dp2.patient.id = :patientId)");
        query.setParameter("patientId", patientId);
        List doctors = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return FXCollections.observableArrayList(doctors);
    }

    public ObservableList getUnassignedPatients(Integer doctorId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT DISTINCT dp.patient FROM DoctorPatient dp WHERE dp.patient NOT IN (SELECT dp2.patient FROM DoctorPatient dp2 WHERE dp2.doctor.id = :doctorId)");
        query.setParameter("doctorId", doctorId);
        List patients = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return FXCollections.observableArrayList(patients);
    }
    public String generatePassword() {
        String password = "";
        for (int i = 0; i < 8; i++) {
            password += (char) (Math.random() * 26 + 97);
        }
        return password;
    }
    public ObservableList getAssignedDoctors(Integer patientId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT DISTINCT dp.doctor FROM DoctorPatient dp WHERE dp.patient.id = :patientId");
        query.setParameter("patientId", patientId);
        List doctors = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return FXCollections.observableArrayList(doctors);
    }

    public ObservableList getAssignedPatients(Integer doctorId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT DISTINCT dp.patient FROM DoctorPatient dp WHERE dp.doctor.id = :doctorId");
        query.setParameter("doctorId", doctorId);
        List patients = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return FXCollections.observableArrayList(patients);
    }

    public ObservableList getMessagesByTask(Integer taskId) {
        //create query to get all messages by task
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM Message WHERE task.id = :taskId ORDER BY task.createdOn ASC");
        query.setParameter("taskId", taskId);
        List messages = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return FXCollections.observableArrayList(messages);

    }
}