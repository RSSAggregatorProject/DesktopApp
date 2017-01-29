package com.rssaggregator.desktop.model;

import com.google.gson.annotations.Expose;

public class StarItemsWrapper {

	@Expose
	private boolean starred;

	public boolean isStarred() {
		return starred;
	}

	public void setStarred(boolean starred) {
		this.starred = starred;
	}
}
