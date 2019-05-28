package main.controllers.scheduleDisplayControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.model.BillboardSize;
import main.model.HttpHelper;
import main.model.Main;
import main.model.collections.BillboardOccupations;
import main.model.collections.Billboards;
import main.model.entities.Advertisement;
import main.model.entities.Billboard;
import main.model.entities.BillboardOccupation;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ScreenSetScheduleDisplayController implements Initializable
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
    private Button buttonClearFields;

    @FXML
    private Button buttonShowBillboards;

    @FXML
    private Button buttonAssign;

    @FXML
    private DatePicker pickerDateFrom;

    @FXML
    private DatePicker pickerDateTo;

    @FXML
    private Label labelTitle;

    private Advertisement advertisement;

    private HttpHelper httpHelper;

    private final String URL_BILLBOARD_OCCUPATION = Main.URL + "/billboardOccupation";

    private final String URL_BILLBOARDS = Main.URL + "/billboard";

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        httpHelper = new HttpHelper();

        buttonShowBillboards.setOnAction(event -> showBillboards());

        buttonClearFields.setOnAction(event -> clearFields());

        buttonAssign.setOnAction(event -> assignDisplay());

        tableBillboards.setOnMouseClicked(event -> {
            if (tableBillboards.getSelectionModel().getSelectedItem() != null)
            {
                pickerDateTo.setValue(null);
                pickerDateFrom.setValue(null);

                disableStartingDates();
                pickerDateTo.setDisable(true);
                pickerDateFrom.setDisable(false);
                buttonAssign.setDisable(true);
            }
        });
        pickerDateFrom.setOnAction(event -> {
            pickerDateTo.setDisable(false);
            disableEndingDates();
        });
        pickerDateFrom.setDisable(true);
        pickerDateTo.setOnAction(event -> buttonAssign.setDisable(false));
        pickerDateTo.setDisable(true);

        buttonAssign.setDisable(true);
    }

    private void disableEndingDates()
    {
        Billboard billboard = tableBillboards.getSelectionModel().getSelectedItem();
        List<BillboardOccupation> occupationList = downloadOccupationListFromDB(billboard.getId());

        boolean test = false;
        if (occupationList != null)
            for (BillboardOccupation occupation : occupationList)
                if (pickerDateFrom.getValue().compareTo(occupation.getDateFrom()) < 0)
                    test = true;
        boolean finalTest = test;
        pickerDateTo.setDayCellFactory(callback -> new DateCell()
        {
            @Override
            public void updateItem(LocalDate item, boolean empty)
            {
                super.updateItem(item, empty);

                if (finalTest)
                {
                    if (occupationList != null)
                    {
                        for (BillboardOccupation occupation : occupationList)
                        {
                            if (item.compareTo(pickerDateFrom.getValue()) >= 0 && item.compareTo(occupation.getDateFrom()) < 0)
                            {
                                setDisable(false);
                                setStyle("-fx-background-color: #FFFFFF;");
                            } else
                            {
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    }
                } else
                {
                    if (item.compareTo(pickerDateFrom.getValue()) < 0)
                    {
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;");
                    }
                }
            }
        });
    }

    private void disableStartingDates()
    {
        Billboard billboard = tableBillboards.getSelectionModel().getSelectedItem();
        List<BillboardOccupation> occupationList = downloadOccupationListFromDB(billboard.getId());

        pickerDateFrom.setDayCellFactory(param -> new DateCell()
        {
            @Override
            public void updateItem(LocalDate item, boolean empty)
            {
                super.updateItem(item, empty);

                setDisable(empty || item.compareTo(LocalDate.now()) < 0);
                setStyle("-fx-background-color: #FFFFFF;");
                if (occupationList != null)
                {
                    for (BillboardOccupation occupation : occupationList)
                    {
                        if (item.compareTo(occupation.getDateFrom()) >= 0 && item.compareTo(occupation.getDateTo()) <= 0)
                        {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                            break;
                        }
                    }
                }
            }
        });
    }

    private void assignDisplay()
    {
        if (pickerDateFrom.getValue() == null || pickerDateTo.getValue() == null)
        {
            showMessage("Please enter dates");
        } else
        {

            BillboardOccupation billboardOccupation = new BillboardOccupation();
            billboardOccupation.setAdvertisement(advertisement);
            billboardOccupation.setBillboard(tableBillboards.getSelectionModel().getSelectedItem());
            billboardOccupation.setDateFrom(pickerDateFrom.getValue());
            billboardOccupation.setDateTo(pickerDateTo.getValue());

            StringWriter sw = new StringWriter();
            JAXB.marshal(billboardOccupation, sw);
            try
            {
                httpHelper.doPost(URL_BILLBOARD_OCCUPATION, sw.toString(), "application/xml");
                showMessage("Scheduled successfully");
            } catch (IOException e)
            {
                showMessage("Something went wrong. Contact the administrator.");
            }
            showBillboards();
        }
    }

    private void clearFields()
    {
        fieldAddress.clear();
        boxSize.getSelectionModel().clearSelection();
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

        buttonAssign.setDisable(true);
        pickerDateFrom.setDisable(true);
        pickerDateTo.setDisable(true);
    }

    private List<BillboardOccupation> downloadOccupationListFromDB(int id)
    {
        String params = URL_BILLBOARD_OCCUPATION + "?ID=" + id + "&type=billboardID&enableHistory=false";
        try
        {
            String result = httpHelper.doGet(params);
            BillboardOccupations occupations = JAXB.unmarshal(new StringReader(result), BillboardOccupations.class);
            return occupations.getList();
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private List<Billboard> downloadBillboardListFromDB()
    {
        try
        {
            String params = URL_BILLBOARDS + "?address=" + fieldAddress.getText().trim() + "&size=";
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

    public void setAdvertisement(Advertisement advertisement)
    {
        this.advertisement = advertisement;
        labelTitle.setText("Setting schedule for " + advertisement.getName() + " advertisement.");
    }
}
