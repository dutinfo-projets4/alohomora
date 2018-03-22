package fr.alohomora.view;

import fr.alohomora.model.Element;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

	private HBox titleBox;
	private TextField title;
	private TextField username;
	private ShowPassField password;

	public PanePassword() {
		this.titleBox = new HBox();
		this.titleBox.getStyleClass().add("titlebox");

		this.title = new TextField();
		this.title.getStyleClass().addAll("elementHiddenField");

		this.username = new TextField();
		this.username.getStyleClass().add("elementField");

		this.password = new ShowPassField();

		this.titleBox.getChildren().addAll(this.title);

		this.getChildren().addAll(this.titleBox, genLabel("Username: "), this.username, genLabel("Password: "), this.password);

	}

	public void update(Element e){
		if (e != null) {
			if (this.titleBox.getChildren().size() == 2)
				this.titleBox.getChildren().set(0, e.getIcon());
			else
				this.titleBox.getChildren().add(0, e.getIcon());

			this.title.setText(e.getLabel());
			this.username.setText(e.getUsername());
			this.password.setText(e.getPassword());
		} else {
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

}
