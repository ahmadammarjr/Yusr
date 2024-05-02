package user;

import annotations.Yahya;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


@Yahya
public class Course implements Saveable
{
    private String name;
    private String code;
    private String instructor;
    private String description = "";
    private  String courseDays;
    private  int courseHours;
    private  int MAX_ENROLLMENT;
    private int enrolledStudents = 0;
    private int credits;
    private File courseFile;

    ArrayList<String> prerequisites = new ArrayList<>();
 
    
    public Course(String name, String code, int credits, String instructor, String courseDays, int courseHours, String description, int MAX_ENROLLMENT) 
    {
        courseFile = new File("Data/Course/" + code.toUpperCase() + ".txt"); 
        this.name = name;
        this.code = code.toUpperCase();
        this.credits = credits;
        this.instructor = instructor;
        this.courseDays = courseDays;
        this.courseHours = courseHours;
        this.description = description;
        this.MAX_ENROLLMENT = MAX_ENROLLMENT;
         
        try
        {
            courseFile.createNewFile();
        }
        catch(IOException e)
        {
            System.out.println("Could not create a new file for course "+ code);
            System.out.println(e);
        }
    }
    
    //this constructor will be used by the loader
    public Course(String fileName)
    {
        courseFile = new File("Data/Course/"+ fileName);
        try
        {
            //removing the extension from the file name to get the course code
            this.code = courseFile.getName().replace(".txt", "");
            
            Scanner reader = new Scanner(courseFile);
            this.name = reader.nextLine();
            this.instructor = reader.nextLine();
            this.courseDays = reader.nextLine();
            this.courseHours = Integer.parseInt(reader.nextLine());
            this.MAX_ENROLLMENT = Integer.parseInt(reader.nextLine());
            this.enrolledStudents = Integer.parseInt(reader.nextLine());
            this.credits = Integer.parseInt(reader.nextLine());
            
            String descriptionLine = reader.nextLine();
            if(!descriptionLine.equals("-")) this.description = descriptionLine;
            
            String prerequisitesLine = reader.nextLine();
            if(!prerequisitesLine.equals("-"))
            {
                String[] prerequisitesArray = prerequisitesLine.split(",");
                for(String prerequisite : prerequisitesArray)
                    prerequisites.add(prerequisite);
            }
            reader.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("loader constructor could not find course file " + code);
            System.out.println(e);
        }
        catch(NumberFormatException e)
        {
            System.out.println("invalid format of course file " + code);
            System.out.println(e);
        }
        catch(Exception e)
        {
            System.out.println("Issue in course loader constructor " +code);
            System.out.println("File might be corrupted");
            System.out.println(e);
        }
    }
 
    public ArrayList<String> getPrerequisites() 
    {
        return prerequisites;
    }

    public String getName()
    {
        return name;
    }



    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getInstructor()
    {
        return instructor;
    }
    
    public int getEnrolledStudents()
    {
        return enrolledStudents;
    }

    public void setInstructor(String instructor)
    {
        this.instructor = instructor;
    }

    public String getDescription()
    {
        return description;
    }

    public String getCourseDays()
    {
        return courseDays;
    }

    public int getCourseHours()
    {
        return courseHours;
    }

    public int getMAX_ENROLLMENT() 
    {
        return MAX_ENROLLMENT;
    }
    
    public String getLectureTime()
    {
        return courseDays+"  "+courseHours +pmOrAm();
    }

    public File getCourseFile()
    {
        return courseFile;
    }

    public int getCredits()
    {
        return credits;
    }
    
    
    
    public String pmOrAm()
    {
        if (courseHours<8)
            return "pm";
        return "am";
    }

    public int getAvailableSeats()
    {
        return MAX_ENROLLMENT - enrolledStudents;
    }


    public void enrollStudent()
    {
        enrolledStudents++;
    }
    
    
    public void removeStudent()
    {
        enrolledStudents--;
    }

    public boolean isEnrollable()
    {
        return enrolledStudents < MAX_ENROLLMENT;
    }
    
    private String descriptionToString()
    {
        if(description.isEmpty()) return"-";
        return description;
    }
    
    private String prerequisitesToString()
    {
        if(prerequisites.isEmpty()) return"-";
        String prerequisitesString = "";
        for(String prerequisite : prerequisites )
            prerequisitesString+= prerequisite + ",";
        
        return prerequisitesString;
    }

    @Override
    public String toString()
    {
        String info =
                (
                    name
                    +"\n"+ instructor
                    +"\n"+ courseDays
                    +"\n"+ courseHours
                    +"\n"+ MAX_ENROLLMENT
                    +"\n"+ enrolledStudents
                    +"\n"+ credits
                    +"\n"+ descriptionToString()
                    +"\n"+ prerequisitesToString()
                );
        return info;
    }
    
    @Override
    public void save()
    {
        try
        {
            PrintWriter wrt = new PrintWriter(courseFile);
            wrt.write(toString());
            wrt.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Problem saving course: " + code);
            System.out.println(e);
        }
    }
 
}
