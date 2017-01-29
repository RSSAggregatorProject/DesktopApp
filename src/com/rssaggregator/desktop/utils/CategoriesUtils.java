package com.rssaggregator.desktop.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.rssaggregator.desktop.model.CategoriesWrapper;
import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.model.Channel;
import com.rssaggregator.desktop.model.Item;
import com.rssaggregator.desktop.model.ItemsWrapper;

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

	public static List<Item> fillDataFromChannelList(ItemsWrapper data) {
		List<Channel> channels = data.getChannels();
		List<Item> items = new ArrayList<Item>();

		if (channels != null && channels.size() != 0) {

			for (Channel channel : channels) {

				if (channel.getItems() != null && channel.getItems().size() != 0) {

					for (Item item : channel.getItems()) {
						item.setChannelId(channel.getChannelId());
						item.setChannelName(channel.getName());
						items.add(item);
					}
				}
			}
		}

		if (items != null && items.size() != 0) {
			Collections.sort(items, new Comparator<Item>() {
				@Override
				public int compare(Item item1, Item item2) {
					return item2.getPubDate().compareTo(item1.getPubDate());
				}
			});
		}
		return items;
	}

	public static List<Item> fillDataFromChannelList(CategoriesWrapper data) {
		List<Category> categories = data.getCategories();
		List<Item> items = new ArrayList<Item>();

		if (categories != null && categories.size() != 0) {

			for (Category category : categories) {

				if (category.getChannels() != null && category.getChannels().size() != 0) {

					for (Channel channel : category.getChannels()) {

						if (channel.getItems() != null && channel.getItems().size() != 0) {

							for (Item item : channel.getItems()) {
								item.setChannelId(channel.getChannelId());
								item.setChannelName(channel.getName());
								items.add(item);
							}
						}
					}
				}
			}
		}

		if (items != null && items.size() != 0) {
			Collections.sort(items, new Comparator<Item>() {
				@Override
				public int compare(Item item1, Item item2) {
					return item2.getPubDate().compareTo(item1.getPubDate());
				}
			});
		}
		return items;
	}
}
