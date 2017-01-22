package com.rssaggregator.desktop.utils;

import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.model.Channel;

public class CategoriesUtils {

	public static int getUnreadArticles(Category category) {
		int count = 0;

		if (category.getChannels() != null) {
			for (Channel channel : category.getChannels()) {
				count += channel.getUnread();
			}
		}
		return count;
	}
}
