

import fr.alohomora.model.Challenge;
import fr.alohomora.model.User;
import fr.alohomora.model.apiservice.Api;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


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
public class UserTest {

	@Before
	public void init(){

	}

	@Test
	public void getChallengeCheckCode() {

		Api apiService = new Api();
		Call<Challenge> call = apiService.getAlohomoraService().getChallenge();
		try {
			Response<Challenge> response = call.execute();
			assertEquals(response.code(), 200);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void getChallengeCheckChallenge() {

		Api apiService = new Api();
		Call<Challenge> call = apiService.getAlohomoraService().getChallenge();
		try {
			Response<Challenge> response = call.execute();
			assertNotNull(response.body().getChallenge());
			assertNotNull(response.body().getID());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void checkUserConnexion() {
		String passcode = null;
		String challenge = null;
		int challengeID = 0;
		Api apiService = new Api();
		try {
			Call<Challenge> call = apiService.getAlohomoraService().getChallenge();
			Response<Challenge> response = call.execute();
			challenge = response.body().getChallenge();
			challengeID = response.body().getID();
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			String usernameHash = messageDigest.digest("toto".getBytes()).toString();
			String challengeHash = messageDigest.digest(challenge.getBytes()).toString();
			passcode = usernameHash + "toto" + challengeHash;
			String passCodeHash = messageDigest.digest(passcode.getBytes()).toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (passcode != null){

		Call<User> call = apiService.getAlohomoraService().connect(passcode,challengeID,"1","1");
			try {
				Response<User> response = call.execute();
				System.out.print(response.code());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

