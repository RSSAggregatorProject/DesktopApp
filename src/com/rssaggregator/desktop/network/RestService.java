package com.rssaggregator.desktop.network;

import com.rssaggregator.desktop.model.CategoriesWrapper;
import com.rssaggregator.desktop.model.Credentials;
import com.rssaggregator.desktop.model.ItemsWrapper;
import com.rssaggregator.desktop.model.SignUpWrapper;
import com.rssaggregator.desktop.model.StarItemsWrapper;
import com.rssaggregator.desktop.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RestService {

	final String BASE_URL = "http://api.comeon.io";

	// AUTH AND USER METHODS
	@POST("/auth/")
	Call<User> logIn(@Body Credentials credentials);

	@POST("/users/")
	Call<SignUpWrapper> signUp(@Body Credentials credentials);

	// CATEGORIES METHODS
	@GET("/categories/")
	Call<CategoriesWrapper> fetchCategories(@Header("Authorization") String authorization);

	// CHANNELS METHODS
	@GET("/feeds/")
	Call<ItemsWrapper> fetchChannels(@Header("Authorization") String authorization/*, @Body StarItemsWrapper wrapper*/);
}
