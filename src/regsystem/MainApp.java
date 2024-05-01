package regsystem;

import annotations.Ammar;
import gui.AdminInterfaceController;
import gui.AdvisorInterfaceController;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import gui.LoginController;
import gui.StudentInterfaceController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import user.Admin;
import user.Advisor;
import user.Course;
import user.Student;
import user.User;
import user.Saveable;
import user.SpecialRequest;



public class MainApp extends Application
{
    
    public HashMap<String, Admin> adminMap = new HashMap<>();
    public HashMap<String, Advisor> advisorMap = new HashMap<>();
    public HashMap<String, Student> studentMap = new HashMap<>();
    public HashMap<String, Course> courseMap = new HashMap<>();
    private Stage loginstage;
    private Stage userStage;
    private User activeUser;
    
    public static void main(String[] args) 
    {
        if(isRunning()) System.exit(0); //preventing opening multiple windows of the system
        launch(args);  
    }
    
    @Ammar
    private static boolean isRunning()
    {
        /*here we're basically checking if the system is already running
        1- when the system runs, the start() method creates a file named RUNNINGFLAG
        2- when the system closes the file is deleted as you can see in login()
        */
        File runningFlag = new File("Data/RUNNINGFLAG.txt");
        return runningFlag.exists();
    }
    
    @Override
    public void start(Stage stage) throws IOException
    {
        new File("Data/RUNNINGFLAG.txt").createNewFile();
        
        load();
        loginstage = stage;
        userStage = new Stage();
//        login();
//        fastLogin("Admin", "aammar", "sama11sama");
//        fastLogin("Student", "222110915", "sounna");
        fastLogin("Advisor", "rramy", "1234");
    }
    
    
    private void load()
    {
        //loading courses from the .txt files data system
        File dir = new File("Data/Course");
        String[] filesArray = dir.list();
        for(String courseFile: filesArray)
        {
            Course courseToLoad = new Course(courseFile);
            courseMap.put(courseToLoad.getCode(), courseToLoad);
        }
        
        //loading advisors
        dir = new File("Data/Advisor");
        filesArray = dir.list();
        for(String advisorFile : filesArray)
        {
            Advisor advisorToLoad = new Advisor(advisorFile);
            advisorMap.put(advisorToLoad.getId(), advisorToLoad);
        }
        
        //loading students 
        dir = new File("Data/Student");
        filesArray = dir.list();
        for(String studentFile: filesArray)
        {
            Student studentToLoad = new Student(studentFile);
            studentMap.put(studentToLoad.getId(), studentToLoad);
            
            /*
            connecting each student to his advisor
            we're doing this becauase special request are stored in students
            files only to make changing advisors easier*/
            advisorMap.get(studentToLoad.getAdvisor()).addStudentToAdvisorList(studentToLoad); 
        }
                
        //loading admins
        dir = new File("Data/Admin");
        filesArray = dir.list();
        for(String adminFile : filesArray)
        {
            Admin adminToLoad = new Admin(adminFile);
            adminMap.put(adminToLoad.getId(), adminToLoad);
        }
    }

    
    @Ammar
    private void login()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(new File("src/gui/fxml/LoginInterface.fxml").toURI().toURL());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            //we're getting the controller of the login interface to
            //pass this instance of MainApp to the controller
            //basically we're connecting the controller to the MainApp
            LoginController loginController = loader.getController();
            loginController.setMainApp(this);

            Image icon = new Image("file:src/gui/icon.png");

            loginstage.setScene(scene);
            loginstage.setResizable(false);
            loginstage.setTitle("Log in");
            loginstage.getIcons().add(icon);

