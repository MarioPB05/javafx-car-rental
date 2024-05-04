package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Car;
import model.Client;
import model.Service;
import utils.Utils;

import java.time.LocalDate;
import java.time.Period;

public class RegisterController implements ControllerInterface {

    @FXML
    private TextField carBrand;

    @FXML
    private TextField carDescription;

    @FXML
    private TextField carKilometres;

    @FXML
    private TextField carPrice;

    @FXML
    private ComboBox<Car> carsDropdown;

    @FXML
    private TextField clientAddress;

    @FXML
    private TextField clientFullName;

    @FXML
    private TextField clientNIF;

    @FXML
    private TextField clientTown;

    @FXML
    private ComboBox<Client> clientsDropdown;

    @FXML
    private DatePicker endDate;

    @FXML
    private DatePicker startDate;

    @FXML
    private TextField totalPrice;

    public void initialize() {
        setMinDate();
        initializeDropdowns();
    }

    private void initializeDropdowns() {
        Utils.warningLogger("Inicializando dropdowns...");

        try {
            ObservableList<Car> cars = Car.getCars();
            carsDropdown.setItems(cars);

            ObservableList<Client> clients = Client.getClients();
            clientsDropdown.setItems(clients);

            Utils.infoLogger("Datos de los vehículos y clientes cargados correctamente.");
        } catch (Exception e) {
            Utils.errorLogger("Error al cargar los datos de los vehículos y clientes.");
        }
    }

    private void setMinDate() {
        LocalDate minDate = LocalDate.now();
        startDate.setValue(minDate);

        final Callback<DatePicker, DateCell> dayCellFactory;

        dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isBefore(minDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ff5353;-fx-text-fill: white;");
                }
            }
        };

        startDate.setDayCellFactory(dayCellFactory);
        endDate.setDayCellFactory(dayCellFactory);
    }

    @Override
    public Stage getStage() {
        return (Stage) carsDropdown.getScene().getWindow();
    }

    public void closeWindow() {
        Utils.openWindow(Utils.WindowType.HOME, this);
    }

    public void selectCar() {
        Car selectedCar = carsDropdown.getValue();

        carBrand.setText(selectedCar.getBrand());
        carDescription.setText(selectedCar.getDescription());
        carKilometres.setText(String.valueOf(selectedCar.getKm()));
        carPrice.setText(String.valueOf(selectedCar.getPrice()));

        reloadRentPrice();
    }

    public void selectClient() {
        Client selectedClient = clientsDropdown.getValue();

        clientFullName.setText(selectedClient.getFullName());
        clientNIF.setText(selectedClient.getNIF());
        clientAddress.setText(selectedClient.getAddress());
        clientTown.setText(selectedClient.getTown());
    }

    public void reloadRentPrice() {
        LocalDate selectedStartDate = startDate.getValue();
        LocalDate selectedEndDate = endDate.getValue();
        Car selectedCar = carsDropdown.getValue();

        if (selectedStartDate == null || selectedEndDate == null || selectedCar == null) {
            return;
        }

        // Si la fecha de inicio es mayor que la fecha de fin, se pone el precio a 0.
        if (selectedStartDate.isAfter(selectedEndDate)) {
            totalPrice.setText("0");
            return;
        }

        long days = Period.between(selectedStartDate, selectedEndDate).getDays();
        int price = Integer.parseInt(carPrice.getText());
        int total = price * (int) days;

        totalPrice.setText(String.valueOf(total));
    }

    private Boolean validate() {
        if (carsDropdown.getValue() == null) {
            Utils.showAlert("Error", "Debe seleccionar un vehículo.", Alert.AlertType.ERROR);
            return false;
        }

        if (clientsDropdown.getValue() == null) {
            Utils.showAlert("Error", "Debe seleccionar un cliente.", Alert.AlertType.ERROR);
            return false;
        }

        if (startDate.getValue() == null) {
            Utils.showAlert("Error", "Debe seleccionar una fecha de inicio.", Alert.AlertType.ERROR);
            return false;
        }

        if (endDate.getValue() == null) {
            Utils.showAlert("Error", "Debe seleccionar una fecha de fin.", Alert.AlertType.ERROR);
            return false;
        }

        if (startDate.getValue().isAfter(endDate.getValue())) {
            Utils.showAlert("Error", "La fecha de inicio no puede ser mayor que la fecha de fin.", Alert.AlertType.ERROR);
            return false;
        }

        if (totalPrice.getText().isEmpty()) {
            Utils.showAlert("Error", "El precio total no puede estar vacío.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    public void save() {
        if (!validate()) {
            return;
        }

        Car selectedCar = carsDropdown.getValue();
        Client selectedClient = clientsDropdown.getValue();
        LocalDate selectedStartDate = startDate.getValue();
        LocalDate selectedEndDate = endDate.getValue();
        int total = Integer.parseInt(totalPrice.getText());

        try {
            Service service = new Service(0, selectedCar, selectedClient, selectedStartDate, selectedEndDate, total);

            if (!service.checkAvailability()) {
                Utils.showAlert("Error", "El vehículo no está disponible en las fechas seleccionadas.", Alert.AlertType.ERROR);
                return;
            }

            if (service.save()) {
                Utils.showAlert("Alquiler guardado", "El alquiler se ha guardado correctamente.", Alert.AlertType.INFORMATION);
                closeWindow();
            } else {
                Utils.showAlert("Error al guardar", "No se ha podido guardar el alquiler.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            Utils.errorLogger("Error al guardar el alquiler. Error: " + e.getMessage());
            Utils.showAlert("Error al guardar", "No se ha podido guardar el alquiler.", Alert.AlertType.ERROR);
        }
    }
}
