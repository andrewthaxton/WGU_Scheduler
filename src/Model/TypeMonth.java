package Model;

import java.time.Month;

/**
 * Type/Month Class
 */

public class TypeMonth {

    private Month month;
    private String type;
    private int numTypeMonth;

    /**
     * Constructor for type/month
     * @param month
     * @param type
     * @param numTypeMonth
     */

    public TypeMonth(Month month, String type, int numTypeMonth){
        this.month = month;
        this.type = type;
        this.numTypeMonth = numTypeMonth;
    }

    /**
     * gets the Month
     * @return the month
     */

    public Month getMonth(){
        return month;
    }

    /**
     * sets the Month
     * @param month to be set
     */

    public void setMonth(Month month){
        this.month = month;
    }

    /**
     * gets the type
     * @return the type
     */

    public String getType(){
        return type;
    }

    /**
     * sets the type
     * @param type to be set
     */

    public void setType(String type){
        this.type = type;
    }

    /**
     * gets the number of type/months
     * @return the number of type/months
     */

    public int getNumTypeMonth(){
        return numTypeMonth;
    }

    /**
     * sets the number of type/months
     * @param nextNum to be set
     */

    public void setNumTypeMonth(int nextNum){

        numTypeMonth = nextNum;

    }

}
