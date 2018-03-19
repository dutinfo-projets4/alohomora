package fr.alohomora.model.apiservice;

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

	public Api() {
		this.retrofit = new Retrofit.Builder()
				.baseUrl("https://alohomora.pw/api/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		this.alohomoraService = retrofit.create(AlohomoraService.class);
	}

	public AlohomoraService getAlohomoraService() {
		return this.alohomoraService;
	}
}
