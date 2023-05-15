package com.giga.htask.controllers;

import com.giga.htask.App;
import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.controllers.content.shared.WelcomeController;
import com.giga.htask.model.Context;
import com.giga.htask.model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Singleton controller of dynamic content part of app that manages TabPane tabs and vertical menu
 *
 * @author GigaNByte
 * @since 1.0
 */
public class MainAuthedController extends AnchorPane implements Initializable{


    private final static MainAuthedController instance = new MainAuthedController();

    @FXML
    private Label labelUserInfo;
    @FXML
    private  TabPane contentTabPane;
    @FXML
    private Button buttonLogout;
    @FXML
    private VBox verticalMenu;

    private User loggedUser;



    public  void addTab(String title, String fxml, ContentController controller, Boolean isCloseable ) {
        Tab singleTab = new Tab(title);
        singleTab.setClosable(isCloseable);
        try{
            AnchorPane anchorPane = (AnchorPane) App.loadViewController(fxml,controller);
            singleTab.setContent(anchorPane);
            getInstance().contentTabPane.getTabs().add(singleTab);
            getInstance().contentTabPane.getSelectionModel().select(contentTabPane.getTabs().size()-1);



        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void addTab(String title,String fxml,Boolean isCloseable ){
        try {
            FXMLLoader fxmlLoader = App.loadLoader(fxml);
            Tab singleTab = new Tab(title, fxmlLoader.load());
            //OnSelectionChanged


            singleTab.setOnSelectionChanged(event -> {
                if(singleTab.isSelected()){
                    ContentController contentController =  fxmlLoader.getController();
                    contentController.onTabSelected();
                }
            });
            singleTab.setClosable(isCloseable);
            contentTabPane.getTabs().add(singleTab);
            contentTabPane.getSelectionModel().select(singleTab);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        this.loggedUser = Context.getInstance().getLoggedUser();
        labelUserInfo.setText(loggedUser.getName() + " " + loggedUser.getSurname() + " " + loggedUser.getRole());
        buttonLogout.setOnAction(event -> {
            try {
                //reset
                Parent root = App.loadView("MainView");
                App.stage.setScene(new Scene(root));
                loggedUser = null;
                Context.getInstance().setLoggedUser(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


         addTab("Welcome","content/shared/Welcome",new WelcomeController(loggedUser.getId()),false);


        Button buttonPatients = new Button("Patients");
        Button buttonDoctors = new Button("Doctors");
        Button buttonSettings = new Button("Settings");
        switch (loggedUser.getRole()){
            case "administrator":
                verticalMenu.getChildren().add(buttonPatients);
                verticalMenu.getChildren().add(buttonDoctors);
                buttonPatients.setOnAction(e -> {
                        addTab("Patients","content/patients/Patients",true);
                });
                buttonDoctors.setOnAction(e -> {
                    addTab("Doctors","content/doctors/Doctors",true);
                });

                break;
            case "receptionist":
                verticalMenu.getChildren().add(buttonPatients);
                verticalMenu.getChildren().add(buttonDoctors);
                buttonPatients.setOnAction(e -> {
                    addTab("Patients","content/patients/Patients",true);
                });
                buttonDoctors.setOnAction(e -> {
                    addTab("Doctors","content/doctors/Doctors",true);
                });
                break;
            case "doctor":
                verticalMenu.getChildren().add(buttonPatients);
                verticalMenu.getChildren().add(buttonDoctors);
                buttonPatients.setOnAction(e -> {
                    addTab("Patients","content/patients/Patients",true);
                });
                buttonDoctors.setOnAction(e -> {

                    ContentController doctorController = new com.giga.htask.controllers.content.doctors.DoctorController(loggedUser.getId());
                    addTab("Doctor","content/doctors/Doctor",doctorController,true);
                });
                buttonDoctors.setText("My doctor profile");
                break;
            case "patient":
                verticalMenu.getChildren().add(buttonPatients);
                verticalMenu.getChildren().add(buttonDoctors);
                buttonPatients.setOnAction(e -> {

                 ContentController patientController = new com.giga.htask.controllers.content.patients.PatientController(loggedUser.getId());
                 addTab("Patient","content/patients/Patient",patientController,true);
                });
                buttonPatients.setText("My patient profile");
                buttonDoctors.setVisible(false);
                break;
        }

        /* Shared */

        verticalMenu.getChildren().add(buttonSettings);
        buttonSettings.setOnAction(e -> {
            addTab("Settings","content/shared/Settings",true);
        });



    }
    public static MainAuthedController getInstance() {
        return instance;
    }

}
