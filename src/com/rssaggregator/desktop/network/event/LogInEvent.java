package com.rssaggregator.desktop.network.event;

import com.rssaggregator.desktop.model.AccessToken;

public class LogInEvent extends BaseEvent<AccessToken> {
	public LogInEvent(AccessToken accessToken) {
		super(accessToken);
	}

	public LogInEvent(Throwable throwable) {
		super(throwable);
	}
}
