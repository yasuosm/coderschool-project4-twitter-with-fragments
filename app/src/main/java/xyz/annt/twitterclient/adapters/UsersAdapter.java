package xyz.annt.twitterclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.annt.twitterclient.R;
import xyz.annt.twitterclient.models.User;

/**
 * Created by annt on 4/3/16.
 */
public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<User> users;
    private Context context;
    private final int VIEW_TYPE_PROGRESS = 0;
    private final int VIEW_TYPE_USER = 1;
    private boolean enableProgress = true;

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivProfileImage) RoundedImageView ivProfileImage;
        @Bind(R.id.tvName) TextView tvName;
        @Bind(R.id.tvScreenName) TextView tvScreenName;
        @Bind(R.id.tvDescription) TextView tvDescription;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public UsersAdapter(ArrayList<User> users) {
        this.users = users;
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
                View tweetView = inflater.inflate(R.layout.item_user, parent, false);
                return new UserViewHolder(tweetView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_PROGRESS:
                onBindProgressViewHolder((ProgressViewHolder) holder, position);
                break;
            default:
                onBindUserViewHolder((UserViewHolder) holder, position);
        }
    }

    private void onBindProgressViewHolder(ProgressViewHolder holder, int position) {

    }

    public void onBindUserViewHolder(UserViewHolder holder, int position) {
        User user = users.get(position);

        Glide.with(context).load(user.getProfileImageUrl()).into(holder.ivProfileImage);
        holder.tvName.setText(user.getName());
        String screenName = context.getString(R.string.user_screen_name_prefix) +
                user.getScreenName();
        holder.tvScreenName.setText(screenName);
        holder.tvDescription.setText(user.getDescription());
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= users.size()) {
            return VIEW_TYPE_PROGRESS;
        }

        return VIEW_TYPE_USER;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (null != users) {
            count = users.size();
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
}
