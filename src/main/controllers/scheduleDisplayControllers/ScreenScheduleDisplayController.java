package main.controllers.scheduleDisplayControllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import main.model.HttpHelper;
import main.model.Main;
import main.model.collections.BillboardOccupations;
import main.model.entities.Advertisement;
import main.model.entities.Billboard;
import main.model.entities.BillboardOccupation;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ScreenScheduleDisplayController implements Initializable
{
    @FXML
    private TableView<BillboardOccupation> table;

    @FXML
    private TableColumn<BillboardOccupation, String> columnName;

    @FXML
    private TableColumn<BillboardOccupation, String> columnAddress;

    @FXML
    private TableColumn<BillboardOccupation, LocalDate> columnDateFrom;

    @FXML
    private TableColumn<BillboardOccupation, LocalDate> columnDateTo;

    private final String URL = Main.URL + "/billboardOccupation";

    private Advertisement advertisement;

    private HttpHelper httpHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        httpHelper = new HttpHelper();
    }

    private void showSchedule()
    {
        table.getItems().clear();
        List<BillboardOccupation> list = downloadBillboardOccupationListFromDB();
        if (list != null)
        {
            if (list.size() > 0)
            {
                populateTableBillboardOccupation(list);
            } else
                showMessage("There are no billboards in database.");
        }
    }

    private List<BillboardOccupation> downloadBillboardOccupationListFromDB()
    {
        try
        {
            String result = httpHelper.doGet(URL + "?id=" + advertisement.getId());
            BillboardOccupations billboardOccupations = JAXB.unmarshal(new StringReader(result), BillboardOccupations.class);
            return billboardOccupations.getList();
        } catch (IOException e)
        {
            showMessage("Something went wrong. Contact the administrator.");
            return null;
        }
    }

    private void showMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void populateTableBillboardOccupation(List<BillboardOccupation> list)
    {
        columnAddress.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BillboardOccupation, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BillboardOccupation, String> param)
            {
                return new SimpleStringProperty(param.getValue().getBillboard().getAddress());
            }
        });
        columnName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BillboardOccupation, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BillboardOccupation, String> param)
            {
                return new SimpleStringProperty(param.getValue().getAdvertisement().getName());
            }
        });
        columnDateFrom.setCellValueFactory(new PropertyValueFactory<>("dateFrom"));
        columnDateTo.setCellValueFactory(new PropertyValueFactory<>("dateTo"));
        table.setItems(FXCollections.observableArrayList(list));
    }

    public void setAdvertisement(Advertisement advertisement)
    {
        this.advertisement = advertisement;
        showSchedule();
    }
}
