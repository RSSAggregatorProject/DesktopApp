package com.rssaggregator.desktop.network;

import com.google.common.eventbus.EventBus;
import com.rssaggregator.desktop.model.ItemReadStateWrapper;
import com.rssaggregator.desktop.model.ItemStateWrapper;

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

	void deleteFeed(Integer channelId);

	//
	//
	// Fetching data
	//
	//
	void fetchAllItems();

	void fetchStarredItems();

	void fetchItemsByCategory(Integer categoryId);

	void fetchItemsByChannel(Integer channelId);

	//
	//
	// Items
	//
	//
	void updateItemState(Integer itemId, ItemStateWrapper wrapper);

	void updateItemStateByChannel(Integer channelId, ItemReadStateWrapper wrapper);
}
