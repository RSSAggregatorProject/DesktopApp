package com.rssaggregator.desktop.network;

import java.util.Date;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.model.AccessToken;
import com.rssaggregator.desktop.model.AddCategoryWrapper;
import com.rssaggregator.desktop.model.AddFeedWrapper;
import com.rssaggregator.desktop.model.ApiError;
import com.rssaggregator.desktop.model.CategoriesWrapper;
import com.rssaggregator.desktop.model.CategoryAddedWrapper;
import com.rssaggregator.desktop.model.Credentials;
import com.rssaggregator.desktop.model.FeedAddedWrapper;
import com.rssaggregator.desktop.model.ItemReadStateWrapper;
import com.rssaggregator.desktop.model.ItemStateWrapper;
import com.rssaggregator.desktop.model.ItemsWrapper;
import com.rssaggregator.desktop.network.event.CategoryAddedEvent;
import com.rssaggregator.desktop.network.event.FeedAddedEvent;
import com.rssaggregator.desktop.network.event.FeedDeletedEvent;
import com.rssaggregator.desktop.network.event.FetchAllItemsEvent;
import com.rssaggregator.desktop.network.event.FetchCategoriesEvent;
import com.rssaggregator.desktop.network.event.FetchItemsByCategoryEvent;
import com.rssaggregator.desktop.network.event.FetchItemsByChannelEvent;
import com.rssaggregator.desktop.network.event.FetchStarredItemsEvent;
import com.rssaggregator.desktop.network.event.ItemUpdatedEvent;
import com.rssaggregator.desktop.network.event.ItemsByChannelUpdatedEvent;
import com.rssaggregator.desktop.network.event.LogInEvent;
import com.rssaggregator.desktop.network.event.SignUpEvent;
import com.rssaggregator.desktop.utils.DateDeserializer;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.PreferencesUtils;
import com.rssaggregator.desktop.utils.TokenRequestInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class which fetches/send data from the API.
 * 
 * @author Irina
 *
 */
public class RssApiImpl implements RssApi {
	private RestService restService;
	private EventBus eventBus;

	/**
	 * Constructor
	 * 
	 * @param restService
	 * @param eventBus
	 */
	public RssApiImpl(RestService restService, EventBus eventBus) {
		this.restService = restService;
		this.eventBus = eventBus;
	}

