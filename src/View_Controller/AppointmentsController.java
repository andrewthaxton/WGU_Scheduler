package View_Controller;

import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.ListManager;
import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * Controller for the appointments screen.
 */

public class AppointmentsController extends ListManager implements Initializable {

    @FXML
    private Label appointmentsTitleLabel;

    @FXML
    private TableView<Appointment> appointmentsTableview;

    @FXML
    private TableColumn<?, ?> aptIdCol;

    @FXML
    private TableColumn<?, ?> titleCol;

    @FXML
    private TableColumn<?, ?> descriptionCol;

    @FXML
    private TableColumn<?, ?> locationCol;

    @FXML
    private TableColumn<?, ?> contactCol;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private TableColumn<?, ?> startCol;

    @FXML
    private TableColumn<?, ?> endCol;

    @FXML
    private TableColumn<?, ?> customerIdCol;

    @FXML
    private RadioButton allRadio;

    @FXML
    private ToggleGroup appointmentGroupToggle;

    @FXML
    private RadioButton monthRadio;

    @FXML
    private RadioButton weekRadio;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private Button updateAppointmentButton;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    Stage stage;
    Parent scene;
    LocalDate currentDate = LocalDate.now();
    Month currentMonth;
    ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
    ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();
    Locale locale = Locale.getDefault();
    int weekOfYear;

    /**
     * Opens the add appointment screen
     * @param event
     * @throws IOException
     */

    @FXML
    void onAddAppointmentButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Add_Appointment.fxml"));
        loader.load();
        Add_AppointmentController addAppointmentController = loader.getController();
        addAppointmentController.setIdField();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Shows all appointments
     * @param event
     */

    @FXML
    void onAllRadio(ActionEvent event) {

        appointmentsTableview.setItems(ListManager.getAppointments());

    }

    /**
     * Displays confirmation alert to delete appointment.
     * @param event
     */

