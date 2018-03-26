

import com.sun.prism.PixelFormat;
import fr.alohomora.Configuration;
import fr.alohomora.database.Database;
import fr.alohomora.model.Challenge;
import fr.alohomora.model.Data;
import fr.alohomora.model.User;
import fr.alohomora.model.apiservice.Api;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Statement;


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
		Configuration.load(new String[]{});

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
		String challenge = null;
		int challengeID = 0;
		String passCodeHash = null;
		Api apiService = new Api();
		try {
			Call<Challenge> call = apiService.getAlohomoraService().getChallenge();
			Response<Challenge> response = call.execute();
			challenge = response.body().getChallenge();
			challengeID = response.body().getID();
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			// hash(username)
			String usernameHash = DatatypeConverter.printHexBinary(messageDigest.digest("toto".getBytes())).toLowerCase();
			// hash(password)
			String passwordHash = DatatypeConverter.printHexBinary(messageDigest.digest("toto".getBytes())).toLowerCase();
			// hash(passwordHash)
			System.out.println(passwordHash);
			passwordHash = DatatypeConverter.printHexBinary(messageDigest.digest(passwordHash.getBytes())).toLowerCase();
			System.out.println(passwordHash);
			String passcode = usernameHash.toLowerCase() + challenge + passwordHash.toLowerCase();

			//hash(passcode)
			passCodeHash = DatatypeConverter.printHexBinary(messageDigest.digest(passcode.getBytes())).toLowerCase();
			System.out.println(passCodeHash);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (passCodeHash != null){
			Call<User> call = apiService.getAlohomoraService().connect(passCodeHash.toLowerCase(),challengeID,"1","test");
			try {
				Response<User> response = call.execute();
				System.out.println(response.code());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



}

