package fr.alohomora.model.apiservice;

import fr.alohomora.model.Challenge;
import fr.alohomora.model.Element;
import fr.alohomora.model.Group;
import fr.alohomora.model.User;
import retrofit2.Call;
import retrofit2.http.*;

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
public interface AlohomoraService {

	@FormUrlEncoded
	@POST("/users")
	Call<User> connect(@Field("passcode") String passcode,
	                   @Field("challenge") String challenge,
	                   @Field("public_key") String public_key,
	                   @Field("machine_name") String machine_name);

	@GET("/challenge")
	Call<Challenge> getChallenge();

	@FormUrlEncoded
	@POST("/element")
	Call<Element> addElement(@Field("req_id") String req_id,
	                         @Field("parent_grp") String parent_grp,
	                         @Field("content") String content);
	@FormUrlEncoded
	@PUT("/element")
	Call<Void> updateElement(@Field("req_id") String req_id,
	                            @Field("id") String id,
	                            @Field("parent_grp") String parent_grp,
	                            @Field("content") String content);
	@DELETE("/element")
	Call<Void> removeElement(@Query("req_id") String req_id,
	                            @Query("id") String IDelement);

	@FormUrlEncoded
	@POST("/directory")
	Call<Group> addGroup(@Field("req_id") String req_id,
	                     @Field("parent_grp") String parent_grp,
	                     @Field("content") String content);
	@FormUrlEncoded
	@PUT("/directory")
	Call<Void> updateGroup(@Field("req_id") String req_id,
	                         @Field("id") String id,
	                         @Field("parent_grp") String parent_grp,
	                         @Field("content") String content);
}
