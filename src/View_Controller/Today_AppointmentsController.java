package View_Controller;

import Model.Appointment;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Controller for the Today's Appointments screen.
 */

public class Today_AppointmentsController extends ListManager implements Initializable {

    @FXML
    private Label todayAppointmentsTitleLabel;

    @FXML
    private TableView<Appointment> todayAppointmentsTableview;

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
    private Button exitButton;

    Stage stage;
    Parent scene;

    /**
     * Returns to Home Screen
     * @param event
     * @throws IOException
     */

    @FXML
    void onExitButton(ActionEvent event) throws IOException {

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
     * Initializes the table with a list of today's appointments.
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

        LocalDate currentDate = LocalDate.now();
        ObservableList<Appointment> todayAppointments = FXCollections.observableArrayList();

        for(Appointment a : ListManager.getAppointments()){
            if(a.getStartTime().toLocalDate().equals(currentDate)){
                todayAppointments.add(a);
            }
        }

        todayAppointmentsTableview.setItems(todayAppointments);

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

