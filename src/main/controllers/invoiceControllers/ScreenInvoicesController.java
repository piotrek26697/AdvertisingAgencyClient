package main.controllers.invoiceControllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.model.HttpHelper;
import main.model.Main;
import main.model.collections.Invoices;
import main.model.entities.Client;
import main.model.entities.Invoice;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ScreenInvoicesController implements Initializable
{
    @FXML
    private TableView<Invoice> tableInvoices;

    @FXML
    private TableColumn<Invoice, Integer> columnNumber;

    @FXML
    private TableColumn<Invoice, LocalDate> columnDate;

    @FXML
    private TableColumn<Invoice, Integer> columnTax;

    @FXML
    private TableColumn<Invoice, Double> columnNetto;

    @FXML
    private TableColumn<Invoice, Double> columnBrutto;

    @FXML
    private Label labelTitle;

    @FXML
    private Button buttonDetails;

    private HttpHelper httpHelper;

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        httpHelper = new HttpHelper();

        buttonDetails.setDisable(true);
        buttonDetails.setOnAction(this::showInvoiceDetails);

        tableInvoices.setOnMouseClicked(event -> {
            if(tableInvoices.getSelectionModel().getSelectedItem() != null)
                buttonDetails.setDisable(false);
        });
    }

    private void showInvoiceDetails(ActionEvent actionEvent)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/screenInvoiceDetails.fxml"));
            Parent root = loader.load();
            ScreenInvoiceDetailsController controller = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Details");
            stage.show();
            controller.setInvoice(tableInvoices.getSelectionModel().getSelectedItem());
        }catch (IOException e)
        {
            showMessage("Something went wrong. Contact the administrator.");
        }
    }

    private void showInvoices()
    {
        List<Invoice> list = downloadInvoicesFromDB();
        if(list != null)
        {
            populateTable(list);
        }
        buttonDetails.setDisable(true);
    }

    private void populateTable(List<Invoice> list)
    {
        columnNumber.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnTax.setCellValueFactory(new PropertyValueFactory<>("tax"));
        columnNetto.setCellValueFactory(new PropertyValueFactory<>("amountNetto"));
        columnBrutto.setCellValueFactory(new PropertyValueFactory<>("amountBrutto"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableInvoices.setItems(FXCollections.observableArrayList(list));
    }

    private List<Invoice> downloadInvoicesFromDB()
    {
        try
        {
            final String URL = Main.URL + "/invoice";
            String result = httpHelper.doGet(URL + "?ID=" + client.getId());
            Invoices invoices = JAXB.unmarshal(new StringReader(result), Invoices.class);
            return invoices.getInvoiceList();
        } catch (IOException e)
        {
            showMessage("Something went wrong. Contact the administrator.");
            e.printStackTrace();
            return null;
        }
    }

    private void showMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    public void setClient(Client client)
    {
        this.client = client;
        labelTitle.setText(client.getName() + " " + client.getLastName()+"'s invoices");
        showInvoices();
    }
}
