package user;

import java.util.Arrays;


public class ScheduleHour
{
    final int SUNDAY = 1;
    final int MONDAY = 2;
    final int TUESDAY = 3;
    final int WEDNESDAY = 4;
    final int THURSDAY = 5;
    String[] hourRow = {"", "", "", "", "", ""};
    
    public ScheduleHour(String hour)
    {
        hourRow[0] = hour;
    }
    
    public void addCourse(String courseDays, String courseName) throws Exception
    {
        String[] days = courseDays.split(" ");
        for(String day: days)
        {
            switch(day)
            {
                case "S": hourRow[SUNDAY] = courseName;
                    break;
                case "M": hourRow[2] = courseName;
                    break;
                case "T": hourRow[3] = courseName;
                    break;
                case "W": hourRow[4] = courseName;
                    break;
                case "Th": hourRow[5] = courseName;
                    break;
                default: throw new Exception("Invalid Days format");
            }
        }
    }
    
    public void removeCourse(String courseDays, String courseName)
    {
        String[] days = courseDays.split(" ");
        for(String day: days)
        {
            switch(day)
            {
                case "S": if(hourRow[SUNDAY].equals(courseName)) hourRow[SUNDAY] = "";
                    break;
                case "M": if(hourRow[MONDAY].equals(courseName)) hourRow[MONDAY] = "";
                    break;
                case "T": if(hourRow[TUESDAY].equals(courseName)) hourRow[TUESDAY] = "";
                    break;
                case "W": if(hourRow[WEDNESDAY].equals(courseName)) hourRow[WEDNESDAY] = "";
                    break;
                case "Th": if(hourRow[THURSDAY].equals(courseName)) hourRow[THURSDAY] = "";
                    break;
            }
        }
    }
    
    public boolean canAdd(String courseDays)
    {
        String[] days = courseDays.split(" ");
        for(String day: days)
        {
            switch(day)
            {
                case "S": if(!hourRow[SUNDAY].isEmpty()) return false;
                    break;
                case "M": if(!hourRow[MONDAY].isEmpty()) return false;
                    break;
                case "T": if(!hourRow[TUESDAY].isEmpty()) return false;
                    break;
                case "W": if(!hourRow[WEDNESDAY].isEmpty()) return false;
                    break;
                case "Th": if(!hourRow[THURSDAY].isEmpty()) return false;
                    break;
            }
        }
        return true;
    }
    
    public String getHour()
    {
        return hourRow[0];
    }
    
    public String getSunday()
    {
        return hourRow[SUNDAY];
    }
    
    public String getMonday()
    {
        return hourRow[MONDAY];
    }
    
    public String getTuesday()
    {
        return hourRow[TUESDAY];
    }
    
    public String getWednesday()
    {
        return hourRow[WEDNESDAY];
    }
    
    public String getThursday()
    {
        return hourRow[THURSDAY];
    }
    
    @Override
    public String toString()
    {
        return Arrays.toString(hourRow);
    }
}
