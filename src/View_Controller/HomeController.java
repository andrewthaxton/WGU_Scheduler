package View_Controller;


import Model.ListManager;
import Model.User;
import Utils.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Controller for the Home screen.
 */

public class HomeController extends ListManager implements Initializable {

    @FXML
    private Label homeTitleLabel;

    @FXML
    private Button customersButton;

    @FXML
    private Button appointmentsButton;

    @FXML
    private Button typeMonthReportButton;

    @FXML
    private Button contactScheduleButton;

    @FXML
    private Button todayAppointments;

    @FXML
    private Button appExitButton;

    Stage stage;
    Parent scene;
    static Boolean appointmentCheck = true;

    /**
     * Exits the application
     * @param event
     */
    @FXML
    void onAppExitButton(ActionEvent event) {

        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are sure you want to exit?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.exit(0);
            }
        }

    }

    /**
     * Opens Appointments Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void onAppointmentsButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Appointments.fxml"));
        loader.load();
        AppointmentsController appointmentsController = loader.getController();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Opens the Contact Schedule screen
     * @param event
     * @throws IOException
     */
    @FXML
    void onContactScheduleButton(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Contact_Schedule.fxml"));
        loader.load();
        Contact_ScheduleController contactScheduleController = loader.getController();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Opens the Customers Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void onCustomersButton(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Customers.fxml"));
        loader.load();
        CustomersController customersController = loader.getController();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Opens the Today's Appointments Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void onTodayAppointments(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Today_Appointments.fxml"));
        loader.load();
        Today_AppointmentsController todayAppointmentsController = loader.getController();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Opens the Type/Month Report Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void onTypeMonthReportButton(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Type_Month_Report.fxml"));
        loader.load();
        Type_Month_ReportController typeMonthReportController = loader.getController();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * This initializes the form to show an Alert if the User has an upcoming Appointment.
     */

    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(appointmentCheck) {
            ZoneId localZoneId = ZoneId.systemDefault();
            ZoneId databaseZoneId = ZoneId.of("UTC");
            DateTimeFormatter dt_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            boolean upcomingAppointment = false;

            try {
                String sql = "SELECT * FROM appointments";

                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int appointmentId = rs.getInt("Appointment_ID");
                    int userId = rs.getInt("User_ID");
                    String dbStart = rs.getString("Start");
                    LocalDateTime utcStart = LocalDateTime.parse(dbStart, dt_formatter);
                    ZonedDateTime utcStartZDT = utcStart.atZone(databaseZoneId);
                    ZonedDateTime startUserZDT = utcStartZDT.withZoneSameInstant(localZoneId);

                    if (userId == ListManager.getUser().getId() && Math.abs(Duration.between(startUserZDT, ZonedDateTime.now()).toMinutes()) <= 15) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Upcoming Appointment");
                        alert.setContentText("Apt. ID: " + String.valueOf(appointmentId) + " | Date/Time: " + startUserZDT);
                        alert.showAndWait();

                        upcomingAppointment = true;
                    }

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (upcomingAppointment == false) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Upcoming Appointment");
                alert.setContentText("No upcoming Appointments.");
                alert.showAndWait();
            }
            appointmentCheck = false;
        }
    }

}
