package main.controllers.invoiceControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.model.HttpHelper;
import main.model.Main;
import main.model.collections.Advertisements;
import main.model.entities.Advertisement;
import main.model.entities.Invoice;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScreenInvoiceDetailsController implements Initializable
{
    @FXML
    private TableView<Advertisement> tableAdvertisements;

    @FXML
    private TableColumn<Advertisement, String> columnName;

    @FXML
    private TableColumn<Advertisement, Double> columnPrice;

    @FXML
    private TableColumn<Advertisement, String> columnDescription;

    @FXML
    private Label labelTitle;

    private HttpHelper httpHelper;

    private Invoice invoice;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        httpHelper = new HttpHelper();
    }

    private void showInvoiceDetails()
    {
        List<Advertisement> list = downloadInvoiceDetailsFromDB();
        if(list!= null)
        {
            populateTable(list);
        }
    }

    private void populateTable(List<Advertisement> list)
    {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableAdvertisements.setItems(FXCollections.observableArrayList(list));
    }

    private List<Advertisement> downloadInvoiceDetailsFromDB()
    {
        final String URL = Main.URL+"/invoice/ads?ID="+invoice.getId();
        try
        {
            String result = httpHelper.doGet(URL);
            Advertisements ads = JAXB.unmarshal(new StringReader(result), Advertisements.class);
            return ads.getAdsList();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setInvoice(Invoice invoice)
    {
        this.invoice = invoice;
        labelTitle.setText("Invoice number " + invoice.getId() + " details");
        showInvoiceDetails();
    }
}
