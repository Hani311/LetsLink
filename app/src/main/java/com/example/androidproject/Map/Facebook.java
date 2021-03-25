package com.example.androidproject.Map;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.androidproject.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.androidproject.Users.PassNotif.CHANNEL_1_ID;

public class Facebook extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private TextView editTextTitle;

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ImageView imageView;
    private TextView textView;
    private ImageView imageView2;
    private ShareButton sbLink;
    private ShareButton sbPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        notificationManager = NotificationManagerCompat.from(this);
        editTextTitle = findViewById(R.id.edit_text_title);


        loginButton = findViewById(R.id.login_button);
        textView = findViewById(R.id.tv_name);
        imageView = findViewById(R.id.iv_profilePic);
        imageView2 = findViewById(R.id.iv_pic);
        sbLink = findViewById(R.id.sb_link);
        sbPhoto = findViewById(R.id.sb_photo);


        imageView2.setImageResource(R.drawable.app_icon);
        callbackManager = CallbackManager.Factory.create();

        // make sure to update permissions with the developer facebook -
        // this will not work for developer account in case was not fixed
        // loginButton.setPermissions(Arrays.asList("user_gender, user_friends"));

        //for login
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String title = editTextTitle.getText().toString();
                Notification notification = new NotificationCompat.Builder(Facebook.this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.thumbs_up)
                        .setContentTitle("Facebook Login")
                        .setContentText("Login to facebook was Successful")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setAutoCancel(true)
                        .build();
                notificationManager.notify(1, notification);
                Log.d("Demo", "Login Successful" + title);
            }

            @Override
            public void onCancel() {
                String title = editTextTitle.getText().toString();
                Notification notification = new NotificationCompat.Builder(Facebook.this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.thumbs_up)
                        .setContentTitle("Facebook Login")
                        .setContentText("Login to facebook was canceled")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setAutoCancel(true)
                        .build();
                notificationManager.notify(1, notification);
                Log.d("Demo", "Login cancel" + title);

            }

            @Override
            public void onError(FacebookException error) {
                String title = editTextTitle.getText().toString();
                Notification notification = new NotificationCompat.Builder(Facebook.this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.thumbs_up)
                        .setContentTitle("Facebook Login")
                        .setContentText("Login to facebook error")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setAutoCancel(true)
                        .build();
                notificationManager.notify(1, notification);
                Log.d("Demo", "Login Onerror" + title);
            }
        });



        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("Demo", object.toString());

                        try {
                            String name = object.getString("name");
                            String id = object.getString("id");
                            textView.setText(name);
                            Picasso.get().load("https://graph.facebook.com/" + id + "/picture?type=large")
                                    .into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle bundle = new Bundle();
        bundle.putString("Fields", "gender, name, id, first_name, last_name");

        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();

        //for share
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder().
                setContentUrl(Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"))
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#test").build())
                .build();
        sbLink.setShareContent(shareLinkContent);


        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView2.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build();
        sbPhoto.setShareContent(sharePhotoContent);


        //show small notification on the bottom of the screen about the login status if it work or no
        LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
            @Override
            public void onCompleted(AccessToken accessToken) {
                // User was previously logged in, can log them in directly here.
                // If this callback is called, a popup notification appears that says
                // "Logged in as <User Name>"
            }

            @Override
            public void onFailure() {
                // No access token could be retrieved for the user
            }

            @Override
            public void onError(Exception exception) {
                // An error occurred
            }
        });

    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                LoginManager.getInstance().logOut();
                textView.setText("");
                imageView.setImageResource(0);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.startTracking();
    }

}