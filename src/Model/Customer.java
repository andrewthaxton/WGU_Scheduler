package Model;

/**
 * Customer Class
 */

public class Customer {

    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;

    /**
     * Customer constructor
     * @param id the id to set
     * @param name the name to set
     * @param address the address to set
     * @param postalCode the postal code to set
     * @param phone the phone to set
     * @param divisionId the division ID to set
     */

    public Customer(int id, String name, String address, String postalCode, String phone, int divisionId){
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    /**
     * sets the customer ID
     * @param id the id to set
     */

    public void setId(int id){
        this.id = id;
    }

    /**
     * gets the customer ID
     * @return the Customer ID
     */

    public int getId(){
        return id;
    }

    /**
     * sets the customer name
     * @param name the name to set
     */

    public void setName(String name){
        this.name = name;
    }

    /**
     * gets the customer name
     * @return the Customer name
     */

    public String getName(){
        return name;
    }

    /**
     * sets the customer address
     * @param address the address to set
     */

    public void setAddress(String address){
        this.address = address;
    }

    /**
     * gets the customer address
     * @return the Customer's address
     */

    public String getAddress(){
        return address;
    }

    /**
     * sets the customer postal code
     * @param postalCode the postal code to set
     */

    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }

    /**
     * gets the customer postal code
     * @return the Customer's postal code
     */

    public String getPostalCode(){
        return postalCode;
    }

    /**
     * sets the customer phone number
     * @param phone the phone to set
     */

    public void setPhone(String phone){
        this.phone = phone;
    }

    /**
     * gets the customer phone number
     * @return the Customer's phone number
     */

    public String getPhone(){
        return phone;
    }

    /**
     * sets the customer division ID
     * @param divisionId the division ID to set
     */

    public void setDivisionId(int divisionId){
        this.divisionId = divisionId;
    }

    /**
     * gets the customer division ID
     * @return the Customer's division ID
     */

    public int getDivisionId(){
        return divisionId;
    }
}
