package com.rssaggregator.desktop.network;

import com.google.common.eventbus.EventBus;

public interface RssApi {

	void setEventBus(EventBus eventBus);

	void logIn(String userEmail, String userPassword);

	void signUp(String userEmail, String userPassword);
	
	void fetchCategories(String authorization);
}
