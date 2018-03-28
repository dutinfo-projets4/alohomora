package fr.alohomora.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.alohomora.model.Element;
import fr.alohomora.model.Group;
import fr.alohomora.view.PanePassword;
import fr.alohomora.view.PasswordCellFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.collections.transformation.FilteredList;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.scene.text.Font;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.Iterator;


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

	private ObservableList<Element> obsElement = FXCollections.observableArrayList();

	@FXML
	private Label addButton1,addButton2;

	@FXML
	public void initialize() {
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

		// Filtered list here
		FilteredList<Element> filteredElements = new FilteredList<Element>(this.obsElement);
		research.textProperty().addListener((observable, oldvalue, newvalue)-> {
			onClickAllElement(null);
			Iterator ite = this.sites.getItems().iterator();
			// if the user wants to delete a character, results of the research are reinitialized
			if(oldvalue.length() <= newvalue.length()){
			while(ite.hasNext()) {
			Element e = (Element) ite.next();
				if (!e.getLabel().toLowerCase().contains(newvalue.toLowerCase()) && !(e.getUsername().toLowerCase().contains(newvalue.toLowerCase()))) {
					ite.remove();
				}
			  }
			}
		});

		this.research.setPromptText("\uf002 Search");
		this.research.getStyleClass().add("researchBar");

		this.addButton1.setText("\uf067");
		this.addButton2.setText("\uf067");

		// -------------------------- DEFINITIVE STUFF --------------------------
		InterfaceController._INSTANCE = this;


		this.passwordPanel = new PanePassword();
		this.centerPanel.getItems().add(this.passwordPanel);
		this.centerPanel.getDividers().get(0).setPosition(.2);

		this.sites.setCellFactory(new PasswordCellFactory());
		this.onClickAllElement(null);
		this.groups.getSelectionModel().getSelectedItem();
	}

	@FXML
	public void onClickAllElement(MouseEvent e) {
		// When you click on "All Elements", the class is added and the selection clicks on root
		this.groups.getSelectionModel().select(0);
		this.onGroupClick(e);
	}

	@FXML
	public void onGroupClick(MouseEvent e){
		// When you click on a group, we clear the element list
		this.sites.getItems().clear();

		// Then we fill it with group's elements
		this.sites.setItems(((Group)this.groups.getSelectionModel().getSelectedItem()).getElements());

		// If we are on the root group, we toggle the "All elements" button
		if (this.groups.getSelectionModel().getSelectedIndex() == 0){
			if (!this.allElements.getStyleClass().contains("allPressed")){
				this.allElements.getStyleClass().add("allPressed");
			}
		} else {
			this.allElements.getStyleClass().remove("allPressed");
		}

		// Lastly, we select the first element of the group if it exists, and we update the render
		ObservableList<Element> elements = ((Group) this.groups.getSelectionModel().getSelectedItem()).getElements();
		if (elements.size() > 0){
			this.sites.getSelectionModel().select(elements.get(0));
		}
		this.onSitesClick(null);
	}

	@FXML
	public void onSitesClick(MouseEvent e){
		this.passwordPanel.update(this.sites.getSelectionModel().getSelectedItem());
	}

	/**
	 * Ugly singleton
	 */
	public static InterfaceController getInstance(){
		return InterfaceController._INSTANCE;
	}


}
