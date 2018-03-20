package alohomora.model.apiservice;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
	private Retrofit retrofit;
	private AlohomoraService alohomoraService;
	private OkHttpClient client;

	public Api() {

		this.client = new OkHttpClient.Builder()
				.addInterceptor(new InterceptorHeader())
				.build();

		this.retrofit = new Retrofit.Builder()
				.baseUrl("http://127.0.0.1:8000")
				.addConverterFactory(GsonConverterFactory.create())
				.client(client)
				.build();

		this.alohomoraService = retrofit.create(AlohomoraService.class);
	}

	public AlohomoraService getAlohomoraService() {
		return this.alohomoraService;
	}
}
