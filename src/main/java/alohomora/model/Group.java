package alohomora.model;

import java.util.ArrayList;

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
public class Group {
	private int id;
	private int parent;
	private String content;
	private ArrayList<Group> groups;
	private ArrayList<Element> elements;

	public Group(int id, int parent, String content) {
		this.id = id;
		this.parent = parent;
		this.content = content;
	}

	public Group(int id, int parent, Group group) {
		this.id = id;
		this.parent = parent;
		this.groups.add(group);
	}

	public int getID() {
		return id;
	}

	public int getParent() {
		return parent;
	}

	public String getContent() {
		return content;
	}

	public ArrayList<Element> getElements() {
		return this.elements;
	}

	public ArrayList<Group> getSubGroups() {
		return groups;
	}

	public boolean updateGroup(Group group) {
		Group oldGroup = this.groups.set(this.groups.indexOf(group), group);
		return oldGroup != null;
	}

	public boolean addSubGroup(Group group) {
		return this.groups.add(group);
	}

	public boolean remmoveSubGroup(Group group) {
		return this.groups.remove(group);
	}

	public boolean addElement(Element elt) {
		return this.elements.add(elt);
	}

	public boolean updateElement(Element elt) {
		Element oldElement = this.elements.set(this.elements.indexOf(elt), elt);
		return oldElement != null;
	}

	public boolean removeElement(Element elt) {
		return this.elements.remove(elt);
	}


}
