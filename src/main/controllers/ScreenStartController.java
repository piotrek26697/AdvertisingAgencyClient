package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenStartController implements Initializable
{
    @FXML
    private Button buttonLoadClientsView;

    @FXML
    private Button buttonLoadAdvertisementsView;

    @FXML
    private Button buttonLoadBillboardsView;

    @FXML
    private Button buttonLoadInvoicesView;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        buttonLoadAdvertisementsView.setDisable(true);
        buttonLoadAdvertisementsView.setOnAction(this::loadAdvertisementsView);

        buttonLoadBillboardsView.setOnAction(this::loadBillboardsView);

        buttonLoadClientsView.setOnAction(this::loadClientsView);

        buttonLoadInvoicesView.setDisable(true);
        buttonLoadInvoicesView.setOnAction(this::loadInvoicesView);
    }

    private void loadInvoicesView(ActionEvent event)
    {

    }

    private void loadClientsView(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/screenClients.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Managing clients");
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadBillboardsView(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/screenBillboards.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Managing billboards");
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadAdvertisementsView(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/screenAdvertisements.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Managing advertisements");
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
