package fr.alohomora.controller;

import fr.alohomora.App;
import fr.alohomora.Configuration;
import fr.alohomora.crypto.RSAObject;
import fr.alohomora.database.Database;
import fr.alohomora.model.Challenge;
import fr.alohomora.model.Config;
import fr.alohomora.model.Data;
import fr.alohomora.model.User;
import fr.alohomora.model.retrofitlistener.RetrofitListenerUser;
import fr.alohomora.model.retrofitlistener.RetrofitListenerChallenge;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
public class ConnectController {

	private RSAObject obj;

	private boolean connected;

	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Button login;
	@FXML
	private ChoiceBox server;
	@FXML
	private HBox usernameFieldLabel;
	@FXML
	private VBox parentField;


	@FXML
	public void initialize() {

		Configuration.LOGIN_TOKEN = Database.getInstance().getToken();
		this.connected = Configuration.LOGIN_TOKEN != null;
		if (this.connected) {
			this.parentField.getChildren().remove(this.usernameFieldLabel);
			this.login.setText("Login");
			this.onOpenDatabaseClick();
			this.onKeyPressedDatabase();
		} else {
			this.onLoginClick();
			this.onKeyPressedLogin(username);
			this.onKeyPressedLogin(this.password);
		}
	}

	public void onOpenDatabaseClick() {
		this.login.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				ConnectController.this.openDatabase();
			}
		});
	}

	public void onKeyPressedDatabase() {
		this.password.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				ConnectController.this.openDatabase();
			}
		});
	}

	/**
	 * decrypt dataBase and load interface
	 */
	public void openDatabase() {
		if (checkEmpty(this.password)) {
			/**
			 * @TODO getUpdate if 403 delete database
			 */

			Configuration.PWD = this.password.getText();
			Data data = Database.getInstance().getData();
			String username = Database.getInstance().getUserName(Configuration.LOGIN_TOKEN);
			User user = new User(0, username,"@",false,Configuration.LOGIN_TOKEN,data);
			System.out.print(username);

			// @TODO create user
			this.setGroupsAndPanel(user);
		}

	}


	/**
	 * Check TextInput is empty or not and add css class "empty" and return true or false
	 *
	 * @param field
	 * @return boolean
	 */
	private boolean checkEmpty(TextInputControl field) {
		boolean res = false;
		if (field.getText().isEmpty()) {
			field.getStyleClass().add("empty");
			res = false;
		} else {
			field.getStyleClass().remove("empty");
			res = true;
		}
		return res;
	}

	/**
	 * add keyevent enter in the node
	 *
	 * @param node
	 */
	private void onKeyPressedLogin(Node node) {
		node.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				ConnectController.this.connect();
			}
		});
	}

	/**
	 * event throw when user click on loginButton
	 */
	private void onLoginClick() {
		this.login.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				ConnectController.this.connect();
			}
		});
	}

	/**
	 * Connection with the username and the password
	 */
	private void connect() {
		boolean checkFieldPassword = this.checkEmpty(this.password);
		boolean checkFieldUsername = this.checkEmpty(this.username);
		if (checkFieldPassword && checkFieldUsername) {
			Configuration.PWD = this.password.getText();
			this.loadKeys();
			User.getChallenge(new RetrofitListenerChallenge() {
				@Override
				public void onChallengeLoad(Challenge challenge) {
					String passCodeHash = ConnectController.this.hashPasscode(challenge.getChallenge());

					RetrofitListenerUser callback = new RetrofitListenerUser() {
						@Override
						public void callback(User user) {
							if (user != null) {
								Database.getInstance().insertConfig(user.getUsername(), user.getToken(), Configuration.PORTABLE);
								ConnectController.this.setGroupsAndPanel(user);
							} else {
								Platform.runLater(() -> {
									Alert alert = new Alert(Alert.AlertType.WARNING);
									alert.setContentText("password or user false");
									alert.showAndWait();
								});
							}
						}

						@Override
						public void error(String msg) {
							Platform.runLater(() -> {
								Alert alert = new Alert(Alert.AlertType.WARNING);
								alert.setContentText("Network error");
								alert.showAndWait();
							});
						}
					};

					User.challengeConnect(callback, passCodeHash, challenge.getID(), ConnectController.this.obj.formatServer(), "test");
				}

				@Override
				public void error(String msg) {
					Platform.runLater(() -> {
						Alert alert = new Alert(Alert.AlertType.WARNING);
						alert.setContentText("Network error");
						alert.showAndWait();
					});
				}
			});
		}
	}

	private void loadKeys() {
		try {
			if (RSAObject.exists(this.username.getText())) {
				try {
					this.obj = new RSAObject(this.username.getText());
				} catch (InvalidKeySpecException e) {
					e.printStackTrace();
				}
			} else
				this.obj = new RSAObject(this.username.getText(), 4096);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Make passcode with the challenge pass in parameter
	 *
	 * @param challenge
	 * @return string hashPassCode
	 */
	private String hashPasscode(String challenge) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// hash(username)
		String usernameHash = DatatypeConverter.printHexBinary(messageDigest.digest(ConnectController.this.username.getText().getBytes()));
		// hash(password)
		String passwordHash = DatatypeConverter.printHexBinary(messageDigest.digest(ConnectController.this.password.getText().getBytes()));
		// hash(passwordHash)
		passwordHash = DatatypeConverter.printHexBinary(messageDigest.digest(passwordHash.toLowerCase().getBytes()));
		//passcode
		String passcode = usernameHash.toLowerCase() + challenge + passwordHash.toLowerCase();
		//hash(passcode)
		return DatatypeConverter.printHexBinary(messageDigest.digest(passcode.getBytes()));
	}


	public void setGroupsAndPanel(User u) {
		try {
			Configuration.USER_INSTANCE = u;
			Configuration.USER_INSTANCE.setRoot();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setController(new InterfaceController());
 						App.setScene(loader.load(getClass().getClassLoader().getResource("fxml/interface.fxml")), new String[]{"assets/css/main.css", "assets/css/interface.css"});
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			Alert al = new Alert(Alert.AlertType.WARNING);
			al.setContentText("Bad password!");
			al.showAndWait();
		}
	}

}

