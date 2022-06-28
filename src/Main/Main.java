package Main;

import Utils.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Andrew Thaxton
 * This is the Main.Main Class that runs the application
 */

public class Main extends Application {

    /**
     * Opens the login screen.
     * LAMBDA EXPRESSIONS LOCATED IN LISTMANAGER CLASS ON UPDATECUSTOMER AND UPDATEAPPOINTMENT METHODS. AS WELL AS IN
     * APPOINTMENT CONTROLLER ON ONMONTHRADIO.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/login.fxml"));
        primaryStage.setTitle("Thaxton Scheduler");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {

        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
