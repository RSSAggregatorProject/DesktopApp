package com.rssaggregator.desktop.network.event;

import com.rssaggregator.desktop.model.CategoriesWrapper;

public class FetchItemsByCategoryEvent extends BaseEvent<CategoriesWrapper> {
	public FetchItemsByCategoryEvent(CategoriesWrapper wrapper) {
		super(wrapper);
	}

	public FetchItemsByCategoryEvent(Throwable throwable) {
		super(throwable);
	}
}
