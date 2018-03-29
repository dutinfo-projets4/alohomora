package fr.alohomora.model.apiservice;

import fr.alohomora.Configuration;
import fr.alohomora.database.Database;
import fr.alohomora.model.Challenge;
import fr.alohomora.model.Element;
import fr.alohomora.model.Group;
import fr.alohomora.model.User;
import javafx.util.Pair;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;

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
public class Api {

	private Pair<String, String> params[];
	private Retrofit retrofit;
	private AlohomoraService service;
	private OkHttpClient client;

	public Api() {

		this.client = new OkHttpClient.Builder()
				.build();

		this.setParams(new Pair[]{});
	}

	public void setParams(Pair<String, String>[] params){
		this.params = params;

		this.client = new OkHttpClient.Builder().addInterceptor(new InterceptorHeader(params)).build();

		this.retrofit = new Retrofit.Builder()
				.baseUrl(Configuration.BASE_PATH)
				.addConverterFactory(GsonConverterFactory.create())
				.client(client)
				.build();

		this.service = retrofit.create(AlohomoraService.class);
	}

	public Call<Challenge> getChallenge() {
		this.setParams(new Pair[]{});
		return this.service.getChallenge();
	}

	private Pair<String, String>[] addRequest(Pair<String, String>[] param) {
		Pair<String, String>[] newParams = new Pair[param.length+1];
		for (int i = 0; i < param.length; i++){
			newParams[i+1] = param[i];
		}

		newParams[0] = new Pair("req_id", "" + Database.getInstance().getRequestId());

		return newParams;
	}

	public Call<User> connect(Pair<String, String>[] params) {
		this.setParams(params);
		return this.service.connect(params[0].getValue(), params[1].getValue(), params[2].getValue(), params[3].getValue());
	}

	public Call<Element> addElement(Pair<String, String>[] params){
		Pair<String, String>[] np = this.addRequest(params);
		this.setParams(np);
		return this.service.addElement(np[0].getValue(), np[1].getValue(), np[2].getValue());
	}

	public Call<Void> updateElement(Pair <String, String>[] params){
		Pair<String, String>[] np = this.addRequest(params);
		return this.service.updateElement(np[0].getValue(), np[1].getValue(), np[2].getValue(), np[3].getValue());
	}

	public Call<Group> addGroup(Pair<String, String>[] params){
		Pair<String, String>[] np = this.addRequest(params);
		this.setParams(np);
		return this.service.addGroup(np[0].getValue(), np[1].getValue(), np[2].getValue());
	}


}