	/**
	 * Sets the EventBus attribute.
	 */
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	/**
	 * Initializes the Network attributes.
	 */
	private void initializeNetworkAttributes() {

		// Set OkHttpClient.
		OkHttpClient client = MainApp.getOkHttpClient();
		TokenRequestInterceptor tokenRequestInterceptor = MainApp.getTokenRequestInterceptor();
		if (client == null) {
			HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
			interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.addInterceptor(interceptor);

			if (tokenRequestInterceptor == null) {
				String apiToken = PreferencesUtils.getApiToken();
				AccessToken accessToken = new AccessToken();
				accessToken.setApiToken(apiToken);
				if (apiToken != null) {
					builder.addInterceptor(new TokenRequestInterceptor(accessToken));
				} else {
					builder.addInterceptor(new TokenRequestInterceptor());
				}
			} else {
				builder.addInterceptor(tokenRequestInterceptor);
			}

			client = builder.build();
		}

		// Set Retrofit.
		Retrofit retrofit = MainApp.getRetrofit();
		if (retrofit == null) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
			gsonBuilder.excludeFieldsWithoutExposeAnnotation();
			Gson gson = gsonBuilder.create();

			Builder retrofitBuilder = new Retrofit.Builder();
			retrofitBuilder.baseUrl(Globals.API_SERVER_URL);
			retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson));
			retrofitBuilder.client(client);
			retrofit = retrofitBuilder.build();
		}

		this.restService = retrofit.create(RestService.class);
	}

	/**
	 * Logs In from the API.
	 */
	@Override
	public void logIn(String userEmail, String userPassword) {
		Credentials credentials = new Credentials(userEmail, userPassword);

		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.logIn(credentials).enqueue(new Callback<AccessToken>() {

			@Override
			public void onFailure(Call<AccessToken> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new LogInEvent(throwable));
				} else {
					eventBus.post(new LogInEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
				if (response.isSuccessful()) {
					AccessToken accessToken = response.body();
					PreferencesUtils.setUserConnected(userEmail, userPassword, accessToken.getApiToken(),
							accessToken.getUserId(), true);

					// Set Interceptor
					MainApp.getTokenRequestInterceptor().setAccessToken(accessToken);

					eventBus.post(new LogInEvent(accessToken));
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new LogInEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new LogInEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new LogInEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}

	/**
	 * Signs Up from the API.
	 */
	@Override
	public void signUp(String userEmail, String userPassword) {
		Credentials credentials = new Credentials(userEmail, userPassword);

		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.signUp(credentials).enqueue(new Callback<Void>() {

			@Override
			public void onFailure(Call<Void> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new SignUpEvent(throwable));
				} else {
					eventBus.post(new SignUpEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				if (response.isSuccessful()) {
					eventBus.post(new SignUpEvent());
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new SignUpEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new SignUpEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new SignUpEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}

	/**
	 * Fetches categories from the API.
	 */
	@Override
	public void fetchCategories() {
		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.fetchCategories().enqueue(new Callback<CategoriesWrapper>() {
			@Override
			public void onFailure(Call<CategoriesWrapper> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new FetchCategoriesEvent(throwable));
				} else {
					eventBus.post(new FetchCategoriesEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<CategoriesWrapper> call, Response<CategoriesWrapper> response) {
				if (response.isSuccessful()) {
					CategoriesWrapper wrapper = response.body();
					eventBus.post(new FetchCategoriesEvent(wrapper));
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new FetchCategoriesEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new FetchCategoriesEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new FetchCategoriesEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}

	/**
	 * Adds a category from the API.
	 */
	@Override
	public void addCategory(String categoryName) {
		AddCategoryWrapper wrapper = new AddCategoryWrapper(categoryName);

		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.addCategory(wrapper).enqueue(new Callback<CategoryAddedWrapper>() {
			@Override
			public void onFailure(Call<CategoryAddedWrapper> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new CategoryAddedEvent(throwable));
				} else {
					eventBus.post(new CategoryAddedEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<CategoryAddedWrapper> call, Response<CategoryAddedWrapper> response) {
				if (response.isSuccessful()) {
					CategoryAddedWrapper wrapper = response.body();
					eventBus.post(new CategoryAddedEvent(wrapper));
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new CategoryAddedEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new CategoryAddedEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new CategoryAddedEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}

	/**
	 * Adds a feed from the API.
	 */
	@Override
	public void addFeed(Integer categoryId, String rssLink) {
		AddFeedWrapper wrapper = new AddFeedWrapper(categoryId, rssLink);

		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.addFeed(wrapper).enqueue(new Callback<FeedAddedWrapper>() {
			@Override
			public void onFailure(Call<FeedAddedWrapper> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new FeedAddedEvent(throwable));
				} else {
					eventBus.post(new FeedAddedEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<FeedAddedWrapper> call, Response<FeedAddedWrapper> response) {
				if (response.isSuccessful()) {
					FeedAddedWrapper wrapper = response.body();
					eventBus.post(new FeedAddedEvent(wrapper));
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new FeedAddedEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new FeedAddedEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new FeedAddedEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}

	/**
	 * Fetchs all items from the api.
	 */
	@Override
	public void fetchAllItems() {
		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.fetchAllItems().enqueue(new Callback<ItemsWrapper>() {
			@Override
			public void onFailure(Call<ItemsWrapper> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new FetchAllItemsEvent(throwable));
				} else {
					eventBus.post(new FetchAllItemsEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<ItemsWrapper> call, Response<ItemsWrapper> response) {
				if (response.isSuccessful()) {
					ItemsWrapper wrapper = response.body();
					eventBus.post(new FetchAllItemsEvent(wrapper));
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new FetchAllItemsEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new FetchAllItemsEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new FetchAllItemsEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}

	/**
	 * Fetches starred items from the api.
	 */
	@Override
	public void fetchStarredItems() {
		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.fetchStarredItems().enqueue(new Callback<ItemsWrapper>() {
			@Override
			public void onFailure(Call<ItemsWrapper> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new FetchStarredItemsEvent(throwable));
				} else {
					eventBus.post(new FetchStarredItemsEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<ItemsWrapper> call, Response<ItemsWrapper> response) {
				if (response.isSuccessful()) {
					ItemsWrapper wrapper = response.body();
					eventBus.post(new FetchStarredItemsEvent(wrapper));
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new FetchStarredItemsEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new FetchStarredItemsEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new FetchStarredItemsEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}

	/**
	 * Fetches items by category from the api.
	 */
	@Override
	public void fetchItemsByCategory(Integer categoryId) {
		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.fetchItemsByCategory(categoryId).enqueue(new Callback<CategoriesWrapper>() {
			@Override
			public void onFailure(Call<CategoriesWrapper> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new FetchItemsByCategoryEvent(throwable));
				} else {
					eventBus.post(new FetchItemsByCategoryEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<CategoriesWrapper> call, Response<CategoriesWrapper> response) {
				if (response.isSuccessful()) {
					CategoriesWrapper wrapper = response.body();
					eventBus.post(new FetchItemsByCategoryEvent(wrapper));
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new FetchItemsByCategoryEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new FetchItemsByCategoryEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new FetchItemsByCategoryEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}

	/**
	 * Fetches items by channel from the api.
	 */
	@Override
	public void fetchItemsByChannel(Integer channelId) {
		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.fetchItemsByChannel(channelId).enqueue(new Callback<ItemsWrapper>() {
			@Override
			public void onFailure(Call<ItemsWrapper> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new FetchItemsByChannelEvent(throwable));
				} else {
					eventBus.post(new FetchItemsByChannelEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<ItemsWrapper> call, Response<ItemsWrapper> response) {
				if (response.isSuccessful()) {
					ItemsWrapper wrapper = response.body();
					eventBus.post(new FetchItemsByChannelEvent(wrapper));
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new FetchItemsByChannelEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new FetchItemsByChannelEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new FetchItemsByChannelEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}

	/**
	 * Updates states of an item by calling the API.
	 */
	@Override
	public void updateItemState(Integer itemId, ItemStateWrapper wrapper) {
		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.updateItemState(itemId, wrapper).enqueue(new Callback<Void>() {
			@Override
			public void onFailure(Call<Void> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new ItemUpdatedEvent(throwable));
				} else {
					eventBus.post(new ItemUpdatedEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				if (response.isSuccessful()) {
					eventBus.post(new ItemUpdatedEvent());
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new ItemUpdatedEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new ItemUpdatedEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new ItemUpdatedEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}

	/**
	 * Updates all the items of a channel by calling the API
	 */
	@Override
	public void updateItemStateByChannel(Integer channelId, ItemReadStateWrapper wrapper) {
		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.updateItemStateByChannel(channelId, wrapper).enqueue(new Callback<Void>() {
			@Override
			public void onFailure(Call<Void> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new ItemsByChannelUpdatedEvent(throwable));
				} else {
					eventBus.post(new ItemsByChannelUpdatedEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				if (response.isSuccessful()) {
					eventBus.post(new ItemsByChannelUpdatedEvent());
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new ItemsByChannelUpdatedEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new ItemsByChannelUpdatedEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new ItemsByChannelUpdatedEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}

	/**
	 * Unsubscribe the feed by calling the API.
	 */
	@Override
	public void deleteFeed(Integer channelId) {
		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.deleteFeed(channelId).enqueue(new Callback<Void>() {
			@Override
			public void onFailure(Call<Void> call, Throwable throwable) {
				if (throwable != null && throwable.getMessage() != null && throwable.getMessage().length() != 0) {
					eventBus.post(new FeedDeletedEvent(throwable));
				} else {
					eventBus.post(new FeedDeletedEvent(new Throwable("Error")));
				}
			}

			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				if (response.isSuccessful()) {
					eventBus.post(new FeedDeletedEvent());
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						eventBus.post(new FeedDeletedEvent(new Throwable(error.getError())));
					} catch (Exception exception) {
						exception.printStackTrace();
						if (exception != null && exception.getMessage() != null
								&& exception.getMessage().length() != 0) {
							eventBus.post(new FeedDeletedEvent(new Throwable(exception.getMessage())));
						} else {
							eventBus.post(new FeedDeletedEvent(new Throwable("Error")));
						}
					}
				}
			}
		});
	}
}
