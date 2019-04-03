package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Client;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable, ControlledScreen
{
    @FXML
    private TableView tableClients;
    @FXML
    private TableColumn columnName;
    @FXML
    private TableColumn columnLastName;
    @FXML
    private TableColumn columnAddress;
    @FXML
    private Button buttonShowClients;

    private ScreensController screensController;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        buttonShowClients.setOnAction((event) -> {
            List<Client> clientList = clientsGet();
            clientsDisplay(clientList);
        });


    }

    //    private void login()
//    {
//        screensController.setScreen(Main.screen2ID);
//    }
    private List<Client> clientsGet()
    {
        //TODO contecting to databse and getting clients
        return null;
    }

    private void clientsDisplay(List<Client> clientList)
    {
//TODO displaying clients
    }

    @Override
    public void setScreenParent(ScreensController screenParent)
    {
        screensController = screenParent;
    }
}
