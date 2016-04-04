package xyz.annt.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.annt.twitterclient.R;
import xyz.annt.twitterclient.adapters.TweetsAdapter;
import xyz.annt.twitterclient.applications.TwitterApplication;
import xyz.annt.twitterclient.listeners.EndlessRecyclerViewScrollListener;
import xyz.annt.twitterclient.models.Tweet;
import xyz.annt.twitterclient.networks.TwitterClient;

/**
 * Created by annt on 4/2/16.
 */
public abstract class TweetsFragment extends Fragment {
    protected static final String TAG = "TweetsFragment";
    protected TwitterClient client;
    protected ArrayList<Tweet> tweets;
    protected TweetsAdapter tweetsAdapter;
    protected LinearLayoutManager layoutManager;

    @Bind(R.id.rvTweets) RecyclerView rvTweets;
    @Bind(R.id.srlContainer) SwipeRefreshLayout srlContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);
        ButterKnife.bind(this, view);

        rvTweets.setAdapter(tweetsAdapter);
        rvTweets.setLayoutManager(layoutManager);
        srlContainer.setColorSchemeResources(R.color.colorPrimary);

        setListeners();
        loadMoreTweets();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(tweets);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    protected void setListeners() {
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreTweets();
            }
        });

        srlContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNewTweets();
            }
        });
    }

    protected abstract void loadNewTweets();

    protected abstract void loadMoreTweets();

    public void add(int index, Tweet tweet) {
        tweets.add(index, tweet);
        tweetsAdapter.notifyItemInserted(index);
    }
}
