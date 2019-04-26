package main.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.model.HttpHelper;
import main.model.Main;
import main.model.collections.Advertisements;
import main.model.collections.Clients;
import main.model.entities.Advertisement;
import main.model.entities.Client;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScreenAddAdvertisementController implements Initializable
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
    private TextField fieldName;

    @FXML
    private TextField fieldLastName;

    @FXML
    private TextField fieldAddress;

    @FXML
    private TextArea textAreaDescription;

    @FXML
    private TextField fieldPrice;

    @FXML
    private TextField fieldTitle;

    @FXML
    private Button buttonAdd;

    private HttpHelper httpHelper;

    private final String URL_CLIENT = Main.URL + "/client";

    private final String URL_AD = Main.URL + "/advertisement";

    private boolean clientSelected = false;

    private boolean textFieldsFilled = false;

    private Client client;

    private ScreenAdvertisementsController advertisementsController;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        httpHelper = new HttpHelper();

        buttonShowClients.setOnAction(event -> showClients());

        buttonAdd.setDisable(true);
        buttonAdd.setOnAction(event -> addAdvertisement());

        tableClients.setOnMouseClicked(event -> {
            if (tableClients.getSelectionModel().getSelectedItem() != null)
            {
                clientSelected = true;
                checkButtonAddAdvertisement();
            }
        });

        fieldPrice.setOnKeyReleased(event -> {
            if (!fieldTitle.getText().trim().isEmpty() && !fieldPrice.getText().trim().isEmpty())
            {
                textFieldsFilled = true;
                checkButtonAddAdvertisement();
            } else
                textFieldsFilled = false;
        });

        fieldTitle.setOnKeyReleased(event -> {
            if (!fieldTitle.getText().trim().isEmpty() && !fieldPrice.getText().trim().isEmpty())
            {
                textFieldsFilled = true;
                checkButtonAddAdvertisement();
            } else
                textFieldsFilled = false;
        });
    }

    private void checkButtonAddAdvertisement()
    {
        if (textFieldsFilled && clientSelected)
            buttonAdd.setDisable(false);
        else
            buttonAdd.setDisable(true);
    }

    private void showClients()
    {
        List<Client> clientList = this.downloadClientsFromDB();
        if (clientList != null)
        {
            if (clientList.size() > 0)
            {
                populateTableClients(clientList);
                clientSelected = false;
                buttonAdd.setDisable(true);
            } else
                this.showMessage("No clients in database.");
        }
    }

    private void populateTableClients(List<Client> clientList)
    {
        columnName.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<Client, String>("lastName"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<Client, String>("address"));

        tableClients.setItems(FXCollections.observableArrayList(clientList));
    }

    private List<Client> downloadClientsFromDB()
    {
        try
        {
            String params = "?name=" + fieldName.getText() + "&lastName=" + fieldLastName.getText() + "&address=" + fieldAddress.getText();
            String url = URL_CLIENT + params;
            String result = httpHelper.doGet(url);
            Clients clients = JAXB.unmarshal(new StringReader(result), Clients.class);
            return clients.getClients();
        } catch (IOException e)
        {
            this.showMessage("Something went wrong. Contact the administrator.");
            return null;
        }
    }

    private void showMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void addAdvertisement()
    {
        if (fieldPrice.getText().matches("[1-9]+\\d*(\\.?\\d{1,2})?|0|0\\.[1-9]{1,2}"))
        {
            Client client = tableClients.getSelectionModel().getSelectedItem();
            Advertisement advertisement = new Advertisement();
            advertisement.setName(fieldTitle.getText().trim());
            advertisement.setDescription(textAreaDescription.getText().trim());
            advertisement.setClient(client);
            advertisement.setPrice(Double.parseDouble(fieldPrice.getText()));
            try
            {
                StringWriter sw = new StringWriter();
                JAXB.marshal(advertisement, sw);
                httpHelper.doPost(URL_AD, sw.toString(), "application/xml");    //saving add

                showMessage("Advertisement has been added.");
                this.clearFields();
                this.advertisementsController.showAdvertisements();
            } catch (IOException e)
            {
                this.showMessage("Something went wrong. Contact the administrator.");
            }
        } else
        {
            this.showMessage("Wrong price. Make sure to use dot as a separator.");
        }
    }

    private void clearFields()
    {
        textAreaDescription.clear();
        fieldPrice.clear();
    }

    public void setClient(Client client)
    {
        this.client = client;
        if (client != null)
        {
            fieldName.setText(client.getName());
            fieldLastName.setText(client.getLastName());
            fieldAddress.setText(client.getAddress());
            showClients();
        }
    }

    public void setAdvertisementsController(ScreenAdvertisementsController controller)
    {
        this.advertisementsController = controller;
    }
}
