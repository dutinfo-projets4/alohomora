package alohomora.model.apiservice;

import alohomora.model.Challenge;
import alohomora.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AlohomoraService {
	@FormUrlEncoded
	@POST("api/users")
	Call<User> ChallengeConnect(@Field("passcode") String passcode,
								@Field("challenge") int challenge,
								@Field("publickey") String publickey,
								@Field("machine_name") String machine_name);

	@GET("api/users")
	Call<Challenge> getChallenge();

}
