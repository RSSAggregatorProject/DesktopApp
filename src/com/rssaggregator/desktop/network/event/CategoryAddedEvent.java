package com.rssaggregator.desktop.network.event;

import com.rssaggregator.desktop.model.CategoryAddedWrapper;

public class CategoryAddedEvent extends BaseEvent<CategoryAddedWrapper> {
	public CategoryAddedEvent(CategoryAddedWrapper wrapper) {
		super(wrapper);
	}

	public CategoryAddedEvent(Throwable throwable) {
		super(throwable);
	}
}
