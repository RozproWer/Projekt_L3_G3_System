package com.giga.htask.controllers;

import com.giga.htask.App;
import com.giga.htask.controllers.content.shared.WelcomeController;
import com.giga.htask.model.Context;
import com.giga.htask.model.User;
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
    private  TabPane contentContainer;

    @FXML
    private Button buttonLogout;

    @FXML
    private VBox verticalMenu;

    private User loggedUser;



    public  void addTab(String title,String fxml,Object controller,Boolean isCloseable ) {
        Tab singleTab = new Tab(title);
        singleTab.setClosable(isCloseable);
        try{
            AnchorPane anchorPane = (AnchorPane) App.loadViewController(fxml,controller);
            singleTab.setContent(anchorPane);
            getInstance().contentContainer.getTabs().add(singleTab);
            System.out.println( getInstance().contentContainer.getTabs().size());
            getInstance().contentContainer.getSelectionModel().select(contentContainer.getTabs().size()-1);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void addTab(String title,String fxml,Boolean isCloseable ){
        try {
            Tab singleTab = new Tab(title, App.loadView(fxml));
            singleTab.setClosable(isCloseable);
            contentContainer.getTabs().add(singleTab);
            contentContainer.getSelectionModel().select(singleTab);
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




        Button buttonAppointments = new Button("Appointments");
        Button buttonPatients = new Button("Patients");
        Button buttonReports = new Button("Reports");
        Button buttonDoctors = new Button("Doctors");
        Button buttonSettings = new Button("Settings");
        Button buttonTasks = new Button("Tasks");
        switch (loggedUser.getRole()){
            case "administrator":
                verticalMenu.getChildren().add(buttonPatients);
                verticalMenu.getChildren().add(buttonDoctors);
                verticalMenu.getChildren().add(buttonReports);

                buttonPatients.setOnAction(e -> {
                        addTab("Patients","content/admin/Patients",true);
                });

                buttonDoctors.setOnAction(e -> {
                    addTab("Doctors","content/admin/Doctors",true);
                });
                buttonReports.setOnAction(e -> {
                    addTab("Reports","content/admin/Reports",true);
                });
                break;
            case "receptionist":

                break;
            case "doctor":
                buttonPatients.setOnAction(e -> {
                    try {
                        contentContainer.getTabs().add(new Tab("Reports", App.loadView("content/doctor/Patients")));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                verticalMenu.getChildren().add(buttonPatients);

                buttonAppointments.setOnAction(e -> {
                    try {
                        contentContainer.getTabs().add(new Tab("Tasks", App.loadView("content/doctor/Tasks")));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                verticalMenu.getChildren().add(buttonAppointments);

                Button tasksButton = new Button("Tasks");
                tasksButton.setOnAction(e -> {

                });
                verticalMenu.getChildren().add(tasksButton);

                buttonReports.setOnAction(e -> {

                });
                verticalMenu.getChildren().add(buttonReports);

                break;
            case "patient":
                buttonAppointments.setOnAction(e -> {

                });
                verticalMenu.getChildren().add(buttonAppointments);

                buttonTasks.setOnAction(e -> {

                });
                verticalMenu.getChildren().add(buttonTasks);

                buttonReports.setOnAction(e -> {

                });
                verticalMenu.getChildren().add(buttonReports);

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
