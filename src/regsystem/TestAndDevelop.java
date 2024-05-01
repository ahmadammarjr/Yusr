package regsystem;

import java.io.File;
import javafx.scene.control.Alert;

import user.Course;
import user.Schedule;

public class TestAndDevelop
{
    public static void main(String[] args) throws Exception
    {
//        Schedule studentSchedule = new Schedule();
//        studentSchedule.printSchedule();
//        Course c1 = new Course("Data Structures", "CS210", 3, "Dr. Ahmad", "M W", 9, "mio", 10);
//        Course c2 = new Course("Chemistry", "ChM101", 4, "Dr. Ihab", "M W", 10, "hhhh", 1);
//        
//        studentSchedule.addCourseToSchedule(c1);
//        studentSchedule.printSchedule();
//        
//        studentSchedule.addCourseToSchedule(c2);
//        studentSchedule.printSchedule();
        
        String pn = "0502554688";
boolean matches = pn.matches("05\\d+");
System.out.println(matches); // This will output 'true'

    }
    
    
    private static boolean validPhoneNumber(String pn)
    {
        int N = pn.length();
        if(N<10 || N>10 || !pn.matches("05 \\d+"))
        {
            
            return false;
        }
        
        
        return true;
    }
    
    
    static void addStudent(String id) throws Exception
    {
        File newStudent = new File("Data/students/"+id+".txt");
        System.out.println(newStudent.getName());
    }
    
    static void removeStudent(String id)
    {
        File studentToRemove = new File("Data/students/"+id+".txt");
        studentToRemove.delete();
    }
    
    static String[] getAdmins() throws Exception
    {
        File admins = new File("Data/Admin");
        String adminArray[] = admins.list();
        
        if(adminArray== null)
            throw new Exception("No admins Found!");
        for (int i = 0; i < adminArray.length; i++)
        {
            String admin = adminArray[i];
            adminArray[i] = admin.substring(0, admin.indexOf("."));
        }
        
        return adminArray;
    }
}
