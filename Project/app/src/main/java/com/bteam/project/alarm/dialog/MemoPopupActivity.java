package com.bteam.project.alarm.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bteam.project.R;

public class MemoPopupActivity extends AppCompatActivity {

    EditText memoContent;
    CheckBox memoCheckBox;
    Button memoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_popup);

        memoContent = findViewById(R.id.memo_content);
        memoCheckBox = findViewById(R.id.memo_checkbox);
        memoButton = findViewById(R.id.memo_save);

        if (getIntent() != null) {
            Intent intent = getIntent();
            String content = intent.getStringExtra("memoContent");
            boolean isRead = intent.getBooleanExtra("isRead", false);
            memoContent.setText(content);
            memoCheckBox.setChecked(isRead);
        }

        memoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("memoContent", memoContent.getText().toString());
                intent.putExtra("isRead", memoCheckBox.isChecked());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        setTitle("메모 작성");
    }
}