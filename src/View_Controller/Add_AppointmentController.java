package View_Controller;

import Model.*;
import Utils.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The controller for the screen to add appointments.
 */

public class Add_AppointmentController extends ListManager implements Initializable {

    @FXML
    private Label addAppointmentTitleLabel;

    @FXML
    private Label AptIdLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label startLabel;

    @FXML
    private Label endLabel;

    @FXML
    private Label customerIdLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private Label contactLabel;

    @FXML
    private ComboBox<Contact> contactCombo;

    @FXML
    private TextField aptIdTextbox;

    @FXML
    private TextField titleTextbox;

    @FXML
    private DatePicker startDatepicker;

    @FXML
    private DatePicker endDatepicker;

    @FXML
    private TextField userIdTextbox;

    @FXML
    private TextField customerIdTextbox;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField startHourTextbox;

    @FXML
    private TextField startMinTextbox;

    @FXML
    private TextField endHourTextbox;

    @FXML
    private TextField endMinTextbox;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextField descriptionTextbox;

    @FXML
    private Label typeLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private TextField typeTextbox;

    @FXML
    private TextField locationTextbox;

    @FXML
    private Button addButton;

    /**
     * Cancels appointment creation and returns to appointments screen
     * @param event
     * @throws IOException
     */

    @FXML
    void onCancelButton(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel? This will clear all fields.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            setStage(event, "Appointments.fxml");
        }

    }

    /**
     * Checks to see if all info was entered correctly, adds it if it is, or returns a customer error message.
     * @param event
     * @throws IOException
     */

    @FXML
    void onAddButton(ActionEvent event) throws IOException{

        int startHour;
        int startMin;
        int endHour;
        int endMin;
        try {
            startHour = Integer.parseInt(startHourTextbox.getText());
            startMin = Integer.parseInt(startMinTextbox.getText());
            endHour = Integer.parseInt(endHourTextbox.getText());
            endMin = Integer.parseInt(endMinTextbox.getText());
            String startHourString;
            String startMinString;
            String endHourString;
            String endMinString;
            if (startHour < 10) {
                startHourString = "0" + Integer.toString(startHour);
            } else {
                startHourString = Integer.toString(startHour);
            }
            if (startMin < 10) {
                startMinString = "0" + Integer.toString(startMin);
            } else {
                startMinString = Integer.toString(startMin);
            }
            if (endHour < 10) {
                endHourString = "0" + Integer.toString(endHour);
            } else {
                endHourString = Integer.toString(endHour);
            }
            if (endMin < 10) {
                endMinString = "0" + Integer.toString(endMin);
            } else {
                endMinString = Integer.toString(endMin);
            }

            if (titleTextbox.getText().isEmpty() || descriptionTextbox.getText().isEmpty() || customerIdTextbox.getText().isEmpty() || userIdTextbox.getText().isEmpty() || contactCombo.getSelectionModel().isEmpty() || startDatepicker.getValue().toString().isEmpty() || startHourTextbox.getText().isEmpty() ||
                    startMinTextbox.getText().isEmpty() || endDatepicker.getValue().toString().isEmpty() || endHourTextbox.getText().isEmpty() || endMinTextbox.getText().isEmpty() || typeTextbox.getText().isEmpty() || locationTextbox.getText().isEmpty()) {
                Alert missingInfo = new Alert(Alert.AlertType.ERROR);
                missingInfo.setTitle("Missing Information");
                missingInfo.setContentText("Please fill in all fields");
                missingInfo.showAndWait();
            } else {
                String startDateTimeString = startDatepicker.getValue().toString() + " " + startHourString + ":" + startMinString + ":00";
                String endDateTimeString = endDatepicker.getValue().toString() + " " + endHourString + ":" + endMinString + ":00";

                DateTimeFormatter dt_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime userStartDT = LocalDateTime.parse(startDateTimeString, dt_formatter);
                LocalDateTime userEndDT = LocalDateTime.parse(endDateTimeString, dt_formatter);

                ZoneId userZoneId = ZoneId.systemDefault();
                ZoneId utcZoneId = ZoneId.of("UTC");
                ZoneId etZoneId = ZoneId.of("America/New_York");

                ZonedDateTime userStartZDT = userStartDT.atZone(userZoneId);
                ZonedDateTime userEndZDT = userEndDT.atZone(userZoneId);
                ZonedDateTime utcStartZDT = userStartZDT.withZoneSameInstant(utcZoneId);
                ZonedDateTime utcEndZDT = userEndZDT.withZoneSameInstant(utcZoneId);
                ZonedDateTime etStartZDT = userStartZDT.withZoneSameInstant(etZoneId);
                ZonedDateTime etEndZDT = userEndZDT.withZoneSameInstant(etZoneId);
                String utcStartString = utcStartZDT.toLocalDateTime().format(dt_formatter);
                String utcEndString = utcEndZDT.toLocalDateTime().format(dt_formatter);

                Boolean appointmentConflict = false;
                for (Appointment A : ListManager.getAppointments()) {
                    if ((userStartZDT.isAfter(A.getStartTime()) && userStartZDT.isBefore(A.getEndTime())) || (userEndZDT.isAfter(A.getStartTime()) && userEndZDT.isBefore(A.getEndTime())) || userStartZDT.isEqual(A.getStartTime()) || userEndZDT.isEqual(A.getEndTime())) {
                        appointmentConflict = true;
                        break;
                    }
                }

                if (etStartZDT.getHour() < 8 || etStartZDT.getHour() > 22) {
                    Alert startNotInBusinessHours = new Alert(Alert.AlertType.ERROR);
                    startNotInBusinessHours.setTitle("Start Time Error");
                    startNotInBusinessHours.setContentText("Start time must be between 8:00 EST and 22:00 EST and entered using 24 time system.");
                    startNotInBusinessHours.showAndWait();
                } else if (etStartZDT.isAfter(etEndZDT)) {
                    Alert startAfterEnd = new Alert(Alert.AlertType.ERROR);
                    startAfterEnd.setTitle("Start after End");
                    startAfterEnd.setContentText("Start time must come before End time.");
                    startAfterEnd.showAndWait();
                } else if (etStartZDT.getDayOfYear() != etEndZDT.getDayOfYear() || etEndZDT.getHour() > 22) {
                    Alert endNotInBusinessHours = new Alert(Alert.AlertType.ERROR);
                    endNotInBusinessHours.setTitle("End Time Error");
                    endNotInBusinessHours.setContentText("End time must not surpass closing time of 10:00 pm EST.");
                    endNotInBusinessHours.showAndWait();
                } else if (appointmentConflict) {
                    Alert conflict = new Alert(Alert.AlertType.ERROR);
                    conflict.setTitle("Appointment Conflict");
                    conflict.setContentText("Conflicting appointments. Please choose another time.");
                    conflict.showAndWait();
                } else {
                    try {
                        int id = Integer.parseInt(aptIdTextbox.getText());
                        String title = titleTextbox.getText();
                        int customerId = Integer.parseInt(customerIdTextbox.getText());
                        int userId = Integer.parseInt(userIdTextbox.getText());
                        Contact contact = contactCombo.getSelectionModel().getSelectedItem();
                        int contactId = contact.getId();
                        String type = typeTextbox.getText();
                        String description = descriptionTextbox.getText();
                        String location = locationTextbox.getText();

                        Appointment appointment = new Appointment(id, title, userStartZDT, userEndZDT, customerId, userId, contactId, type, description, location);

                        ListManager.addAppointment(appointment);

                        try {
                            String sql = "INSERT INTO appointments VALUES (" + id + ",'" + title + "','" + description + "','" + location + "','" + type + "','" + utcStartString + "','" + utcEndString + "',NOW(),'" + ListManager.getUser().getUsername() + "',NOW(),'" + ListManager.getUser().getUsername() + "'," + customerId + "," + userId + "," + contactId + ");";
                            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                            ps.executeUpdate();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        setStage(event, "Appointments.fxml");
                    } catch(NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialogue");
                        alert.setContentText("Please enter a valid value for each text field");
                        alert.showAndWait();
                    }
                }


            }
        } catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Please enter a valid value for each text field");
            alert.showAndWait();
        }

    }

    /**
     * Sets the Appointment ID automatically
     */

    public void setIdField() {

        int id = ListManager.getAppointments().size() + 1;
        for(Appointment a: ListManager.getAppointments()){
            if(a.getId() == id){
                do{
                    id++;
                }while(a.getId() == id);
            }
        }
        aptIdTextbox.setText(String.valueOf(id));
    }

    /**
     * Initializes the combo box with the contact names.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        contactCombo.setItems(ListManager.getContacts());

    }

}
