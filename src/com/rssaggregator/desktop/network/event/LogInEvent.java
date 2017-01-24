package com.rssaggregator.desktop.network.event;

import com.rssaggregator.desktop.model.User;

public class LogInEvent extends BaseEvent<User> {
	public LogInEvent(User user) {
		super(user);
	}

	public LogInEvent(Throwable throwable) {
		super(throwable);
	}
}
