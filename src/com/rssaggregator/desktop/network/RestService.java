package com.rssaggregator.desktop.network;

import com.rssaggregator.desktop.model.ComeOn_Credentials;
import com.rssaggregator.desktop.model.ComeOn_CredentialsWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestService {

	final String BASE_URL = "http://api.comeon.io";

	@POST("/1.0/auth/")
	Call<ComeOn_Credentials> logIn(@Body ComeOn_CredentialsWrapper wrapper);
}
