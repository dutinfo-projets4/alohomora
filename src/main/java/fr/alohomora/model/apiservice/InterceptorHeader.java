package fr.alohomora.model.apiservice;


import fr.alohomora.Configuration;
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

	private String[][] routeExcluded;

	/**
	 * constructor interceptor (cf okhttp3.Interceptor) and create the differents excluded root
	 */
	public InterceptorHeader() {
		super();
		this.routeExcluded = new String[][]{
				{"users", "GET", "POST", "PUT"},
				{"challenge", "GET"}
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
		Request request = chain.request();
		HttpUrl urlHttp = request.url();
		String method = request.method();
		String[] url = urlHttp.toString().split("/");

		//add userAgent
		Request.Builder builder = request.newBuilder().addHeader("User-Agent", "ALOHOMORA-DESKTOP");

		for (String[] route : this.routeExcluded) {
			if (route[0].equalsIgnoreCase(url[3].toLowerCase())) {
				for (int i = 1; i < route.length; ++i) {
					if (route[i].equalsIgnoreCase(method)){
						return chain.proceed(builder.build());
					}
				}
			}
		}

		//add signature & token
		builder.addHeader("X-ALOHOMORA-TOKEN", Configuration.LOGIN_TOKEN);
		builder.addHeader("X-ALOHOMORA-SIGNATURE", ""); // @TODO: check signature
		return chain.proceed(builder.build());
	}

}
