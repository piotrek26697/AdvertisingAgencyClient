package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.model.entities.Client;
import main.model.HttpHelper;
import main.model.Main;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenEditClientController implements Initializable
{
    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonUpdate;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldLastName;

    @FXML
    private TextField fieldAddress;

    private HttpHelper httpHelper;

    private final String URL = Main.URL + "/client";

    private Client client;

    private ScreenClientsController controller;

    public void setController(ScreenClientsController controller)
    {
        this.controller = controller;
    }

    public void setClient(Client client)
    {
        this.client = client;

        fieldName.setText(client.getName());
        fieldAddress.setText(client.getAddress());
        fieldLastName.setText(client.getLastName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        httpHelper = new HttpHelper();

        buttonUpdate.setDisable(true);

        fieldName.setOnKeyReleased(event -> {
            if (!fieldName.getText().equals(client.getName()) && !fieldName.getText().trim().isEmpty())
                buttonUpdate.setDisable(false);
            else
                buttonUpdate.setDisable(true);
        });
        fieldLastName.setOnKeyReleased(event -> {
            if (!fieldLastName.getText().equals(client.getLastName()) && !fieldLastName.getText().trim().isEmpty())
                buttonUpdate.setDisable(false);
            else
                buttonUpdate.setDisable(true);
        });
        fieldAddress.setOnKeyReleased(event -> {
            if (!fieldAddress.getText().equals(client.getAddress()) && !fieldAddress.getText().trim().isEmpty())
                buttonUpdate.setDisable(false);
            else
                buttonUpdate.setDisable(true);
        });

        buttonExit.setOnAction(this::close);

        buttonUpdate.setOnAction(event -> updateClient());
    }

    private void updateClient()
    {
        client.setAddress(fieldAddress.getText().trim());
        client.setName(fieldName.getText().trim());
        client.setLastName(fieldLastName.getText().trim());
        try
        {
            StringWriter wr = new StringWriter();
            JAXB.marshal(client, wr);
            httpHelper.doPut(URL, wr.toString(), "application/xml");
            this.showInfoMessage("Client has been updated.");
            buttonUpdate.setDisable(true);

            this.controller.showClients();

        } catch (IOException e)
        {
            this.showInfoMessage("Something went wrong.\nContact the administrator.");
        }
    }

    private void close(ActionEvent event)
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showInfoMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message + "\nPress \"Close\" to finish editing clients.", ButtonType.OK, ButtonType.CLOSE);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.CLOSE)
            this.buttonExit.fire();
    }
}
