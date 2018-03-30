package fr.alohomora.controller;

import fr.alohomora.Configuration;
import fr.alohomora.model.Config;
import fr.alohomora.model.Element;
import fr.alohomora.model.Group;
import fr.alohomora.model.User;
import fr.alohomora.view.PanePassword;
import fr.alohomora.view.PasswordCellFactory;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.Iterator;
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
public class InterfaceController {

	private static InterfaceController _INSTANCE;

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
	private TextField research;

	@FXML
	private Label addElement;

	@FXML
	private Label addGroup;

	@FXML
	public void initialize() {
		this.groups.setRoot(Configuration.USER_INSTANCE.getRoot());

		// Filtered list here
		research.textProperty().addListener((observable, oldvalue, newvalue) -> {
			onClickAllElement(null);
			Iterator ite = this.sites.getItems().iterator();
			// if the user wants to delete a character, results of the research are reinitialized
			if (oldvalue.length() <= newvalue.length()) {
				while (ite.hasNext()) {
					Element e = (Element) ite.next();
					if (!e.getLabel().toLowerCase().contains(newvalue.toLowerCase()) && !(e.getUsername().toLowerCase().contains(newvalue.toLowerCase()))) {
						ite.remove();
					}
				}
			}
		});

		this.research.setPromptText(" Search");
		this.research.getStyleClass().add("researchBar");

		this.addElement.setText("");
		this.addGroup.setText("");


		// -------------------------- DEFINITIVE STUFF --------------------------
		InterfaceController._INSTANCE = this;


		this.passwordPanel = new PanePassword(this);
		this.centerPanel.getItems().add(this.passwordPanel);
		this.centerPanel.getDividers().get(0).setPosition(.2);

		this.sites.setCellFactory(new PasswordCellFactory());
		this.onClickAllElement(null);
		this.groups.getSelectionModel().getSelectedItem();
		this.addElementInSelectedGroup();
		this.addGroupInSelectedGroup();
	}

	public void addElementInSelectedGroup() {
		this.addElement.setOnMouseClicked(event -> {
			Group groupSelected = (Group) InterfaceController.this.groups.getSelectionModel().getSelectedItem();
			// @TODO get id of element from bd
			groupSelected.addElementFirstposition(new Element(-1, groupSelected, "empty", "\uF084", "empty", "empty"));
			//update view
			InterfaceController.this.onGroupClick(null);
			InterfaceController.this.onSitesClick(null);
		});
	}

	public void addGroupInSelectedGroup() {
		this.addGroup.setOnMouseClicked(mouseEvent -> {
			Group groupSelected = (Group) InterfaceController.this.groups.getSelectionModel().getSelectedItem();
			TextInputDialog dialog = new TextInputDialog("name");
			dialog.setTitle("Enter the group name");
			dialog.setHeaderText("Enter the group name");
			dialog.setContentText("Enter a group name");
			Optional<String> result = dialog.showAndWait();
			System.out.print(groupSelected.getID());
			result.ifPresent(s -> {
				groupSelected.updateGroup(new Group(-1, groupSelected.getID(), s, ""));
			});
			// update view
			InterfaceController.this.onGroupClick(null);
		});
	}

	@FXML
	public void onClickAllElement(MouseEvent e) {
		// When you click on "All Elements", the class is added and the selection clicks on root
		this.groups.getSelectionModel().select(0);
		this.onGroupClick(e);
	}

	@FXML
	public void onGroupClick(MouseEvent e) {
		Group selectedGroup = (Group) this.groups.getSelectionModel().getSelectedItem();
		// When you click on a group, we clear the element list
		this.sites.getItems().clear();

		// Then we fill it with group's elements
		this.sites.setItems(selectedGroup.getElements());

		// If we are on the root group, we toggle the "All elements" button
		if (this.groups.getSelectionModel().getSelectedIndex() == 0) {
			if (!this.allElements.getStyleClass().contains("allPressed")) {
				this.allElements.getStyleClass().add("allPressed");
			}
		} else {
			this.allElements.getStyleClass().remove("allPressed");
		}

		// Lastly, we select the first element of the group if it exists, and we update the render
		ObservableList<Element> elements = (selectedGroup).getElements();
		if (elements.size() > 0) {
			this.sites.getSelectionModel().select(elements.get(0));
		}
		this.onSitesClick(null);

		if (e != null && e.getClickCount() == 2) {
			TextInputDialog dialog = new TextInputDialog("name");
			dialog.setTitle("Change the group name");
			dialog.setHeaderText("Change the group name");
			dialog.setContentText("Enter a group name");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				selectedGroup.setName(result.get());
			}
		}

	}

	@FXML
	public void onSitesClick(MouseEvent e) {
		this.passwordPanel.update(this.sites.getSelectionModel().getSelectedItem());
	}

	/**
	 * Ugly singleton
	 */
	public static InterfaceController getInstance() {
		return InterfaceController._INSTANCE;
	}
}
