package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.model.Client;
import main.model.HttpHelper;
import main.model.Main;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenAddClientController implements Initializable
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

    private HttpHelper httpHelper;

    private final String URL = Main.URL + "/client";

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        httpHelper = new HttpHelper();
        buttonExit.setOnAction(event -> close(event));
        buttonAdd.setOnAction(event -> addClient());
    }

    public void addClient()
    {
        if (!fieldName.getText().trim().isEmpty() && !fieldLastName.getText().trim().isEmpty() && !fieldAddress.getText().trim().isEmpty())
        {
            Client client = new Client(fieldName.getText(), fieldLastName.getText(), fieldAddress.getText());
            StringWriter writer = new StringWriter();
            JAXB.marshal(client, writer);
            try
            {
                httpHelper.doPost(URL, writer.toString(), "application/xml");
                showInfoMessage("Client added");

                fieldName.clear();
                fieldAddress.clear();
                fieldLastName.clear();
            } catch (IOException e)
            {
                showInfoMessage("Something went wrong. Contact the administrator");
            }
        } else
        {
            showInfoMessage("Fill up all the blank spaces");
        }
    }

    public void close(ActionEvent event)
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void showInfoMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message + "\nPress \"Close\" to finish adding clients.", ButtonType.OK, ButtonType.CLOSE);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.CLOSE)
            this.buttonExit.fire();
    }
}
