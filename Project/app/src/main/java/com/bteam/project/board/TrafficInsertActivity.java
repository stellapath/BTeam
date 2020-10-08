package com.bteam.project.board;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bteam.project.R;
import com.bteam.project.network.FileUploadHelper;
import com.bteam.project.util.Common;
import com.kroegerama.imgpicker.BottomSheetImagePicker;
import com.kroegerama.imgpicker.ButtonType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TrafficInsertActivity extends AppCompatActivity
        implements BottomSheetImagePicker.OnImagesSelectedListener {

    private EditText title, content;
    private ImageButton close;
    private TextView writer, date, filename;
    private LinearLayout attachment;
    private Button insertButton;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_insert);

        initView();

        // 작성자는 로그인 정보에서 가져온다
        writer.setText(Common.login_info.getUser_nickname());

        // 날짜는 현재 시간으로 자동 생성한다
        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분");
        date.setText(sdf.format(date1));

        // 이미지 첨부 클릭 시
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 외부저장소 접근 권한 체크
                checkPermission();
                new BottomSheetImagePicker.Builder("fileProvider")
                        .cameraButton(ButtonType.Button)
                        .galleryButton(ButtonType.Button)
                        .singleSelectTitle(R.string.image_picker_title)
                        .requestTag("single")
                        .show(getSupportFragmentManager(), null);
            }
        });

        // TODO 파일 삭제 버튼

        // 작성 버튼 클릭 시
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 글 작성 요청
                finish();
            }
        });
    }

    private void initView() {
        title = findViewById(R.id.board_insert_title);
        content = findViewById(R.id.board_insert_content);
        close = findViewById(R.id.board_insert_close);
        writer = findViewById(R.id.board_insert_writer);
        date = findViewById(R.id.board_insert_date);
        filename = findViewById(R.id.board_insert_file_name);
        attachment = findViewById(R.id.board_insert_attachment);
        insertButton = findViewById(R.id.board_insert_button);
    }

    private void insertTraffic() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("tra_")
    }

    @Override
    public void onImagesSelected(@NotNull List<? extends Uri> list, @Nullable String s) {
        Uri uri = list.get(0);
        file = new File( FileUploadHelper.getPath(this, uri) );
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 거절된 상태
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 거절된 상태
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);
        }
    }
}