package fr.alohomora.controller;

import com.sun.javafx.collections.ElementObservableListDecorator;
import fr.alohomora.model.Field;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ListView;
import fr.alohomora.model.Element;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Scanner;

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
		// for testing the content of the field which has to be a username under the website
		ArrayList<Field> fields = new ArrayList<Field>();
		Field f = new Field("username","ValueUsername",true);
		fields.add(f);
		ObservableList<Element> items = FXCollections.observableArrayList(new Element(0,0,"Site1", fields), new Element(1,0,"Site2", fields));
		sites.setItems(items);

		for (int index = 0 ; index < items.size() ; index++)
		{
		sites.setCellFactory(new Callback<ListView<Element>, ListCell<Element>>(){
			@Override
			public ListCell<Element> call(ListView<Element> list){
				ListCell<Element> e = new ListCell<Element>(){
					@Override
					protected void updateItem(Element item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getContent());// get the name of the website
							if (item.getField(0).getValue() != null ) {
								item.getField(0).getValue();
							}
							else{
								item.getField(0).getName();
							}
						} else {
						}
					}
				};
				return e;
			}
		});
	}

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
// Fait planter l'application , doit ajouter un site
// @FXML
//	public void onClickAddItem(MouseEvent e){
//		Scanner scan = new Scanner(System.in);
//		String NewItem = scan.next();
//		sites.getItems().add(NewItem);
//	}

	@FXML
	public void onClickAddUsername(MouseEvent e){
//		Scanner scan = new Scanner(System.in);
//		String NewUsername = scan.next();
		//sites.getSelectionModel().setItems(NewUsername);

	}
}
