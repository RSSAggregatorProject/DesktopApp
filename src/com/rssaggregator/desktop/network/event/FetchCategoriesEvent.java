package com.rssaggregator.desktop.network.event;

import com.rssaggregator.desktop.model.CategoriesWrapper;

public class FetchCategoriesEvent extends BaseEvent<CategoriesWrapper> {
	public FetchCategoriesEvent(CategoriesWrapper wrapper) {
		super(wrapper);
	}

	public FetchCategoriesEvent(Throwable throwable) {
		super(throwable);
	}
}
