package xyz.annt.twitterclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.annt.twitterclient.R;
import xyz.annt.twitterclient.models.Tweet;
import xyz.annt.twitterclient.models.User;

public class TweetActivity extends AppCompatActivity {
    private Tweet tweet;

    @Bind(R.id.ivProfileImage) RoundedImageView ivUserProfileImage;
    @Bind(R.id.tvCreatedAt) TextView tvCreatedAt;
    @Bind(R.id.tvName) TextView tvUserName;
    @Bind(R.id.tvScreenName) TextView tvUserScreenName;
    @Bind(R.id.tvText) TextView tvText;
    @Bind(R.id.tvRetweetsCount) TextView tvRetweetsCount;
    @Bind(R.id.tvFavoritesCount) TextView tvFavoritesCount;
    @Bind(R.id.ibtnReply) ImageButton ibtnReply;
    @Bind(R.id.ibtnRetweet) ImageButton ibtnRetweet;
    @Bind(R.id.ibtnFavorite) ImageButton ibtnFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        tweet = getIntent().getParcelableExtra("tweet");

        populateViews();
    }

    private void populateViews() {
        ivUserProfileImage.setImageResource(0);
        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivUserProfileImage);

        tvUserName.setText(tweet.getUser().getName());

        String userScreenName = this.getResources().getString(R.string.user_screen_name_prefix)
                + tweet.getUser().getScreenName();
        tvUserScreenName.setText(userScreenName);

        tvText.setText(tweet.getText());

        String createdRelative = getDateString(tweet.getCreatedAt());
        tvCreatedAt.setText(createdRelative);

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String retweetsCount = formatter.format(tweet.getRetweetsCount());
        tvRetweetsCount.setText(retweetsCount);

        String favoritesCount = formatter.format(tweet.getFavoritesCount());
        tvFavoritesCount.setText(favoritesCount);

        ibtnRetweet.setActivated(tweet.isRetweeted());

        ibtnFavorite.setActivated(tweet.isFavorited());

        // Listeners
        ivUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfile(tweet.getUser());
            }
        });
    }

    private void showProfile(User user) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user", user);
        this.startActivity(intent);
    }

    private String getDateString(String rawJsonDate){
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat fromFormat = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        fromFormat.setLenient(true);

        try {
            long dateMillis = fromFormat.parse(rawJsonDate).getTime();
            SimpleDateFormat toFormat = new SimpleDateFormat("h:m a · d MMM yy");
            Date date = (new Date(dateMillis));
            return toFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
