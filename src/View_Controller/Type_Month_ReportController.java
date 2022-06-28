package View_Controller;

import Model.Appointment;
import Model.ListManager;
import Model.TypeMonth;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.ResourceBundle;

/**
 * Controller for the Type/Month Report screen.
 */

public class Type_Month_ReportController extends ListManager implements Initializable {

    @FXML
    private Label typeMonthReportTitleLabel;

    @FXML
    private TableView<TypeMonth> typeMonthTableview;

    @FXML
    private TableColumn<?, ?> monthCol;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private TableColumn<?, ?> numAptCol;

    @FXML
    private Button exitButton;

    Stage stage;
    Parent scene;

    /**
     * Returns to Home Page
     * @param event
     * @throws IOException
     */

    @FXML
    void onExitButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home.fxml"));
        loader.load();
        HomeController homeController = loader.getController();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Initializes the table to display the number of each month and type combo.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        ObservableList<TypeMonth> typeMonthNum = FXCollections.observableArrayList();
        for(Appointment a : ListManager.getAppointments()){
            Month month = a.getStartTime().getMonth();
            String type = a.getType();
            TypeMonth typeMonth = new TypeMonth(month, type, 1);
            if(typeMonthNum.isEmpty()){
                typeMonthNum.add(typeMonth);
            }
            else{
                boolean typeMonthExists = false;
                for(TypeMonth tm : typeMonthNum){
                    if (tm.getMonth().equals(typeMonth.getMonth()) && tm.getType().equals(typeMonth.getType())){
                        typeMonthExists = true;
                        tm.setNumTypeMonth(tm.getNumTypeMonth() + 1);
                        break;
                    }
                }
                if(!typeMonthExists){
                    typeMonthNum.add(typeMonth);
                }
            }
        }

        typeMonthTableview.setItems(typeMonthNum);

        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        numAptCol.setCellValueFactory(new PropertyValueFactory<>("numTypeMonth"));

    }

}

