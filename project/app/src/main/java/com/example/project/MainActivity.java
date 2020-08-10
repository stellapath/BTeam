package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.project.Adapt.MainRecyclerAdapter;
import com.example.project.dto.ViewDTO;

public class MainActivity extends AppCompatActivity {

    private MainRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView main_recyclerView = findViewById(R.id.main_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        main_recyclerView.setLayoutManager(linearLayoutManager);

        /** 어댑터 연결 */
        adapter = new MainRecyclerAdapter();
        main_recyclerView.setAdapter(adapter);

        getData();

    }

    private void getData() {
        String[] title = { "제목1", "제목2", "제목3" };
        String[] content = { "내용1", "내용2", "내용3" };
        int[] resId = { R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground };

        for (int i = 0; i < title.length; i++) {
            ViewDTO dto = new ViewDTO();
            dto.setTitle(title[i]);
            dto.setContent(content[i]);
            dto.setResid(resId[i]);

            adapter.addItem(dto);
        }

        adapter.notifyDataSetChanged();
    }

}