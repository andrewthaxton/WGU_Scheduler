package Model;

/**
 * Country Class
 */

public class Country {

    private int id;
    private String name;

    /**
     * Country constructor
     * @param id the Country ID
     * @param name the Country name
     */

    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * sets the country ID
     * @param id the id to set
     */

    public void setId(int id){
        this.id = id;
    }

    /**
     * gets the country ID
     * @return the id
     */

    public int getId(){
        return id;
    }

    /**
     * sets the country name
     * @param name the name to set
     */

    public void setName(String name){
        this.name = name;
    }

    /**
     * gets the country name
     * @return the Country name
     */

    public String getName(){
        return name;
    }

    /**
     * Overrides the toString method to return the Country name
     * @return the Country name
     */

    @Override
    public String toString(){
        return (name);
    }
}
