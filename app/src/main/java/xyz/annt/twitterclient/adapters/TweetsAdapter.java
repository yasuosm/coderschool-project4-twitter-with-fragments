package xyz.annt.twitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.annt.twitterclient.R;
import xyz.annt.twitterclient.activities.ProfileActivity;
import xyz.annt.twitterclient.activities.TweetActivity;
import xyz.annt.twitterclient.models.Tweet;
import xyz.annt.twitterclient.models.User;

/**
 * Created by annt on 3/27/16.
 */
public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Tweet> tweets;
    Context context;
    private final int VIEW_TYPE_PROGRESS = 0;
    private final int VIEW_TYPE_TWEET = 1;
    private boolean enableProgress = true;

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    class TweetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.ivProfileImage) RoundedImageView ivUserProfileImage;
        @Bind(R.id.tvCreatedAt) TextView tvCreatedAt;
        @Bind(R.id.tvName) TextView tvUserName;
        @Bind(R.id.tvScreenName) TextView tvUserScreenName;
        @Bind(R.id.tvText) TextView tvText;
        @Bind(R.id.ibtnReply) ImageButton ibtnReply;
        @Bind(R.id.ibtnRetweet) ImageButton ibtnRetweet;
        @Bind(R.id.ibtnFavorite) ImageButton ibtnFavorite;
        @Bind(R.id.tvRetweetsCount) TextView tvRetweetsCount;
        @Bind(R.id.tvFavoritesCount) TextView tvFavoritesCount;

        public TweetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Tweet tweet = tweets.get(position);
            Intent intent = new Intent(context, TweetActivity.class);
            intent.putExtra("tweet", tweet);
            context.startActivity(intent);
        }
    }

    public TweetsAdapter(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case VIEW_TYPE_PROGRESS:
                View progressView = inflater.inflate(R.layout.item_progress, parent, false);
                return new ProgressViewHolder(progressView);
            default:
                View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
                return new TweetViewHolder(tweetView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_PROGRESS:
                onBindProgressViewHolder((ProgressViewHolder) holder, position);
                break;
            default:
                onBindTweetViewHolder((TweetViewHolder) holder, position);
        }
    }

    private void onBindProgressViewHolder(ProgressViewHolder holder, int position) {

    }

    private void onBindTweetViewHolder(TweetViewHolder holder, int position) {
        final Tweet tweet = tweets.get(position);

        holder.ivUserProfileImage.setImageResource(0);
        Glide.with(context).load(tweet.getUser().getProfileImageUrl())
                .into(holder.ivUserProfileImage);

        String createdRelative = getRelativeTimeAgo(tweet.getCreatedAt());
        holder.tvCreatedAt.setText(createdRelative);

        holder.tvUserName.setText(tweet.getUser().getName());

        String userScreenName = context.getResources().getString(R.string.user_screen_name_prefix)
                + tweet.getUser().getScreenName();
        holder.tvUserScreenName.setText(userScreenName);

        holder.tvText.setText(tweet.getText());

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String retweetsCount = formatter.format(tweet.getRetweetsCount());
        holder.tvRetweetsCount.setText(retweetsCount);

        String favoritesCount = formatter.format(tweet.getFavoritesCount());
        holder.tvFavoritesCount.setText(favoritesCount);

        // Colors
        holder.ibtnRetweet.setActivated(tweet.isRetweeted());
        holder.tvRetweetsCount.setActivated(tweet.isRetweeted());

        holder.ibtnFavorite.setActivated(tweet.isFavorited());
        holder.tvFavoritesCount.setActivated(tweet.isFavorited());

        // Listeners
        holder.ivUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfile(tweet.getUser());
            }
        });
    }

    private void showProfile(User user) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= tweets.size()) {
            return VIEW_TYPE_PROGRESS;
        }

        return VIEW_TYPE_TWEET;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (null != tweets) {
            count = tweets.size();
        }
        if (enableProgress) {
            count++;
        }
        return count;
    }

    public void setEnableProgress(boolean enableProgress) {
        this.enableProgress = enableProgress;
        if (this.enableProgress) {
            this.notifyItemInserted(this.getItemCount());
        } else {
            this.notifyItemRemoved(this.getItemCount() - 1);
        }
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
