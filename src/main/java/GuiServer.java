import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public class GuiServer extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	static ArrayList<Integer>clientNumber = new ArrayList<>();
	static javafx.scene.control.TextArea textArea;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GuiServer.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 500, 500);

		// Load the CSS stylesheet
		scene.getStylesheets().add(getClass().getResource("GuiServer.css").toExternalForm());

		primaryStage.setTitle("Program 4");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}