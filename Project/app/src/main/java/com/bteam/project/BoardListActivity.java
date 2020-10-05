package com.bteam.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.board.adapter.BoardListAdapter;
import com.bteam.project.board.model.BoardVO;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.util.Common;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardListActivity extends AppCompatActivity {

    private RecyclerView board_recyclerView;
    private List<BoardVO> list;

    private int category = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        initView();

        list = new ArrayList<>();

        category = getIntent().getIntExtra("category", 0);
        getBoardList(category);
    }

    private void initView() {
        board_recyclerView = findViewById(R.id.board_recyclerView);
    }

    private void getBoardList(int category) {
        String url = "";
        switch (category) {
            case 0:
                url = Common.SERVER_URL + "andNotice";
                break;
        }
        StringRequest request = new StringRequest(Request.Method.GET, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                BoardVO[] vo = gson.fromJson(response.trim(), BoardVO[].class);
                list.addAll(Arrays.asList(vo));
                onPostExecute(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BoardListActivity.this, "게시글을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void onPostExecute(List<BoardVO> list) {
        board_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        board_recyclerView.setAdapter(new BoardListAdapter(this, list));
    }
}