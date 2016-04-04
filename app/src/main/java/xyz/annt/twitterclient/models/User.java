package xyz.annt.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by annt on 3/27/16.
 */
public class User implements Parcelable {
    long id;
    String name;
    String screenName;
    String description;
    String profileImageUrl;
    int followersCount;
    int friendsCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public static User fromJSONObject(JSONObject object) {
        User user = new User();

        try {
            user.setId(object.getLong("id"));
            user.setName(object.getString("name"));
            user.setScreenName(object.getString("screen_name"));
            user.setDescription(object.getString("description"));
            user.setProfileImageUrl(object.getString("profile_image_url"));
            user.setFriendsCount(object.getInt("friends_count"));
            user.setFollowersCount(object.getInt("followers_count"));

            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<User> fromJSONArray(JSONArray array) {
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                User user = fromJSONObject(array.getJSONObject(i));
                if (null != user) {
                    users.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return users;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.screenName);
        dest.writeString(this.description);
        dest.writeString(this.profileImageUrl);
        dest.writeInt(this.followersCount);
        dest.writeInt(this.friendsCount);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.screenName = in.readString();
        this.description = in.readString();
        this.profileImageUrl = in.readString();
        this.followersCount = in.readInt();
        this.friendsCount = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
