package main.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.model.*;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable
{
    @FXML
    private TableView<Client> tableClients;

    @FXML
    private TableColumn<Client, String> columnName;

    @FXML
    private TableColumn<Client, String> columnLastName;

    @FXML
    private TableColumn<Client, String> columnAddress;

    @FXML
    private Button buttonShowClients;

    @FXML
    private Button buttonAddClient;

    @FXML
    private Button buttonDeleteClient;

    @FXML
    private Button buttonEditClient;


    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldLastName;

    @FXML
    private TextField fieldAddress;

    @FXML
    private Label labelError;

    private HttpHelper httpHelper;

    private final String URL = Main.URL + "/client";

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        httpHelper = new HttpHelper();

        buttonEditClient.setDisable(true);
        buttonEditClient.setOnAction(event -> {
            Client client = tableClients.getSelectionModel().getSelectedItem();
            editClientWindow(client);
        });

        buttonDeleteClient.setDisable(true);
        buttonDeleteClient.setOnAction(event -> {
            deleteClient();
            buttonShowClients.fire();
        });

        buttonShowClients.setOnAction((event) -> {
            List<Client> clientList = clientsGet();
            if (clientList != null)
            {
                clientsDisplay(clientList);
                buttonDeleteClient.setDisable(true);
                buttonEditClient.setDisable(true);
            }
        });

        buttonAddClient.setOnAction((event -> {
            addingClientWindow();
        }));

//        tableClients.setEditable(false);
        tableClients.setOnMouseClicked(event -> {
            if (tableClients.getSelectionModel().getSelectedItem() != null)
            {
                buttonDeleteClient.setDisable(false);
                buttonEditClient.setDisable(false);
            }
        });
    }

    private void editClientWindow(Client client)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/screenEditClient.fxml"));
            Parent root = loader.load();

            ScreenEditClientController controller = loader.getController();
            controller.setClient(client);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Edit Client");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void deleteClient()
    {
        Client client = tableClients.getSelectionModel().getSelectedItem();
        try
        {
            httpHelper.doDelete(URL + "?id=" + client.getId());
        } catch (IOException e)
        {
            labelError.setText("Something went wrong. Contact the administrator");
        }
    }


    public void addingClientWindow()
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/screenAddClient.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Add Client");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public List<Client> clientsGet()
    {
        try
        {
            String params = "?name=" + fieldName.getText() + "&lastName=" + fieldLastName.getText() + "&address=" + fieldAddress.getText();
            String url = URL + params;
            String result = httpHelper.doGet(url);
            Clients clients = JAXB.unmarshal(new StringReader(result), Clients.class);
            return clients.getClients();
        } catch (IOException e)
        {
            labelError.setText("Something went wrong. Contact the administrator");
            return null;
        }
    }

    public void clientsDisplay(List<Client> clientList)
    {
        columnName.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<Client, String>("lastName"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<Client, String>("address"));

        tableClients.setItems(FXCollections.observableArrayList(clientList));
    }
}
