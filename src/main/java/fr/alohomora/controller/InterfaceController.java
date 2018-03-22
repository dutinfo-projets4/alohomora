package fr.alohomora.controller;

import com.sun.javafx.collections.ElementObservableListDecorator;
import fr.alohomora.model.Field;
import fr.alohomora.model.Group;
import fr.alohomora.view.PanePassword;
import fr.alohomora.view.TreeViewRenderer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
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
	private ListView<Element> sites;

	@FXML
	private SplitPane centerPanel;

	private PanePassword passwordPanel;

	@FXML
	public void initialize() {
		// Sets the cell renderer to the group tree view
		//this.groups.setCellFactory(new TreeViewRenderer());


		// -------------------------- TEMPORARY STUFF --------------------------

		Group g = new Group(1, "Key file", "\uf108");
		g.addGroup(new Group(1, "Réseaux sociaux", "\uf0ac"));
		g.addGroup(new Group(2, "Mails", "\uf0e0"));
		g.addGroup(new Group(3, "Sites achat", "\uf155"));

		Group groupWithSub = new Group(4, "Group4", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAnCAYAAABuf0pMAAAIh3pUWHRSYXcgcHJvZmlsZSB0eXBlIGV4aWYAAHja1ZhZkhw5DkT/eYo5AglwPQ5Xs7nBHH8eInKpLlW1qq37pzOkZIoRQQJwhwOU2//773H");
		groupWithSub.addGroup(new Group(5, "SubGroup1", ""));
		groupWithSub.addGroup(new Group(6, "SubGroup2", ""));
		groupWithSub.addGroup(new Group(7, "SubGroup3", ""));

		g.addGroup(groupWithSub);

		this.groups.setRoot(g);

		sites.setEditable(true);
		// for testing the content of the field which has to be a username under the website
		ObservableList<Element> items = FXCollections.observableArrayList(new Element(0, groupWithSub,"Site1", "\uf270", "Toto", "Ansdfnz"), new Element(1, groupWithSub,"Site2", "\uf179", "Toto2", "tutututu2"));
		sites.setItems(items);

		for (int index = 0; index < items.size(); index++) {
			sites.setCellFactory(new Callback<ListView<Element>, ListCell<Element>>() {
				@Override
				public ListCell<Element> call(ListView<Element> list) {
					ListCell<Element> e = new ListCell<Element>() {
						@Override
						protected void updateItem(Element item, boolean empty) {
							super.updateItem(item, empty);
							if (item != null) {
								setText(item.getLabel());  // get the name of the website
								/*if (item.getField(0).getValue() != null) {
									item.getField(0).getValue();
								} else {
									item.getField(0).getName();
								}*/
							} else {
							}
						}
					};
					return e;
				}
			});
		}

		this.sites.getSelectionModel().select(0);

		// -------------------------- DEFINITIVE STUFF --------------------------

		this.passwordPanel = new PanePassword();
		this.centerPanel.getItems().add(this.passwordPanel);
		this.centerPanel.getDividers().get(0).setPosition(.2);
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
	public void onSitesClick(MouseEvent e){
		this.passwordPanel.update(this.sites.getSelectionModel().getSelectedItem());
	}

}
