package View_Controller;

import Model.ListManager;
import Model.User;
import Utils.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Controller for the login screen.
 */

public class LoginController extends ListManager implements Initializable {

    @FXML
    private Label loginTitleLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label zoneIDLabel;

    @FXML
    private TextField usernameTextbox;

    @FXML
    private TextField passwordTextbox;

    @FXML
    private Button loginButton;

    private String errorMessage = "Username and/or password is incorrect.";
    private String errorTitle = "Error";
    Stage stage;
    Parent Scene;
    String filename = "login_activity.txt", item;

    /**
     * Attempts to log into scheduling app and returns error message if username or password is incorrect
     * @param event
     * @throws IOException
     */
    @FXML
    void onLoginButton(ActionEvent event) throws IOException {

        Boolean loginFailed = true;

        try{
            String sql = "SELECT * FROM users";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int userId = rs.getInt("User_ID");
                String username = rs.getString("User_Name");
                String password = rs.getString("Password");

                if(usernameTextbox.getText().equals(username) && passwordTextbox.getText().equals(password)){

                    ListManager.setUser(userId, username, password);

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("Home.fxml"));
                    loader.load();
                    HomeController homeController = loader.getController();
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    Parent scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.show();

                    loginFailed = false;

                    item = usernameTextbox.getText() + " logged in successfully at " + ZonedDateTime.now();
                    FileWriter fwriter = new FileWriter(filename, true);
                    PrintWriter outputFile = new PrintWriter(fwriter);
                    outputFile.println(item);
                    outputFile.close();
                    break;
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(loginFailed == true){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(errorTitle);
            alert.setContentText(errorMessage);
            alert.showAndWait();

            item = usernameTextbox.getText() + " login failed at " + ZonedDateTime.now();
            FileWriter fwriter = new FileWriter(filename, true);
            PrintWriter outputFile = new PrintWriter(fwriter);
            outputFile.println(item);
            outputFile.close();
        }

    }

    /**
     * Initializes the page to display in French or English depending on the native computer language.
     * @param url
     * @param resourceBundle
     */

    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResourceBundle rb = ResourceBundle.getBundle("Utils/login", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("fr")){
            loginTitleLabel.setText(rb.getString("Thaxton"));
            usernameLabel.setText(rb.getString("username"));
            passwordLabel.setText(rb.getString("password"));
            loginButton.setText(rb.getString("login"));
            errorMessage = rb.getString("errorMessage");
            errorTitle = rb.getString("errorTitle");
        }

        zoneIDLabel.setText(String.valueOf(ZoneId.of(String.valueOf(TimeZone.getDefault().getID()))));
    }

}