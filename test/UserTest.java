

import fr.alohomora.model.Challenge;
import fr.alohomora.model.apiservice.Api;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;


import static junit.framework.TestCase.assertEquals;
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

	@Test
	public void getChallenge() {

		Api apiService = new Api();
		Call<Challenge> call = apiService.getAlohomoraService().getChallenge();
		try {
			Response<Challenge> response = call.execute();
			assertEquals(response.code(),200);
		} catch (IOException e) {
			fail(e.getMessage());
		}


	}

}

