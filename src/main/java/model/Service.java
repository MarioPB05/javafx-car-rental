package model;

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
    private double totalPrice;

    public Service(int id, Car car, Client client, LocalDate startDate, LocalDate endDate, double totalPrice) {
        this.id = id;
        this.car = car;
        this.client = client;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Service " + id;
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

}
