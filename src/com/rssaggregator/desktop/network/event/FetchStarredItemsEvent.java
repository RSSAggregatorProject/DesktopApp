package com.rssaggregator.desktop.network.event;

import com.rssaggregator.desktop.model.ItemsWrapper;

public class FetchStarredItemsEvent extends BaseEvent<ItemsWrapper> {
	public FetchStarredItemsEvent(ItemsWrapper wrapper) {
		super(wrapper);
	}

	public FetchStarredItemsEvent(Throwable throwable) {
		super(throwable);
	}
}
