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
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import fr.alohomora.model.Element;
import javafx.util.Callback;

import java.awt.*;
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
	private TreeView<Group> groups;

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
		Group rs = new Group(1, "Réseaux sociaux", "\uf0ac");
		g.addGroup(rs);
		rs.addElement(new Element(0, rs, "Facebook", "\uf082", "toto", "toto"));
		rs.addElement(new Element(1, rs, "Twitter", "\uf099", "toto", "toto"));
		rs.addElement(new Element(2, rs, "Instagram", "\uf16d", "toto", "toto"));
		g.addGroup(new Group(2, "Mails", "\uf0e0"));
		g.addGroup(new Group(3, "Sites achat", "\uf155"));

		Group groupWithSub = new Group(4, "Group4", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAnCAYAAABuf0pMAAAIh3pUWHRSYXcgcHJvZmlsZSB0eXBlIGV4aWYAAHja1ZhZkhw5DkT/eYo5AglwPQ5Xs7nBHH8eInKpLlW1qq37pzOkZIoRQQJwhwOU2//773H");
		groupWithSub.addGroup(new Group(5, "SubGroup1", ""));
		groupWithSub.addGroup(new Group(6, "SubGroup2", ""));
		groupWithSub.addGroup(new Group(7, "SubGroup3", ""));

		g.addGroup(groupWithSub);

		this.groups.setRoot(g);

		sites.setCellFactory(new Callback<ListView<Element>, ListCell<Element>>() {
			@Override
			public ListCell<Element> call(ListView<Element> list) {
				ListCell<Element> newCell = new ListCell<Element>() {
						@Override
						protected void updateItem(Element item, boolean empty) {
							super.updateItem(item, empty);
							this.getStyleClass().add("cellListView");
							if (item != null) {
								GridPane grid = new GridPane();
								//grid.getStyleClass().add("cellListView");
								Label icon = new Label(item.getStringIcon());
								icon.getStyleClass().add("iconCenter");
								grid.add(icon, 0, 0, 1, 2);
								setGraphic(grid);

								Label website = new Label(item.getLabel());
								website.getStyleClass().add("website");
								grid.add(website, 1, 0);

								Label username = new Label(item.getUsername());
								grid.add(username, 1, 1);
								username.getStyleClass().add("username");
							}
						}
					};
				return newCell;
			}
		});

		//this.sites.getSelectionModel().select(0); // useless ?

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
	public void onGroupClick(MouseEvent e){
		this.sites.getItems().clear();
		this.sites.getItems().setAll(((Group) this.groups.getSelectionModel().getSelectedItem()).getElements());
	}

	@FXML
	public void onSitesClick(MouseEvent e){
		this.passwordPanel.update(this.sites.getSelectionModel().getSelectedItem());
	}

}
