package fr.alohomora.view;

import com.sun.glass.events.MouseEvent;
import fr.alohomora.App;
import fr.alohomora.controller.InterfaceController;
import fr.alohomora.database.Database;
import fr.alohomora.model.Element;
import fr.alohomora.model.retrofitlistener.RetrofitListnerElement;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Pair;

import java.util.Optional;

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
public class PanePassword extends VBox {

	private Element currElement;
	private HBox titleBox;
	private TextField title;
	private TextField username;
	private ShowPassField password;

	private Button cancelbt, removebt, savebt;

	public PanePassword() {
		this.titleBox = new HBox();
		this.titleBox.getStyleClass().add("titlebox");

		this.title = new TextField();
		this.title.getStyleClass().addAll("elementHiddenField");

		this.username = new TextField();
		this.username.getStyleClass().add("elementField");

		this.password = new ShowPassField();

		this.titleBox.getChildren().addAll(this.title);

		Region empty = new Region();
		VBox.setVgrow(empty, Priority.ALWAYS);

		this.cancelbt = new Button("\uf2ea");
		this.removebt = new Button("\uf2ed");
		this.removebt.setOnMouseClicked(mouseEvent -> this.handleRemove());


		this.cancelbt.setDisable(true);
		this.savebt = new Button("\uf0c7");
		this.savebt.setOnMouseClicked(mouseEvent -> this.handleSave());
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		HBox box = new HBox();
		box.getStyleClass().add("boxSave");
		box.setSpacing(10.0);
		box.getChildren().addAll(this.removebt, this.cancelbt, spacer, this.savebt);

		this.getChildren().addAll(this.titleBox, genLabel("Username: "), this.username, genLabel("Password: "), this.password, empty, box);

	}

	public void update(Element e) {
		this.currElement = e;

		// If we update the screen with no elements, we disable all the input
		this.title.setDisable(e == null);
		this.username.setDisable(e == null);
		this.password.setDisable(e == null);
		this.removebt.setDisable(e == null);

		// If there is an element, we update the icon
		if (e != null) {
			if (this.titleBox.getChildren().size() == 2)
				this.titleBox.getChildren().set(0, e.getIcon());
			else
				this.titleBox.getChildren().add(0, e.getIcon());

			// Then the fields
			this.title.setText(e.getLabel());
			this.username.setText(e.getUsername());
			this.password.setText(e.getPassword());
		} else {
			if (this.titleBox.getChildren().size() == 2)
				this.titleBox.getChildren().remove(0);
			this.title.setText("");
			this.username.setText("");
			this.password.setText("");
		}
	}

	private Label genLabel(String text) {
		Label lab = new Label(text);
		lab.getStyleClass().add("elementFieldnames");
		return lab;
	}

	private void handleRemove() {
		if (this.currElement != null) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Removing an element");
			alert.setHeaderText("You're about to remove " + this.currElement.getLabel());
			alert.setContentText("Are you sure ?");

			ButtonType btYes = new ButtonType("Yes");
			ButtonType btNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(btNo, btYes);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == btYes) {
				this.currElement.getParentGroup().removeElement(this.currElement);
				// @TODO remove from database
				InterfaceController.getInstance().onGroupClick(null);
			}
		}
	}

	private void handleSave() {
		if (this.currElement != null) {
			this.currElement.setLabel(this.title.getText());
			this.currElement.setPassword(this.password.getText());
			this.currElement.setUsername(this.username.getText());
			System.out.println(this.currElement.getID());
			if (Database.getInstance().checkElementExist(this.currElement.getID())) {
				// @TODO updata database
			} else {

				PanePassword.this.currElement.addElement(new RetrofitListnerElement() {
					@Override
					public void onIdLoad(Element element) {
						if (element != null) {
							//update label
							PanePassword.this.currElement.setID(element.getID());
							//insert in DB
							Database.getInstance().insertElement(
									PanePassword.this.currElement.getID(),
									PanePassword.this.currElement.getParentGroup().getID(),
									PanePassword.this.currElement.getContent());
							//information user
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									Alert alert = new Alert(Alert.AlertType.INFORMATION);
									alert.setContentText("add successfull");
									alert.showAndWait();
								}
							});
						} else {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									Alert alert = new Alert(Alert.AlertType.WARNING);
									alert.setContentText("Error network");
									alert.showAndWait();
								}
							});


						}
					}

					@Override
					public void error(String msg) {
						System.out.print("Erreur de l'ajout de l'élément");
					}
				}, "" + PanePassword.this.currElement.getParentGroup().getID(), PanePassword.this.currElement.getContent());
			}

		}
		InterfaceController.getInstance().onGroupClick(null);
	}
}


