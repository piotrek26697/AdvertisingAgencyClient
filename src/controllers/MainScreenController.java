package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.*;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable
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

    @FXML
    private Button buttonAddClient;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldLastName;

    @FXML
    private TextField fieldAddress;

    private HttpHelper httpHelper;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        httpHelper = new HttpHelper();

        buttonShowClients.setOnAction((event) -> {
            List<Client> clientList = clientsGet();
            clientsDisplay(clientList);
        });

        buttonAddClient.setOnAction((event -> {

        }));
    }

    private List<Client> clientsGet()
    {
        String params = "?name=" + fieldName.getText() + "&lastName=" + fieldLastName.getText() + "&address=" + fieldAddress.getText();
        String url = Main.URL + "/client" + params;
        String result = httpHelper.doGet(url);
        Clients clients = JAXB.unmarshal(new StringReader(result), Clients.class);
        return clients.getClients();
    }

    private void clientsDisplay(List<Client> clientList)
    {
//TODO displaying clients
    }
}
