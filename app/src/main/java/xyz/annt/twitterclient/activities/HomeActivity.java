package xyz.annt.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
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
import xyz.annt.twitterclient.adapters.HomePagerAdapter;
import xyz.annt.twitterclient.adapters.TweetsAdapter;
import xyz.annt.twitterclient.applications.TwitterApplication;
import xyz.annt.twitterclient.fragments.ComposeFragment;
import xyz.annt.twitterclient.fragments.HomeTimelineFragment;
import xyz.annt.twitterclient.fragments.TweetsFragment;
import xyz.annt.twitterclient.listeners.EndlessRecyclerViewScrollListener;
import xyz.annt.twitterclient.models.Tweet;
import xyz.annt.twitterclient.models.User;
import xyz.annt.twitterclient.networks.TwitterClient;

public class HomeActivity extends AppCompatActivity
        implements ComposeFragment.ComposeFragmentListener {

    private static final String TAG = "HomeActivity";
    private User user;
    private HomePagerAdapter pagerAdapter;

    @Bind(R.id.fabCompose) FloatingActionButton fabCompose;
    @Bind(R.id.pstsTabs) PagerSlidingTabStrip pstsTabs;
    @Bind(R.id.vpPager) ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.ic_logo_white);
        getSupportActionBar().setTitle("");
        ButterKnife.bind(this);

        user = getIntent().getParcelableExtra("user");

        pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(pagerAdapter);

        pstsTabs.setViewPager(vpPager);

        setListeners();
    }

    private void setListeners() {
        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComposeFragment();
            }
        });
    }

    private void showComposeFragment() {
        ComposeFragment fragment = new ComposeFragment();
        // Set fullscreen
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        // Set arguments
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        // Show
        FragmentManager manager = getSupportFragmentManager();
        fragment.show(manager, "fragment_compose");
    }

    @Override
    public void onTweetSuccess(JSONObject response) {
        Tweet tweet = Tweet.fromJSONObject(response);
        HomeTimelineFragment fragment = (HomeTimelineFragment) pagerAdapter.getItem(0);
        fragment.add(0, tweet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.miProfile:
                showProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
