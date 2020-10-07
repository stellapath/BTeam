package com.bteam.project.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bteam.project.R;
import com.bteam.project.board.adapter.TrafficListAdapter;
import com.bteam.project.board.model.TrafficVO;
import com.bteam.project.home.adapter.TrafficAdapter;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.util.Common;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class TrafficListActivity extends AppCompatActivity {

    private RecyclerView board_traffic_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_list);

        Toolbar toolbar = findViewById(R.id.traffic_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("실시간 교통 정보 게시판");

        initView();

        getBoardList();
    }

    private void initView() {
        board_traffic_recyclerView = findViewById(R.id.board_traffic_recyclerView);
    }

    private void getBoardList() {
        final String url = Common.SERVER_URL + "andTraffic";
        StringRequest request = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                TrafficVO[] vos = gson.fromJson(response.trim(), TrafficVO[].class);
                List<TrafficVO> list = Arrays.asList(vos);
                onPostExecute(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TrafficListActivity.this,
                        "게시글을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void onPostExecute(List<TrafficVO> list) {
        board_traffic_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        board_traffic_recyclerView.setAdapter(new TrafficListAdapter(list));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}