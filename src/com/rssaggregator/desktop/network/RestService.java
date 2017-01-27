package com.rssaggregator.desktop.network;

import com.rssaggregator.desktop.model.AccessToken;
import com.rssaggregator.desktop.model.AddCategoryWrapper;
import com.rssaggregator.desktop.model.AddFeedWrapper;
import com.rssaggregator.desktop.model.CategoriesWrapper;
import com.rssaggregator.desktop.model.CategoryAddedWrapper;
import com.rssaggregator.desktop.model.Credentials;
import com.rssaggregator.desktop.model.FeedAddedWrapper;
import com.rssaggregator.desktop.model.ItemsWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestService {

	//
	//
	// AUTH AND USER METHODS
	//
	//
	@POST("rssserver/auth/")
	Call<AccessToken> logIn(@Body Credentials credentials);

	@POST("rssserver/users/")
	Call<Void> signUp(@Body Credentials credentials);

	//
	//
	// CATEGORIES METHODS
	//
	//
	@GET("rssserver/categories/")
	Call<CategoriesWrapper> fetchCategories();

	@POST("rssserver/categories/")
	Call<CategoryAddedWrapper> addCategory(@Body AddCategoryWrapper wrapper);

	//
	//
	// CHANNELS METHODS
	//
	//
	@POST("rssserver/feeds/")
	Call<FeedAddedWrapper> addFeed(@Body AddFeedWrapper wrapper);

	@GET("rssserver/feeds/")
	Call<ItemsWrapper> fetchAllItems();

	@GET("rssserver/feeds/starred/")
	Call<ItemsWrapper> fetchStarredItems();

	@GET("rssserver/feeds/{id_channel}/")
	Call<ItemsWrapper> fetchItemsByChannel(@Path("id_channel") Integer channelId);

	@DELETE("rssserver/feeds/{id_channel}")
	Call<Void> deleteFeed(@Path("id_channel") Integer channelId);
}
