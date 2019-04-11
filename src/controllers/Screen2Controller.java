package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class Screen2Controller implements Initializable
{
    @FXML
    private Button returnButton;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        returnButton.setOnAction(event -> {
            //goBack();
        });
    }
}
