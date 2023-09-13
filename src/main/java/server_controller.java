import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class server_controller {

    Server serverConnection;
    @FXML
    ListView<String> listItems;
    public void initialize(){
        serverConnection = new Server(data -> {
            Platform.runLater(()->{
                listItems.getItems().add(data.toString());
            });
        });
    }

}
