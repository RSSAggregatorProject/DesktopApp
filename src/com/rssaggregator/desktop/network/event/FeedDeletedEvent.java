package com.rssaggregator.desktop.network.event;

public class FeedDeletedEvent extends BaseEvent<Void> {

	public FeedDeletedEvent(Throwable throwable) {
		super(throwable);
	}

	public FeedDeletedEvent() {
		super((Void) null);
	}
}
