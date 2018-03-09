package alohomora.model.apiservice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
