package com.rssaggregator.desktop.network.event;

public class SignUpEvent extends BaseEvent<Void> {

	public SignUpEvent(Throwable throwable) {
		super(throwable);
	}

	public SignUpEvent() {
		super((Void) null);
	}
}