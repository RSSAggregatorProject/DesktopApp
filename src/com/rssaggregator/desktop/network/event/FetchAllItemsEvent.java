package com.rssaggregator.desktop.network.event;

import com.rssaggregator.desktop.model.ItemsWrapper;

public class FetchAllItemsEvent extends BaseEvent<ItemsWrapper> {
	public FetchAllItemsEvent(ItemsWrapper wrapper) {
		super(wrapper);
	}

	public FetchAllItemsEvent(Throwable throwable) {
		super(throwable);
	}
}
