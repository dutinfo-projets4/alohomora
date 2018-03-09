package alohomora;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/connection.fxml"));
		primaryStage.setMinWidth(768);
		primaryStage.setMinHeight(453);
		primaryStage.setTitle("Alohomora");

		Font.loadFont(getClass().getClassLoader().getResource("assets/hack.ttf").toExternalForm(), 15);
		Font.loadFont(getClass().getClassLoader().getResource("assets/hack_bold.ttf").toExternalForm(), 15);

		Scene scene = new Scene(root);
		scene.getStylesheets().addAll("assets/css/main.css", "assets/css/connection.css");
		primaryStage.setScene(scene);
		primaryStage.show();
		/**
		 Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/interface.fxml"));
		 primaryStage.setMinWidth(768);
		 primaryStage.setMinHeight(453);
		 primaryStage.setTitle("Alohomora");

		 Font.loadFont(getClass().getClassLoader().getResource("assets/hack.ttf").toExternalForm(), 15);
		 Font.loadFont(getClass().getClassLoader().getResource("assets/hack_bold.ttf").toExternalForm(), 15);

		 Scene scene = new Scene(root);
		 scene.getStylesheets().addAll("assets/css/main.css", "assets/css/interface.css");
		 primaryStage.setScene(scene);
		 primaryStage.show();
		 **/
	}


	public static void main(String[] args) {
		launch(args);
	}
}
