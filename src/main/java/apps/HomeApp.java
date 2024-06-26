package apps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.Utils;

import java.io.IOException;
import java.util.Objects;

public class HomeApp  extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/home.fxml")));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/app-icon.png")));

        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.setTitle("Gestión de Vehículos");
        stage.setOnCloseRequest(e -> Utils.infoLogger("Cerrando la aplicación..."));

        stage.show();

        Utils.infoLogger("Aplicación iniciada correctamente.");
    }

    public static void main(String[] args) {
        Utils.initMessage();
        launch(args);
    }

}
