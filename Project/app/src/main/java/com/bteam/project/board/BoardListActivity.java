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
import com.bteam.project.R;
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

    private int category = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        Toolbar toolbar = findViewById(R.id.board_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        category = getIntent().getIntExtra("category", 0);
        switch (category) {
            case 0 :
                setTitle("공지사항");
                break;
        }

        initView();

        getBoardList(category);

        // TODO 스크롤 내리면 다음페이지 불러오는 처리
    }

    private void initView() {
        board_recyclerView = findViewById(R.id.board_recyclerView);
    }

    private void getBoardList(int category) {
        String url = Common.SERVER_URL + "andBoardList?category=" + category;
        StringRequest request = new StringRequest(Request.Method.GET, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                BoardVO[] vo = gson.fromJson(response.trim(), BoardVO[].class);
                onPostExecute(Arrays.asList(vo));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BoardListActivity.this, "게시글을 불러오는 데 실패했습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void onPostExecute(List<BoardVO> list) {
        board_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        board_recyclerView.setAdapter(new BoardListAdapter(this, list));
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