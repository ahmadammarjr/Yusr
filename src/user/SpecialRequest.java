package user;

import annotations.Yahya;
import java.util.Scanner;


@Yahya
public class SpecialRequest
{
    public final String SENDER;
    public final String COURSE;
    private enum Status
    {
        PENDING, DENIED, ACCEPTED
    }
    Status currentStatus;

    
    public SpecialRequest(String sender, String course)
    {
        this.currentStatus = Status.PENDING;
        this.SENDER = sender;
        this.COURSE = course;
    }
    
    //this constructor will be used by the loader
    public SpecialRequest(String data) throws Exception
    {
        Scanner dataReader = new Scanner(data);
        this.SENDER = dataReader.next();
        this.COURSE = dataReader.next();
        
        switch(dataReader.next().charAt(0))
        {
            case 'a': this.currentStatus = Status.ACCEPTED;
                break;
            case 'd': this.currentStatus = Status.DENIED;
                break;
            case 'p': this.currentStatus = Status.PENDING;
                break;
            default: throw new Exception("Invalid special request format in " + SENDER);
        }
    }

    public String getSENDER()
    {
        return SENDER;
    }

    public String getCOURSE()
    {
        return COURSE;
    }
    
    public void approve()
    {
        this.currentStatus = Status.ACCEPTED;
    }
    
    public void deny()
    {
        this.currentStatus = Status.DENIED;
    }

    public String getCurrentStatus()
    {
        return currentStatus.toString();
    }
    
    public char getCurrentStatusAsChar()
    {
        //getting the status format that will be used in the .txt files data system
        return currentStatus.toString().toLowerCase().charAt(0);
    }
}
