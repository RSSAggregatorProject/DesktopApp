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
import com.rssaggregator.desktop.model.StateCountWrapper;

import javafx.collections.ObservableList;

/**
 * Utility class with some Array methods.
 * 
 * @author Irina
 *
 */
public class CategoriesUtils {

	/**
	 * Gets the number of unread articles in a category.
	 * 
	 * @param category
	 *            Category to get the count.
	 * @return Number of unread items.
	 */
	public static int getUnreadArticles(Category category) {
		int count = 0;

		if (category.getChannels() != null) {
			for (Channel channel : category.getChannels()) {
				count += channel.getUnread();
			}
		}
		return count;
	}

	/**
	 * Knows if the category is already created or not/
	 * 
	 * @param categories
	 *            List of categories already created
	 * @param categoryName
	 *            new category to create
	 * @return boolean is already created or not.
	 */
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

	/**
	 * Gets a category thanks to its name.
	 * 
	 * @param categories
	 *            List of Category to find
	 * @param categoryName
	 *            Name of the category to search
	 * @return Category found in the list. Returns null if the category is not
	 *         in the list.
	 */
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

	/**
	 * Fills a list of items and sorts it.
	 * 
	 * @param data
	 *            Data received to fill.
	 * 
	 * @return List of Items with the data.
	 */
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

	/**
	 * Fills a list of items and sorts it.
	 * 
	 * @param data
	 *            Data received to fill.
	 * 
	 * @return List of Items with the data.
	 */
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

	/**
	 * Gets the number of unread and star items.
	 * 
	 * @param categories
	 *            Data
	 * 
	 * @return Number of unread and star items wrapped in a class.
	 */
	public static StateCountWrapper getCountStateItems(ObservableList<Category> categories) {
		StateCountWrapper wrapper = new StateCountWrapper(0, 0);
		int unreadCount = 0;
		int starCount = 0;

		if (categories == null || categories.size() == 0) {
			return wrapper;
		}

		for (Category category : categories) {
			if (category.getChannels() != null && category.getChannels().size() != 0) {
				for (Channel channel : category.getChannels()) {
					if (channel != null && channel.getUnread() != null) {
						unreadCount += channel.getUnread();
					}
				}
			}
		}
		wrapper.setReadCount(unreadCount);
		wrapper.setStarCount(starCount);
		return wrapper;
	}
}
