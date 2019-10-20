package com.deco.moodify;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider.MOODIFY";

    //@BindView(R.id.image_view) ImageView mImageView;

    @BindView(R.id.emojify_button) Button mEmojifyButton;
    @BindView(R.id.share_button) FloatingActionButton mShareFab;
    @BindView(R.id.save_button) FloatingActionButton mSaveFab;
    @BindView(R.id.clear_button) FloatingActionButton mClearFab;
    @BindView(R.id.test_emotion_text) TextView emotionText;

    @BindView(R.id.title_text_view) TextView mTitleTextView;
    //@BindView(R.id.test_image) ImageView imageView;

    private static final String CLIENT_ID = "4074007710e047fb97963d863c1ac6c0";
    private static final String REDIRECT_URI = "http://google.com/";
    private static final int REQUEST_CODE = 1337;
    private SpotifyAppRemote mSpotifyAppRemote;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static int love_count;
    public static int joy_count;
    public static int happy_count;
    public static int anger_count;
    public static int sad_count;

    private String mTempPhotoPath;

    private Bitmap mResultsBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind the views
        ButterKnife.bind(this);

        // Set up Timber
        Timber.plant(new Timber.DebugTree());

//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
//        Menu menu = bottomNavigationView.getMenu();
//        MenuItem menuItem = menu.getItem(0);
//        menuItem.setChecked(true);

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.navigation_home:
//                        break;
//                    case R.id.navigation_stats:
//                        Intent intent = new Intent(MainActivity.this, StatsPage.class);
//                        startActivity(intent);
//                        break;
//                }
//                return false;
//            }
//        });

//        load_data();
    }

    @OnClick(R.id.emojify_button)
    public void emojifyMe() {
        // Check for the external storage permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // Launch the camera if the permission exists
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void processAndSetImage(Bitmap bmp) {

//         Toggle Visibility of the views
        mEmojifyButton.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.GONE);
//        mSaveFab.setVisibility(View.VISIBLE);
//        mShareFab.setVisibility(View.VISIBLE);
//        mClearFab.setVisibility(View.VISIBLE);

        // Resample the saved image to fit the ImageView
//        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);


        // Detect the faces and overlay the appropriate emoji
      //  mResultsBitmap = new Emojifier().detectFacesandOverlayEmoji(this, bmp);
        Log.d("", "processAndSetImage: " + " Reached!");

        // Set the new bitmap to the ImageView
        //mImageView.setImageBitmap(mResultsBitmap);
    }

    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the temporary File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = BitmapUtils.createTempImageFile(this);
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                ex.printStackTrace();
//
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//
//                // Get the path of the temporary file
//                mTempPhotoPath = photoFile.getAbsolutePath();
//
//                // Get the content URI for the image file
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        FILE_PROVIDER_AUTHORITY,
//                        photoFile);
//
//                // Add the URI so the camera can store the image
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//
//                // Launch the camera activity
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
//        }
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
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
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

//        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        AuthenticationClient.openLoginInBrowser(this, request);

        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DXaSYtampkg5n");
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    private void happy_song() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

//        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        AuthenticationClient.openLoginInBrowser(this, request);

        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DWSf2RDTDayIx");
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    private void anger_song() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

//        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        AuthenticationClient.openLoginInBrowser(this, request);

        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:1aUGVrGipiT9vOb11jO4n4");
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    private void sadness_song() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

//        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        AuthenticationClient.openLoginInBrowser(this, request);

        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX7qK8ma5wgG1");
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = intent.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String emotion = new Emojifier(this).detectFacesandOverlayEmoji(getApplicationContext(), imageBitmap);
            emotionText.setText(emotion);
            if(emotion.equalsIgnoreCase("smile")){
                happy_song();
            }
            else if(emotion.equalsIgnoreCase("frown")){
                anger_song();
            }
            else if(emotion.equalsIgnoreCase("LEFT_WINK_FROWN") || emotion.equalsIgnoreCase("RIGHT WINK FROWN")){
                joy_song();
            }
//            processAndSetImage(imageBitmap);
            //ttimageView.setImageBitmap(imageBitmap);

//            imageView.setImageBitmap(imageBitmap);
        } else {

            // Otherwise, delete the temporary image file
//            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
            Log.d("", "onActivityResult: " + " processAndSetImage");

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

    public void onRecognize() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

//        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        try {
            AuthenticationClient.openLoginInBrowser(this, request);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}