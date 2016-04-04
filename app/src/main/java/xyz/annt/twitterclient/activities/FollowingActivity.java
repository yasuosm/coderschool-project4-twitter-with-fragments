package xyz.annt.twitterclient.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import xyz.annt.twitterclient.R;
import xyz.annt.twitterclient.fragments.FollowersFragment;
import xyz.annt.twitterclient.fragments.FollowingFragment;
import xyz.annt.twitterclient.models.User;

public class FollowingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.following));

        User user = getIntent().getParcelableExtra("user");

        if (null == savedInstanceState) {
            FollowingFragment fragment = FollowingFragment.newInstance(user);
            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment)
                    .commit();
        }
    }

}
