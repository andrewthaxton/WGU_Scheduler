package Model;

/**
 * Contact Class
 */

public class Contact {

    private int id;
    private String name;

    /**
     * Contact Constructor
     * @param id the id to set
     * @param name the name to set
     */

    public Contact(int id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * Sets the contact ID
     * @param id the id to set
     */

    public void setId(int id){
        this.id = id;
    }

    /**
     * gets the contact ID
     * @return the Contact ID
     */

    public int getId(){
        return id;
    }

    /**
     * sets the contact name
     * @param name the name to set
     */

    public void setName(String name){
        this.name = name;
    }

    /**
     * gets the contact name
     * @return the Contact name
     */

    public String getName(){
        return name;
    }

    /**
     * Overrides the toString method to return the Contact name
     * @return the Contact name
     */

    @Override
    public String toString(){
        return (name);
    }
}
