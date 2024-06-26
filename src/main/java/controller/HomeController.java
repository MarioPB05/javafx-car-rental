package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.Utils;

public class HomeController implements ControllerInterface {

    public Button btnRegister;

    @FXML
    private void openRegisterWindow() {
        Utils.openWindow(Utils.WindowType.REGISTER, this);
    }

    @FXML
    public void openListWindow() {
        Utils.openWindow(Utils.WindowType.LIST, this);
    }

    @Override
    public Stage getStage() {
        return (Stage) btnRegister.getScene().getWindow();
    }

    @Override
    public void closeWindow() {
        // Al cerrar la ventana de inicio, no se realiza ninguna acción. Se cierra la aplicación.
    }

}
