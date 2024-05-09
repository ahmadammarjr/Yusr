package gui;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import yusr.MainApp;
import user.Advisor;
import user.Course;
import user.Schedule;
import user.ScheduleHour;
import user.SpecialRequest;
import user.Student;
import user.User;



public class AdvisorInterfaceController
{
    //home page tab elements
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label eMailLabel;
    @FXML
    private Label iDlabel;
    @FXML
    private Label phoneNumberLabel;
    
    
    //students tab elements
    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private TableColumn<Student, String> iDCol;
    @FXML
    private TableColumn<Student, String> nameCol;
    @FXML
    private TableColumn<Student, Integer> gpaCol;
    @FXML
    private TableColumn<Student, String> eMailCol;
    @FXML
    private TableColumn<Student, String> numberCol;
    
    
    //schedules tab elements
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
    @FXML
    private ListView<String> ScheduleStudentsList;
    
    private Student currentScheduleStudent;
    
    
    //Special Registration Requests elements
    @FXML
    private TableView<SpecialRequest> spReqTable;
    @FXML
    private TableColumn<SpecialRequest, String> StudentNameCol;
    @FXML
    private TableColumn<SpecialRequest, String> studentIdCol;
    @FXML
    private TableColumn<SpecialRequest, String> courseCol;
    @FXML
    private Label errorMessage;
    
    //
    @FXML
    private TabPane tabPane;
    private Advisor activeAdvisor;
    private MainApp mainApp;
    private ArrayList<Student> studentsList;
    private ArrayList<SpecialRequest>spReqList = new ArrayList<>();
    private Schedule stdSchedule;