            loginstage.show();
            loginstage.setOnCloseRequest( (e) -> new File("Data/RUNNINGFLAG.txt").delete());
        }
        
        catch(FileNotFoundException e)
        {
            System.out.println("Loging Interface was not Found");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }   
    }

    public void setActiveUser(User activeUser)
    {
        this.activeUser = activeUser;
    }
    
    
    
    @Ammar
    public void showUserInterface(String userType)
    {
         try
        {
            FXMLLoader loader = new FXMLLoader(new File("src/gui/fxml/"+ userType +"Interface.fxml").toURI().toURL());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            
            switch (userType)
            {
                case "Admin":
                    //connecting MainApp to the AdminInterfaceController
                    //and passing the Active user to the controller
                    AdminInterfaceController adminInterfaceController = loader.getController();
                    adminInterfaceController.setMainApp(this);
                    adminInterfaceController.setActiveAdmin(activeUser);
                    break;
                case "Student":
                    StudentInterfaceController studentInterfaceController = loader.getController();
                    studentInterfaceController.setMainApp(this);
                    studentInterfaceController.setActiveStudent(activeUser);
                    break;
                case "Advisor":
                    AdvisorInterfaceController advisorInterfaceController = loader.getController();
                    advisorInterfaceController.setMainApp(this);
                    advisorInterfaceController.setActiveAdvisor(activeUser);
                    break;
            }
            
            userStage.setScene(scene);
            userStage.setResizable(false);
            userStage.show();
            
            //returning to the loging interface when the user closes
            //the program
            userStage.setOnCloseRequest( (event) ->
            {
                    event.consume(); //disabling the default closing behavoir
                    logout(userStage);
            });
        }
        catch(Exception e)
        {
            System.out.println("issue in showAdminInterface()\n");
            e.printStackTrace();
            new File("Data/RUNNINGFLAG.txt").delete();
        }
    }
    
    
    //this method saves the changes to the .txt files
    public void saveAll()
    {
        ArrayList<Saveable> savableList = new ArrayList<>();
        savableList.addAll(studentMap.values());
        savableList.addAll(courseMap.values());
        savableList.addAll(advisorMap.values());

        
        for(Saveable saveable: savableList)
        {
            saveable.save();
        }
    }
    
    
    
    //<<logging out>>
    
    //confirming logging out when the user try to close the program
    //if the user confirms, the program is closed and the user is redirected
    //to the login interface
    @Ammar
    public void logout(Stage stage)
    {
        Alert logoutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        logoutAlert.setTitle("Loggin out");
        logoutAlert.setHeaderText("you might have unsaved changes");
        logoutAlert.setContentText("Do you want to save before logging out?");
        logoutAlert.getButtonTypes().add(ButtonType.YES);
        logoutAlert.getButtonTypes().add(ButtonType.NO);
        logoutAlert.getButtonTypes().remove(ButtonType.OK);

        
        ButtonType userResponse = logoutAlert.showAndWait().get();

        if(userResponse == ButtonType.YES)
        {
            saveAll();
            stage.close();
            login();
        }
        
        if (userResponse == ButtonType.NO)
        {
            stage.close();
            login(); //redirecting to the login interface
        }
        
    }
    
    
    //<<Admins action methods>>
    
    @Ammar
    public void addStudent(String name, String id, String password, double gpa, String advisor, ArrayList<String> passedCourses)
    {
        Student studentToAdd = new Student(name, id, password, gpa, advisor, passedCourses);
        studentMap.put(studentToAdd.getId(), studentToAdd);
        
        //saving the student to avoid getting empty files -> problems when trying to load
        studentToAdd.save();
    }
    
    
    @Ammar
    public void removeStudent(Student s)
    {
        s.getStudentFile().delete();
        studentMap.remove(s.getId());
        
        //need to delete the student from the advisor list as well
        advisorMap.get(s.getAdvisor()).removeStudentFromAdvisorList(s);
    }
    
    public void changeStudentAdvisor(Student student, String nAdvisor)
    {
        Advisor oldAdvisor = advisorMap.get((student.getAdvisor()));
        Advisor newAdvisor = advisorMap.get(nAdvisor);
        
        //deleting the student from the old advisor list
        //and adding it to the new advisor list
        //*special requests are resent to the new advisor automatically
        oldAdvisor.removeStudentFromAdvisorList(student);
        newAdvisor.addStudentToAdvisorList(student);
        
        //resetting the student advisor
        student.setAdvisor(newAdvisor.getId());
    }
    
    
    @Ammar
    public void addAdvisor(String name, String id, String password)
    {
        Advisor advisorToAdd = new Advisor(name, id, password);
        advisorMap.put(advisorToAdd.getId(), advisorToAdd);
        advisorToAdd.save();
    }
    
    @Ammar
    public void removeAdvisor(Advisor a)
    {
        advisorMap.remove(a.getId());
        a.getAdvisorFile().delete();
        //Admins cannot delete advisors who currently have students under their guidance
    }
    
    @Ammar
    public void addCourse(String name, String code, int credits, String instructor, String courseDays, int courseHours, String description, int availableSeats, List<String> prerequisites)
    {
        Course courseToAdd = new Course(name, code, credits, instructor, courseDays, courseHours, description, availableSeats);
        courseMap.put(courseToAdd.getCode(), courseToAdd);
        courseToAdd.getPrerequisites().addAll(prerequisites);
        
        //avoiding getting an empty file if the admin doesn't save when logging out
        courseToAdd.save();
    }
    
    @Ammar
    public void removeCourse(Course course)
    {
        courseMap.remove(course.getCode());
        course.getCourseFile().delete();  
    }
    

    
    //Student Actions methods
    //<<<<<<<<<<>>>>>>>>>>>>
    public void register(Student student, Course course)
    {
        //adding the course code to the list of the student's courses
        student.getCourses().add(course.getCode());
        
        course.enrollStudent();
    }
    
    public void drop(Student student, Course course)
    {
        student.getCourses().remove(course.getCode());
        course.removeStudent();
    }
    
    
    //a recursive method to get all prerequisites and prerequisites of prerequisites...etc for a given course
    //the output is formated as a nested structure
    public String getPrerequisites(String courseCode, int depth)
    {
        Course course = courseMap.get(courseCode);
        if(course == null || course.getPrerequisites().isEmpty())
        {
            return "";
        }
        
        //adjusting the indentation based on the depth of recursion
        String indentation = "";
        for(int i = 0; i < depth; i++)
        {
            indentation+= "\t";
        }
        
        String result = "";
        for(String Prerequisite: course.getPrerequisites())
        {
            result+= indentation + Prerequisite
                     + "\n" + getPrerequisites(Prerequisite, depth+1);
        }
        
        return result;
    }
    
    //overloading the method for improved readability
    //we're not required to provide the depth the first time we call the method
    public String getPrerequisites(String courseCode)
    {
        return getPrerequisites(courseCode, 0);
    }
    
    
    public void sendNewSpecialRequest(Student student, String course)
    {
        Advisor advisor = advisorMap.get(student.getAdvisor());
        
        SpecialRequest newSpReq = new SpecialRequest(student.getId(), course);
        
        //adding the request to the student list
        student.addNewSpecialRequest(newSpReq);

        //adding the request to the advisor list
        advisor.getSpecialRequests().add(newSpReq);
    }
    
    
    
    
    //Advisors Actions methods
    //<<<<<<<<<<>>>>>>>>>>>>
    public void approveSpecialRequest(SpecialRequest r)
    {
        //checking and validating is pre-done in the advisor controller class
        Student s = studentMap.get(r.SENDER);
        Advisor a = (Advisor)activeUser;
        String courseCode = r.COURSE;
        
        //changing the status of the request to approved and enrolling the student in the course
        r.approve();
        s.getCourses().add(courseCode);
        courseMap.get(courseCode).enrollStudent();
        
        //removing the request from the student and advisor lists after approving
        s.getPendingSpecialRequests().remove(r);
        a.getSpecialRequests().remove(r);
    }
    
    
    public void denySpecialRequest(SpecialRequest r)
    {
        Student s = studentMap.get(r.SENDER);
        Advisor a = (Advisor)activeUser;
        String courseCode = r.COURSE;
        
        //changing the status of the request to denied
        r.deny();
        
        //removing the request from the student and advisor lists after denying
        s.getPendingSpecialRequests().remove(r);
        a.getSpecialRequests().remove(r);
    }
   
    
    
    
    
//<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>    
//for testing, should be deleted later
    
    @Ammar
    public void fastLogin(String t, String u, String p)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(new File("src/gui/fxml/LoginInterface.fxml").toURI().toURL());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            LoginController loginController = loader.getController();
            loginController.setMainApp(this);
            loginController.choiceBox.setValue(t);
            loginController.passwordTxtField.setText(p);
            loginController.usernameTxtField.setText(u);

            loginstage.setScene(scene);
            loginstage.show();
            loginstage.setOnCloseRequest( (e) -> new File("Data/RUNNINGFLAG.txt").delete());
        }
        
        catch(FileNotFoundException e)
        {
            System.out.println("Loging Interface was not Found");
            System.out.println(e);
        }
        catch(IOException e)
        {
            System.out.println(e);
        } 
    }
}
