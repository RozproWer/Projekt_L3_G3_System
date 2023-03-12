
package com.giga.model;

import com.giga.HibernateConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private ObservableList<FireTest> fireTestTable = FXCollections.observableArrayList();
    private ObservableList<Vehicle> vehicleTable = FXCollections.observableArrayList();
    private ObservableList<Gun> gunTable = FXCollections.observableArrayList();
    private FilteredList<Gun> filteredGunTable = new FilteredList<>(gunTable, p -> true);
    private FilteredList<FireTest> filteredFireTestTable = new FilteredList<>(fireTestTable, p -> true);
    private FilteredList<Vehicle> filteredVehicleTable = new FilteredList<>(vehicleTable, p -> true);
    private SortedList<Gun> sortedGunTable =  new SortedList<>(filteredGunTable);
    private SortedList<FireTest> sortedFireTestTable =  new SortedList<>(filteredFireTestTable);
    private SortedList<Vehicle> sortedVehicleTable =  new SortedList<>(filteredVehicleTable);

    /**
     * @since 1.2
     * @return FilteredList of Gun Entities
     */
    public FilteredList<Gun> getFilteredGunTable() {
        return filteredGunTable;
    }

    /**
     * @since 1.2
     * @return SortedList of Gun Entities
     */
    public SortedList<Gun> getSortedGunTable() {
        return sortedGunTable;
    }

    /**
     * @since 1.2
     * @return SortedList of FireTests Entities
     */
    public SortedList<FireTest> getSortedFireTestTable() {
        return new SortedList<FireTest>(filteredFireTestTable);
    }

    /**
     * @since 1.2
     * @return SortedList of Vehicles Entities
     */
    public SortedList<Vehicle> getSortedVehicleTable() {
        return sortedVehicleTable;
    }

    /**
     * @since 1.2
     * @return FilteredList of FireTests Entities
     */
    public FilteredList<FireTest> getFilteredFireTestTable() {
        return filteredFireTestTable;
    }

    /**
     * @since 1.2
     * @return FilteredList of Vehicle Entities
     */
    public FilteredList<Vehicle> getFilteredVehicleTable() {
        return filteredVehicleTable;
    }

    /**
     * @return FireTests Entities from Database
     */
    public ObservableList<FireTest> getFireTestTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<FireTest> allFireTests = session.createQuery("FROM FireTest ").list();
        session.getTransaction().commit();
        session.close();
        fireTestTable.setAll(allFireTests);
        filteredFireTestTable = new FilteredList<FireTest>(fireTestTable, p -> true);
        return fireTestTable;
    }

    /**
     * Refreshes FireTest Entities from Database
     */
    public void refreshFireTestTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<FireTest> allFireTests = session.createQuery("FROM FireTest ").list();
        session.getTransaction().commit();
        session.close();
        fireTestTable.setAll(allFireTests);
    }

    /**
     * Adds or Updates entity in the database
     *
     * @param entityObject entity Object
     */

    //TODO: Check if entity needs to be only saved or updated with associated entities (performance)
    public void saveOrUpdateEntity(Object entityObject) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(entityObject);
        session.getTransaction().commit();
        session.close();

        if (entityObject.getClass() == Vehicle.class ){
            updateFireTestsByVehicle((Vehicle) entityObject);

        }else if (entityObject.getClass() == Gun.class ) {
            updateVehiclesByGun((Gun) entityObject);
        }
    }

    /**
    * Updates All Vehicles associated with Gun object
    *
    */
    private void updateVehiclesByGun(Gun entityObject) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Vehicle v where v.gun=:updatedGun");
        query.setParameter("updatedGun", entityObject);
        List<Vehicle> vehiclesToUpdate = query.getResultList();
        session.getTransaction().commit();
        session.close();

        for (Vehicle vehicleToUpdate: vehiclesToUpdate) {
            updateFireTestsByVehicle(vehicleToUpdate);
        }
    }
    /**
     * Updates All Firetests (recalculates penetration result) from associated with Vehicle object
     *
     */

    public void updateFireTestsByVehicle(Vehicle  updatedVehicle){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from FireTest ft where ft.targetVehicle = :updatedVehicleId or ft.vehicle = :updatedVehicleId");
        query.setParameter("updatedVehicleId",updatedVehicle);
        List<FireTest> fireTestsToUpdate= query.getResultList();
        session.getTransaction().commit();
        session.close();
        for (FireTest fireTestToUpdate : fireTestsToUpdate) {
            calculateAndUpdateFireTestResult(fireTestToUpdate);
        }
    }

    /**
     * @return Vehicles Entities from Database
     */
    public ObservableList<Vehicle> getVehicleTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Vehicle> allVehicles = session.createQuery("FROM Vehicle").list();
        session.getTransaction().commit();
        session.close();
        vehicleTable.setAll(allVehicles);
        return vehicleTable;
    }

    /**
     * Refreshes VehicleTable Entities from Database
     */
    public void refreshVehicleTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Vehicle> allVehicles = session.createQuery("FROM Vehicle").list();
        session.getTransaction().commit();
        session.close();
        vehicleTable.setAll(allVehicles);
    }

    /**
     * @return Guns Entities from Database
     */
    public ObservableList<Gun> getGunTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Gun> allGuns = session.createQuery("FROM Gun ").list();
        session.getTransaction().commit();
        session.close();
        gunTable.setAll(allGuns);
        return gunTable;
    }

    /**
     * deletes Vehicle by id (propagates deletion of linked entities: FireTest)
     *
     * @param id id of gun
     */

    public void deleteVehicleById(Integer id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Vehicle vehicleToDelete = session.find(Vehicle.class, id);
        Query query = session.createQuery("from FireTest ft where ft.targetVehicle = :vehicleToDelete or ft.vehicle = :vehicleToDelete");
        query.setParameter("vehicleToDelete", vehicleToDelete);
        List<FireTest> fireTestsToDelete = query.getResultList();

        for (FireTest fireTestToDelete : fireTestsToDelete) {
            session.remove(fireTestToDelete);
        }
        session.remove(vehicleToDelete);
        session.getTransaction().commit();
        session.close();

        refreshFireTestTable();
    }



    public void deleteGunById(Integer id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Gun gunToDelete = session.find(Gun.class, id);
        Query query = session.createQuery("from Vehicle v where v.gun=:gunToDelete");
        query.setParameter("gunToDelete", gunToDelete);
        query.getResultList();
        List<Vehicle> vehiclesToDelete = query.getResultList();

        session.getTransaction().commit();
        session.close();

        for (Vehicle vehicleToDelete : vehiclesToDelete) {
            deleteVehicleById(vehicleToDelete.getId());
        }


        session = sessionFactory.openSession();
        session.beginTransaction();

        session.remove(gunToDelete);

        session.getTransaction().commit();
        session.close();

        refreshVehicleTable();
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

        //propagate delete of certain entities

        if (entityClass.getClass().getSimpleName() == "Gun") {
            String selectionQuery = "SELECT FireTest FROM FireTest WHERE FireTest.targetVehicle = " + id;
            List<FireTest> fireTests = (List<FireTest>) session.createQuery(selectionQuery).list();
            session.remove(fireTests);
            String selectionQuery2 = "SELECT FireTest FROM FireTest WHERE FireTest.targetVehicle = " + id;
            List<FireTest> fireTests2 = session.createQuery(selectionQuery2).list();
            session.remove(fireTests2);
        } else if (entityClass.getClass().getSimpleName() == "Vehicle") {
            String selectionQuery = "SELECT FireTest FROM FireTest WHERE FireTest.targetVehicle = " + id;
            List<FireTest> fireTests = (List<FireTest>) session.createQuery(selectionQuery).list();
            session.remove(fireTests);
            String selectionQuery2 = "SELECT FireTest FROM FireTest WHERE FireTest.targetVehicle = " + id;
            List<FireTest> fireTests2 = session.createQuery(selectionQuery2).list();
            session.remove(fireTests2);
        }

        T entity = session.find(entityClass, id);
        session.remove(entity);


        session.getTransaction().commit();
        session.close();
    }

    /**
     * @param entityClass Class Object instance of entity
     * @param id          id of entity
     * @param <T>         Object Type of Entity
     * @return entity by id
     */
    public <T> T getEntityById(Class<T> entityClass, Integer id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        T entity = session.find(entityClass, id);
        session.getTransaction().commit();
        session.close();
        return entity;
    }

    /**
     * Calculates and updates in db test result of Firetest by Id
     *
     * @param fireTest Firetest Object
     */
    private void calculateAndUpdateFireTestResult(FireTest fireTest) {
        calculateFireTestResult(fireTest);
        saveOrUpdateEntity(fireTest);
    }

    /**
     * Calculates test result of Firetest Object
     *
     * @param firetest Firetest Object
     */

    public void calculateFireTestResult(FireTest firetest) {
        //TODO: Optimize radians to degrees etc.
        Double armorThickness = 0d;
        Double relativeArmorThickness = 0d;
        Double absVerticalShotAngle = 0d;
        Integer rhaGunPenetration = 0;

        //calculates relative armor thickness
        if (firetest.getTargetVehiclePart() == FireTest.VehiclePart.FRONT_ARMOR) {
            armorThickness = Double.valueOf(firetest.getTargetVehicle().getFrontArmorThickness());
            absVerticalShotAngle = (double) (firetest.getTargetVehicle().getFrontArmorAngle() + firetest.getShotVerticalAngle());

        } else if (firetest.getTargetVehiclePart() == FireTest.VehiclePart.SIDE_ARMOR) {
            armorThickness = Double.valueOf(firetest.getTargetVehicle().getSideArmorThickness());
            absVerticalShotAngle = (double) (firetest.getTargetVehicle().getSideArmorAngle() + firetest.getShotVerticalAngle());
        }
        Double a = Math.toRadians(Math.abs(absVerticalShotAngle));
        Double b = Math.toRadians(firetest.getShotHorizontalAngle());

        double[] vectorVertical = { 0., Math.tan(Math.toRadians(90-Math.abs(absVerticalShotAngle))), 1. };
        double[] vectorHorizontal = { Math.tan(Math.toRadians((double) firetest.getShotHorizontalAngle())), 1., 0. };

        Double shotAngleCompound =  VectorAngle.angleBetweenVectors(vectorVertical,vectorHorizontal);


        //TODO:Implement "Shell Critical Bounce Angle" variable dependent from ammo type
        //TODO: Penetration values could be stored here as Map

        switch (firetest.getShotDistance()) {
            case 100:
                rhaGunPenetration = firetest.getVehicle().getGun().getPen100();
                break;
            case 300:
                rhaGunPenetration = firetest.getVehicle().getGun().getPen300();
                break;
            case 500:
                rhaGunPenetration = firetest.getVehicle().getGun().getPen500();
            case 1000:
                rhaGunPenetration = firetest.getVehicle().getGun().getPen1000();
                break;
            case 1500:
                rhaGunPenetration = firetest.getVehicle().getGun().getPen1500();
                break;
            case 2000:
                rhaGunPenetration = firetest.getVehicle().getGun().getPen2000();
                break;
            case 2500:
                rhaGunPenetration = firetest.getVehicle().getGun().getPen2500();
                break;
            case 3000:
                rhaGunPenetration = firetest.getVehicle().getGun().getPen3000();
                break;
            default:
                // code block
        }
        double CosAngleCompound = Math.cos(Math.toRadians(shotAngleCompound));
        relativeArmorThickness = armorThickness / CosAngleCompound;
        if (Math.cos(Math.toRadians(shotAngleCompound)) <= 0 ||  absVerticalShotAngle >= 90.){

            firetest.setResult(FireTest.TestResult.NO_PENETRATION);
            firetest.setRelativeArmorThickness(Double.POSITIVE_INFINITY);
            firetest.setShotCompoundAngle(Math.round(shotAngleCompound*100.0)/100.0);
        }else {

            firetest.setShotCompoundAngle(Math.round(shotAngleCompound*100.0)/100.0);
            firetest.setRelativeArmorThickness(Math.round(relativeArmorThickness*100.0)/100.0);
        }

        if (rhaGunPenetration >= relativeArmorThickness) {
            firetest.setResult(FireTest.TestResult.PENETRATION);
        } else {
            firetest.setResult(FireTest.TestResult.NO_PENETRATION);
        }


        System.out.println("=========Firetest========");
        System.out.println("actual: " + armorThickness);
        System.out.printf("efective: %f\n", relativeArmorThickness);
        System.out.println("abs vertical angle: " + absVerticalShotAngle);
        System.out.println("shot angle vertical" + firetest.getShotVerticalAngle());
        System.out.println("shot angle horizontal" + firetest.getShotHorizontalAngle());
        System.out.println("angle compound" + shotAngleCompound);
        System.out.println("armor angle" + firetest.getTargetVehicle().getFrontArmorAngle());
        System.out.println("gun penetration: " + rhaGunPenetration);
        System.out.println("=========================");

    }

    private final static Context instance = new Context();

    /**
     * @return singleton instance of Context object
     */
    public static Context getInstance() {
        return instance;
    }


}