    public void setActiveAdvisor(User activeAdvisor)
    {
        this.activeAdvisor = (Advisor)activeAdvisor;
        
        setupStudentsList();
        setupHomePage();
    }

    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }
    
    private void setupStudentsList()
    {
        studentsList = activeAdvisor.getStudents();
        
        setupSpReqList();
    }
    
    private void setupSpReqList()
    {
        for(Student s: studentsList)
        {
            spReqList.addAll(s.getPendingSpecialRequests());
        }
    }
    
    
    @FXML
    void logout(ActionEvent event)
    {
        mainApp.logout();
    }
    
    
    @FXML
    public void updateSelectedTab(Event event)
    {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab!= null)
            switch(selectedTab.getId())
            {
                case "homePageTab":
                    if(activeAdvisor!=null) //to skip the function in the first time it opens
                        setupHomePage();
                    break;
                case "studentsTab":
                    setupStudentsPage();
                    //clear the table from any selections
                    studentsTable.getSelectionModel().clearSelection();
                    break;
                case "studentsSchedulesTab":
                    setupStudentsSchedulesPage();
                    break;
                case "specialRegistrationTab":
                    setupSpecialRegistrationPage();
                    spReqTable.getSelectionModel().clearSelection();

            }
    }

    private void setupHomePage()
    {
        nameLabel.setText("Name: " + activeAdvisor.getName());
        iDlabel.setText("ID: " + activeAdvisor.getId());
        eMailLabel.setText("eMail: " + activeAdvisor.getId()+ "@mio.com");
        //phoneNumberLabel.setText("Phone Number: " + activeAdvisor.getPhoneNumber());  
    }

    

    private void setupStudentsSchedulesPage()
    {
        //setting up students list
        ArrayList<String> studentsArray = new ArrayList<>();
        for(Student s: studentsList)
        {
            studentsArray.add(s.getId());
        }
        ScheduleStudentsList.getItems().setAll(studentsArray);
        
        //clearing the schedule
        scheduleTable.setItems(null);
        currentScheduleStudent = null;
    }

    
    
    
    
    //<<<<<<Schedules Tab>>>>>>
    //<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>
    private void setupStudentsPage()
    {
        ObservableList<Student> stdList = FXCollections.observableArrayList(studentsList);
        
        iDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        gpaCol.setCellValueFactory(new PropertyValueFactory<>("gpa"));
        eMailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        numberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        
        studentsTable.setItems(stdList);
        
    }
    
    @FXML
    void updateSchedule(MouseEvent event) throws Exception
    {
        Student selectedStudent = mainApp.studentMap.get(ScheduleStudentsList.getSelectionModel().getSelectedItem());
        //early return if the user chooses the same student twice or does not choose any student
        if(selectedStudent == null || selectedStudent == currentScheduleStudent)
            return;
        
        currentScheduleStudent = selectedStudent;
        setupStdSchedule(selectedStudent);
    }
    
    private void setupStdSchedule(Student s) throws Exception
    {
        stdSchedule = new Schedule();
        for(String courseCode: s.getCourses())
        {
            if(!mainApp.courseMap.containsKey(courseCode)) continue;
            Course course = mainApp.courseMap.get(courseCode);
            stdSchedule.addCourseToSchedule(course);
        }
        showSchedule();
    }
    
    
    //<<<<<<Special Requests Tab>>>>>>
    //<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>
    private void setupSpecialRegistrationPage()
    {
        defaultSRRview();
        ObservableList<SpecialRequest> specialReqList = FXCollections.observableArrayList(spReqList);
        
        StudentNameCol.setCellValueFactory(new PropertyValueFactory<>("SENDER"));
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("SENDER"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("COURSE"));
        
        spReqTable.setItems(specialReqList);
    }
    
    @FXML
    void approveReq(ActionEvent event) throws Exception
    {
        if(!validApproval())
            return;
        
        SpecialRequest sr = spReqTable.getSelectionModel().getSelectedItem();
        spReqList.remove(sr);
        mainApp.approveSpecialRequest(sr);
        setupSpecialRegistrationPage();
    }

    @FXML
    void denyReq(ActionEvent event) 
    {
        SpecialRequest sr = spReqTable.getSelectionModel().getSelectedItem();
        if(sr == null)
            return;
        
        spReqList.remove(sr);
        mainApp.denySpecialRequest(sr);
        setupSpecialRegistrationPage();
    }
    
    
    //<<<<<<Helping methods>>>>>>
    private void showSchedule()
    {
        ObservableList<ScheduleHour> hoursList = FXCollections.observableArrayList(stdSchedule.getScheduleHours());
        hourCol.setCellValueFactory(new PropertyValueFactory<>("hour"));
        sundayCol.setCellValueFactory(new PropertyValueFactory<>("sunday"));        
        mondayCol.setCellValueFactory(new PropertyValueFactory<>("monday"));        
        tuesdayCol.setCellValueFactory(new PropertyValueFactory<>("tuesday"));        
        wednesdayCol.setCellValueFactory(new PropertyValueFactory<>("wednesday"));        
        thursdayCol.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        
        scheduleTable.setItems(hoursList);
        scheduleTable.setMouseTransparent(true);
        scheduleTable.setFixedCellSize(57);
    }
    
    private void defaultSRRview()
    {
        errorMessage.setText("");
    }
    
    private boolean validApproval() throws Exception
    {
        SpecialRequest selectedRequest = spReqTable.getSelectionModel().getSelectedItem();
        
        if(selectedRequest == null)
        {
            errorMessage.setText("No request Is selected!");
            return false;
        }
        
        Student student = mainApp.studentMap.get(selectedRequest.SENDER);
        Course course = mainApp.courseMap.get(selectedRequest.COURSE);
        
        if(course == null)
        {
            errorMessage.setText("This course has been deleted by admins\nPlease contact the student.");
            return false;
        }
        
        if(!canEnroll(student, course))
        {
            errorMessage.setText("Schedule conflict detected!\n please contact the student");
            return false;
        }
        return true;
    }
    
    private boolean canEnroll(Student s, Course c) throws Exception
    {
        setupStdSchedule(s);
        return stdSchedule.canAdd(c);
    }
    
}
