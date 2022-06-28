package Model;

/**
 * Division Class
 */

public class Division {

    private int id;
    private String name;
    private int countryId;

    /**
     * Division constructor
     * @param id the division ID
     * @param name the division name
     * @param countryId the country ID the division belongs to
     */

    public Division(int id, String name, int countryId){
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }

    /**
     * sets the Division ID
     * @param id the id to set
     */

    public void setId(int id){
        this.id = id;
    }

    /**
     * gets the division ID
     * @return the ID
     */

    public int getId(){
        return id;
    }

    /**
     * sets the Division Name
     * @param name the name to set
     */

    public void setName(String name){
        this.name = name;
    }

    /**
     * gets the division name
     * @return the name of the division
     */

    public String getName(){
        return name;
    }

    /**
     * sets the Division's counrty ID
     * @param countryId the Country ID to be set
     */

    public void setCountryId(int countryId){
        this.countryId = countryId;
    }

    /**
     * gets the division's country ID
     * @return the Country ID the division belongs to
     */

    public int getCountryId(){
        return countryId;
    }

    /**
     * Overrides the toString method to just give Division name
     * @return the division name
     */

    @Override
    public String toString(){
        return (name);
    }
}
