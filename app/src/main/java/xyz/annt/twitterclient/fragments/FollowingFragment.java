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
import xyz.annt.twitterclient.models.User;
import xyz.annt.twitterclient.networks.NetworkState;

/**
 * Created by annt on 4/4/16.
 */
public class FollowingFragment extends UsersFragment {
    protected long nextCursor = -1;

    public static FollowingFragment newInstance(User user) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadMoreUsers() {
        NetworkState state = new NetworkState(getContext());
        if (!state.isNetworkAvailable()) {
            usersAdapter.setEnableProgress(false);
            Toast.makeText(getContext(), getString(R.string.network_is_not_available),
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (0 == nextCursor) {
            return;
        }

        User user = getArguments().getParcelable("user");
        if (null == user) {
            return;
        }

        RequestParams params = new RequestParams();
        params.put("user_id", user.getId());
        params.put("cursor", nextCursor);

        client.getFriendsList(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());

                try {
                    JSONArray usersJSONArray = response.getJSONArray("users");
                    users.addAll(User.fromJSONArray(usersJSONArray));
                    int curSize = usersAdapter.getItemCount();
                    usersAdapter.notifyItemRangeInserted(curSize, users.size());

                    nextCursor = response.getLong("next_cursor");
                    if (0 == nextCursor) {
                        usersAdapter.setEnableProgress(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    usersAdapter.setEnableProgress(false);
                    Toast.makeText(getContext(), getString(R.string.an_error_has_occurred),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.w(TAG, throwable.toString());
                usersAdapter.setEnableProgress(false);
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
    protected void refreshUsers() {
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
        params.put("cursor", -1);

        client.getFriendsList(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());
                srlContainer.setRefreshing(false);

                try {
                    JSONArray usersJSONArray = response.getJSONArray("users");
                    users.clear();
                    users.addAll(User.fromJSONArray(usersJSONArray));
                    usersAdapter.notifyDataSetChanged();

                    nextCursor = response.getLong("next_cursor");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), getString(R.string.an_error_has_occurred),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
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
}
