package fr.alohomora;

import fr.alohomora.model.apiservice.Api;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Alohomora Password Manager
 * Copyright (C) 2018 Team Alohomora
 * Léo BERGEROT, Sylvain COMBRAQUE, Sarah LAMOTTE, Nathan JANCZEWSKI
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 **/
public class App extends Application {

	private static App _INSTANCE;
	private Api api;
	private static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {

		App._INSTANCE = this;
		this.api = new Api();

		//Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/connection.fxml"));
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/interface.fxml"));
		primaryStage.setMinWidth(768);
		primaryStage.setMinHeight(453);
		primaryStage.setTitle("Alohomora");

		Font.loadFont(getClass().getClassLoader().getResource("assets/hf.ttf").toExternalForm(), 15);
		Font.loadFont(getClass().getClassLoader().getResource("assets/hf_b.ttf").toExternalForm(), 15);
		Font.loadFont(getClass().getClassLoader().getResource("assets/hf_i.ttf").toExternalForm(), 15);
		Font.loadFont(getClass().getClassLoader().getResource("assets/hf_bi.ttf").toExternalForm(), 15);

		Scene scene = new Scene(root);
		scene.getStylesheets().addAll("assets/css/main.css", "assets/css/connection.css", "assets/css/interface.css");
		primaryStage.setScene(scene);
		primaryStage.show();

		//event close
		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
		App.primaryStage = primaryStage;
	}

	public static App getInstance() {
		return App._INSTANCE;
	}

	public static Api getAPI() {
		return App._INSTANCE.api;
	}

	public static void main(String[] args) {
		Configuration.load(args);
		launch(args);
	}

	/**
	 * modify the scene
	 * @param root fxml
	 * @param css
	 */
	public static void setScene(Parent root, String[] css) {
		Scene oldScene = App.primaryStage.getScene();
		Scene newScene = new Scene(root, oldScene.getWidth(), oldScene.getHeight());
		App.primaryStage.setScene(newScene);
	}
}
