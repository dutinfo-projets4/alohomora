package fr.alohomora.view;

import fr.alohomora.model.Element;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

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
public class PasswordCellFactory implements Callback<ListView<Element>, ListCell<Element>> {

	@Override
	public ListCell<Element> call(ListView<Element> elementListView) {
		return new PasswordListCell();
	}
}

class PasswordListCell extends javafx.scene.control.ListCell<Element> {
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
		} else {
			setDisable(false);
			setGraphic(null);
		}

	}

}
