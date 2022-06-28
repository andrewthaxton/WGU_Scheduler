package Model;

/**
 * The User Class
 */

public class User {

    private int id;
    private String username;
    private String password;

    /**
     * User Constructor
     * @param id the id to set
     * @param username the username to set
     * @param password the password to set
     */

    public User(int id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * sets the user ID
     * @param id the id to set
     */

    public void setId(int id){
        this.id = id;
    }

    /**
     * gets the user ID
     * @return the User ID
     */

    public int getId(){
        return id;
    }

    /**
     * sets the username
     * @param username the username to set
     */

    public void setUsername(String username){
        this.username = username;
    }

    /**
     * gets the username
     * @return the User's username
     */

    public String getUsername(){
        return username;
    }

    /**
     * sets the password
     * @param password the password to set
     */

    public void setPassword(String password){
        this.password = password;
    }

    /**
     * gets the password
     * @return The User's password
     */

    public String getPassword(){
        return password;
    }
}
