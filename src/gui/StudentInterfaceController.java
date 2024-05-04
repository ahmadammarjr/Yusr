package gui;

import annotations.Ammar;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import yusr.MainApp;
import user.Course;
import user.Schedule;
import user.ScheduleHour;
import user.SpecialRequest;
import user.Student;
import user.User;

@Ammar
public class StudentInterfaceController
{
    //home page elements
    @FXML
    private Label eMailLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label advisorLabel;
    @FXML
    private Label gpaLabel;
    @FXML
    private Label advisorEMailLabel;
    @FXML
    private Label numberLabel;
    @FXML
    private ListView<String> passedCoursesList;
    
    //registration tab elements
    @FXML
    private TableView<Course> coursesTable;
    @FXML
    private TableColumn<Course, String> courseNameCol;
    @FXML
    private TableColumn<Course, String> courseCodeCol;
    @FXML
    private TableColumn<Course, String> courseInsCol;
    @FXML
    private TableColumn<Course, String> lectureTimeCol;
    @FXML
    private TableColumn<Course, String> statusCol;
    @FXML
    private Label courseDescriptionLabel;
    @FXML
    private Label prerequisitesLabel;
    @FXML
    private Label errorMessage;
    @FXML
    private Label creditsLabel;
    @FXML
    private Button detailButton;
    @FXML
    private Button hideDetailButton;
    @FXML
    private Button dropButton;
    @FXML
    private Button regbutton;
    
    //schedule tab elements
    @FXML
    private TableView<ScheduleHour> scheduleTable;
    @FXML
    private TableColumn<ScheduleHour, String> hourCol;
    @FXML
    private TableColumn<ScheduleHour, String> sundayCol;
    @FXML
    private TableColumn<ScheduleHour, String> mondayCol;
    @FXML
    private TableColumn<ScheduleHour, String> tuesdayCol;
    @FXML
    private TableColumn<ScheduleHour, String> wednesdayCol;
    @FXML
    private TableColumn<ScheduleHour, String> thursdayCol;
    
    //special reqistration tab elements
    @FXML
    private TableView<SpecialRequest> spReqTable;
    @FXML
    private TableColumn<SpecialRequest, String> spCourseCol;
    @FXML
    private TableColumn<SpecialRequest, String> spStatusCol;
    @FXML
    private ChoiceBox<String> spCourseChoiceBox;
    @FXML
    private Label spErrormessage;
    
    //update info tab elements
    @FXML
    private TextField newName;
    @FXML
    private TextField newPhoneNumber;
    @FXML
    private TextField newGpa;
    @FXML
    private TextField newAdvisorEmail;
    @FXML
    private TextField newEMail;
    @FXML
    private TextField newId;
    @FXML
    private TextField newPassword;
    @FXML
    private PasswordField oldPassword;
    
