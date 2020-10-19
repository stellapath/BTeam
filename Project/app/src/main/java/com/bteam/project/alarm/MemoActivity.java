package com.bteam.project.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bteam.project.R;
import com.bteam.project.alarm.dialog.MemoPopupActivity;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;

import java.util.Locale;

public class MemoActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private AlarmSharedPreferencesHelper sharPrefHelper;
    private TextView textView;
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        sharPrefHelper = new AlarmSharedPreferencesHelper(this);

        Toolbar toolbar = findViewById(R.id.memo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("저장된 메모");

        initView();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.KOREA);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MemoActivity.this, "한글 언어팩이 존재하지 않습니다.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        tts.setPitch(0.7f);
                        tts.setSpeechRate(1.2f);
                    }
                }
            }
        });

        textView.setText(sharPrefHelper.getMemo());
        tts.speak(sharPrefHelper.getMemo(), TextToSpeech.QUEUE_FLUSH, null, "ttsEnd");

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        tts.stop();
    }

    private void initView() {
        textView = findViewById(R.id.memo_saved);
        finishButton = findViewById(R.id.memo_finishButton);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}