package com.rssaggregator.desktop.network;

import com.rssaggregator.desktop.model.Credentials;
import com.rssaggregator.desktop.model.CredentialsWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestService {

	final String BASE_URL = "http://api.comeon.io";

	@POST("/1.0/auth/")
	Call<Credentials> logIn(@Body CredentialsWrapper wrapper);
}
