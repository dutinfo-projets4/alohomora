package fr.alohomora.model.apiservice;


import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


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
public class InterceptorHeader implements okhttp3.Interceptor {
	private String[][] rootExcluded;
	private Request newRequest;
	private Request request;

	/**
	 * constructor interceptor (cf okhttp3.Interceptor) and create the differents excluded root
	 */
	public InterceptorHeader() {
		super();
		this.rootExcluded = new String[][]{
				{"users"},
				{"GET", "POST", "PUT"}
		};
	}

	/**
	 * this method permit to ba call in the build of the retfrofit in order to modify the header HTTP request
	 * @param chain
	 * @return Response
	 * @throws IOException
	 */
	@Override
	public Response intercept(Chain chain) throws IOException {
		this.request = chain.request();
		HttpUrl urlHttp = request.url();
		String method = request.method();
		String[] url = urlHttp.toString().split("/");

		//add userAgent
		this.newRequest = request.newBuilder()
				.addHeader("User-Agent", "ALOHOMORA-DESKTOP")
				.build();

		//check root
		for (int i = 0; i < this.rootExcluded.length; i++) {
			if (url[3].equals(this.rootExcluded[0][0]) && method.equals(this.rootExcluded[i][0])) {
				return chain.proceed(newRequest);
			}
		}

		//add signature & token
		this.newRequest = request.newBuilder()
				.addHeader("User-Agent", "X-ALOHOMORA")
				//.addHeader("X-ALOHOMORA-TOKEN", X)
				//.addHeader("X-ALOHOMORA-SIGNATURE", X)
				.build();
		return chain.proceed(this.newRequest);
	}

}
