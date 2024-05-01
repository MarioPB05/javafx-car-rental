package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.Utils;

public class HomeController implements ControllerInterface {

    public Button btnRegister;

    @FXML
    private void openRegisterWindow() {
        Utils.openWindow(Utils.WindowType.HOME, this);
    }

    @Override
    public Stage getStage() {
        return (Stage) btnRegister.getScene().getWindow();
    }

    @Override
    public void closeWindows() {
        // Al cerrar la ventana de inicio, no se realiza ninguna acción. Se cierra la aplicación.
    }

}
