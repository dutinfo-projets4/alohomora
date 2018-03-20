package alohomora.model;

import alohomora.model.retrofitlistener.RetrofitListenerChallenge;
import alohomora.model.retrofitlistener.RetrofitListenerUser;
import alohomora.model.apiservice.Api;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
public class User {
	private int id;
	private String username;
	private String email;
	private boolean isAdmin;
	private String token;
	private ArrayList<Token> tokens;
	private Data data;


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

	/**
	 * Permet de passer une instance de User si l'idtendification est valide sinon null aux travers de l'interface RetrofitListerUser
	 * Cette méthode permet de faire un requête sur l'api verifie le mot de passe de l'utilisateur et nom utilisateur
	 * Permet de recevoir une instance complète de user contenant l'ensemble de toute ces informations
	 * Attention les groupes et les éléments ce trouve dans une array il faut les retrier
	 *
	 * @param callback    Permet de passer une instance de l'utilisateur si l'identtification se fait avec succès
	 * @param passcode    SHA512(SHA512(username) + idChallenge + SHA512(password))
	 * @param challenge   id du challenge
	 * @param publickey   clef public de l'utilisateur générer lors de la premère connexion sur une nouvelle machine
	 * @param machineName nom de la nouvelle machine
	 */
	public static void challengeConnect(final RetrofitListenerUser callback, String passcode, int challenge, String publickey, String machineName) {
		Api apiService = new Api();
		Call<User> call = apiService.getAlohomoraService().ChallengeConnect(passcode, challenge, publickey, machineName);
		call.enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				if (response.code() == 200)
					callback.onUserLoad(response.body());
				else
					callback.onUserLoad(null);

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
		Api apiService = new Api();
		Call<Challenge> call = apiService.getAlohomoraService().getChallenge();
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
}
