package main.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.model.HttpHelper;
import main.model.Main;
import main.model.collections.Advertisements;
import main.model.entities.Advertisement;
import main.model.entities.Client;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScreenAdvertisementsController implements Initializable
{
    @FXML
    private TableView<Advertisement> tableAdvertisements;

    @FXML
    private TableColumn<Advertisement, String> columnTitle;

    @FXML
    private TableColumn<Advertisement, String> columnDescription;

    @FXML
    private TableColumn<Advertisement, Double> columnPrice;

    @FXML
    private Label labelTitle;

    @FXML
    private Button buttonMenu;

    @FXML
    private Button buttonAddAdvertisement;

    @FXML
    private Button buttonDeleteAdvertisement;

    @FXML
    private Button buttonEditAdvertisement;

    @FXML
    private Button buttonFilter;

    @FXML
    private TextField fieldTitle;

    @FXML
    private TextField fieldPriceFrom;

    @FXML
    private TextField fieldPriceTo;

    private Client client;

    private HttpHelper httpHelper;

    private final String URL_CLIENT = Main.URL + "/client";

    private final String URL_ADVERTISEMENT = Main.URL + "/advertisement";

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        httpHelper = new HttpHelper();

        buttonAddAdvertisement.setOnAction(event -> addAdvertisementWindow());

        buttonEditAdvertisement.setOnAction(this::editAdvertisementWindow);

        buttonMenu.setOnAction(this::goToMenu);

        labelTitle.setText("All advertisements");

        buttonDeleteAdvertisement.setDisable(true);
        buttonDeleteAdvertisement.setOnAction(this::deleteAdvertisement);
        buttonEditAdvertisement.setDisable(true);

        tableAdvertisements.setOnMouseClicked(event ->
        {
            if (tableAdvertisements.getSelectionModel().getSelectedItem() != null)
            {
                buttonDeleteAdvertisement.setDisable(false);
                buttonEditAdvertisement.setDisable(false);
            }
        });
    }

    private void editAdvertisementWindow(ActionEvent actionEvent)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/screenEditAdvertisement.fxml"));
            Parent root = loader.load();
            ScreenEditAdvertisementController controller = loader.getController();
            controller.setAdvertisement(tableAdvertisements.getSelectionModel().getSelectedItem());
            controller.setController(this);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit advertisement");
            stage.show();
        } catch (IOException e)
        {
            showMessage("Something went wrong. Contact the administrator.");
        }
    }

    private void deleteAdvertisement(ActionEvent actionEvent)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete selected advertisement?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES)
        {
            try
            {
                Advertisement ad = tableAdvertisements.getSelectionModel().getSelectedItem();
                httpHelper.doDelete(URL_ADVERTISEMENT + "?advertisementID=" + ad.getId());
                showAdvertisements();
            } catch (IOException e)
            {
                e.printStackTrace();
                this.showMessage("Something went wrong. Contact the administrator.");
            }
        }
    }

    public void showAdvertisements()
    {
        int id = client.getId();
        List<Advertisement> adsList = downloadAdvertisementsDB(id);
        if (adsList != null)
        {
            if (adsList.size() > 0)
            {
                populateAdsTable(adsList);
            } else
                showMessage("No ads to display");
        } else
            tableAdvertisements.getItems().clear();

        buttonDeleteAdvertisement.setDisable(true);
        buttonEditAdvertisement.setDisable(true);
    }

    private void populateAdsTable(List<Advertisement> adsList)
    {
        columnTitle.setCellValueFactory(new PropertyValueFactory<Advertisement, String>("name"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<Advertisement, String>("description"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<Advertisement, Double>("price"));
        tableAdvertisements.setItems(FXCollections.observableArrayList(adsList));
    }

    private List<Advertisement> downloadAdvertisementsDB(int id)
    {
        try
        {
            String result = httpHelper.doGet(URL_CLIENT + "/advertisementList?clientID=" + client.getId());
            Advertisements ads = JAXB.unmarshal(new StringReader(result), Advertisements.class);
            return ads.getAdsList();
        } catch (IOException e)
        {
            showMessage("Something went wrong. Contact the administrator.");
            return null;
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
            controller.setAdvertisementsController(this);
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
        labelTitle.setText(client.getName() + " " + client.getLastName() + "'s advertisements");
        fieldPriceFrom.setDisable(true);
        fieldPriceTo.setDisable(true);
        fieldTitle.setDisable(true);
        buttonFilter.setDisable(true);
        showAdvertisements();
    }

    private void showMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}
