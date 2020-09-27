package com.bteam.project.alarm.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bteam.project.R;
import com.bteam.project.util.MyMotionToast;

import java.util.Locale;

/**
 * 알람탭의 메모를 작성하는 곳
 */
public class MemoPopupActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private EditText memoContent;
    private CheckBox memoCheckBox;
    private Button memoButton, readButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_popup);

        setTitle("메모 작성");

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.KOREA);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        MyMotionToast.errorToast(MemoPopupActivity.this, "한글 언어팩이 존재하지 않습니다.");
                    } else {
                        tts.setPitch(0.7f);
                        tts.setSpeechRate(1.2f);
                    }
                }

            }
        });

        memoContent = findViewById(R.id.memo_content);
        memoCheckBox = findViewById(R.id.memo_checkbox);
        memoButton = findViewById(R.id.memo_save);
        readButton = findViewById(R.id.memo_readButton);

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

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(memoContent.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}