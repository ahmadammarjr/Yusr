package user;

import annotations.Alkahtani;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


@Alkahtani
public class Student extends User implements Saveable
{
    private double gpa;
    private String advisor;
    private String phoneNumber = "";
    private File studentFile;
    
    private ArrayList<String> courses = new ArrayList<>();
    private ArrayList<String> passedCourses = new ArrayList<>();
    private HashMap<String, SpecialRequest> specialRequests = new HashMap<>();
    private ArrayList<SpecialRequest> pendingSpecialRequests = new ArrayList<>();

    
    //full constructer inculding the super class Variables
    public Student(String name, String id, String password, double gpa, String advisor, ArrayList<String>passedCourses) 
    {
        super(name, id, password);
        studentFile = new File("Data/Student/" + id+".txt");
        this.gpa = gpa;
        this.advisor = advisor;
        this.passedCourses = passedCourses;
        
        try
        {
            studentFile.createNewFile();
        }
        catch(IOException e)
        {
            System.out.println("Could not create student new file\n" + id);
            System.out.println(e);
        }
    }
    
    //this constructor will be used by the loader to load students from .txt files
    public Student(String fileName)
    {
        studentFile = new File("Data/Student/" + fileName);
        try 
        {
            Scanner reader = new Scanner(studentFile);
            this.hashedPassword = Integer.parseInt(reader.nextLine());
            this.name = reader.nextLine();
            
            //removing the extension from the file name to get the id
            this.id = studentFile.getName().replace(".txt", "");
            this.gpa = Double.parseDouble(reader.nextLine());
            this.advisor = reader.nextLine();
            
            String passedCoursesLine = reader.nextLine();
            if(!passedCoursesLine.equals("-"))this.passedCourses = new ArrayList<> (Arrays.asList(passedCoursesLine.split(",")));
            
            String coursesLine = reader.nextLine();
            if(!coursesLine.equals("-"))this.courses = new ArrayList<> (Arrays.asList(coursesLine.split(",")));
            
            String phoneLine = reader.nextLine();
            if(!phoneLine.equals("-"))this.phoneNumber = phoneLine;
            
            String specialReqsLine = reader.nextLine();
            if(!specialReqsLine.equals("-"))
            {
                String specialReqsArray[] = specialReqsLine.split(",");
                
                for(String specialReqData : specialReqsArray)
                {
                    SpecialRequest spReq = new SpecialRequest(specialReqData);
                    specialRequests.put(spReq.getCOURSE(), spReq);
                    if(spReq.getCurrentStatusAsChar() == 'p')
                    {
                        pendingSpecialRequests.add(spReq);
                    }
                }
            }
            
            reader.close();
        } 
        
        catch (FileNotFoundException e)
        {
            System.out.println("issue in Student loader constructor");
            System.out.println(e);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid password format in student file " + fileName);
        }
        catch (Exception e)
        {
            System.out.println("Issue in student loader constructor\n" + id);
            System.out.println(e);
        }
    }

    public File getStudentFile()
    {
        return studentFile;
    }
    

    public void setName(String name)
    {
        this.name = name;
    }


    public void setId(String id)
    {
        this.id = id;
    }

    public int getHashedPassword()
    {
        return hashedPassword;
    }

    public void setHashedPassword(int hashedPassword)
    {
        this.hashedPassword = hashedPassword;
    }

    public double getGpa()
    {
        return gpa;
    }

    public void setGpa(double gpa)
    {
        this.gpa = gpa;
    }

    public String getAdvisor()
    {
        return advisor;
    }

    public void setAdvisor(String advisor) 
    {
        this.advisor = advisor;
    }

    public String getPhoneNumber()
    {
        if(phoneNumber.isEmpty()) return"-";
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail()
    {
        return id+"@mio.com";
    }

    public ArrayList<String> getCourses()
    {
        return courses;
    }

    public void setCourses(ArrayList<String> courses)
    {
        this.courses = courses;
    }

    public ArrayList<String> getPassedCourses() 
    {
        return passedCourses;
    }

    public void setPassedCourses(ArrayList<String> passedCourses)
    {
        this.passedCourses = passedCourses;
    }

    public HashMap<String, SpecialRequest> getSpecialRequests()
    {
        return specialRequests;
    }

    public ArrayList<SpecialRequest> getPendingSpecialRequests()
    {
        return pendingSpecialRequests;
    }
    
    public void addNewSpecialRequest(SpecialRequest newSpReq)
    {
        //this map works as a history of all special requests
        //regardless of their status
        specialRequests.put(newSpReq.getCOURSE(), newSpReq);
        
        //while this array only stores current pending requests
        pendingSpecialRequests.add(newSpReq);
    }
    
    
    private String passedCoursesToString()
    {
        if(passedCourses.isEmpty()) return"-";
        String coursesString = "";
        for(String course: passedCourses)
        {
            coursesString+= course+",";
        }
        return coursesString;
    }
    
    private String coursesToString()
    {
        if(courses.isEmpty()) return"-";
        String coursesString = "";
        for(String course: courses)
        {
            coursesString+= course+",";
        }
        return coursesString;
    }
    
    private String specialReqsToString()
    {
        if(specialRequests.isEmpty()) return "-";
        String specialReqsString = "";
        for (SpecialRequest sr : specialRequests.values())
        {
            specialReqsString+= sr.getSENDER()+" " +sr.getCOURSE()+" " +sr.getCurrentStatusAsChar()+",";
        }
        return specialReqsString;
    }

    @Override
    public String toString()
    {
        String info =
                (
                    hashedPassword
                    +"\n"+ name
                    +"\n"+ gpa
                    +"\n"+ advisor
                    +"\n"+ passedCoursesToString()
                    +"\n"+ coursesToString()
                    +"\n"+ getPhoneNumber()
                    +"\n"+ specialReqsToString()
                );
        
        return(info);
    }
    
    
    @Override
    public void save()
    {
        try
        {
            PrintWriter wrt = new PrintWriter(studentFile);
            wrt.write(toString());
            wrt.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Problem saving student: " + id);
            System.out.println(e);
        }
    }
    
    
    
}