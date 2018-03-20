package fr.alohomora.controller;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ListView;

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
public class InterfaceController {

	private boolean allElementIsActive = true;

	@FXML
	private HBox allElements;

	@FXML
	private TreeView groups;

	@FXML
	private ListView sites;

	@FXML
	public void initialize() {
		TreeItem<String> root = new TreeItem<>("Root");
		root.setExpanded(true);

		TreeItem<String> item = new TreeItem<>("Item");
		root.getChildren().add(item);

		TreeItem<String> item2 = new TreeItem<>("Item");
		root.getChildren().add(item2);

		TreeItem<String> grp= new TreeItem<>("Groupe");
		grp.setExpanded(true);
		TreeItem<String> subgrp = new TreeItem<>("Item");
		grp.getChildren().add(subgrp);
		root.getChildren().add(grp);


		sites.setEditable(true);
		OservableList<String> items = FXCollections.observableArrayList("Website 1", "Website 2");
		sites.setItems(items);

		groups.setRoot(root);
	}

	@FXML
	public void onClickAllElement(MouseEvent e) {
		if (this.allElementIsActive) {
			this.allElements.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), true);
			this.allElementIsActive = false;
		} else {
			this.allElements.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), false);
			this.allElementIsActive = true;
		}
	}

	@FXML
	public void onClickAddItem(MouseEvent e){
		Scanner scan = new Scanner(System.in);
		string NewItem = scan.next();
		sites.getItems().add(NewItem);
	}

	@FXML
	public void onClickAddUsername(MouseEvent e){
		Scanner scan = new Scanner(System.in);
		string NewUsername = scan.next();
		sites.getSelectedItem().setItems(NewUsername);

	}
}