    @FXML
    void onDeleteAppointmentButton(ActionEvent event) {

        Appointment selectedAppointment = appointmentsTableview.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel appointment " + String.valueOf(selectedAppointment.getId()) + " " + selectedAppointment.getType() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try{
                String sql = "DELETE FROM appointments WHERE Appointment_ID=" + String.valueOf(selectedAppointment.getId());
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                ps.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            ListManager.deleteAppointment(selectedAppointment);
        }

    }

    /**
     * Returns to home screen.
     * @param event
     * @throws IOException
     */

    @FXML
    void onHomeButton(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home.fxml"));
        loader.load();
        HomeController homeController = loader.getController();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Filters appointments by month.
     * LAMBDA EXPRESSION: This lambda expression goes through each appointment and checks whether the current month
     * matches the appointment month.
     * @param event
     */

    @FXML
    void onMonthRadio(ActionEvent event) {

        currentMonth = currentDate.getMonth();

        ListManager.getAppointments().forEach((appointment) -> {
            if(appointment.getStartTime().getMonth().equals(currentMonth)){
                boolean appointmentExists = false;
                for(Appointment b : monthAppointments){
                    if(b.getId() == appointment.getId()){
                        appointmentExists = true;
                    }
                }
                if(!appointmentExists){
                    monthAppointments.add(appointment);
                }
            }
        });

        appointmentsTableview.setItems(monthAppointments);

    }

    /**
     * Opens the update appointment screen.
     * @param event
     * @throws IOException
     */

    @FXML
    void onUpdateAppointmentButton(ActionEvent event) throws IOException{

        if (appointmentsTableview.getSelectionModel().getSelectedItem() == null)
            return;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Update_Appointment.fxml"));
        loader.load();
        Update_AppointmentController updateAppointmentController = loader.getController();
        updateAppointmentController.sendAppointment(appointmentsTableview.getSelectionModel().getSelectedItem());
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * filters appointments by week.
     * @param event
     */

    @FXML
    void onWeekRadio(ActionEvent event) {

        weekOfYear = currentDate.get(WeekFields.of(locale).weekOfYear());
        System.out.println(weekOfYear);

        for(Appointment a : ListManager.getAppointments()){
            if(weekOfYear == a.getStartTime().get(WeekFields.of(locale).weekOfYear())){
                boolean appointmentExists = false;
                for(Appointment b : weekAppointments){
                    if(b.getId() == a.getId()){
                        appointmentExists = true;
                    }
                }
                if(!appointmentExists){
                    weekAppointments.add(a);
                }
            }
        }

        appointmentsTableview.setItems(weekAppointments);

    }

    /**
     * Displays the next month or week of appointments depending on the selected radio button.
     * @param event
     */

    @FXML
    void onNextButton(ActionEvent event) {

        if(allRadio.isSelected()){
            return;
        }
        if(monthRadio.isSelected()){
            currentMonth = currentMonth.plus(1);
            for(Appointment a : ListManager.getAppointments()){
                if(a.getStartTime().getMonth().equals(currentMonth)){
                    monthAppointments.add(a);
                }
            }

            appointmentsTableview.setItems(monthAppointments);

        }
        if(weekRadio.isSelected()){
            weekOfYear = weekOfYear + 1;
            for(Appointment a : ListManager.getAppointments()){
                if(weekOfYear == a.getStartTime().get(WeekFields.of(locale).weekOfYear())){
                    weekAppointments.add(a);
                }
            }

            appointmentsTableview.setItems(weekAppointments);
        }

    }

    /**
     * Displays the previous month or week of appointments depending on the selected radio button.
     * @param event
     */

    @FXML
    void onPreviousButton(ActionEvent event) {

        if(allRadio.isSelected()){
            return;
        }
        if(monthRadio.isSelected()){
            currentMonth = currentMonth.minus(1);
            for(Appointment a : ListManager.getAppointments()){
                if(a.getStartTime().getMonth().equals(currentMonth)){
                    monthAppointments.add(a);
                }
            }

            appointmentsTableview.setItems(monthAppointments);

        }
        if(weekRadio.isSelected()){
            weekOfYear = weekOfYear - 1;
            for(Appointment a : ListManager.getAppointments()){
                if(weekOfYear == a.getStartTime().get(WeekFields.of(locale).weekOfYear())){
                    weekAppointments.add(a);
                }
            }

            appointmentsTableview.setItems(weekAppointments);
        }

    }

    /**
     * Initializes the table to have all appointments listed.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZoneId databaseZoneId = ZoneId.of("UTC");
        DateTimeFormatter dt_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try{
            String sql = "SELECT * FROM appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String Title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                int contactId = rs.getInt("Contact_ID");
                String dbStart = rs.getString("Start");
                LocalDateTime utcStart = LocalDateTime.parse(dbStart, dt_formatter);
                ZonedDateTime utcStartZDT = utcStart.atZone(databaseZoneId);
                ZonedDateTime startUserZDT = utcStartZDT.withZoneSameInstant(localZoneId);
                String dbEnd = rs.getString("End");
                LocalDateTime utcEnd = LocalDateTime.parse(dbEnd, dt_formatter);
                ZonedDateTime utcEndZDT = utcEnd.atZone(databaseZoneId);
                ZonedDateTime endUserZDT = utcEndZDT.withZoneSameInstant(localZoneId);
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                String type = rs.getString("Type");

                if(ListManager.getAppointments().isEmpty()){
                    Appointment A = new Appointment(appointmentId, Title, startUserZDT, endUserZDT, customerId, userId, contactId, type, description, location);
                    ListManager.addAppointment(A);
                }
                else {
                    boolean existingAppointment = false;
                    for (Appointment temp : ListManager.getAppointments()) {
                        if (temp.getId() == appointmentId) {
                            existingAppointment = true;
                        }
                    }
                    if(!existingAppointment){
                        Appointment A = new Appointment(appointmentId, Title, startUserZDT, endUserZDT, customerId, userId, contactId, type, description, location);
                        ListManager.addAppointment(A);
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try{
            String sql = "SELECT * FROM contacts";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int contactId = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");

                if(ListManager.getContacts().isEmpty()){
                    Contact C = new Contact(contactId, name);
                    ListManager.addContact(C);
                }
                else {
                    boolean existingContact = false;
                    for (Contact temp : ListManager.getContacts()) {
                        if (temp.getId() == contactId) {
                            existingContact = true;
                        }
                    }
                    if(!existingContact){
                        Contact C = new Contact(contactId, name);
                        ListManager.addContact(C);
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        appointmentsTableview.setItems(ListManager.getAppointments());

        aptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

}
