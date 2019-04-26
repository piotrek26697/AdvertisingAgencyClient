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
import main.model.BillboardSize;
import main.model.HttpHelper;
import main.model.Main;
import main.model.collections.Billboards;
import main.model.entities.Billboard;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScreenBillboardsController implements Initializable
{
    @FXML
    private TableView<Billboard> tableBillboards;

    @FXML
    private TableColumn<Billboard, String> columnAddress;

    @FXML
    private TableColumn<Billboard, BillboardSize> columnSize;

    @FXML
    private TextField fieldAddress;

    @FXML
    private ComboBox<BillboardSize> boxSize;

    @FXML
    private Button buttonMenu;

    @FXML
    private Button buttonDetails;

    @FXML
    private Button buttonClearFields;

    @FXML
    private Button buttonShowBillboards;

    @FXML
    private Button buttonAddBillboard;

    private HttpHelper httpHelper;

    private final String URL = Main.URL + "/billboard";

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        httpHelper = new HttpHelper();

        buttonMenu.setOnAction(this::goToMenu);

        buttonClearFields.setOnAction(event -> clearFields());

        buttonDetails.setDisable(true);

        buttonShowBillboards.setOnAction(event -> showBillboards());

        boxSize.getItems().addAll(BillboardSize.values());

        buttonAddBillboard.setOnAction(this::addBillboardWindow);

        tableBillboards.setOnMouseClicked(event -> {
            if (tableBillboards.getSelectionModel().getSelectedItem() != null)
            {
                buttonDetails.setDisable(false);
            }
        });
    }

    private void addBillboardWindow(ActionEvent actionEvent)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/screenAddBillboard.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Add billboard");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void showBillboards()
    {
        List<Billboard> list = downloadBillboardListFromDB();
        if (list != null)
        {
            if (list.size() > 0)
            {
                populateTableBillboards(list);
            } else
                showMessage("There are no billboards in database.");
        } else
            tableBillboards.getItems().clear();

        buttonDetails.setDisable(true);
    }

    private List<Billboard> downloadBillboardListFromDB()
    {
        try
        {
            String params = URL + "?address=" + fieldAddress.getText().trim() + "&size=";
            if (boxSize.getSelectionModel().getSelectedItem() != null)
                params += boxSize.getSelectionModel().getSelectedItem().toString();

            String result = httpHelper.doGet(params);
            Billboards billboards = JAXB.unmarshal(new StringReader(result), Billboards.class);
            return billboards.getBillboardList();
        } catch (IOException e)
        {
            this.showMessage("Something went wrong. Contact the administrator.");
            return null;
        }
    }

    private void showMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void populateTableBillboards(List<Billboard> list)
    {
        columnAddress.setCellValueFactory(new PropertyValueFactory<Billboard, String>("address"));
        columnSize.setCellValueFactory(new PropertyValueFactory<Billboard, BillboardSize>("billboardSize"));
        tableBillboards.setItems(FXCollections.observableArrayList(list));
    }

    private void clearFields()
    {
        fieldAddress.clear();
        boxSize.getSelectionModel().clearSelection();
    }

    private void goToMenu(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/screenStart.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Advertising agency");
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
