package gui;

import annotations.Ammar;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import regsystem.MainApp;


@Ammar
public class LoginController implements Initializable
{
    @FXML
    private Stage stage;
    @FXML
    public ChoiceBox<String> choiceBox;
    private final String[] USER_TYPES = {"Student", "Advisor", "Admin"};
    @FXML
    private Label errorMessage;
    @FXML
    public TextField usernameTxtField;
    @FXML
    public PasswordField passwordTxtField;
    @FXML
    public Button logoutButton;
    
    private MainApp mainApp;
    

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
         //setting up the choice box here because it can't be done
        //on the FXML file or sceneBuilder
        choiceBox.setValue("Type of User");
        choiceBox.getItems().addAll(USER_TYPES);
        choiceBox.setStyle("-fx-font-size: 15px;");
    }
    
    //we're connecting the controller to the MainApp so we can call 
    //its methods and access its data fields from here
    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }
    
    
    @FXML
    public void redirect(ActionEvent a) throws Exception
    {
        String userType = choiceBox.getValue();
        String username = usernameTxtField.getText();
        String password = passwordTxtField.getText();
        
        if (userType==null || username.equals("") || password.equals(""))
            errorMessage.setText("Please Fill All Fields!");
        
        else
        { 
            switch(handleLogin(userType, username, password))
            {
            case 0: //successful login
                stage = (Stage) choiceBox.getScene().getWindow();
                stage.close(); //closing the login interface
                
                //here we're opening the appropriate interface 
                //based on the type of user
                if (userType.equals("Admin"))
                {
                    mainApp.setActiveUser(mainApp.adminMap.get(username));
                }
                else if(userType.equals("Advisor"))
                {
                    mainApp.setActiveUser(mainApp.advisorMap.get(username));
                }
                else
                {
                    mainApp.setActiveUser(mainApp.studentMap.get(username));
                }
                mainApp.showUserInterface(userType);
                break;
            case 1: //username exists but wrong password
                errorMessage.setText("Wrong Password!");
                break;
            default: //username doesn't exist
                errorMessage.setText("User Not Found!");
            }
        }
        
    }
    
    
    @Ammar
    //method returns 0 if the login was sucessful, 1 if the password is incorrect
    //and 2 if the user does not exist
    private int handleLogin(String typeOfUser, String username, String pswrd) throws Exception
    {
        try
        {
            Scanner passwordReader;
            File user = new File("Data/" + typeOfUser+"/" + username+".txt");
            if (user.exists()) 
            {
                passwordReader = new Scanner(user);

                if(pswrd.hashCode() == (passwordReader.nextInt()))
                {
                    passwordReader.close();
                    return 0;
                }
                passwordReader.close();
                return 1;
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Data file was not found (handleLogin)");
        }
        return 2;
    }
} 

