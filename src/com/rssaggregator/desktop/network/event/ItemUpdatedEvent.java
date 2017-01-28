package com.rssaggregator.desktop.network.event;

public class ItemUpdatedEvent extends BaseEvent<Void> {

	public ItemUpdatedEvent(Throwable throwable) {
		super(throwable);
	}

	public ItemUpdatedEvent() {
		super((Void) null);
	}
}
