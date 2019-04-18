package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import main.model.entities.Advertisement;
import main.model.entities.Client;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ScreenAdvertisementsController implements Initializable
{
    @FXML
    private TableView<Advertisement> tableAdvertisements;

    @FXML
    private TableColumn<Advertisement, String> columnDescription;

    @FXML
    private TableColumn<Advertisement, Calendar> columnDateFrom;

    @FXML
    private TableColumn<Advertisement, Calendar> columnDateTo;

    @FXML
    private TableColumn<Advertisement, Double> columnPrice;

    @FXML
    private Label labelTitle;

    @FXML
    private Button buttonMenu;

    @FXML
    private Button buttonAddAdvertisement;

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        buttonAddAdvertisement.setOnAction(event -> addAdvertisementWindow());

        buttonMenu.setOnAction(event -> goToMenu(event));

        if (client == null)
        {
            labelTitle.setText("All advertisements");
        } else
        {
            labelTitle.setText("Advertisements for " + client.getName() + " " + client.getLastName());
        }
    }

    private void addAdvertisementWindow()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/screenAddAdvertisement.fxml"));
            Parent root = loader.load();
            ScreenAddAdvertisementController controller = loader.getController();
            controller.setClient(client);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Add advertisement");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void goToMenu(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/screenStart.fxml"));
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

    public void setClient(Client client)
    {
        this.client = client;
    }
}
