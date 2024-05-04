package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.ConexionDB;
import utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {

    private String NIF;
    private String town;
    private String address;
    private String fullName;

    public Client(String NIF, String town, String address, String fullName) {
        this.NIF = NIF;
        this.town = town;
        this.address = address;
        this.fullName = fullName;
    }

    public String getNIF() {
        return NIF;
    }

    public String getTown() {
        return town;
    }

    public String getAddress() {
        return address;
    }

    public String getFullName() {
        return fullName;
    }

    public static ObservableList<Client> getClients() throws SQLException {
        ObservableList<Client> clients = FXCollections.observableArrayList();

        ConexionDB database = Utils.getDatabaseConnection();

        database.ejecutarConsulta("SELECT * FROM clientes");

        ResultSet result = database.getResultSet();

        while(result.next()) {
            Client client = new Client(
                    result.getString("NIF"),
                    result.getString("Poblacion"),
                    result.getString("Direcion"),
                    result.getString("NyA")
            );

            clients.add(client);
        }

        database.cerrarConexion();

        return clients;
    }

    public static Client getClient(String NIF) throws SQLException {
        ConexionDB database = Utils.getDatabaseConnection();
        database.ejecutarConsulta("SELECT * FROM clientes WHERE NIF = '" + NIF + "'");
        ResultSet result = database.getResultSet();
        result.next();
        Client client = new Client(
                result.getString("NIF"),
                result.getString("Poblacion"),
                result.getString("Direcion"),
                result.getString("NyA")
        );
        database.cerrarConexion();
        return client;
    }

    @Override
    public String toString() {
        return fullName;
    }

}
