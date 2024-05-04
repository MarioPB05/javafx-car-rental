package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Service;
import utils.Utils;

import java.time.LocalDate;

public class ListController implements ControllerInterface {

    public TableColumn<Service, String> colNIF;
    public TableColumn<Service, String> colFullName;
    public TableColumn<Service, String> colPlate;
    public TableColumn<Service, String> colBrand;
    public TableColumn<Service, Integer> colPrice;
    public TableColumn<Service, LocalDate> colStartDate;
    public TableColumn<Service, LocalDate> colEndDate;
    public TableColumn<Service, Integer> colTotalPrice;
    public TableView<Service> tableServices;

    public void initialize() {
        colNIF.setCellValueFactory(serviceStringCellDataFeatures -> {
            if (serviceStringCellDataFeatures.getValue().getClient() != null) {
                return new SimpleStringProperty(serviceStringCellDataFeatures.getValue().getClient().getNIF());
            } else {
                return new SimpleStringProperty("Sin NIF");
            }
        });

        colFullName.setCellValueFactory(serviceStringCellDataFeatures -> {
            if (serviceStringCellDataFeatures.getValue().getClient() != null) {
                return new SimpleStringProperty(serviceStringCellDataFeatures.getValue().getClient().getFullName());
            } else {
                return new SimpleStringProperty("Sin nombre");
            }
        });

        colPlate.setCellValueFactory(serviceStringCellDataFeatures -> {
            if (serviceStringCellDataFeatures.getValue().getCar() != null) {
                return new SimpleStringProperty(serviceStringCellDataFeatures.getValue().getCar().getPlate());
            } else {
                return new SimpleStringProperty("Sin matrícula");
            }
        });

        colBrand.setCellValueFactory(serviceStringCellDataFeatures -> {
            if (serviceStringCellDataFeatures.getValue().getCar() != null) {
                return new SimpleStringProperty(serviceStringCellDataFeatures.getValue().getCar().getBrand());
            } else {
                return new SimpleStringProperty("Sin marca");
            }
        });

        colPrice.setCellValueFactory(serviceIntegerCellDataFeatures -> {
            int price = serviceIntegerCellDataFeatures.getValue().getCar().getPrice();
            return new SimpleIntegerProperty(price).asObject();
        });

        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        ObservableList<Service> services = Service.getServices();

        tableServices.setItems(services);
    }

    @Override
    public Stage getStage() {
        return (Stage) tableServices.getScene().getWindow();
    }

    @Override
    public void closeWindow() {
        Utils.openWindow(Utils.WindowType.HOME, this);
    }

}
