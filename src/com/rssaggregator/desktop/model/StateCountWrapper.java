package com.rssaggregator.desktop.model;

/**
 * Class which holds number of unread items and number of starred items.
 * 
 * @author Irina
 *
 */
public class StateCountWrapper {

	private int readCount;
	private int starCount;

	public StateCountWrapper(int readCount, int starCount) {
		this.readCount = readCount;
		this.starCount = starCount;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public int getStarCount() {
		return starCount;
	}

	public void setStarCount(int starCount) {
		this.starCount = starCount;
	}
}
