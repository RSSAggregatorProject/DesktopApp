package com.rssaggregator.desktop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.rssaggregator.desktop.model.CategoriesWrapper;
import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.model.Channel;
import com.rssaggregator.desktop.model.Item;
import com.rssaggregator.desktop.model.ItemsWrapper;
import com.rssaggregator.desktop.utils.CategoriesUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoriesUtilsTest {

	private List<Category> setCategoriesData() {
		List<Category> categories = new ArrayList<>();
		List<Channel> channels = new ArrayList<>();
		List<Item> items = new ArrayList<>();

		Category category = new Category();
		Channel channel = new Channel();
		Channel channel2 = new Channel();
		channel2.setUnread(3);
		channel.setUnread(20);
		Item itemFalse = new Item();
		itemFalse.setPubDate(new Date());
		itemFalse.setRead(false);
		Item itemTrue = new Item();
		itemTrue.setPubDate(new Date());
		itemTrue.setRead(true);
		Item item = new Item();
		item.setPubDate(new Date());
		item.setRead(false);
		items.add(item);
		items.add(itemFalse);
		items.add(itemTrue);
		channel.setItems(items);
		channels.add(channel);
		channels.add(channel2);
		category.setChannels(channels);
		categories.add(category);

		return categories;
	}

	private ObservableList<Category> setCategoriesObservableData() {
		ObservableList<Category> observableCategories = FXCollections.observableArrayList();
		List<Category> categories = new ArrayList<>();
		Category category = new Category();
		Category category1 = new Category();
		Category category2 = new Category();
		category.setName("Category");
		category.setCategoryId(0);
		category1.setName("Category1");
		category1.setCategoryId(1);
		category2.setName("Category2");
		category2.setCategoryId(2);
		categories.add(category);
		categories.add(category1);
		categories.add(category2);
		observableCategories.addAll(categories);
		return observableCategories;
	}

	@Test
	public void simpleTest() {
		Assert.assertTrue(true);
	}

	@Test
	public void getUnreadArticlesNull_Test() {
		Assert.assertEquals(0, CategoriesUtils.getUnreadArticles(null));
	}

	@Test
	public void getUnreadArticlesEmpty_Test() {
		Assert.assertEquals(0, CategoriesUtils.getUnreadArticles(new Category()));
	}

	@Test
	public void getUnreadArticlesFilled_Test() {
		List<Category> categories = setCategoriesData();
		Assert.assertEquals(23, CategoriesUtils.getUnreadArticles(categories.get(0)));
	}

	@Test
	public void isAlreadyCreatedNullNull_Test() {
		Assert.assertEquals(false, CategoriesUtils.isAlreadyCreated(null, null));
	}

	@Test
	public void isAlreadyCreatedEmptyNull_Test() {
		Assert.assertEquals(false, CategoriesUtils.isAlreadyCreated(FXCollections.observableArrayList(), null));
	}

	@Test
	public void isAlreadyCreatedFilled_Test() {
		ObservableList<Category> categories = setCategoriesObservableData();
		Assert.assertEquals(false, CategoriesUtils.isAlreadyCreated(categories, "Sport"));
		Assert.assertEquals(true, CategoriesUtils.isAlreadyCreated(categories, "Category2"));
	}

	@Test
	public void getCategoryByNameNull_Test() {
		Assert.assertEquals(null, CategoriesUtils.getCategoryByName(null, "Test"));
		Assert.assertEquals(null, CategoriesUtils.getCategoryByName(null, null));
		Category category = CategoriesUtils.getCategoryByName(setCategoriesObservableData(), null);
		Assert.assertEquals(null, category);
	}

	@Test
	public void getCategoryByNameEmpty_Test() {
		Assert.assertEquals(null, CategoriesUtils.getCategoryByName(FXCollections.observableArrayList(), "Test"));
	}

	@Test
	public void getCategoryByNameFilled_Test() {
		Category category = CategoriesUtils.getCategoryByName(setCategoriesObservableData(), "Category1");
		Assert.assertEquals("1", category.getCategoryId().toString());
	}

	@Test
	public void fillDataFromChannelListItemsWrapperNull_Test() {
		ItemsWrapper wrapper = null;
		int size = CategoriesUtils.fillDataFromChannelList(wrapper).size();
		Assert.assertEquals(0, size);
	}

	@Test
	public void fillDataFromChannelListCategoriesWrapperNull_Test() {
		CategoriesWrapper wrapper = null;
		int size = CategoriesUtils.fillDataFromChannelList(wrapper).size();
		Assert.assertEquals(0, size);
	}

	@Test
	public void fillDataFromChannelListItemsWrapperFilled_Test() {
		ItemsWrapper wrapper = new ItemsWrapper();
		List<Category> categories = setCategoriesData();
		wrapper.setChannels(categories.get(0).getChannels());

		int size = CategoriesUtils.fillDataFromChannelList(wrapper).size();
		Assert.assertEquals(3, size);
	}

	@Test
	public void fillDataFromChannelListCategoriesWrapperFilled_Test() {
		CategoriesWrapper wrapper = new CategoriesWrapper();
		List<Category> categories = setCategoriesData();
		wrapper.setCategories(categories);

		int size = CategoriesUtils.fillDataFromChannelList(wrapper).size();
		Assert.assertEquals(3, size);
	}
}
