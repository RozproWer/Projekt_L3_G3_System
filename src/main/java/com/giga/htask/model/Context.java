
package com.giga.htask.model;

import com.giga.htask.HibernateConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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

        //log email and password
        System.out.println("email: " + email);
        System.out.println("password: " + password);
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

    private ObservableList<User> usersTable = FXCollections.observableArrayList();
    public ObservableList<User> getUserTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<User> allUsers = session.createQuery("FROM User ").list();
        session.getTransaction().commit();
        session.close();
        usersTable.setAll(allUsers);
        filteredDoctorsTable = new FilteredList<User>(usersTable, p -> p.getRole().equals("doctor"));//TODO: MABE WE CAN OMIT INICIALIZATION HERE
        return usersTable;
    }

    /*Doctors*/

    private FilteredList<User> filteredDoctorsTable = new FilteredList<>(usersTable, p -> p.getRole().equals("doctor")); //TODO: MABE WE CAN OMIT INICIALIZATION HERE
    private SortedList<User> sortedDoctorsTable =  new SortedList<>(filteredDoctorsTable);

    public SortedList<User> getSortedDoctorsTable() {
        //check if usersTable is empty
        if (usersTable.isEmpty()) {
            //if it is empty then refresh it
            getUserTable();
        }
        sortedDoctorsTable = new SortedList<User>(filteredDoctorsTable);
        return  sortedDoctorsTable;
    }
    public FilteredList<User> getFilteredDoctorsTable() {
        return filteredDoctorsTable;
    }

    /*Patients*/
    private FilteredList<User> filteredPatientsTable = new FilteredList<>(usersTable, p -> p.getRole().equals("patient"));
    private SortedList<User> sortedPatientsTable =  new SortedList<>(filteredPatientsTable);

    //create getters and setters for all tables


    public void refreshUsersTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        usersTable.clear();
        usersTable.addAll(session.createQuery("FROM User").list());
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Adds or Updates entity in the database
     *
     * @param entityObject entity Object
     */
    public void saveOrUpdateEntity(Object entityObject) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(entityObject);
        session.getTransaction().commit();
        session.close();

        if (entityObject.getClass() == User.class ){

        }
    }

    /**
     * deletes Entity by id (no delete propagation)
     *
     * @param entityClass class object of entity to be deleted
     * @param id          id of entity
     * @param <T>         class name of entity to be deleted
     */
    public <T> void deleteEntityById(Class<T> entityClass, Integer id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        T entity = session.find(entityClass, id);
        session.remove(entity);

        session.getTransaction().commit();
        session.close();
    }


    public <T> T getEntityById(Class<T> entityClass, Integer userId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        T entity = session.find(entityClass, userId);
        session.getTransaction().commit();
        session.close();
        return entity;
    }

    public ObservableList getDoctorPatientsTable() {
        return null;
    }
}