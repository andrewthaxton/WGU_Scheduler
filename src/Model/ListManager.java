package Model;

import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The ListManager class is composed of mostly static members and used to hold and manipulate the Scheduler's data
 */

public class ListManager {

    private static ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private static User user;
    private static ObservableList<Division> divisions = FXCollections.observableArrayList();
    private static ObservableList<Country> countries = FXCollections.observableArrayList();
    private static ObservableList<Contact> contacts = FXCollections.observableArrayList();

    /**
     * Adds a new contact
     * @param contact the contact to add
     */

    public static void addContact(Contact contact) {
        contacts.add(contact);
    }

    /**
     * @return a list of Contacts
     */

    public static ObservableList<Contact> getContacts() {
        return contacts;
    }

    /**
     * Finds a contact by its ID
     * @param contactId the ID used to find the Contact
     * @return the Contact
     */

    public static Contact lookupContact(int contactId) {
        Contact found = null;
        for (int index = 0; index < contacts.size(); index++){
            Contact c = contacts.get(index);
            if(contactId == c.getId()){
                found = c;
                break;
            }
        }
        return found;
    }

    /**
     * Adds a new country
     * @param country the country to be added
     */

    public static void addCountry(Country country){
        countries.add(country);
    }

    /**
     * @return the list of countries
     */

    public static ObservableList<Country> getCountries() {
        return countries;
    }

    /**
     * Finds country by its ID
     * @param countryId the ID used to find the country
     * @return the country with given country ID
     */

    public static Country lookupCountry(int countryId) {
        Country found = null;
        for (int index = 0; index < countries.size(); index++){
            Country c = countries.get(index);
            if(countryId == c.getId()){
                found = c;
                break;
            }
        }
        return found;
    }

    /**
     * Adds a new division
     * @param division the division to be added
     */

    public static void addDivision(Division division) { divisions.add(division); }

    /**
     * @return the list of divisions
     */

    public static ObservableList<Division> getDivisions() {
        return divisions;
    }

    /**
     * Finds a division by its ID
     * @param divisionId used to find the division
     * @return the Division with the given division ID
     */

    public static Division lookupDivision(int divisionId) {
        Division found = null;
        for(int index = 0; index < divisions.size(); index++){
            Division d = divisions.get(index);
            if(divisionId == d.getId()){
                found = d;
                break;
            }
        }
        return found;
    }

    /**
     * Adds a new User
     * @param userId the User ID to set
     * @param username the username to set
     * @param password the password to set
     */

    public static void setUser(int userId, String username, String password){
        user = new User(userId, username, password);
    }

    /**
     * @return the User
     */

    public static User getUser(){
        return user;
    }

    /**
     * Adds a new customer
     * @param customer the customer to be added
     */

    public static void addCustomer(Customer customer){
        customers.add(customer);
    }

    /**
     * Adds a new appointment
     * @param appointment the appointment to be added
     */

    public static void addAppointment(Appointment appointment){
        appointments.add(appointment);
    }

    /**
     * Updates an existing customer.
     * Contains LAMBDA EXPRESSION: iterates through the customers to check for the matching customer ID.
     * @param id the Customer's ID
     * @param name the Customer's updated name
     * @param address the Customer's updated address
     * @param postalCode the Customer's updated postalCode
     * @param phone the Customer's updated phone number
     * @param divisionId the Customer's updated division ID
     */

    public static void updateCustomer(int id, String name, String address, String postalCode, String phone, int divisionId){
        customers.forEach((customer) -> {
            if (customer.getId() == id) {
                try {
                    String sql = "UPDATE customers SET Customer_Name='" + name + "', Address='" + address + "', Postal_Code='" + postalCode + "', Phone='" + phone + "', Division_ID=" + Integer.toString(divisionId) + " WHERE Customer_ID=" + Integer.toString(id);

                    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                    ps.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                customer.setName(name);
                customer.setAddress(address);
                customer.setPostalCode(postalCode);
                customer.setPhone(phone);
                customer.setDivisionId(divisionId);
            }

        });

    }

    /**
     * Updates an existing Appointment.
     * Contains LAMBDA EXPRESSION: iterates through the appointments to check for the matching appointment ID.
     * @param id the appointment ID
     * @param title the updated appointment title
     * @param startTime the updated appointment start time
     * @param endTime the updated appointment end time
     * @param customerId the updated customer ID
     * @param userId the updated user ID
     * @param contactId the updated contact ID
     * @param type the updated appointment type
     * @param description the updated appointment description
     * @param location the updated appointment description
     */

    public static void updateAppointment(int id, String title, ZonedDateTime startTime, ZonedDateTime endTime, int customerId, int userId, int contactId, String type, String description, String location){

        DateTimeFormatter dt_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcStartZDT = startTime.withZoneSameInstant(utcZoneId);
        ZonedDateTime utcEndZDT = endTime.withZoneSameInstant(utcZoneId);
        String utcStartString = utcStartZDT.toLocalDateTime().format(dt_formatter);
        String utcEndString = utcEndZDT.toLocalDateTime().format(dt_formatter);
        try {
            String sql = "UPDATE appointments SET Title='" + title + "', Type='" + type + "', Start='" + utcStartString + "', End='" + utcEndString + "', Customer_ID=" + Integer.toString(customerId) + ", User_ID=" + Integer.toString(userId) + ", Contact_ID=" + Integer.toString(contactId) + ", Description='" + description + "', Location='" + location + "' WHERE Appointment_ID=" + Integer.toString(id);
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        appointments.forEach((appointment) -> {
            if(appointment.getId() == id) {
                appointment.setTitle(title);
                appointment.setType(type);
                appointment.setStartTime(startTime);
                appointment.setEndTime(endTime);
                appointment.setCustomerId(customerId);
                appointment.setUserId(userId);
                appointment.setContactId(contactId);
                appointment.setDescription(description);
                appointment.setLocation(location);
            }
        });
    }

    /**
     * Deletes existing customer
     * @param selectedCustomer the customer to be deleted
     */

    public static void deleteCustomer(Customer selectedCustomer){
        customers.remove(selectedCustomer);
    }

    /**
     * Deletes existing appointment
     * @param selectedAppointment
     */

    public static void deleteAppointment(Appointment selectedAppointment){
        appointments.remove(selectedAppointment);
    }

    /**
     * @return the list of all customers
     */

    public static ObservableList<Customer> getCustomers(){
        return customers;
    }

    /**
     * @return the list of all appointments
     */

    public static ObservableList<Appointment> getAppointments(){
        return appointments;
    }

    /**
     * Sets the stage for the next window.
     * @param event
     * @param location
     * @throws IOException
     */

    public void setStage(ActionEvent event, String location) throws IOException {
        Stage stage;
        Parent scene;
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(location));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
