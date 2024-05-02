package user;

import annotations.Yahya;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


@Yahya
public class Admin extends User 
{ 
    public Admin(String fileName)
    {
        File adminFile = new File("Data/Admin/"+fileName);
        try
        {
            Scanner reader = new Scanner(adminFile);
            
            this.hashedPassword = Integer.parseInt(reader.nextLine());
            this.id = adminFile.getName().replace(".txt", "");
            this.name = reader.nextLine();
            
            reader.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("issue in admin loader constructor");
            System.out.println(e);
        }
        catch(NumberFormatException e)
        {
            System.out.println("invalid password format in admin file");
            System.out.println(e);
        }
        catch(Exception e)
        {
            System.out.println("issue in admin loader constructor");
            System.out.println(e);
        }
    }

    public int getHashedPassword()
    {
        return hashedPassword;
    }
         
}
