package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.ConexionDB;
import utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Car {

    private int km;
    private int price;
    private String brand;
    private String plate;
    private String description;

    public Car(int km, int price, String brand, String plate, String description) {
        this.km = km;
        this.price = price;
        this.brand = brand;
        this.plate = plate;
        this.description = description;
    }

    public int getKm() {
        return km;
    }

    public int getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getPlate() {
        return plate;
    }

    public String getDescription() {
        return description;
    }

    public static ObservableList<Car> getCars() throws SQLException {
        ObservableList<Car> cars = FXCollections.observableArrayList();

        ConexionDB database = Utils.getDatabaseConnection();

        database.ejecutarConsulta("SELECT * FROM vehiculos");

        ResultSet result = database.getResultSet();

        while(result.next()) {
            Car car = new Car(
                    result.getInt("kilometros"),
                    result.getInt("precio"),
                    result.getString("marca"),
                    result.getString("matricula"),
                    result.getString("descripcion")
            );

            cars.add(car);
        }

        database.cerrarConexion();

        return cars;
    }

    @Override
    public String toString() {
        return plate;
    }

}