    //
    @FXML
    private TabPane tabPane;
    private MainApp mainApp;
    private Student activeStudent;
    private final Schedule activeStudentSchedule = new Schedule();
    
    
    public void setActiveStudent(User activeUser)
    {
        this.activeStudent = (Student)activeUser;
        
        setupHomePage();
        setupStudentSchedule();
    }
    
    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }
       
    
    @FXML
    public void updateSelectedTab(Event event)
    {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        switch(selectedTab.getId())
        {
            case "homePageTab":
                //skip the the method untill the active student is set
                if(activeStudent!=null)setupHomePage();
                break;
            case "registrationTab":
                setupRegistrationTab();
                coursesTable.getSelectionModel().clearSelection();
                break;
            case "scheduleTab":
                setupScheduleTab();
                scheduleTable.refresh();
                break;
            case "specialRegistrationTab":
                setupSpecialRegistrationTab();
                break;
            case "updateInfoTab":
                setupUpdateInfoTab();
                break;
        }
    }

    private void setupHomePage()
    {
        nameLabel.setText("Name: " +activeStudent.getName());
        eMailLabel.setText("eMail: " +activeStudent.getId() +"@mio.com");
        gpaLabel.setText("GPA: " +activeStudent.getGpa());
        if(!activeStudent.getPhoneNumber().equals("-"))numberLabel.setText("Phone Number: " +activeStudent.getPhoneNumber());
        advisorLabel.setText("Advisor ID: " +activeStudent.getAdvisor());
        advisorEMailLabel.setText("Advisor eMail: " +activeStudent.getAdvisor() +"@mio.com");
        
        passedCoursesList.getItems().setAll(activeStudent.getPassedCourses());
    }

    private void setupScheduleTab() 
    {
        //setting up the schedule table
        ObservableList<ScheduleHour> hoursList = FXCollections.observableArrayList(activeStudentSchedule.getScheduleHours());
        hourCol.setCellValueFactory(new PropertyValueFactory<>("hour"));
        sundayCol.setCellValueFactory(new PropertyValueFactory<>("sunday"));        
        mondayCol.setCellValueFactory(new PropertyValueFactory<>("monday"));        
        tuesdayCol.setCellValueFactory(new PropertyValueFactory<>("tuesday"));        
        wednesdayCol.setCellValueFactory(new PropertyValueFactory<>("wednesday"));        
        thursdayCol.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        
        setupScheduleStyle();
        
        
        scheduleTable.setItems(hoursList);
        scheduleTable.setMouseTransparent(true);
        scheduleTable.setFixedCellSize(57);
    }


    //<<Registretion Tab>>
    //<<<<<<<<>>>>>>>>>>>
    private void setupRegistrationTab() 
    {
        defaultViewRP();
        ObservableList<Course> corList = FXCollections.observableArrayList(mainApp.courseMap.values());
        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseInsCol.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        lectureTimeCol.setCellValueFactory(new PropertyValueFactory<>("lectureTime"));

        /*dynamically setting the status column for each course depending
        on whether the student has passed/ is registered for that course or not*/
        statusCol.setCellValueFactory(cellData -> {
            Course course = cellData.getValue();
            if (activeStudent.getCourses().contains(course.getCode()))
            {
                return new SimpleStringProperty("Registered");
            }
            else if(activeStudent.getPassedCourses().contains(course.getCode()))
            {
                return new SimpleStringProperty("Passed");
            }
            else
            {
                return new SimpleStringProperty("Not registered");
            }
        });

        coursesTable.setItems(corList);
    }
    
    @FXML
    void register(ActionEvent event) throws Exception
    {
        defaultViewRP();
        Course courseToRgister = coursesTable.getSelectionModel().getSelectedItem();
        if (courseToRgister == null)
        {
            errorMessage.setText("No course is selected!");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        
        if (activeStudent.getCourses().contains(courseToRgister.getCode()))
        {
            errorMessage.setText("You're already registered in that course!");
            return;
        }
        
        if (activeStudent.getPassedCourses().contains(courseToRgister.getCode()))
        {
            errorMessage.setText("You have already passed that course!");
            return;
        }
        
        if (!courseToRgister.isEnrollable())
        {
            alert.setContentText("Course has reached enrollment capacity.");
            alert.setHeaderText("Registration Closed");
            alert.show();
            return;
        }
        
        if(!activeStudent.getPassedCourses().containsAll(courseToRgister.getPrerequisites()))
        {
            alert.setContentText("You can not register for this course.");
            alert.setHeaderText("Prerequisites not met");
            alert.show();
            return;
        }
        
        if(!activeStudentSchedule.canAdd(courseToRgister))
        {
            alert.setContentText(" It appears there's a schedule conflict with your selected course. Please review your schedule and try again.");
            alert.setHeaderText("Schedule Conflict Detected");
            alert.show();
            return;
        }

        
        mainApp.register(activeStudent, courseToRgister);
        activeStudentSchedule.addCourseToSchedule(courseToRgister);
        coursesTable.refresh();
        alert.setContentText("Your registration for the course has been successfully completed.");
        alert.setHeaderText("Registration Successful");
        alert.show(); 
    }
    
    @FXML
    void dropCourse(ActionEvent event)
    {
        defaultViewRP();
        Course courseToDrop = coursesTable.getSelectionModel().getSelectedItem();
        if (courseToDrop == null)
        {
            errorMessage.setText("No course is selected!");
            return;
        }
        
        if (!activeStudent.getCourses().contains(courseToDrop.getCode()))
        {
            errorMessage.setText("You're not registered in that course!");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.YES);
        alert .setHeaderText("Are you sure you want to drop " + courseToDrop.getCode() + " ?");
        
        if (alert.showAndWait().get() == ButtonType.YES)
        {
            mainApp.drop(activeStudent, courseToDrop);
            activeStudentSchedule.removeCourseFromSchedule(courseToDrop);
            coursesTable.refresh();
        } 
    }

    @FXML
    void showDetails(ActionEvent event)
    {
        defaultViewRP();
        
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if(selectedCourse == null)
        {
            errorMessage.setText("No course is selected!");
            return;
        }
        detailButton.setVisible(false);
        hideDetailButton.setVisible(true);
        regbutton.setVisible(false);
        dropButton.setVisible(false);
        
        courseDescriptionLabel.setText("Course Description: \n"+selectedCourse.getDescription());
        prerequisitesLabel.setText("Prerequisites: \n"+mainApp.getPrerequisites(selectedCourse.getCode()));
        creditsLabel.setText("Credits: " +selectedCourse.getCredits() + " Hours");
    }
    
    @FXML
    void hideDetails(ActionEvent event)
    {
        defaultViewRP();
    }
    
    //<<Special registration Tab>>
    //<<<<<<<<>>>>>>>>
    private void setupSpecialRegistrationTab() 
    {
        //setting up the table of past/current special requests
        ObservableList<SpecialRequest> spReqList = FXCollections.observableArrayList(activeStudent.getSpecialRequests().values());
        spCourseCol.setCellValueFactory(new PropertyValueFactory<>("COURSE"));
        spStatusCol.setCellValueFactory(new PropertyValueFactory<>("currentStatus"));
        
        spReqTable.setItems(spReqList);
        spReqTable.setMouseTransparent(true);
        
        //setting up the courses choice box
        Set<String> coursesList = mainApp.courseMap.keySet();
        spCourseChoiceBox.getItems().setAll(coursesList);
        
        //clearing the error message
        spErrormessage.setText("");
    }
    
    @FXML
    void sendNewSpReq(ActionEvent event)
    {
        if (!validNewSpReq()) return;
        String course = spCourseChoiceBox.getValue();
        
        //adding the special request the student and advisor lists
        mainApp.sendNewSpecialRequest(activeStudent, course);
        setupSpecialRegistrationTab();
        
        //informing the student the the request was sent successfully
        Alert alert = new Alert(AlertType.INFORMATION, "Your Acadimic advisor will review the request.");
        alert.setHeaderText("Request Sent");
        alert.setTitle("");
        alert.show();
        
    }
    
    
    //<<update info Tab>>
    //<<<<<<<<>>>>>>>>
    private void setupUpdateInfoTab()
    {
        newName.setPromptText(activeStudent.getName());
        newId.setPromptText(activeStudent.getId());
        newEMail.setPromptText(activeStudent.getId()+"@mio.com");
        newPhoneNumber.setPromptText(activeStudent.getPhoneNumber());
        newGpa.setPromptText(""+activeStudent.getGpa());
        newAdvisorEmail.setPromptText(activeStudent.getAdvisor()+"@mio.com");
        
        //disabling fields student cannot update
        newId.setMouseTransparent(true);
        newEMail.setMouseTransparent(true);
        newGpa.setMouseTransparent(true);
        newAdvisorEmail.setMouseTransparent(true);
        
        //clearing the fields
        newName.setText("");
        newPhoneNumber.setText("");
        newPassword.clear();
        oldPassword.clear();
    }
    
    @FXML
    void saveUpdate(ActionEvent event)
    {
        if(newName.getText().isEmpty()&&newPhoneNumber.getText().isEmpty())
            return;
        if(!newName.getText().trim().isEmpty())
        {
            if(!validName()) return;
            activeStudent.setName(newName.getText());
        }
        if(!newPhoneNumber.getText().trim().isEmpty())
        {
            if(!validPhoneNumber()) return;
            activeStudent.setPhoneNumber(newPhoneNumber.getText());
        }
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("Info Updated Succesfully");
        setupUpdateInfoTab();
        alert.show();
    }
    
    @FXML
    void resetPassword(ActionEvent event)
    {
        if(oldPassword.getText().trim().isEmpty() || newPassword.getText().trim().isEmpty())
            return;
        
        //checking if the hashcode of the password the student entered
        //matches the correct password to allow him to reset the password
        if(oldPassword.getText().hashCode() == activeStudent.getHashedPassword())
        {
            activeStudent.setHashedPassword(newPassword.getText().hashCode());
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Password was reset Successfully");
            alert.show();
            setupUpdateInfoTab();
        }
        else
        {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Wrong Password");
            alert.show();
            setupUpdateInfoTab();
        }
        
    }

  
    //<<helping methods>>
    //<<<<<<<<<<<>>>>>>>>
    private void defaultViewRP()
    {
        hideDetailButton.setVisible(false);
        detailButton.setVisible(true);
        regbutton.setVisible(true);
        dropButton.setVisible(true);
        errorMessage.setText("");
        courseDescriptionLabel.setText("");
        prerequisitesLabel.setText("");
        creditsLabel.setText("");
    }
    
    private void setupStudentSchedule()
    {
        /*cleaning the student courses list -> if a course is not found
          in mainApp.courseMap we delete it form the student courses list
        */
        activeStudent.getCourses().retainAll(mainApp.courseMap.keySet());

        for(String course : activeStudent.getCourses())
        {
            //adding the courses to the schedule
            try
            {
                activeStudentSchedule.addCourseToSchedule(mainApp.courseMap.get(course));
            } 
            catch (Exception e) 
            {
                System.out.println("problem in setupStudentSchedule()");
                System.out.println(e);
            }
        }
    }
    
    private void setupScheduleStyle()
    {
        hourCol.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #999999;");
        sundayCol.setStyle("-fx-font-weight: bold;");
        mondayCol.setStyle("-fx-font-weight: bold;");
        tuesdayCol.setStyle("-fx-font-weight: bold;");
        wednesdayCol.setStyle("-fx-font-weight: bold;");
        thursdayCol.setStyle("-fx-font-weight: bold;");
    }
    
    private boolean validPhoneNumber()
    {
        String pn = newPhoneNumber.getText();
        int N = pn.length();
        if(N<10 || N>10 || !pn.matches("05\\d+"))
        {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Invalid Phone Number!");
            alert.show();
            return false;
        }
        return true;
    }
    
    private boolean validName()
    {
        String name = newName.getText();
        int N = name.length();
        if(N<3 || N>39 || !name.matches("^[a-zA-Z]+( [a-zA-Z]+)*$"))
        {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Invalid Name!");
            alert.show();
            return false;
        }
        return true;
    }
    
    private boolean validNewSpReq()
    {
        String courseCode = spCourseChoiceBox.getValue();
        if(courseCode == null)
        {
            spErrormessage.setText("Please select a course!");
            return false;
        }
        if(activeStudent.getPassedCourses().contains(courseCode) || activeStudent.getCourses().contains(courseCode))
        {
            spErrormessage.setText("You've already passed/registered for this course!");
            return false;
        }
        if(activeStudent.getSpecialRequests().keySet().contains(courseCode))
        {
            spErrormessage.setText("You've already sent a request for that course");
            return false;
        }
        Course course = mainApp.courseMap.get(courseCode);
        if(!activeStudent.getPassedCourses().containsAll(course.getPrerequisites()))
        {
            spErrormessage.setText("You can't register for this course.\nPrerequisites not met");
            return false;
        }
        if (course.isEnrollable())
        {
            spErrormessage.setText("This course has available seats!");
            return false;
        }
        try
        {
            if(!activeStudentSchedule.canAdd(course))
            {
                spErrormessage.setText("Schedule conflict!\nCheck your schedule and try again");
                return false;
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }
        
        return true;
    }
}
