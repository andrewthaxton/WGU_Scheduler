package View_Controller;

import Model.Country;
import Model.Customer;
import Model.Division;
import Model.ListManager;
import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the update customer screen.
 */

public class Update_CustomerController extends ListManager implements Initializable {

    @FXML
    private Label updateCustomerTitleLabel;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label customerAddressLabel;

    @FXML
    private Label customerPostalLabel;

    @FXML
    private Label customerPhoneLabel;

    @FXML
    private Label customerIDLabel;

    @FXML
    private Label customerCountryLabel;

    @FXML
    private Label customerDivisionLabel;

    @FXML
    private TextField customerNameTextbox;

    @FXML
    private TextField customerAddressTextbox;

    @FXML
    private TextField customerPostalTextbox;

    @FXML
    private TextField customerPhoneTextbox;

    @FXML
    private TextField customerIDTextbox;

    @FXML
    private ComboBox<Country> customerCountryCombo;

    @FXML
    private ComboBox<Division> customerDivisionCombo;

    @FXML
    private Button updateCancelButton;

    @FXML
    private Button updateButton;

    /**
     * Filters the Division combo box based on the country
     * @param event
     */

    @FXML
    void onCustomerCountryCombo(ActionEvent event) {

        Country country = customerCountryCombo.getSelectionModel().getSelectedItem();
        ObservableList<Division> filteredDivisions = FXCollections.observableArrayList();
        for(Division d : ListManager.getDivisions()){
            if(country.getId() == d.getCountryId()){
                filteredDivisions.add(d);
            }
        }
        customerDivisionCombo.setItems(filteredDivisions);

    }

    /**
     * Updates the customer in the Database and in the program
     * @param event
     */

    @FXML
    void onUpdateButton(ActionEvent event) throws IOException {

        int id = Integer.parseInt(customerIDTextbox.getText());
        String name = customerNameTextbox.getText();
        String address = customerAddressTextbox.getText();
        String postal = customerPostalTextbox.getText();
        String phone = customerPhoneTextbox.getText();
        Division division = customerDivisionCombo.getSelectionModel().getSelectedItem();
        int divisionId = division.getId();

        ListManager.updateCustomer(id, name, address, postal, phone, divisionId);

        setStage(event, "Customers.fxml");

    }

    /**
     * Cancels new customer creation with a confirmation alert and opens the Customers screen.
     * @param event
     * @throws IOException
     */

    @FXML
    void onUpdateCancelButton(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel? This will clear all fields.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            setStage(event, "Customers.fxml");
        }

    }

    /**
     * Preloads the customer info in the text boxes.
     * @param customer
     */

    public void sendCustomer(Customer customer) {

        customerIDTextbox.setText(String.valueOf(customer.getId()));
        customerNameTextbox.setText(String.valueOf(customer.getName()));
        customerAddressTextbox.setText(String.valueOf(customer.getAddress()));
        customerPostalTextbox.setText(String.valueOf(customer.getPostalCode()));
        customerPhoneTextbox.setText(String.valueOf(customer.getPhone()));
        customerDivisionCombo.setValue(ListManager.lookupDivision(customer.getDivisionId()));
        customerCountryCombo.setValue(ListManager.lookupCountry(ListManager.lookupDivision(customer.getDivisionId()).getCountryId()));
    }

    /**
     * Initializes the combo boxes with the countries and divisions.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        try{
            String sql = "SELECT * FROM first_level_divisions";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs.getInt("COUNTRY_ID");
                if(ListManager.getDivisions().isEmpty()){
                    Division d = new Division(divisionId, divisionName,countryId);
                    ListManager.addDivision(d);
                }
                else {
                    boolean existingDivision = false;
                    for (Division temp : ListManager.getDivisions()) {
                        if (temp.getId() == divisionId) {
                            existingDivision = true;
                        }
                    }
                    if(!existingDivision){
                        Division d = new Division(divisionId, divisionName,countryId);
                        ListManager.addDivision(d);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try{
            String sql = "SELECT * FROM countries";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                if(ListManager.getCountries().isEmpty()){
                    Country c = new Country(countryId, countryName);
                    ListManager.addCountry(c);
                }
                else {
                    boolean existingCountry = false;
                    for (Country temp : ListManager.getCountries()) {
                        if (temp.getId() == countryId) {
                            existingCountry = true;
                        }
                    }
                    if(!existingCountry){
                        Country c = new Country(countryId, countryName);
                        ListManager.addCountry(c);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        customerCountryCombo.setItems(ListManager.getCountries());
        customerDivisionCombo.setItems(ListManager.getDivisions());

    }

}
