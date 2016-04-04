package xyz.annt.twitterclient.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.annt.twitterclient.R;
import xyz.annt.twitterclient.models.User;
import xyz.annt.twitterclient.networks.NetworkState;
import xyz.annt.twitterclient.networks.TwitterClient;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {
	private static final String TAG = "LoginActivity";

    @Bind(R.id.btnLogin) Button btnLogin;
    @Bind(R.id.pbLoading) ProgressBar pbLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
        NetworkState state = new NetworkState(this);
        if (!state.isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.network_is_not_available), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Get user credentials before start home
        btnLogin.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);

        RequestParams params = new RequestParams();
        getClient().getUserCredentials(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());
                User user = User.fromJSONObject(response);
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                i.putExtra("user", user);
                startActivity(i);
                finish(); // finish activity
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                Log.w(TAG, throwable.toString());
                pbLoading.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.VISIBLE);

                Toast.makeText(LoginActivity.this,
                        "Unable to retrieve user information. Please try again.",
                        Toast.LENGTH_LONG).show();
            }
        });
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
        pbLoading.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.VISIBLE);

        Toast.makeText(LoginActivity.this, getString(R.string.an_error_has_occurred),
                Toast.LENGTH_LONG).show();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
        NetworkState state = new NetworkState(this);
        if (!state.isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.network_is_not_available), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        btnLogin.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);

        getClient().connect();
	}
}
