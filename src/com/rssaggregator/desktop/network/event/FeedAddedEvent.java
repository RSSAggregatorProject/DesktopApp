package com.rssaggregator.desktop.network.event;

import com.rssaggregator.desktop.model.FeedAddedWrapper;

public class FeedAddedEvent extends BaseEvent<FeedAddedWrapper> {
	public FeedAddedEvent(FeedAddedWrapper wrapper) {
		super(wrapper);
	}

	public FeedAddedEvent(Throwable throwable) {
		super(throwable);
	}
}
