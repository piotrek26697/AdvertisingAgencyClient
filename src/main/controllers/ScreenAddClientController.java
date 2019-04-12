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

    @FXML
    private Label labelOutput;

    private HttpHelper httpHelper;

    private final String URL = Main.URL + "/client";

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        httpHelper = new HttpHelper();
        buttonExit.setOnAction(event -> close(event));
        buttonAdd.setOnAction(event -> {
            addClient();
            fieldName.clear();
            fieldAddress.clear();
            fieldLastName.clear();
        });
    }

    public void addClient()
    {
        if (!fieldName.getText().trim().isEmpty() && !fieldLastName.getText().trim().isEmpty() && !fieldAddress.getText().trim().isEmpty())
        {
            labelOutput.setText("Adding in progress...");
            Client client = new Client(fieldName.getText(), fieldLastName.getText(), fieldAddress.getText());
            StringWriter writer = new StringWriter();
            JAXB.marshal(client, writer);
            try
            {
                httpHelper.doPost(URL, writer.toString(), "application/xml");
                labelOutput.setText("Client added");
            } catch (IOException e)
            {
                labelOutput.setText("Something went wrong.\nContact the administrator");
            }
        } else
        {
            labelOutput.setText("Fill up all the blank spaces");
        }
    }

    public void close(ActionEvent event)
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
