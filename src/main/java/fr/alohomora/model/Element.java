package fr.alohomora.model;

import fr.alohomora.App;
import fr.alohomora.Configuration;
import fr.alohomora.crypto.CryptoUtil;
import fr.alohomora.model.apiservice.Api;
import fr.alohomora.model.retrofitlistener.RetrofitListnerElement;
import fr.alohomora.model.retrofitlistener.RetrofitListnerVoidResponse;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import org.json.simple.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
 * (at your option) any later version. * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 **/
public class Element {

	private int id;
	private int parent;

	// Pre-decryption stuff
	private String content;

	// Post-decryption stuff
	private Group parentGroup;
	private String label;
	private String icon;
	private String username;
	private String pwd;

	private ArrayList<Field> fields;


	/**
	 * Decrypting constructor
	 */
	public Element(int id, int parent, String content) {
		this.id = id;
		this.parent = parent;
		this.content = content;

		// After decryption, thoses fields NEED to be set (Even if empty)
		{
			this.label = "";
			this.icon = "";
			this.username = "";
			this.pwd = "";
			this.fields = new ArrayList<>();
		}
	}

	/**
	 * Decrypted constructor (For tests only ?)
	 */
	public Element(int id, Group gpe, String label, String icon, String username, String password) {
		this.id = id;
		this.label = label;
		this.icon = icon;
		this.username = username;
		this.pwd = password;
		this.parentGroup = gpe;
	}


	public int getID() {
		return this.id;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.pwd;
	}

	public String getLabel() {
		return this.label;
	}

	public Group getParentGroup() {
		return this.parentGroup;
	}

	public int getParent() {
		return this.parent;
	}

	public String getStringIcon() {
		return this.icon;
	}

	public Node getIcon() {
		if (this.icon.startsWith("data:image") && this.icon.split(";")[1].startsWith("base64")) {
			// @TODO get this working
			String ico = this.icon.split(",")[1];
			return new ImageView(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(ico))));
		} else {
			Label lab = new Label(this.icon);
			lab.getStyleClass().addAll("elementLabel", "elementIcon");
			return lab;
		}
	}

	public Field getField(int index) {
		return this.fields.get(index);
	}

	public Element setUsername(String name) {
		this.username = name;
		return this;
	}

	public Element setPassword(String pwd) {
		this.pwd = pwd;
		return this;
	}

	public Element setLabel(String lab) {
		this.label = lab;
		return this;
	}

	public Element setParentGroup(Group gpe) {
		this.parentGroup = gpe;
		return this;
	}

	public Element setIcon(String ico) {
		this.icon = ico;
		return this;
	}

	public Element setID(int id) {
		this.id = id;
		return this;
	}

	/**
	 * Request api to addElement with parent_grp and content pass througout parameters
	 *
	 * @param parent_grp
	 * @param content
	 */
	public void addElement(final RetrofitListnerElement callback, String parent_grp, String content) {
		Pair<String, String>[] params = new Pair[]{
				new Pair("parent_grp", parent_grp),
				new Pair("content", content)
		};
		Call<Element> call = App.getAPI().addElement(params);
		call.enqueue(new Callback<Element>() {
			@Override
			public void onResponse(Call<Element> call, Response<Element> response) {
				System.out.print(response.code());
				if (response.code() == 201)
					callback.onIdLoad(response.body());
				else
					callback.onIdLoad(null);
			}

			@Override
			public void onFailure(Call<Element> call, Throwable throwable) {
				callback.error(throwable.toString());
			}
		});
	}

	/**
	 * Request Api to update element
	 *
	 * @param callback
	 * @param id
	 * @param parent_grp
	 * @param content
	 */
	public void updateElement(final RetrofitListnerVoidResponse callback, String id, String parent_grp, String content) {
		Pair<String, String>[] params = new Pair[]{
				new Pair("id", id),
				new Pair("parent_grp", parent_grp),
				new Pair("content", content)
		};
		Call<Void> call = App.getAPI().updateElement(params);
		call.enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				callback.onResponseLoad(response.code());
			}

			@Override
			public void onFailure(Call<Void> call, Throwable throwable) {
				callback.error(throwable.getMessage());
			}
		});
	}

	/**
	 * Transform the element content to a json format encrypted to the server
	 *
	 * @return String Json encrypted
	 */
	public String getContent() {
		if (this.content == null) {
			JSONObject obj = new JSONObject();
			obj.put("type", "0");
			obj.put("id", this.getID());
			obj.put("name", this.label);
			obj.put("username", this.username);
			obj.put("password", this.pwd);
			obj.put("icon", this.icon);
			return CryptoUtil.encrypt(Configuration.PWD, obj.toJSONString());
		} else
			return this.content;
	}

}
