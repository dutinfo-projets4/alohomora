package alohomora.model;

import java.util.ArrayList;

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
