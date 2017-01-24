package com.rssaggregator.desktop.network.event;

import com.rssaggregator.desktop.model.SignUpWrapper;

public class SignUpEvent extends BaseEvent<SignUpWrapper> {
	public SignUpEvent(SignUpWrapper wrapper) {
		super(wrapper);
	}

	public SignUpEvent(Throwable throwable) {
		super(throwable);
	}
}
