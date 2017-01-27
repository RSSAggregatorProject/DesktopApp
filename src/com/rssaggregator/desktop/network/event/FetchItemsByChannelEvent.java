package com.rssaggregator.desktop.network.event;

import com.rssaggregator.desktop.model.ItemsWrapper;

public class FetchItemsByChannelEvent extends BaseEvent<ItemsWrapper> {
	public FetchItemsByChannelEvent(ItemsWrapper wrapper) {
		super(wrapper);
	}

	public FetchItemsByChannelEvent(Throwable throwable) {
		super(throwable);
	}
}
