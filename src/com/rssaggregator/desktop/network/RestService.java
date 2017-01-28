package com.rssaggregator.desktop.network;

import com.rssaggregator.desktop.model.AccessToken;
import com.rssaggregator.desktop.model.AddCategoryWrapper;
import com.rssaggregator.desktop.model.AddFeedWrapper;
import com.rssaggregator.desktop.model.CategoriesWrapper;
import com.rssaggregator.desktop.model.CategoryAddedWrapper;
import com.rssaggregator.desktop.model.Credentials;
import com.rssaggregator.desktop.model.FeedAddedWrapper;
import com.rssaggregator.desktop.model.ItemReadStateWrapper;
import com.rssaggregator.desktop.model.ItemStateWrapper;
import com.rssaggregator.desktop.model.ItemsWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

	@GET("rssserver/categories/{id_category}/")
	Call<CategoriesWrapper> fetchItemsByCategory(@Path("id_category") Integer categoryId);

	@GET("rssserver/feeds/{id_channel}/")
	Call<ItemsWrapper> fetchItemsByChannel(@Path("id_channel") Integer channelId);

	@DELETE("rssserver/feeds/{id_channel}")
	Call<Void> deleteFeed(@Path("id_channel") Integer channelId);

	//
	//
	// ITEMS METHODS.
	//
	//
	@PUT("rssserver/items/{id_item}")
	Call<Void> updateItemState(@Path("id_item") Integer itemId, @Body ItemStateWrapper wrapper);

	@PUT("rssserver/feeds/{id_channel}/items/")
	Call<Void> updateItemStateByChannel(@Path("id_channel") Integer channelId, @Body ItemReadStateWrapper wrapper);
}
