package com.rssaggregator.desktop.model;

import com.google.gson.annotations.Expose;

public class ItemStateWrapper {

	@Expose
	private boolean starred;
	@Expose
	private boolean read;

	public ItemStateWrapper(boolean starred, boolean read) {
		this.starred = starred;
		this.read = read;
	}

	public boolean isStarred() {
		return starred;
	}

	public void setStarred(boolean starred) {
		this.starred = starred;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}
