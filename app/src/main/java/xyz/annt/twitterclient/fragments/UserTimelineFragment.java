package xyz.annt.twitterclient.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xyz.annt.twitterclient.R;
import xyz.annt.twitterclient.models.Tweet;
import xyz.annt.twitterclient.models.User;
import xyz.annt.twitterclient.networks.NetworkState;

/**
 * Created by annt on 4/3/16.
 */
public class UserTimelineFragment extends TweetsFragment {
    public static UserTimelineFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        UserTimelineFragment fragment = new UserTimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void loadNewTweets() {
        NetworkState state = new NetworkState(getContext());
        if (!state.isNetworkAvailable()) {
            srlContainer.setRefreshing(false);
            Toast.makeText(getContext(), getString(R.string.network_is_not_available),
                    Toast.LENGTH_LONG).show();
            return;
        }

        User user = getArguments().getParcelable("user");
        if (null == user) {
            return;
        }

        RequestParams params = new RequestParams();
        params.put("user_id", user.getId());
        if (0 < tweets.size()) {
            long sinceId = tweets.get(0).getId();
            params.put("since_id", sinceId);
        }

        client.getUserTimeline(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(TAG, response.toString());
                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response);
                if (0 < newTweets.size()) {
                    for (int i = newTweets.size() - 1; i >= 0; i--) {
                        tweets.add(0, newTweets.get(i));
                    }
                }
                int curSize = tweetsAdapter.getItemCount();
                tweetsAdapter.notifyItemRangeInserted(curSize, tweets.size() - 1);

                srlContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                Log.w(TAG, throwable.toString());
                srlContainer.setRefreshing(false);
                String errorMessage = getString(R.string.an_error_has_occurred);

                try {
                    JSONArray errors = errorResponse.getJSONArray("errors");
                    JSONObject error = errors.getJSONObject(0);
                    errorMessage = error.getString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void loadMoreTweets() {
        NetworkState state = new NetworkState(getContext());
        if (!state.isNetworkAvailable()) {
            tweetsAdapter.setEnableProgress(false);
            Toast.makeText(getContext(), getString(R.string.network_is_not_available),
                    Toast.LENGTH_LONG).show();
            return;
        }

        User user = getArguments().getParcelable("user");
        if (null == user) {
            return;
        }

        RequestParams params = new RequestParams();
        params.put("user_id", user.getId());
        if (0 < tweets.size()) {
            long maxId = tweets.get(tweets.size() - 1).getId() - 1;
            params.put("max_id", maxId);
        }

        client.getUserTimeline(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(TAG, response.toString());
                ArrayList<Tweet> moreTweets = Tweet.fromJSONArray(response);
                if (0 == moreTweets.size()) {
                    tweetsAdapter.setEnableProgress(false);
                    return;
                }

                tweets.addAll(moreTweets);
                int curSize = tweetsAdapter.getItemCount();
                tweetsAdapter.notifyItemRangeInserted(curSize, tweets.size());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                Log.w(TAG, throwable.toString());
                tweetsAdapter.setEnableProgress(false);
                String errorMessage = getString(R.string.an_error_has_occurred);

                try {
                    JSONArray errors = errorResponse.getJSONArray("errors");
                    JSONObject error = errors.getJSONObject(0);
                    errorMessage = error.getString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
