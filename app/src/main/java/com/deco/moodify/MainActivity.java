package com.deco.moodify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "fa5b55e21207424a9647c16ab7037a30";
    private static final String REDIRECT_URI = "moodify://callback/";
    private static final int REQUEST_CODE = 1337;
    private SpotifyAppRemote mSpotifyAppRemote;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static int love_count;
    public static int joy_count;
    public static int happy_count;
    public static int anger_count;
    public static int sad_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_stats:
                        Intent intent = new Intent(MainActivity.this, StatsPage.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        load_data();
    }

    @Override
    protected void onStart() {
        super.onStart();



        // Set the connection parameters
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        mSpotifyAppRemote.getPlayerApi().setShuffle(true);
                        Log.d("MainActivity", "Connected! Yay!");

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
        load_data();
    }

    public void get_song() {
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    private void love_song() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

//        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        AuthenticationClient.openLoginInBrowser(this, request);

        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:4QuJ2DbcTe7R8lzqfNXz7v");
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    private void joy_song() {
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DXaSYtampkg5n");
    }

    private void happy_song() {
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DWSf2RDTDayIx");
    }

    private void anger_song() {
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:1aUGVrGipiT9vOb11jO4n4");
    }

    private void sadness_song() {
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX7qK8ma5wgG1");
    }


    @Override
    protected void onStop() {
        super.onStop();
        // Aaand we will finish off here.
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }


    // Methods that will be triggered by the buttons
    // When a button is clicked, a new dialog fragment will be created using the ButtonClickDialogFragment()
    // class. This will allow the user to enter a message.
    public void love(View view) {
        love_song();
        love_count += 1;
        save_data();

    }

    public void joy(View view) {
        joy_song();
        joy_count += 1;
        save_data();
    }

    public void happy(View view) {
        happy_song();
        happy_count += 1;
        save_data();
    }

    public void anger(View view) {
        anger_song();
        anger_count += 1;
        save_data();
    }

    public void sadness(View view) {
        sadness_song();
        sad_count += 1;
        save_data();
    }

    public void play_button(View view) {
        mSpotifyAppRemote.getPlayerApi().resume();
    }

    public void pause_button(View view) {
        mSpotifyAppRemote.getPlayerApi().pause();
    }

    public void save_data() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("LOVE_COUNT", love_count);
        editor.putInt("JOY_COUNT", joy_count);
        editor.putInt("HAPPY_COUNT", happy_count);
        editor.putInt("ANGER_COUNT", anger_count);
        editor.putInt("SAD_COUNT", sad_count);

        editor.apply();

        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
    }

    public void load_data() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        love_count = sharedPreferences.getInt("LOVE_COUNT", 0);
        joy_count = sharedPreferences.getInt("JOY_COUNT", 0);
        happy_count = sharedPreferences.getInt("HAPPY_COUNT", 0);
        anger_count = sharedPreferences.getInt("ANGER_COUNT", 0);
        sad_count = sharedPreferences.getInt("SAD_COUNT", 0);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Toast.makeText(this, "Authentication success", Toast.LENGTH_SHORT).show();
                    Log.d("", "onActivityResult: " + "Authentication success");
                    break;

                // Auth flow returned an error
                case ERROR:
                    Toast.makeText(this, "AUTHENTICATION ERROR!!!!1", Toast.LENGTH_SHORT).show();
                    Log.d("", "onActivityResult: " + "AUTHENTICATION ERROR!!!!1");
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = AuthenticationResponse.fromUri(uri);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Toast.makeText(this, "Web Authentication success", Toast.LENGTH_SHORT).show();
                    Log.d("", "onActivityResult: " + "Authentication success");
                    break;

                // Auth flow returned an error
                case ERROR:
                    Toast.makeText(this, "WEB AUTHENTICATION ERROR!!!!1", Toast.LENGTH_SHORT).show();
                    Log.d("", "onActivityResult: " + "AUTHENTICATION ERROR!!!!1");
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
}