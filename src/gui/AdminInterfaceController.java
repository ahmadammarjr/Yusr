package gui;

import annotations.Ammar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import yusr.MainApp;
import user.Admin;
import user.Advisor;
import user.Course;
import user.Student;
import user.User;


@Ammar
public class AdminInterfaceController
{
    //home page elements
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label welcomeMessage;
    
    
    //manage student page elements
    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private TableColumn<Student, String> idStudentCol;
    @FXML
    private TableColumn<Student, String> nameStudentCol;
    @FXML
    private TableColumn<Student, String> gpaCol;
    @FXML
    private TableColumn<Student, String> studentAdvisorCol;
    @FXML
    private Button addStudentButton;
    @FXML
    private Button newStudentSaveButton;
    @FXML
    private Button manageStudentButton;
    @FXML
    private Button removeStudentButton;
    @FXML
    private Button updateStudentButton;
    @FXML
    private TextField resetPasswordField;
    @FXML
    private TextField newStudentName;
    @FXML
    private TextField newStudentID;
    @FXML
    private ChoiceBox<String> newStudentAdvisor;
    @FXML
    private TextField gpaTxtField;
    @FXML
    private TextField newStudentPassword;
    @FXML
    private ChoiceBox<String> changeAdvisorChoiceBox;
    @FXML
    private Label errorMessage;
    @FXML
    private ListView<String> newStudentPassedList;
    @FXML
    private Label passedCoursesLabel;
    
    
    //mange advisors page elements
    @FXML
    private TableView<Advisor> advisorsTable;
    @FXML
    private TableColumn<Advisor, String> nameAdvisorCol;
    @FXML
    private TableColumn<Advisor, String> iDadvisorCol;
    @FXML
    private Button addAdvisorButton;
    @FXML
    private TextField newAdvisorName;
    @FXML
    private TextField newAdvisorId;
    @FXML
    private TextField newAdvisorPassword;
    @FXML
    private Button saveNewAdvisorButton;
    @FXML
    private Button manageAdvisorButton;
    @FXML
    private TextField resetAdvisorPasswordField;
    @FXML
    private Button updateAdvisorButton;
    @FXML
    private Button removeAdvisorButton;
    @FXML
    private Label advisorErrorMessage;
    
    
    //manage courses page elements
    @FXML
    private TableView<Course> coursesTable;
    @FXML
    private TableColumn<Course, String> courseCodeCol;
    @FXML
    private TableColumn<Course, String> courseNameCol;
    @FXML
    private TableColumn<Course, String> courseInsCol;
    @FXML
    private TableColumn<Course, String> lectureTimeCol;
    @FXML
    private TableColumn<Course, Integer> enrolledStudentsCol;
    @FXML
    private TableColumn<Course, Integer> maxEnrlCol;
    @FXML
    private TableColumn<Course, Integer> creditsCol;
    @FXML
    private Button addCourseButton;
    @FXML
    private TextField newCourseCode;
    @FXML
    private TextField newCourseName;
    @FXML
    private TextField newCourseIns;
    @FXML
    private ChoiceBox<String> newCourseDays;
    @FXML
    private ChoiceBox<String> newCourseHours;
    @FXML
    private TextField newCourseMaxEnrl;
    @FXML
    private Button saveNewCourseButton;
    @FXML
    private Label coursesErrorMessage;
    @FXML
    private TextArea newCourseDescription;
    @FXML
    private ChoiceBox<String> newCourseCredits;
    @FXML
    private ListView<String> newCoursePrerequisites;
    
    
    //system statistics page elements
    @FXML
    private Label numOfUsers;
    @FXML
    private Label numOfCources;
    @FXML
    private Label avgClassSize;
    @FXML
    private Label numOfStudents;
    @FXML
    private Label numOfAdmins;
    @FXML
    private Label numOfAdvisors;
    
    //
    @FXML
    private TabPane tabPane;
    private MainApp mainApp;
    private Admin activeAdmin;

    
    
