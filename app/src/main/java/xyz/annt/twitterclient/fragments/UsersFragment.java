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

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.annt.twitterclient.R;
import xyz.annt.twitterclient.adapters.UsersAdapter;
import xyz.annt.twitterclient.listeners.EndlessRecyclerViewScrollListener;
import xyz.annt.twitterclient.models.User;
import xyz.annt.twitterclient.networks.TwitterClient;

/**
 * Created by annt on 4/3/16.
 */
public abstract class UsersFragment extends Fragment {
    protected static final String TAG = "UsersFragment";
    protected TwitterClient client;
    protected ArrayList<User> users;
    protected UsersAdapter usersAdapter;
    protected LinearLayoutManager layoutManager;

    @Bind(R.id.rvUsers) RecyclerView rvUsers;
    @Bind(R.id.srlContainer) SwipeRefreshLayout srlContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new TwitterClient(getContext());
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users);
        layoutManager = new LinearLayoutManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        ButterKnife.bind(this, view);

        rvUsers.setAdapter(usersAdapter);
        rvUsers.setLayoutManager(layoutManager);
        srlContainer.setColorSchemeResources(R.color.colorPrimary);

        setListeners();
        loadMoreUsers();

        return view;
    }

    protected void setListeners() {
        rvUsers.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreUsers();
            }
        });

        srlContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUsers();
            }
        });
    }

    protected abstract void loadMoreUsers();

    protected abstract void refreshUsers();
}
