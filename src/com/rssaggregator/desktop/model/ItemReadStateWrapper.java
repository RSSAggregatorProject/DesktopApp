package com.rssaggregator.desktop.model;

import com.google.gson.annotations.Expose;

public class ItemReadStateWrapper {

	@Expose
	private boolean read;

	public ItemReadStateWrapper(boolean read) {
		this.read = read;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}
