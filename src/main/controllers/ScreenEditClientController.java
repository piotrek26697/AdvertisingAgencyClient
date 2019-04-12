package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.model.Client;
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
    private Button buttonAdd;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldLastName;

    @FXML
    private TextField fieldAddress;

    @FXML
    private Label labelOutput;

    private HttpHelper httpHelper;

    private final String URL = Main.URL + "/client";

    private Client client;

    public void setClient(Client client)
    {
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        httpHelper = new HttpHelper();

        buttonAdd.setDisable(true);

        fieldName.setOnKeyTyped(event -> {
            if (fieldName.getText() != client.getName())
                buttonAdd.setDisable(false);
            else
                buttonAdd.setDisable(true);
        });
        fieldLastName.setOnKeyTyped(event -> {
            if (fieldLastName.getText() != client.getLastName())
                buttonAdd.setDisable(false);
            else
                buttonAdd.setDisable(true);
        });
        fieldAddress.setOnKeyTyped(event -> {
            if (fieldAddress.getText() != client.getAddress())
                buttonAdd.setDisable(false);
            else
                buttonAdd.setDisable(true);
        });

        fieldName.setText(client.getName());
        fieldAddress.setText(client.getAddress());
        fieldLastName.setText(client.getLastName());

        buttonExit.setOnAction(event -> close(event));

        buttonAdd.setOnAction(event -> updateClient());
    }

    private void updateClient()
    {
        client.setAddress(fieldAddress.getText());
        client.setName(fieldName.getText());
        client.setLastName(fieldLastName.getText());
        try
        {
            StringWriter wr = new StringWriter();
            JAXB.marshal(client, wr);
            httpHelper.doPut(URL, wr.toString(), "application/xml");
        } catch (IOException e)
        {
            labelOutput.setText("Something went wrong.\nContact the administrator.");
        }
    }

    public void close(ActionEvent event)
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
