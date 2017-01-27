package com.rssaggregator.desktop.network;

import com.google.common.eventbus.EventBus;

public interface RssApi {

	void setEventBus(EventBus eventBus);

	void logIn(String userEmail, String userPassword);

	void signUp(String userEmail, String userPassword);

	//
	//
	// Categories
	//
	//

	void fetchCategories();

	void addCategory(String categoryName);

	//
	//
	// Channels
	//
	//
	void addFeed(Integer categoryId, String rssLink);

	//
	//
	// Fetching data
	//
	//
	void fetchAllItems();
}
