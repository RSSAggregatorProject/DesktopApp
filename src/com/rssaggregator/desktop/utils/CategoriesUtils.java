package com.rssaggregator.desktop.utils;

import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.model.Channel;

import javafx.collections.ObservableList;

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

	public static boolean isAlreadyCreated(ObservableList<Category> categories, String categoryName) {
		if (categories == null) {
			return false;
		}
		if (categories.size() == 0) {
			return false;
		}

		for (Category category : categories) {
			if (categoryName.equals(category.getName())) {
				return true;
			}
		}
		return false;
	}

	public static Category getCategoryByName(ObservableList<Category> categories, String categoryName) {
		if (categories == null) {
			return null;
		}
		if (categories.size() == 0) {
			return null;
		}

		for (Category category : categories) {
			if (categoryName.equals(category.getName())) {
				return category;
			}
		}
		return null;
	}
}
