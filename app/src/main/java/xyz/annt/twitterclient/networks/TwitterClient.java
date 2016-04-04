package xyz.annt.twitterclient.networks;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1/";
	public static final String REST_CONSUMER_KEY = "S8s4s39BGk9flw58pBH7tIEjH";
	public static final String REST_CONSUMER_SECRET =
            "R1tLOgs3e4gu8wzmOxOOHEyJ7yw3NWH8KzZIvHpmkjx9akpF95";
	public static final String REST_CALLBACK_URL = "oauth://twitterclient";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET,
                REST_CALLBACK_URL);
	}

	public void getHomeTimeline(RequestParams params, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		getClient().get(apiUrl, params, handler);
	}

	public void getMentionsTimeline(RequestParams params, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		getClient().get(apiUrl, params, handler);
	}

    public void getUserTimeline(RequestParams params, JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        getClient().get(apiUrl, params, handler);
    }

	public void getUserCredentials(RequestParams params, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl, params, handler);
	}

    public void updateStatus(RequestParams params, JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        getClient().post(apiUrl, params, handler);
    }

    public void getFollowersList(RequestParams params, JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("followers/list.json");
        getClient().get(apiUrl, params, handler);
    }

    public void getFriendsList(RequestParams params, JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("friends/list.json");
        getClient().get(apiUrl, params, handler);
    }

	public void postRetweet(long id, boolean retweeted, JsonHttpResponseHandler handler) {
		String api = retweeted ? "unretweet" : "retweet";
		String apiUrl = getApiUrl("statuses/" + api + "/" + id + ".json");
		getClient().post(apiUrl, null, handler);
	}
}