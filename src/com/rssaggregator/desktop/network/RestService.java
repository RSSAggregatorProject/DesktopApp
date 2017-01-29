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

/**
 * Interface of the REST API.
 * 
 * @author Irina
 *
 */
public interface RestService {

	//
	//
	// AUTH AND USER METHODS
	//
	//
	@POST("auth")
	Call<AccessToken> logIn(@Body Credentials credentials);

	@POST("users")
	Call<Void> signUp(@Body Credentials credentials);

	//
	//
	// CATEGORIES METHODS
	//
	//
	@GET("categories")
	Call<CategoriesWrapper> fetchCategories();

	@POST("categories")
	Call<CategoryAddedWrapper> addCategory(@Body AddCategoryWrapper wrapper);

	//
	//
	// CHANNELS METHODS
	//
	//
	@POST("feeds")
	Call<FeedAddedWrapper> addFeed(@Body AddFeedWrapper wrapper);

	@GET("feeds")
	Call<ItemsWrapper> fetchAllItems();

	@GET("feeds/starred/")
	Call<ItemsWrapper> fetchStarredItems();

	@GET("categories/{id_category}/")
	Call<CategoriesWrapper> fetchItemsByCategory(@Path("id_category") Integer categoryId);

	@GET("feeds/{id_channel}/")
	Call<ItemsWrapper> fetchItemsByChannel(@Path("id_channel") Integer channelId);

	@DELETE("feeds/{id_channel}")
	Call<Void> deleteFeed(@Path("id_channel") Integer channelId);

	//
	//
	// ITEMS METHODS.
	//
	//
	@PUT("items/{id_item}")
	Call<Void> updateItemState(@Path("id_item") Integer itemId, @Body ItemStateWrapper wrapper);

	@PUT("feeds/{id_channel}/items/")
	Call<Void> updateItemStateByChannel(@Path("id_channel") Integer channelId, @Body ItemReadStateWrapper wrapper);
}
