package com.deco.moodify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import timber.log.Timber;

import static android.content.ContentValues.TAG;

class Emojifier extends AppCompatActivity {
    Context context;
    private static final String CLIENT_ID = "4074007710e047fb97963d863c1ac6c0";
    private static final String REDIRECT_URI = "http://google.com/";
    private static final int REQUEST_CODE = 1337;
    private SpotifyAppRemote mSpotifyAppRemote;

    private static final float EMOJI_SCALE_FACTOR = .9f;
    private static final double SMILING_PROB_THRESHOLD = .15;
    private static final double EYE_OPEN_PROB_THRESHOLD = .5;

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
                        Log.i(TAG, "onFailure: " + "triggered!!!!!!");
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }




    Bitmap detectFacesandOverlayEmoji(Context context, Bitmap picture) {


        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();


        Frame frame = new Frame.Builder().setBitmap(picture).build();


        SparseArray<Face> faces = detector.detect(frame);


        Timber.d("detectFaces: number of faces = " + faces.size());


        Bitmap resultBitmap = picture;


        if (faces.size() == 0) {
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else {


            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);

                Bitmap emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.good);
                switch (whichEmoji(face)) {
                    case SMILE:
//                      onRecognize();
                        love_song();
//                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
//                                R.drawable.good);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "smiling");
                        Toast.makeText(this, "You're smiling :D", Toast.LENGTH_SHORT).show();


                        break;
                    case FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.doubt);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "frown");

                        break;
                    case CRAZY:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.crazy);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "crazy");

                        break;
                    case RIGHT_WINK:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.right_wink);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "Right wink");

                        break;
                    case SHOCK:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.shock);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "Shock");

                        break;
                    case SUSPECT:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.suspect);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "Suspect");

                        break;
                    case DOUBT:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.doubt);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "Doubt");

                        break;
                    case KISS:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.kiss);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "Kiss");
                        break;
                    case LOVELY:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.lovely);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "Lovely");
                        break;

                    case LEFT_WINK_FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.left_wink);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "LEFT WINK FROWN");
                        break;
                    case RIGHT_WINK_FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.right_wink);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "RIGHT WINK FROWN");
                        break;
                    case CLOSED_EYE_SMILE:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.happy);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "CLOSED EYE SMILE");
                        break;
                    case CLOSED_EYE_FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.crazy);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "CLOSED EYE FROWN");
                        break;
                    default:
                        emojiBitmap = null;
                        Toast.makeText(context, R.string.no_emoji, Toast.LENGTH_SHORT).show();
                }


//                resultBitmap = addBitmapToFace(resultBitmap, emojiBitmap, face);
            }
        }



        detector.release();

        return resultBitmap;
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

    private void love_song() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

//        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
try {
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
catch (Exception e){
    e.printStackTrace();
    Log.d(TAG, "love_song: didnt work :(");
}
    }




    private static Emoji whichEmoji(Face face) {

        Timber.d("whichEmoji: smilingProb = " + face.getIsSmilingProbability());
        Timber.d("whichEmoji: leftEyeOpenProb = "
                + face.getIsLeftEyeOpenProbability());
        Timber.d("whichEmoji: rightEyeOpenProb = "
                + face.getIsRightEyeOpenProbability());


        boolean smiling = face.getIsSmilingProbability() > SMILING_PROB_THRESHOLD;

        boolean leftEyeClosed = face.getIsLeftEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;
        boolean rightEyeClosed = face.getIsRightEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;

        Emoji emoji;
        if(smiling) {
            if (leftEyeClosed && !rightEyeClosed) {
                emoji = Emoji.LEFT_WINK;
            }  else if(rightEyeClosed && !leftEyeClosed){
                emoji = Emoji.RIGHT_WINK;
            } else if (leftEyeClosed){
                emoji = Emoji.CLOSED_EYE_SMILE;
            } else {
                emoji = Emoji.SMILE;
            }
        } else {
            if (leftEyeClosed && !rightEyeClosed) {
                emoji = Emoji.LEFT_WINK_FROWN;
            }  else if(rightEyeClosed && !leftEyeClosed){
                emoji = Emoji.RIGHT_WINK_FROWN;
            } else if (leftEyeClosed){
                emoji = Emoji.CLOSED_EYE_FROWN;
            } else {
                emoji = Emoji.FROWN;
            }
        }



        Timber.d("whichEmoji: " + emoji.name());


        return emoji;
    }

    private static Bitmap addBitmapToFace(Bitmap backgroundBitmap, Bitmap emojiBitmap, Face face) {


        Bitmap resultBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),
                backgroundBitmap.getHeight(), backgroundBitmap.getConfig());


        float scaleFactor = EMOJI_SCALE_FACTOR;


        int newEmojiWidth = (int) (face.getWidth() * scaleFactor);
        int newEmojiHeight = (int) (emojiBitmap.getHeight() *
                newEmojiWidth / emojiBitmap.getWidth() * scaleFactor);


       emojiBitmap = Bitmap.createScaledBitmap(emojiBitmap, newEmojiWidth, newEmojiHeight, false);

       float emojiPositionX =
                (face.getPosition().x + face.getWidth() / 2) - emojiBitmap.getWidth() / 2;
        float emojiPositionY =
                (face.getPosition().y + face.getHeight() / 2) - emojiBitmap.getHeight() / 3;
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        canvas.drawBitmap(emojiBitmap, emojiPositionX, emojiPositionY, null);

        return resultBitmap;
    }
    private enum Emoji {
        SMILE,
        CRAZY,
        SHOCK,
        LOVELY,
        KISS,
        DOUBT,
        SUSPECT,
        FROWN,
        LEFT_WINK,
        RIGHT_WINK,
        LEFT_WINK_FROWN,
        RIGHT_WINK_FROWN,
        CLOSED_EYE_SMILE,
        CLOSED_EYE_FROWN
    }


}

