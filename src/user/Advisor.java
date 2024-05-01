package user;

import annotations.Alkahtani;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;



@Alkahtani
public class Advisor extends User implements Saveable
{
    private File advisorFile;
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<SpecialRequest> SpecialRequests = new ArrayList<>();
    
    //full constructer inculding the super class Variables
    public Advisor(String name, String id, String password) 
    {
        super(name, id, password);
        advisorFile = new File("Data/Advisor/"+ id+".txt");
        
        try
        {
            advisorFile.createNewFile();
        }
        catch(IOException e)
        {
            System.out.println("Couldn't create Advisor File " + id);
            System.out.println(e);
        }
    }
    
    public Advisor(String fileName)
    {
        advisorFile = new File("Data/Advisor/"+fileName);
        Scanner reader;
        try
        {
            reader = new Scanner(advisorFile);
            
            //removing the extension from the file name to get the id
            this.id = advisorFile.getName().replace(".txt", "");
            this.hashedPassword = Integer.parseInt(reader.nextLine());
            this.name = reader.nextLine();
            
            reader.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("loader constructor couldn't find advisor file "+id);
            System.out.println(e);
        }
        catch(NumberFormatException e)
        {
            System.out.println("Invalid password format in advisor file " +id);
            System.out.println(e);
        }
        catch(Exception e)
        {
            System.out.println("Advisor file might be corrupted " +id);
            System.out.println(e);
        }
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public void setId(String id)
    {
        this.id = id;
    }

    public void setHashedPassword(int hashedPassword)
    {
        this.hashedPassword = hashedPassword;
    }

    public File getAdvisorFile()
    {
        return advisorFile;
    }

    public ArrayList<Student> getStudents()
    {
        return students;
    }

    public ArrayList<SpecialRequest> getSpecialRequests()
    {
        return SpecialRequests;
    }

    
    public void addStudentToAdvisorList(Student student)
    {
        students.add(student);
        SpecialRequests.addAll(student.getPendingSpecialRequests());
    }
    
    public void removeStudentFromAdvisorList(Student student)
    {
        students.remove(student);
        SpecialRequests.removeAll(student.getPendingSpecialRequests());
    }
    
    @Override
    public String toString()
    {
        return hashedPassword +"\n" + name;
    }
    
    @Override
    public void save()
    {
        try
        {
            PrintWriter wrt = new PrintWriter(advisorFile);
            wrt.write(toString());
            wrt.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println(".save() couldn't find the advisor file "+id);
        }
    }

}