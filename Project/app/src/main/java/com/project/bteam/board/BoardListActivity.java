package com.project.bteam.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.bteam.R;

public class BoardListActivity extends AppCompatActivity {

    private DatabaseReference refer;
    private FloatingActionButton fab;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        initView();

        if (getIntent() != null) {
            category = getIntent().getStringExtra("category");
        }

        loadBoardList();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BoardListActivity.this, BoardInsertActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        fab = findViewById(R.id.board_list_insert);
    }

    private void loadBoardList() {
        refer = FirebaseDatabase.getInstance().getReference("Categories").child(category);
    }
}