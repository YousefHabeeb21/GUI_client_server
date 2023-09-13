import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class client_controller {
    @FXML
    TextField c1, c2,c5;
    String list;
    Client clientConnection;
    @FXML
    ListView<String> listItems2;
    public void initialize(){
        clientConnection = new Client(data->{
            Platform.runLater(()->{listItems2.getItems().add(data.toString());
                transfer(clientConnection.getList());
            });
        });
        clientConnection.start();
    }

    @FXML
    public void send()
    {
        String recipient = c2.getText();
        String message = c1.getText();
        String all = message +  ":" + recipient;

        // messages will only be sent if the recipients are typed properly
        if (recipient.equalsIgnoreCase("All") || recipient.equalsIgnoreCase("ALL") || recipient.equalsIgnoreCase("all") || recipient.matches("^[0-9]+(,[0-9]+)*$")) {
            if (recipient.length() != 0) {
                clientConnection.send(all);
                c1.clear();
                c2.clear();
            }
        }
    }

    public void transfer(String list ){
        c5.clear();
        c5.setText(list);
    }
}
