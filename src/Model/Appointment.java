package Model;

import java.time.ZonedDateTime;

/**
 * The Appointment Class
 */

public class Appointment {

    private int id;
    private String title;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private int customerId;
    private int userId;
    private int contactId;
    private String type;
    private String description;
    private String location;

    /**
     * Appointment Constructor
     * @param id the id to set
     * @param title the title to set
     * @param startTime the start time to set
     * @param endTime the end time to set
     * @param customerId the customer to set
     * @param userId the user to set
     * @param contactId the contact to set
     * @param type the type to set
     * @param description the description to set
     * @param location the location to set
     */

    public Appointment(int id, String title, ZonedDateTime startTime, ZonedDateTime endTime, int customerId, int userId, int contactId, String type, String description, String location){
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.type = type;
        this.description = description;
        this.location = location;
    }

    /**
     * Sets the appointment ID
     * @param id the id to set
     */

    public void setId(int id){
        this.id = id;
    }

    /**
     * Gets the appointment ID
     * @return the Appointment ID
     */

    public int getId(){
        return id;
    }

    /**
     * Sets the appointment title
     * @param title the title to set
     */

    public void setTitle(String title){
        this.title = title;
    }

    /**
     * gets the appointment title
     * @return the title of the Appointment
     */

    public String getTitle(){
        return title;
    }

    /**
     * sets the appointment start time
     * @param startTime the start time to set
     */

    public void setStartTime(ZonedDateTime startTime){ this.startTime = startTime;
    }

    /**
     * gets the appointment start time
     * @return the start time of the Appointment
     */

    public ZonedDateTime getStartTime(){
        return startTime;
    }

    /**
     * sets the appointment end time
     * @param endTime the end time to set
     */

    public void setEndTime(ZonedDateTime endTime){
        this.endTime = endTime;
    }

    /**
     * gets the appointment end time
     * @return the end time of the Appointment
     */

    public ZonedDateTime getEndTime(){
        return endTime;
    }

    /**
     *sets the customer ID for appointment
     * @param customerId the customer ID to set
     */

    public void setCustomerId(int customerId){
        this.customerId = customerId;
    }

    /**
     * gets the customer ID for appointment
     * @return the customer ID the Appointment is for
     */

    public int getCustomerId(){
        return customerId;
    }

    /**
     * sets the user ID for appointment
     * @param userId the user to set
     */

    public void setUserId(int userId){
        this.userId = userId;
    }

    /**
     * gets user ID for appointment
     * @return the User ID scheduling the appointment
     */

    public int getUserId(){
        return userId;
    }

    /**
     * sets contact ID for appointment
     * @param contactId the contact to set
     */

    public void setContactId(int contactId){
        this.contactId = contactId;
    }

    /**
     * gets contact ID for appointment
     * @return the contact ID for the Appointment
     */

    public int getContactId(){
        return contactId;
    }

    /**
     * sets the appointment type
     * @param type the type to set
     */

    public void setType(String type){
        this.type = type;
    }

    /**
     * gets the appointment type
     * @return the type of Appointment
     */

    public String getType(){
        return type;
    }

    /**
     * sets the appointment description
     * @param description the description to set
     */

    public void setDescription(String description){
        this.description = description;
    }

    /**
     * gets the appointment description
     * @return the description of the Appointment
     */

    public String getDescription(){
        return description;
    }

    /**
     * sets the appointment location
     * @param location the location to set
     */

    public void setLocation(String location){
        this.location = location;
    }

    /**
     * gets the appointment location
     * @return the location of the Appointment
     */

    public String getLocation(){
        return location;
    }
}
