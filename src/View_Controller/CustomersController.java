package View_Controller;

import Model.Customer;
import Model.ListManager;
import Utils.DBConnection;
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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the customer screen
 */

public class CustomersController implements Initializable {

    @FXML
    private Label customersTitleLabel;

    @FXML
    private TableView<Customer> customersTableview;

    @FXML
    private TableColumn<?, ?> customerIdCol;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableColumn<?, ?> addressCol;

    @FXML
    private TableColumn<?, ?> postalCol;

    @FXML
    private TableColumn<?, ?> phoneCol;

    @FXML
    private TableColumn<?, ?> divisionIdCol;

    @FXML
    private TableColumn<?, ?> divisionNameCol;

    @FXML
    private TableColumn<?, ?> countryCol;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button homeButton;

    Stage stage;
    Parent scene;

    /**
     * Opens the add customer page and sets the ID field.
     * @param event
     * @throws IOException
     */

    @FXML
    void onAddCustomerButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Add_Customer.fxml"));
        loader.load();
        Add_CustomerController addCustomerController = loader.getController();
        addCustomerController.setIdField();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Checks if a customer has an appointment and returns an error message if they do.
     * If not, a confirmation window appears and allows the user to delete the selected customer.
     * @param event
     */

    @FXML
    void onDeleteCustomerButton(ActionEvent event) {
        Customer selectedCustomer = customersTableview.getSelectionModel().getSelectedItem();
        boolean hasAppointment = false;
        try{
            String sql = "SELECT Customer_ID FROM appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int customerId = rs.getInt("Customer_ID");
                if(selectedCustomer.getId() == customerId) {
                    hasAppointment = true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(selectedCustomer == null)
            return;
        if(hasAppointment){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Cannot delete Customer with a scheduled appointment.");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Customer?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try{
                    String sql = "DELETE FROM customers WHERE Customer_ID=" + String.valueOf(selectedCustomer.getId());
                    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                    ps.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                ListManager.deleteCustomer(selectedCustomer);
            }
        }
    }

    /**
     * Opens the Home Screen
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
     * Opens the Update Customer screen and sends the selected Customer's information to be preloaded in the screen.
     * @param event
     * @throws IOException
     */

    @FXML
    void onUpdateCustomerButton(ActionEvent event) throws IOException {

        if (customersTableview.getSelectionModel().getSelectedItem() == null)
            return;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Update_Customer.fxml"));
        loader.load();
        Update_CustomerController updateCustomerController = loader.getController();
        updateCustomerController.sendCustomer(customersTableview.getSelectionModel().getSelectedItem());
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Initializes the page to have a customer table loaded with customers from the database.
     * @param url
     * @param resourcebundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourcebundle) {

        try{
            String sql = "SELECT * FROM customers";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int customerId = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                if(ListManager.getCustomers().isEmpty()){
                    Customer C = new Customer(customerId, name, address, postalCode, phone, divisionId);
                    ListManager.addCustomer(C);
                }
                else {
                    boolean existingCustomer = false;
                    for (Customer temp : ListManager.getCustomers()) {
                        if (temp.getId() == customerId) {
                            existingCustomer = true;
                        }
                    }
                    if(!existingCustomer){
                        Customer C = new Customer(customerId, name, address, postalCode, phone, divisionId);
                        ListManager.addCustomer(C);
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        customersTableview.setItems(ListManager.getCustomers());

        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        divisionIdCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

    }


}

