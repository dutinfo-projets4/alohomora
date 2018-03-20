package fr.alohomora.model;

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
public class Data {
	private ArrayList<Config> config;
	private ArrayList<Element> elements;
	private ArrayList<Group> groups;

	public ArrayList<Config> getConfig() {
		return config;
	}

	public ArrayList<Element> getElements() {
		return elements;
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}
}
