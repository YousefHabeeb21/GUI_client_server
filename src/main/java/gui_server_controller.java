import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class gui_server_controller {

    @FXML
    private Button serverButton;

    @FXML
    private Button clientButton;


    @FXML
    public void initialize() {
        serverButton.setStyle("-fx-pref-width: 150px");
        serverButton.setStyle("-fx-pref-height: 150px");
        serverButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("server.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("Server.css").toExternalForm());
                Stage stage = (Stage) serverButton.getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        clientButton.setStyle("-fx-pref-width: 150px");
        clientButton.setStyle("-fx-pref-height: 150px");
        clientButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Client.fxml"));
                Parent root = loader.load();
                client_controller clientController = loader.getController();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("Client.css").toExternalForm());
                Stage stage = (Stage) clientButton.getScene().getWindow();
                stage.setScene(scene);
//                stage.setOnCloseRequest(event -> {
//                    clientController.closeConnection();
//                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}