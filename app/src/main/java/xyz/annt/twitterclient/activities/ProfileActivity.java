package xyz.annt.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.annt.twitterclient.R;
import xyz.annt.twitterclient.fragments.UserTimelineFragment;
import xyz.annt.twitterclient.models.User;

public class ProfileActivity extends AppCompatActivity {
    private User user;

    @Bind(R.id.ivProfileImage) ImageView ivUserProfileImage;
    @Bind(R.id.tvName) TextView tvUserName;
    @Bind(R.id.tvScreenName) TextView tvUserScreenName;
    @Bind(R.id.tvFollowingCount) TextView tvFollowingCount;
    @Bind(R.id.tvFollowersCount) TextView tvFollowersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        user = getIntent().getParcelableExtra("user");

        getSupportActionBar().setTitle(user.getName());
        populateUserInfo();

        if (null == savedInstanceState) {
            UserTimelineFragment fragment = UserTimelineFragment.newInstance(user);
            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment)
                    .commit();
        }
    }

    private void populateUserInfo() {
        Glide.with(this).load(user.getProfileImageUrl()).into(ivUserProfileImage);
        tvUserName.setText(user.getName());
        String userScreenName = getString(R.string.user_screen_name_prefix) + user.getScreenName();
        tvUserScreenName.setText(userScreenName);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String followingCount = formatter.format(user.getFriendsCount());
        tvFollowingCount.setText(followingCount);
        String followersCount = formatter.format(user.getFollowersCount());
        tvFollowersCount.setText(followersCount);
    }

    public void showFollowing(View view) {
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void showFollowers(View view) {
        Intent intent = new Intent(this, FollowersActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
