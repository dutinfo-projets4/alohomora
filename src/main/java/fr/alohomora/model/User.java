package fr.alohomora.model;

import fr.alohomora.App;
import fr.alohomora.model.retrofitlistener.RetrofitListenerUser;
import fr.alohomora.model.retrofitlistener.RetrofitListenerChallenge;
import javafx.util.Pair;
import org.json.simple.parser.ParseException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Iterator;

import static java.sql.Types.NULL;

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
public class User {

	private int id;
	private String username;
	private String email;
	private boolean isAdmin;
	private String token;
	private Data data;
	private ArrayList<Token> tokens;
	private Group root;

	public User(int id, String username, String email, boolean isAdmin, String token, Data data) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.isAdmin = isAdmin;
		this.token = token;
		this.data = data;
	}


	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public String getToken() {
		return token;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAdmin(boolean admin) {
		isAdmin = admin;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ArrayList<Token> getTokens() {
		return tokens;
	}

	public Data getData() { return this.data; }

	public Group getRoot() { return this.root; }

	/**
	 * Permet de passer une instance de User si l'idtendification est valide sinon null aux travers de l'interface RetrofitListerUser
	 * Cette méthode permet de faire un requête sur l'api verifie le mot de passe de l'utilisateur et nom utilisateur
	 * Permet de recevoir une instance complète de user contenant l'ensemble de toute ces informations
	 * Attention les groupes et les éléments ce trouve dans une array il faut les retrier
	 *
	 * @param callback     Permet de passer une instance de l'utilisateur si l'identtification se fait avec succès
	 * @param passcode     SHA512(SHA512(username) + idChallenge + SHA512(password))
	 * @param challenge    id du challenge
	 * @param public_key   clef public de l'utilisateur générer lors de la premère connexion sur une nouvelle machine
	 * @param machine_name nom de la nouvelle machine
	 */
	public static void challengeConnect(final RetrofitListenerUser callback, String passcode, String challenge, String public_key, String machine_name) {
		Pair<String, String> params[] = new Pair[]{
				new Pair("passcode", passcode),
				new Pair("challenge", challenge),
				new Pair("public_key", public_key),
				new Pair("machine_name", machine_name)
		};

		Call<User> call = App.getAPI().connect(params);
		call.enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				if (response.code() == 200)
					callback.callback(response.body());
				else
					callback.callback(null);

			}


			@Override
			public void onFailure(Call<User> call, Throwable t) {
				System.out.print("Erreur de connexion : " + t.toString());
				callback.error(t.toString());
			}
		});
	}

	/**
	 * Permet de recevoir une instance de challenge aux travers de l'interface RetrofitListenerUser
	 * Cette méthode permet de faire une requête sur l'api pour obtenir le challenge et son id
	 *
	 * @param callback Permet de passer une instance de Challenge si succès sinon null
	 */
	public static void getChallenge(final RetrofitListenerChallenge callback) {
		Call<Challenge> call = App.getAPI().getChallenge();
		call.enqueue(new Callback<Challenge>() {
			@Override
			public void onResponse(Call<Challenge> call, Response<Challenge> response) {
				if (response.code() == 200)
					callback.onChallengeLoad(response.body());
				else
					callback.onChallengeLoad(null);

			}

			@Override
			public void onFailure(Call<Challenge> call, Throwable t) {
				System.out.print("Erreur d'obtention du challenge");
				callback.error(t.toString());
			}
		});
	}

	/**
	 * @return Le groupe racine de l'utilisateur
	 */
	public Group getSubgroups(Group src){
		Group result = null;
		for (int i = 0; i < this.data.getGroups().size() ;  ++i){
			if(this.data.getGroups().get(i).getParentGroup() == src.getID()){
				src.addGroup(this.getSubgroups(this.data.getGroups().get(i)));
			}
		}
		return src;
	}

	public void setRoot() throws InvalidKeyException {
		for (Element e : this.data.getElements()){
			try {
				e.decrypt();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}

		for(Group g2: this.data.getGroups()){
			// We decrypt the group data
			if (g2.getParentGroup() != -1) {
				try {
					g2.decrypt();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				g2.setName("Root group");
				g2.setIcon("\uF07C");
			}

			// Then we add his elements
			Iterator i = this.data.getElements().iterator();
			while (i.hasNext()){
				Element e = (Element) i.next();
				if (e.getParent() == g2.getID()){
					g2.addElement(e);
					i.remove();
				}
			}

			// Then we build the tree
			if(g2.getParentGroup() == -1){
				this.root = this.getSubgroups(g2);
			}
		}
	}
}
