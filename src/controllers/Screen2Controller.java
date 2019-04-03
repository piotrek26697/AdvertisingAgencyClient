package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class Screen2Controller implements Initializable, ControlledScreen
{
    @FXML
    private Button returnButton;

    private ScreensController screensController;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        returnButton.setOnAction(event -> {
            goBack();
        });
    }

    private void goBack()
    {
        screensController.setScreen(Main.mainScreenID);
    }

    @Override
    public void setScreenParent(ScreensController parentController)
    {
        screensController = parentController;
    }
}