    public void setActiveAdmin(User activeUser)
    {
        this.activeAdmin = (Admin)activeUser;
        setupHomePage(); 
    }
    
    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;  
    }
    
    
    @FXML
    public void updateSelectedTab(Event event)
    {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab!= null)
            switch(selectedTab.getId())
            {
                case "homePageTab":
                    if(activeAdmin!=null) //to skip the function in the first time it opens
                        setupHomePage();
                    break;
                case "manageStudentsTab":
                    setupManageStudentPage();
                    //clear the table from any selections
                    studentsTable.getSelectionModel().clearSelection();
                    break;
                case "manageAdvisorsTab":
                    setupManageAdvisorPage();
                    advisorsTable.getSelectionModel().clearSelection();
                    break;
                case "manageCoursesTab":
                    setupManageCoursePage();
                    coursesTable.getSelectionModel().clearSelection();
                    break;
                case "systemStatTab": 
                    setupSystemStatPage();
            }
    }
    
    private void setupHomePage()
    {
        nameLabel.setText("Name: " + activeAdmin.getName());
        welcomeMessage.setText("Welcmome back Admin!");
        emailLabel.setText("eMail: "+activeAdmin.getId()+"@mio.com");

    }
    
    
  
    private void setupSystemStatPage()
    {
        String totalUsers = ""+(mainApp.adminMap.size() + mainApp.advisorMap.size() + mainApp.studentMap.size());
        numOfUsers.setText(totalUsers);
        
        String totalStudents = ""+ mainApp.studentMap.size();
        numOfStudents.setText(totalStudents);
        
        String totalAdvisors = ""+mainApp.advisorMap.size();
        numOfAdvisors.setText(totalAdvisors);
        
        String totalAdmins = ""+mainApp.adminMap.size();
        numOfAdmins.setText(totalAdmins);
        
        String totalCourses = ""+mainApp.courseMap.size();
        numOfCources.setText(totalCourses);
    }
    
    
    //<<manage student tab>>
    //<<<<<<<<>>>>>>>>>>>
     private void setupManageStudentPage()
    {
        //hiding elements not needed by default
        defaultViewMSP();
        
        //setting up the students table
        ObservableList<Student> stdList = FXCollections.observableArrayList(mainApp.studentMap.values());
        idStudentCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        gpaCol.setCellValueFactory(new PropertyValueFactory<>("gpa"));
        studentAdvisorCol.setCellValueFactory(new PropertyValueFactory<>("advisor"));
        nameStudentCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentsTable.setItems(stdList);
        
        //setting up the new student passed courses list
        newStudentPassedList.getItems().setAll(mainApp.courseMap.keySet());
        newStudentPassedList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); 
    }
    
    @FXML
    public void showAddStudent(ActionEvent event)
    {
        setupManageStudentPage();
        addStudentButton.setVisible(false);
        
        newStudentPassword.setVisible(true);
        gpaTxtField.setVisible(true);
        newStudentID.setVisible(true);
        newStudentName.setVisible(true);
        newStudentSaveButton.setVisible(true);
        newStudentPassedList.setVisible(true);
        passedCoursesLabel.setVisible(true);
        
        //setting up the Advisor choice box
        newStudentAdvisor.setVisible(true);
        newStudentAdvisor.getItems().addAll(mainApp.advisorMap.keySet());
        newStudentAdvisor.getItems().add("Advisor*");
        newStudentAdvisor.setValue("Advisor*");
    }
    
    @FXML
    public void saveNewStudent(ActionEvent event)
    {
        if(!validNewStudent())
            return;
        String sName = newStudentName.getText();
        String sId = newStudentID.getText();   
        String sAdvisor = newStudentAdvisor.getValue();
        String sPassword = newStudentPassword.getText();
        double gpa = (gpaTxtField.getText().equals("")) ? 0 : Double.parseDouble(gpaTxtField.getText());
        
        //getting the passed courses the admin selects
        List selectedCourses = newStudentPassedList.getSelectionModel().getSelectedItems();
        ArrayList<String> passedCourses = new ArrayList<>(selectedCourses);
        
        //adding the student to the studentMap in MainApp
        mainApp.addStudent(sName, sId, sPassword, gpa, sAdvisor, passedCourses);
            
        //refreshing the page
        setupManageStudentPage();
    }
    
    @FXML
    public void saveUpdateStudent(ActionEvent event)
    {
        //early return if the admin haven't written anything in the update fields
        if(changeAdvisorChoiceBox.getValue().equals("Change Advisor") && resetPasswordField.getText().equals(""))
            return;
        Student studentToUpdate = studentsTable.getSelectionModel().getSelectedItem();

        if(!changeAdvisorChoiceBox.getValue().equals("Change Advisor"))
        {
            //changing stdent's advisor
            String newAdvisor = changeAdvisorChoiceBox.getValue();
            mainApp.changeStudentAdvisor(studentToUpdate, newAdvisor);
        }
        if (!resetPasswordField.getText().equals(""))
        {
            //resetting student's password
            studentToUpdate.setHashedPassword(resetPasswordField.getText().hashCode());
        }
        
        //informing the admin that the update was done
        studentsTable.refresh();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successful Update");
        alert.showAndWait();
        setupManageStudentPage(); 
    }
    
    @FXML
    public void showManageStudent(ActionEvent event)
    {
        if (studentsTable.getSelectionModel().getSelectedItem() == null)
        {
            errorMessage.setText("No Student is selected!");
            return;
        }
        
        setupManageStudentPage();
        manageStudentButton.setVisible(false);
        resetPasswordField.setVisible(true);
        updateStudentButton.setVisible(true);
        removeStudentButton.setVisible(true);
        changeAdvisorChoiceBox.setVisible(true);
        
        //setting up the change advisor choice box options
        changeAdvisorChoiceBox.getItems().setAll(mainApp.advisorMap.keySet());
        changeAdvisorChoiceBox.getItems().add("Change Advisor");
        changeAdvisorChoiceBox.setValue("Change Advisor");
    }
    
    @FXML
    void removeStudent(ActionEvent event)
    {
        //asking the admin to confirm the removal of the student
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This action will cause the Student data to be permanently deleted ");
        if (alert.showAndWait().get() == ButtonType.OK)
        {
            mainApp.removeStudent(studentsTable.getSelectionModel().getSelectedItem());
            setupManageStudentPage();
        }       
    }
    
    
    
    //<<mange Advisors tab>>
    //<<<<<<<<>>>>>>>>>>>
    private void setupManageAdvisorPage()
    {
        //hiding elements not needed by default
        defaultViewMAP();
        
        //setting up the students table
        ObservableList<Advisor> advList = FXCollections.observableArrayList(mainApp.advisorMap.values());
        nameAdvisorCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        iDadvisorCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        advisorsTable.setItems(advList);
    }
    
    @FXML
    void showManageAdvisor(ActionEvent event)
    {
        //early return if the admin hasn't selected an advisor to manage
        if(advisorsTable.getSelectionModel().getSelectedItem() == null)
        {
            advisorErrorMessage.setText("No User is selected!");
            return;
        }
        
        setupManageAdvisorPage();
        //disabling auto focus on random elements!!
        resetAdvisorPasswordField.getParent().requestFocus();
        manageAdvisorButton.setVisible(false);
        
        resetAdvisorPasswordField.setVisible(true);
        updateAdvisorButton.setVisible(true);
        removeAdvisorButton.setVisible(true);
    }
    
    @FXML
    void showAddAdvisor(ActionEvent event)
    {
        setupManageAdvisorPage();
        //disabling focus on random elements
        newAdvisorName.getParent().requestFocus();
        
        newAdvisorName.setVisible(true);
        newAdvisorId.setVisible(true);
        newAdvisorPassword.setVisible(true);
        saveNewAdvisorButton.setVisible(true);
        addAdvisorButton.setVisible(false);   
    }
    
    @FXML
    void saveNewAdvisor(ActionEvent event)
    {
        if(newAdvisorName.getText().equals("") || newAdvisorId.getText().equals("")
                || newAdvisorPassword.getText().equals(""))
        {
            advisorErrorMessage.setText("* are required fields");
            return;
        }
        
        if(!newAdvisorName.getText().matches("^[a-zA-Z .]+$"))
        {
            advisorErrorMessage.setText("Invalid name!");
            return;
        }
        
        if(newAdvisorId.getText().matches(".*\\s+.*"))
        {
            advisorErrorMessage.setText("Advisor ID can not contain spaces!");
            return;
        }
        
        String aName = newAdvisorName.getText();
        String aId = newAdvisorId.getText();
        String aPassword = newAdvisorPassword.getText();
        
        if (mainApp.advisorMap.keySet().contains(aId))
        {
            advisorErrorMessage.setText("ID is already used!!");
            return;
        }
        
        mainApp.addAdvisor(aName, aId, aPassword);
        setupManageAdvisorPage(); //refresh the page
    }
    
    @FXML
    void resetAdvisorPassword(ActionEvent event)
    {
        if(resetAdvisorPasswordField.getText()== null) return;
        Advisor advisorToUpdate = advisorsTable.getSelectionModel().getSelectedItem();
        int newHashedPassword = resetAdvisorPasswordField.getText().hashCode();
        
        advisorToUpdate.setHashedPassword(newHashedPassword);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Successful restet");
        alert.setTitle("");
        alert.showAndWait();
        
        setupManageAdvisorPage();//refresh the page
    }
    
    @FXML
    void removeAdvisor(ActionEvent event)
    {
        //this button is not shown unless the admin selects an advisor to manage
        //so there's no chance we're getting null
        Advisor advisorToRemove = advisorsTable.getSelectionModel().getSelectedItem();
        
        //checking if the advisor has students under his guidance
        //if so the admin would have to assign the students to another advisor
        //before deleting the advisor
        if(!advisorToRemove.getStudents().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "This Advisor has Students under his guidance\nPlease Assign Students to another Advisor first.");
            alert.setHeaderText("Advisor can't be deleted!");
            alert.setTitle("");
            alert.show();
            return;
        }
        //we need to check if the advisor has students under his guidance first
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This action will cause the Admin data to be permanently deleted ");
        if (alert.showAndWait().get() == ButtonType.OK)
        {
            mainApp.removeAdvisor(advisorToRemove);
            setupManageAdvisorPage();
        } 
    }
    
    
    
    //<<manage courses tab>>
    //<<<<<<<<>>>>>>>>>>>
    private void setupManageCoursePage()
    {
        defaultViewMCP();
        
        //setting up the course table
        ObservableList<Course> corList = FXCollections.observableArrayList(mainApp.courseMap.values());
        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseInsCol.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        maxEnrlCol.setCellValueFactory(new PropertyValueFactory<>("MAX_ENROLLMENT"));
        enrolledStudentsCol.setCellValueFactory(new PropertyValueFactory<>("enrolledStudents"));
        lectureTimeCol.setCellValueFactory(new PropertyValueFactory<>("lectureTime"));
        creditsCol.setCellValueFactory(new PropertyValueFactory<>("credits"));
        
        coursesTable.setItems(corList);
        
        //setting up the choice boxes 
        final String[] days = {"Course Days*", "S T", "M W", "S T Th", "S M T W"};
        final String[] hours = {"Course Hours*", "8am", "9am", "10am", "11am", "1pm", "2pm"};
        final String[] credits = {"Credits*","2", "3", "4"};
        
        newCourseDays.getItems().setAll(days);
        newCourseDays.setValue("Course Days*");
        newCourseHours.getItems().setAll(hours);
        newCourseHours.setValue("Course Hours*");
        newCourseCredits.getItems().setAll(credits);
        newCourseCredits.setValue("Credits*");
        
        newCoursePrerequisites.getItems().setAll(mainApp.courseMap.keySet());
        newCoursePrerequisites.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    
    @FXML
    void removeCourse(ActionEvent event)
    {
        setupManageCoursePage();
        Course courseToDelete = coursesTable.getSelectionModel().getSelectedItem();
        if(courseToDelete == null)
        {
            coursesErrorMessage.setText("Please select a course!");
            return;
        }
        //check if the course has registred students first
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This action will delete the course permanently");
        if(alert.showAndWait().get() == ButtonType.OK)
        {
            mainApp.removeCourse(courseToDelete);
            setupManageCoursePage();
        }
    }
    
    @FXML
    void saveNewCourse(ActionEvent event)
    {
        if(validNewCourse())
        {
            String name = newCourseName.getText();
            String code = newCourseCode.getText();
            int credits = Integer.parseInt(newCourseCredits.getValue());
            String instructor = newCourseIns.getText();
            String days = newCourseDays.getValue();
            
            //removing am and pm from the hours to convert it to int
            int hours = Integer.parseInt(newCourseHours.getValue().replace("pm", "").replace("am", ""));
            String description = newCourseDescription.getText();
            int availableSeats = Integer.parseInt(newCourseMaxEnrl.getText());
            List<String> prerequisites = newCoursePrerequisites.getSelectionModel().getSelectedItems();
            
            mainApp.addCourse(name, code, credits, instructor, days, hours, description, availableSeats, prerequisites);
            setupManageCoursePage();
        }
        
        
    }
    
    @FXML
    void showAddCourse(ActionEvent event)
    {
        setupManageCoursePage();
        addCourseButton.setVisible(false);
        newCourseCode.setVisible(true);
        saveNewCourseButton.setVisible(true);
        newCourseName.setVisible(true);
        newCourseIns.setVisible(true);
        newCourseDays.setVisible(true);
        newCourseHours.setVisible(true);
        newCourseMaxEnrl.setVisible(true);
        newCourseCredits.setVisible(true);
        newCourseDescription.setVisible(true);
        newCoursePrerequisites.setVisible(true);
    }
    
    
    //<<system statistics tab>>
    //<<<<<<<<>>>>>>>>>>>
    @FXML
    void refreshStat(ActionEvent event)
    {
        
    }
    

    //<<Helping methods>>
    //<<<<<<<<>>>>>>>>>>>
    
    //defualt view for manage students page
    private void defaultViewMSP()
    {
        addStudentButton.setVisible(true);
        manageStudentButton.setVisible(true);
        
        gpaTxtField.setVisible(false);
        newStudentPassword.setVisible(false);
        newStudentAdvisor.setVisible(false);
        newStudentID.setVisible(false);
        newStudentName.setVisible(false);
        resetPasswordField.setVisible(false);
        updateStudentButton.setVisible(false);
        removeStudentButton.setVisible(false);
        newStudentSaveButton.setVisible(false); 
        changeAdvisorChoiceBox.setVisible(false);
        newStudentPassedList.setVisible(false);
        passedCoursesLabel.setVisible(false);
        
        
        //clearing the fields
        errorMessage.setText("");
        newStudentName.clear();
        newStudentID.clear();
        newStudentAdvisor.getItems().setAll();
        changeAdvisorChoiceBox.getItems().setAll();
        newStudentPassword.clear();
        gpaTxtField.clear();
        resetPasswordField.clear();
        newStudentPassedList.getSelectionModel().clearSelection();
    }
    
    //defualt view for mange advisor page
    private void defaultViewMAP()
    {
        manageAdvisorButton.setVisible(true);
        addAdvisorButton.setVisible(true);
        
        //clearign fields
        newAdvisorName.clear();
        newAdvisorId.clear();
        newAdvisorPassword.clear();
        resetAdvisorPasswordField.clear();
        advisorErrorMessage.setText("");
        
        newAdvisorName.setVisible(false);
        newAdvisorId.setVisible(false);
        newAdvisorPassword.setVisible(false);
        saveNewAdvisorButton.setVisible(false);
        resetAdvisorPasswordField.setVisible(false);
        updateAdvisorButton.setVisible(false);
        removeAdvisorButton.setVisible(false); 
    }
    
    //default view for "manage courses" tab
    private void defaultViewMCP()
    {                
        addCourseButton.setVisible(true);
        
        //clearing fields
        newCourseCode.clear();
        newCourseName.clear();
        newCourseIns.clear();
        newCourseMaxEnrl.clear();
        coursesErrorMessage.setText("");
        newCourseDescription.clear();
        
        saveNewCourseButton.setVisible(false);
        newCourseCode.setVisible(false);
        newCourseName.setVisible(false);
        newCourseIns.setVisible(false);
        newCourseDays.setVisible(false);
        newCourseHours.setVisible(false);
        newCourseMaxEnrl.setVisible(false);
        newCourseDescription.setVisible(false);
        newCourseCredits.setVisible(false);
        newCoursePrerequisites.setVisible(false);
        newCoursePrerequisites.getSelectionModel().clearSelection();
    }
    
    //validating new students the admin wants to add
    private boolean validNewStudent()
    {
        if (newStudentName.getText().equals("") || newStudentID.getText().equals("")
            || newStudentAdvisor.getValue().equals("Advisor*")
            || newStudentPassword.getText().equals(""))   
        {
            errorMessage.setText("* are required fields");
            return false;
        }
        
        if(!newStudentID.getText().matches("\\d+"))
        {
            errorMessage.setText("Student ID should consist only of numbers!");
            return false;
        }
        
        if(!newStudentName.getText().matches("^[a-zA-Z]+( [a-zA-Z]+)*$"))
        {
            errorMessage.setText("Invalid name!");
            return false;
        }
        
        if (mainApp.studentMap.containsKey(newStudentID.getText()))
        {
            errorMessage.setText("ID is already used!");
            return false;
        }
        
        if(!gpaTxtField.getText().equals(""))
        {
            try
            {
                double gpa = Double.parseDouble(gpaTxtField.getText());
                if(gpa<0 || gpa>4)
                {
                    errorMessage.setText("GPA should be between 0.00 and 4.00");
                    return false;
                }
            }
            catch (NumberFormatException e)
            {
                errorMessage.setText("Invalid GPA format, please enter a number");
                return false;
            }
        }
        return true;
    }
    
    //validating new courses the admin wants to add
    private boolean validNewCourse()
    {
        if (newCourseCode.getText().isEmpty() || newCourseName.getText().isEmpty()
            || newCourseIns.getText().isEmpty() || newCourseMaxEnrl.getText().isEmpty()
            || newCourseDays.getValue().equals("Course Days*") || newCourseHours.getValue().equals("Course Hours*")
            || newCourseCredits.getValue().equals("Credits*"))
        {
            coursesErrorMessage.setText("* are required fields");
            return false;
        }
        
        if(!newCourseCode.getText().matches("^[a-zA-Z]{2,}.+[0-9]{3}$") || newCourseCode.getText().length()>7)
        {
            coursesErrorMessage.setText("Invalid Course Code!");
            return false;
        }
        
        if (mainApp.courseMap.containsKey(newCourseCode.getText().toUpperCase()))
        {
            coursesErrorMessage.setText("Course Code is already used!");
            return false;
        }
        
        if (newCourseCode.getText().contains(" "))
        {
            coursesErrorMessage.setText("Invalid Course Code!");
            return false;
        }
        
        try
        {
            int maxEnrl = Integer.parseInt(newCourseMaxEnrl.getText());
            if (maxEnrl<5 || maxEnrl>10)
            {
                coursesErrorMessage.setText("Max Enrollment should be between 5 and 10");
                return false;
            }
        }
        catch(NumberFormatException e)
        {
            coursesErrorMessage.setText("Invalid input in (Max Enrollment)");
            return false;
        }
    
        return true;
    }
}
