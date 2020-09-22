package com.bteam.project.board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bteam.project.R;

import org.w3c.dom.Text;

public class BoardInsertActivity extends AppCompatActivity {

    private int category;

    private EditText title, content;
    private ImageButton close;
    private TextView writer, date, filename, filesize;
    private LinearLayout attachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_insert);

        initView();

        // 글 작성을 위한 카테고리를 받아온다
        if (getIntent() != null) {
            Intent intent = getIntent();
            category = intent.getIntExtra("category", 0);
        }

        // 작성 버튼 클릭 시
        // 글 작성 요청

    }

    private void initView() {
        title = findViewById(R.id.board_insert_title);
        content = findViewById(R.id.board_insert_content);
        close = findViewById(R.id.board_insert_close);
        writer = findViewById(R.id.board_insert_writer);
        date = findViewById(R.id.board_insert_date);
        filename = findViewById(R.id.board_insert_file_name);
        filesize = findViewById(R.id.board_insert_file_size);
        attachment = findViewById(R.id.board_insert_attachment);
    }
}