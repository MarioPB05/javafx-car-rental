package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.ConexionDB;
import utils.Utils;

import java.sql.SQLException;
import java.time.LocalDate;

public class Service {

    private int id;
    private Car car;
    private Client client;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalPrice;

    public Service(int id, Car car, Client client, LocalDate startDate, LocalDate endDate, int totalPrice) {
        this.id = id;
        this.car = car;
        this.client = client;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
    }

    public Car getCar() {
        return car;
    }

    public Client getClient() {
        return client;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "Service " + id;
    }

    public Boolean checkAvailability() {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "SELECT * FROM servicios WHERE matricula_vehiculo = '" + car.getPlate() + "' AND fecha_entrega >= '" + startDate + "' AND fecha_alquiler <= '" + endDate + "'";
            database.ejecutarConsulta(query);

            if (database.getResultSet().next()) {
                Utils.errorLogger("El vehículo no está disponible en las fechas seleccionadas.");
                return false;
            }

            database.cerrarConexion();

            return true;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return false;
        }
    }

    public Boolean save() {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "INSERT INTO servicios (matricula_vehiculo, nif_cliente, fecha_alquiler, fecha_entrega, total) VALUES (" +
                    "'" + car.getPlate() + "', " +
                    "'" + client.getNIF() + "', " +
                    "'" + startDate + "', " +
                    "'" + endDate + "', " +
                    totalPrice + ")";

            int rows = database.ejecutarInstruccion(query);

            if (rows == 0) {
                Utils.errorLogger("No se ha podido guardar el alquiler.");
                return false;
            }

            database.cerrarConexion();

            return true;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return false;
        }
    }

    public static ObservableList<Service> getServices() {
        ObservableList<Service> services = FXCollections.observableArrayList();

        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "SELECT * FROM servicios";
            database.ejecutarConsulta(query);

            while (database.getResultSet().next()) {
                int id = database.getResultSet().getInt("id_servicio");
                String plate = database.getResultSet().getString("matricula_vehiculo");
                String nif = database.getResultSet().getString("nif_cliente");
                LocalDate startDate = database.getResultSet().getDate("fecha_alquiler").toLocalDate();
                LocalDate endDate = database.getResultSet().getDate("fecha_entrega").toLocalDate();
                int total = database.getResultSet().getInt("total");

                Car car = Car.getCar(plate);
                Client client = Client.getClient(nif);

                Service service = new Service(id, car, client, startDate, endDate, total);
                services.add(service);
            }

            database.cerrarConexion();

            return services;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());

            return null;
        }
    }

}
