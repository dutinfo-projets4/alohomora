package fr.alohomora.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;

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
public class Group extends TreeItem {

	private int id;
	private int parent;

	private String name;
	private	String icon;

	private String content;
	private ArrayList<Element> elements;

	private Group() {
		this.elements = new ArrayList<>();
		this.setExpanded(true);
	}

	public Group(int id, int parent, String content) {
		this();
		this.id = id;
		this.parent = parent;
		this.content = content;
	}

	public Group(int id, String name, String icon) {
		this();
		this.id   = id;
		this.name = name;
		this.icon = icon;
		this.setValue(this.name);
		this.setGraphic(this.getIcon());
	}

	public int getID() {
		return id;
	}

	public int getParentGroup() {
		return parent;
	}

	public String getContent() {
		return content;
	}

	// Wont work because group is already modified so it wont be in the array
	public boolean updateGroup(Group group) {
		// @TODO
		return false;
	}

	public boolean addGroup(Group group) {
		return this.getChildren().add(group);
	}

	public boolean removeGroup(Group group) {
		return this.getChildren().remove(group);
	}

	public boolean addElement(Element elt) {
		return this.elements.add(elt);
	}

	// Same as updateGroup
	public boolean updateElement(Element elt) {
		Element oldElement = this.elements.set(this.elements.indexOf(elt), elt);
		this.setValue(this.name);
		return oldElement != null;
	}

	public boolean removeElement(Element elt) {
		return this.elements.remove(elt);
	}

	public ObservableList<Element> getElements() {
		ObservableList<Element> elts = FXCollections.observableArrayList();
		elts.addAll(this.elements);
		for (Object g : this.getChildren()) {
			Group gpe = (Group)g;
			elts.addAll(gpe.getElements());
		}
		return elts;
	}

	public Node getIcon() {
		if (this.icon.startsWith("data:image") && this.icon.split(";")[1].startsWith("base64")) {
			// @TODO get this working
			String ico = this.icon.split(",")[1];
			return new ImageView(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(ico))));
		} else {
			Label lab = new Label(this.icon);
			lab.getStyleClass().add("groupLabel");
			return lab;
		}
	}
}
