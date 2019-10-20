package com.deco.moodify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
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

import java.util.zip.Inflater;

import timber.log.Timber;

import static android.content.ContentValues.TAG;

class Emojifier {
    Context context;

    Emojifier(Context context){
        this.context = context;
    }

//    Emojifier(){}

    private static final String CLIENT_ID = "4074007710e047fb97963d863c1ac6c0";
    private static final String REDIRECT_URI = "http://google.com/";
    private static final int REQUEST_CODE = 1337;
    private SpotifyAppRemote mSpotifyAppRemote;

    private static final float EMOJI_SCALE_FACTOR = .9f;
    private static final double SMILING_PROB_THRESHOLD = .15;
    private static final double EYE_OPEN_PROB_THRESHOLD = .5;


    String detectFacesandOverlayEmoji(Context context, Bitmap picture) {

        String emotion = "";
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
                    case "SMILE":
//                      onRecognize();
//                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
//                                R.drawable.good);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "smiling");
                        emotion = "SMILE";


                        break;
                    case "FROWN":
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.doubt);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "frown");
                        emotion = "FROWN";


                        break;
                    case "LEFT_WINK_FROWN":
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.crazy);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "crazy");
                        emotion = "LEFT WINK FROWN";


                        break;
                    case "RIGHT WINK FROWN":
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.right_wink);
                        Log.d(TAG, "detectFacesandOverlayEmoji: " + "Right wink");
                        emotion =  "RIGHT WINK FROWN";
                        break;
                    default:
                        emojiBitmap = null;
                        Toast.makeText(context, R.string.no_emoji, Toast.LENGTH_SHORT).show();
                }


//                resultBitmap = addBitmapToFace(resultBitmap, emojiBitmap, face);
            }
        }
        //detector.release();
        return emotion;
    }




    private static String whichEmoji(Face face) {

        Timber.d("whichEmoji: smilingProb = " + face.getIsSmilingProbability());
        Timber.d("whichEmoji: leftEyeOpenProb = "
                + face.getIsLeftEyeOpenProbability());
        Timber.d("whichEmoji: rightEyeOpenProb = "
                + face.getIsRightEyeOpenProbability());


        boolean smiling = face.getIsSmilingProbability() > SMILING_PROB_THRESHOLD;

        boolean leftEyeClosed = face.getIsLeftEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;
        boolean rightEyeClosed = face.getIsRightEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;

        String emoji;
        if(smiling) {
            if (leftEyeClosed && !rightEyeClosed) {
                emoji = "LEFT WINK";
                Log.d(TAG, "left wink");
//                Toast.makeText(context, "Left Wink", Toast.LENGTH_SHORT).show();

            }  else if(rightEyeClosed && !leftEyeClosed){
                emoji = "RIGHT WINK";
                Log.d(TAG, "right wink");

            } else if (leftEyeClosed){
                emoji = "CLOSED_EYE_SMILE";
                Log.d(TAG, "closed eye smile");

            } else {
                emoji = "SMILE";
            }
        } else {
            if (leftEyeClosed && !rightEyeClosed) {
                emoji = "LEFT_WINK_FROWN";
            }  else if(rightEyeClosed && !leftEyeClosed){
                emoji = "RIGHT WINK FROWN";
            } else if (leftEyeClosed){
                emoji = "CLOSED EYE FROWN";
            } else {
                emoji = "FROWN";
            }
        }





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

