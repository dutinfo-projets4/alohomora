package fr.alohomora.model;

import fr.alohomora.App;
import fr.alohomora.Configuration;
import fr.alohomora.crypto.CryptoUtil;
import fr.alohomora.database.Database;
import fr.alohomora.model.retrofitlistener.RetrofitListnerElement;
import fr.alohomora.model.retrofitlistener.RetrofitListnerGroup;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayInputStream;
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
	private int parent_grp;

	private String name;
	private String icon;

	private String content;
	private ObservableList<Element> elements;

	private Group() {
		this.elements = FXCollections.observableArrayList();
		this.setExpanded(true);
	}

	public Group(int id, Integer parent_grp, String content) {
		this();
		this.id = id;
		this.parent_grp = parent_grp;
		this.content = content;
	}

	public Group(int id, String name, String icon) {
		this();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.setValue(this.name);
		this.setGraphic(this.getIcon());
	}


	public Group(int id, int parent_grp, String name, String icon) {
		this();
		this.parent_grp = parent_grp;
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.setValue(this.name);
		this.setGraphic(this.getIcon());
	}

	public int getID() {

		return this.id;
	}

	public int getParentGroup() {

		return this.parent_grp;
	}

	public void setName(String name) {
		this.name = name;
		this.setValue(name);
	}

	public String getName(){
		return this.name;
	}


	// Wont work because group is already modified so it wont be in the array
	public void updateGroup(Group group) {
		if (!Database.getInstance().checkGroupExist(group.id)) {
			this.addGroup(new RetrofitListnerGroup() {
				@Override
				public void onIdLoad(Group idGroup) {
					if (idGroup != null) {
						//change the current id
						group.id = idGroup.getID();
						//add to database
						Database.getInstance().insertGroup(group.id, group.parent_grp, group.getContent());
						Group.this.addGroup(group);
						//information to the user
						Platform.runLater(() -> {
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setContentText("Successfull add");
							alert.showAndWait();
						});
					} else {
						//information to the user
						Platform.runLater(() -> {
							Alert alert = new Alert(Alert.AlertType.WARNING);
							alert.setContentText("Error network");
							alert.showAndWait();
						});
					}
				}

				@Override
				public void error(String msg) {
					//information to the user
					Platform.runLater(() -> {
						Alert alert = new Alert(Alert.AlertType.WARNING);
						alert.setContentText(msg);
						alert.showAndWait();
					});
				}
			}, "" + group.parent_grp, group.getContent());
		}

	}

	/**
	 * Add group in the tree
	 *
	 * @param group
	 * @return
	 */
	public boolean addGroup(Group group) {
		return this.getChildren().add(group);
	}

	public boolean removeGroup(Group group) {
		return this.getChildren().remove(group);
	}

	public boolean addElement(Element elt) {
		return this.elements.add(elt);

	}

	public void addElementFirstposition(Element elt) {
		this.elements.add(0, elt);
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
			Group gpe = (Group) g;
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

	/**
	 * Request api to add Group with parent_grp and content pass througout parameters
	 *
	 * @param parent_grp
	 * @param content
	 */
	public void addGroup(final RetrofitListnerGroup callback, String parent_grp, String content) {
		Pair<String, String>[] params = new Pair[]{
				new Pair("parent_grp", parent_grp),
				new Pair("content", content)
		};
		Call<Group> call = App.getAPI().addGroup(params);
		call.enqueue(new Callback<Group>() {
			@Override
			public void onResponse(Call<Group> call, Response<Group> response) {
				System.out.print(response.code());
				if (response.code() == 201)
					callback.onIdLoad(response.body());
				else
					callback.onIdLoad(null);
			}

			@Override
			public void onFailure(Call<Group> call, Throwable throwable) {
				callback.error(throwable.toString());
			}
		});
	}

	/**
	 * Transform the group content to a json format encrypted to the server
	 * @return String Json encrypted
	 */
	public String getContent() {
		JSONObject obj = new JSONObject();
		obj.put("name", this.name);
		obj.put("icon", this.icon);
		return CryptoUtil.encrypt(Configuration.PWD, obj.toJSONString());
	}

	public void decrypt() throws ParseException {
		JSONObject obj = (JSONObject) Configuration.parser.parse(this.content);
		this.setName((String)obj.get("name"));
		this.icon = (String) obj.get("icon");
		this.setName(this.content);
		this.setGraphic(this.getIcon());
	}

}
