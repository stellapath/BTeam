package com.bteam.project.board;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.R;
import com.bteam.project.board.model.TrafficVO;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.util.Common;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class TrafficDetailActivity extends AppCompatActivity {

    private static final String TAG = "TrafficDetailActivity";
    private String num;

    private ImageButton close;
    private TextView writer, date, content;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_detail);

        if (getIntent() != null) {
            num = getIntent().getStringExtra("num");
        }

        initView();
        getTrafficDetail();
    }

    private void initView() {
        close = findViewById(R.id.traffic_detail_close);
        writer = findViewById(R.id.traffic_detail_writer);
        date = findViewById(R.id.traffic_detail_date);
        content = findViewById(R.id.traffic_detail_content);
        image = findViewById(R.id.traffic_detail_image);
    }

    private void getTrafficDetail() {
        final String url = Common.SERVER_URL + "andTrafficView?num= " + num;
        StringRequest request = new StringRequest(Request.Method.GET, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                showTrafficDetail(gson.fromJson(response.trim(), TrafficVO.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void showTrafficDetail(TrafficVO vo) {
        writer.setText(vo.getTra_username());
        date.setText(vo.getTra_time());
        content.setText(vo.getTra_content());
        if (!TextUtils.isEmpty(vo.getTra_content_image()))
            Glide.with(this).load(Common.SERVER_URL + vo.getTra_content_image())
                    .into(image);
    }
}