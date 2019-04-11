package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Client;
import model.HttpHelper;
import model.Main;

import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenAddClientController implements Initializable
{
    @FXML
    private Button buttonReturn;

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

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        httpHelper = new HttpHelper();
        buttonReturn.setOnAction(event -> close(event));
        buttonAdd.setOnAction(event -> addClient());
    }

    public void addClient()
    {
        if (!fieldName.getText().trim().isEmpty() && !fieldLastName.getText().trim().isEmpty() && !fieldAddress.getText().trim().isEmpty())
        {
            Client client = new Client(fieldName.getText(), fieldLastName.getText(), fieldAddress.getText());
            String url = Main.URL + "/client";
            StringWriter writer = new StringWriter();
            JAXB.marshal(client, writer);
            httpHelper.doPost(url, writer.toString(), "application/xml");
            labelOutput.setText("Client added");
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
