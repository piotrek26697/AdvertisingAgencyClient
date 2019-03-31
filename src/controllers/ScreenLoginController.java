package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class ScreenLoginController implements Initializable, ControlledScreen
{
    @FXML
    private Button loginButton;

    @FXML
    private TextField login;

    private ScreensController screensController;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        loginButton.setOnAction((event) -> {
            login();
        });


    }

    private void login()
    {
        screensController.setScreen(Main.screen2ID);
    }

    @Override
    public void setScreenParent(ScreensController screenParent)
    {
        screensController = screenParent;
    }
}
