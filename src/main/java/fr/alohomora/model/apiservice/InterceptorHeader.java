package fr.alohomora.model.apiservice;


import com.google.gson.Gson;
import fr.alohomora.Configuration;
import fr.alohomora.crypto.RSAObject;
import javafx.util.Pair;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;


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

	private static String[][] routeExcluded = new String[][]{
				{"users", "GET", "POST", "PUT"},
				{"challenge", "GET"}
		};
	private Pair<String,String>[] map;

	/**
	 * constructor interceptor (cf okhttp3.Interceptor) and create the differents excluded root
	 */
	public InterceptorHeader(Pair<String, String>[] map) {
		super();
		this.map = map;
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

		for (String[] route : InterceptorHeader.routeExcluded) {
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

		JSONObject obj = new JSONObject();

		for (Pair<String, String> val : this.map){
			obj.put(val.getKey(), val.getValue());
		}

		RSAObject rsa = null;
		try {
			rsa = new RSAObject("key");
			builder.addHeader("X-ALOHOMORA-SIGNATURE", rsa.sign(obj.toJSONString()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return chain.proceed(builder.build());
	}


}
