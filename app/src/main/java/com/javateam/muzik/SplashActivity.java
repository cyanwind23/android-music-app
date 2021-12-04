package com.javateam.muzik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.javateam.muzik.config.AppConfig;
import com.javateam.muzik.singleton.RequestSingleton;


public class SplashActivity extends AppCompatActivity {

    private String albumString = null;
    private String artistString = null;
    private String songString = null;
    private String categoryString = null;

    private VolleyError generalError = null;

    /** Intent keys **/
    public static final String IK_JSON_ALBUM = "json_album";
    public static final String IK_JSON_ARTIST = "json_artist";
    public static final String IK_JSON_CATEGORY = "json_category";
    public static final String IK_JSON_SONG = "json_song";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getAlbumData();
        getArtistData();
        getCategoryData();
        getSongData();
        toHome();
    }

    private void toHome() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, Home.class);

            if (generalError != null) {
                handleVolleyErrorMessage(generalError);
            }

            intent.putExtra(IK_JSON_ALBUM, albumString);
            intent.putExtra(IK_JSON_ARTIST, artistString);
            intent.putExtra(IK_JSON_CATEGORY, categoryString);
            intent.putExtra(IK_JSON_SONG, songString);

            startActivity(intent);
            finish();
        }, 3000);
    }

    private void getAlbumData() {
        StringRequest albumRequest = new StringRequest(Request.Method.GET, AppConfig.ALBUMS_API,
                response -> albumString = response, volleyError -> generalError = volleyError) {
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        RequestSingleton.getInstance(this).addToRequestQueue(albumRequest);
    }

    private void getArtistData() {
        StringRequest artistRequest = new StringRequest(Request.Method.GET, AppConfig.ARTISTS_API,
                response -> artistString = response, volleyError -> generalError = volleyError) {
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        RequestSingleton.getInstance(this).addToRequestQueue(artistRequest);
    }

    private void getCategoryData() {
        StringRequest categoryRequest = new StringRequest(Request.Method.GET, AppConfig.CATEGORIES_API,
                response -> categoryString = response, volleyError -> generalError = volleyError) {
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        RequestSingleton.getInstance(this).addToRequestQueue(categoryRequest);
    }

    private void getSongData() {
        StringRequest songRequest = new StringRequest(Request.Method.GET, AppConfig.SONGS_API,
                response -> songString = response, volleyError -> generalError = volleyError);

        RequestSingleton.getInstance(this).addToRequestQueue(songRequest);
    }

    private void handleVolleyErrorMessage(VolleyError volleyError) {
        // TODO: Messages need to be changed accordingly
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}