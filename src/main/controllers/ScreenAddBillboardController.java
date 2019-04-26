package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.model.BillboardSize;
import main.model.HttpHelper;
import main.model.Main;
import main.model.entities.Billboard;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenAddBillboardController implements Initializable
{
    @FXML
    private TextField fieldAddress;

    @FXML
    private ComboBox<BillboardSize> boxSize;

    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonAdd;

    private HttpHelper httpHelper;

    private final String URL = Main.URL + "/billboard";

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        httpHelper = new HttpHelper();

        buttonExit.setOnAction(this::close);

        buttonAdd.setDisable(true);
        buttonAdd.setOnAction(event -> addBillboard());

        fieldAddress.setOnKeyReleased(event -> buttonAddActivator());

        boxSize.getItems().addAll(BillboardSize.values());
        boxSize.valueProperty().addListener((observable, oldValue, newValue) -> buttonAddActivator());
    }

    private void addBillboard()
    {
        Billboard billboard = new Billboard();
        billboard.setAddress(fieldAddress.getText().trim());
        billboard.setBillboardSize(boxSize.getSelectionModel().getSelectedItem());
        StringWriter sw = new StringWriter();
        JAXB.marshal(billboard, sw);
        try
        {
            httpHelper.doPost(URL, sw.toString(), "application/xml");
            showMessage("Billboard added");

            fieldAddress.clear();
            boxSize.getSelectionModel().clearSelection();
            buttonAdd.setDisable(true);

        } catch (IOException e)
        {
            showMessage("Something went wrong. Contact the administrator.");
        }
    }

    private void buttonAddActivator()
    {
        if (!fieldAddress.getText().trim().isEmpty() && boxSize.getSelectionModel().getSelectedItem() != null)
            buttonAdd.setDisable(false);
        else
            buttonAdd.setDisable(true);
    }

    private void close(ActionEvent actionEvent)
    {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}
