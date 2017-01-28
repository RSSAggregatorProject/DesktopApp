package com.rssaggregator.desktop.network.event;

public class ItemsByChannelUpdatedEvent extends BaseEvent<Void> {

	public ItemsByChannelUpdatedEvent(Throwable throwable) {
		super(throwable);
	}

	public ItemsByChannelUpdatedEvent() {
		super((Void) null);
	}
}
