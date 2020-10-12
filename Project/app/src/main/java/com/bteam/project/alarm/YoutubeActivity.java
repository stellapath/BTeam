package com.bteam.project.alarm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.R;
import com.bteam.project.network.VolleySingleton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity {

    private static final String TAG = "YoutubeActivity";

    private YouTubePlayerView playerView;
    private YouTubePlayer player;
    private String videoID;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        button = findViewById(R.id.youtube_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getRecentWeatherYoutube();
    }

    private void getRecentWeatherYoutube() {
        final String url = "https://www.weather.go.kr/weather/warning/wtouchqNew.jsp";
        StringRequest request = new StringRequest(Request.Method.GET, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // iframe
                int begin = response.indexOf("<iframe");
                int end = response.indexOf("</iframe>");
                String cut = response.substring(begin, end);

                // 주소 가져오기
                String str = "https://www.youtube.com/embed/";
                begin = cut.indexOf(str) + str.length();
                end = cut.indexOf("?");
                videoID = cut.substring(begin, end);
                Log.i(TAG, "onResponse: " + videoID);
                initPlayer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void initPlayer() {
        playerView = findViewById(R.id.youtube_player);
        playerView.initialize(getString(R.string.api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer;
                player.cueVideo(videoID);

                player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(String s) {
                        Log.i(TAG, "onLoaded: " + s);
                        player.play();
                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {

                    }

                    @Override
                    public void onVideoEnded() {
                        finish();
                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {
                        Toast.makeText(YoutubeActivity.this,
                                "날씨 예보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT)
                                .show();
                        finish();
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(YoutubeActivity.this,
                        "날씨 예보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}