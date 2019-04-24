package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.model.HttpHelper;
import main.model.Main;
import main.model.entities.Advertisement;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenEditAdvertisementController implements Initializable
{
    @FXML
    private Button buttonUpdate;

    @FXML
    private TextField fieldTitle;

    @FXML
    private TextField fieldPrice;

    @FXML
    private TextArea fieldDescription;

    private final String URL = Main.URL + "/advertisement";

    private HttpHelper httpHelper;

    private ScreenAdvertisementsController controller;

    private Advertisement advertisement;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        httpHelper = new HttpHelper();

        buttonUpdate.setDisable(true);
        buttonUpdate.setOnAction(this::updateAdvertisement);

        fieldTitle.setOnKeyReleased(event -> {
            if (!fieldTitle.getText().equals(advertisement.getName()) && !fieldTitle.getText().trim().isEmpty())
                buttonUpdate.setDisable(false);
            else
                buttonUpdate.setDisable(true);
        });

        fieldPrice.setOnKeyReleased(event -> {
            if (!fieldPrice.getText().equals(Double.toString(advertisement.getPrice())) && !fieldPrice.getText().trim().isEmpty())
                buttonUpdate.setDisable(false);
            else
                buttonUpdate.setDisable(true);
        });

        fieldDescription.setOnKeyReleased(event -> {
            if (!fieldDescription.getText().equals(advertisement.getDescription()) && !fieldDescription.getText().trim().isEmpty())
                buttonUpdate.setDisable(false);
            else
                buttonUpdate.setDisable(true);
        });
    }

    private void updateAdvertisement(ActionEvent actionEvent)
    {
        if (fieldPrice.getText().matches("[1-9]+\\d*(\\.?\\d{1,2})?|0|0\\.[0-9]{1,2}"))
        {
            advertisement.setName(fieldTitle.getText().trim());
            advertisement.setPrice(Double.parseDouble(fieldPrice.getText().trim()));
            advertisement.setDescription(fieldDescription.getText().trim());

            StringWriter sw = new StringWriter();
            JAXB.marshal(advertisement, sw);
            try
            {
                httpHelper.doPut(URL, sw.toString(), "application/xml");
                buttonUpdate.setDisable(true);
                showMessage("Advertisement updated successfully.");
                controller.showAdvertisements();
            } catch (IOException e)
            {
                e.printStackTrace();
                showMessage("Something went wrong. Contact the administrator.");
            }
        } else
        {
            this.showMessage("Wrong price. Make sure to use dot as a separator.");
        }
    }

    private void showMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    public void setController(ScreenAdvertisementsController controller)
    {
        this.controller = controller;
    }

    public void setAdvertisement(Advertisement advertisement)
    {
        this.advertisement = advertisement;
        fieldDescription.setText(advertisement.getDescription());
        fieldPrice.setText(Double.toString(advertisement.getPrice()));
        fieldTitle.setText(advertisement.getName());
    }
}
