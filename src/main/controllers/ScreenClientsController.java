package main.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.model.*;
import main.model.collections.Clients;
import main.model.entities.Client;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScreenClientsController implements Initializable
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
    private Button buttonClearFields;

    @FXML
    private Button buttonShowClientAdvertisements;

    @FXML
    private Button buttonMenu;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldLastName;

    @FXML
    private TextField fieldAddress;

    private HttpHelper httpHelper;

    private Client client;

    private final String URL = Main.URL + "/client";

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        httpHelper = new HttpHelper();

        buttonMenu.setOnAction(this::goToMenu);

        buttonShowClientAdvertisements.setDisable(true);
        buttonShowClientAdvertisements.setOnAction(this::showAdvertisementsWindow);

        buttonClearFields.setOnAction(event -> clearFields());

        buttonEditClient.setDisable(true);
        buttonEditClient.setOnAction(event -> editClientWindow());

        buttonDeleteClient.setDisable(true);
        buttonDeleteClient.setOnAction(event -> deleteClient());

        buttonShowClients.setOnAction((event) -> showClients());

        buttonAddClient.setOnAction((event -> addingClientWindow()));

//        tableClients.setEditable(false);
        tableClients.setOnMouseClicked(event -> {
            if (tableClients.getSelectionModel().getSelectedItem() != null)
            {
                client = tableClients.getSelectionModel().getSelectedItem();
                buttonDeleteClient.setDisable(false);
                buttonEditClient.setDisable(false);
                buttonShowClientAdvertisements.setDisable(false);
            }
        });
    }

    private void goToMenu(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/screenStart.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Advertising agency");
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void showAdvertisementsWindow(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/screenAdvertisements.fxml"));
            Parent root = loader.load();
            ScreenAdvertisementsController controller = loader.getController();
            controller.setClient(client);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Managing advertisements");
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void showClients()
    {
        List<Client> clientList = this.downloadClientsFromDB();
        if (clientList != null)
        {
            if (clientList.size() > 0)
            {
                populateTableClients(clientList);
            } else
                this.showMessage("No clients in database.");
        } else
            tableClients.getItems().clear();

        buttonDeleteClient.setDisable(true);
        buttonEditClient.setDisable(true);
        buttonShowClientAdvertisements.setDisable(true);
    }

    private void clearFields()
    {
        fieldAddress.clear();
        fieldName.clear();
        fieldLastName.clear();
    }

    private void editClientWindow()
    {
        Client client = tableClients.getSelectionModel().getSelectedItem();
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/screenEditClient.fxml"));
            Parent root = loader.load();

            ScreenEditClientController controller = loader.getController();
            controller.setClient(client);
            controller.setController(this);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Edit client");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void deleteClient()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete selected client?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES)
        {
            Client client = tableClients.getSelectionModel().getSelectedItem();
            try
            {
                httpHelper.doDelete(URL + "?id=" + client.getId());
            } catch (IOException e)
            {
                this.showMessage("Something went wrong. Contact the administrator.\nMake sure that there are no advertisements" +
                        "bound with selected client");
            }
            this.showClients();
        }
    }


    private void addingClientWindow()
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/screenAddClient.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Add client");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private List<Client> downloadClientsFromDB()
    {
        try
        {
            String params = "?name=" + fieldName.getText().trim() + "&lastName=" + fieldLastName.getText().trim() + "&address=" + fieldAddress.getText().trim();
            String url = URL + params;
            String result = httpHelper.doGet(url);
            Clients clients = JAXB.unmarshal(new StringReader(result), Clients.class);
            return clients.getClients();
        } catch (IOException e)
        {
            this.showMessage("Something went wrong. Contact the administrator.");
            return null;
        }
    }

    private void populateTableClients(List<Client> clientList)
    {
        columnName.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<Client, String>("lastName"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<Client, String>("address"));

        tableClients.setItems(FXCollections.observableArrayList(clientList));
    }

    private void showMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}
