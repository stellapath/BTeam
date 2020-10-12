package com.bteam.project.board;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bteam.project.R;
import com.bteam.project.network.FileUploadHelper;
import com.bteam.project.util.Common;
import com.kroegerama.imgpicker.BottomSheetImagePicker;
import com.kroegerama.imgpicker.ButtonType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TrafficInsertActivity extends AppCompatActivity
        implements BottomSheetImagePicker.OnImagesSelectedListener {

    private static final String TAG = "TrafficInsertActivity";

    private EditText content;
    private ImageButton close, delete;
    private ImageView image;
    private TextView writer, date, filename;
    private LinearLayout attachment;
    private Button insertButton;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_insert);

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

        // 이미지 삭제 버튼 클릭 시
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file = null;
                image.setImageResource(R.drawable.placeholder);
                image.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }
        });

        // 작성 버튼 클릭 시
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 글 작성 요청
                insertTraffic();
            }
        });
    }

    private void initView() {
        content = findViewById(R.id.traffic_insert_content);
        close = findViewById(R.id.traffic_insert_close);
        writer = findViewById(R.id.traffic_insert_writer);
        date = findViewById(R.id.traffic_insert_date);
        filename = findViewById(R.id.traffic_insert_file_name);
        attachment = findViewById(R.id.traffic_insert_attachment);
        insertButton = findViewById(R.id.traffic_insert_button);
        image = findViewById(R.id.traffic_insert_image);
        delete = findViewById(R.id.traffic_insert_delete);
    }

    private void insertTraffic() {
        RequestBody requestBody = null;
        if (file != null) {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("tra_user_email", Common.login_info.getUser_email())
                    .addFormDataPart("tra_username", Common.login_info.getUser_nickname())
                    .addFormDataPart("tra_user_image", Common.login_info.getUser_imagepath())
                    .addFormDataPart("tra_content", content.getText().toString())
                    .addFormDataPart("file", file.getName(), RequestBody.create(file, MultipartBody.FORM))
                    .build();
        } else {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("tra_user_email", Common.login_info.getUser_email())
                    .addFormDataPart("tra_username", Common.login_info.getUser_nickname())
                    .addFormDataPart("tra_user_image", Common.login_info.getUser_imagepath())
                    .addFormDataPart("tra_content", content.getText().toString())
                    .build();
        }

        Request request = new Request.Builder()
                .url(Common.SERVER_URL + "andTrafficInsert")
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onImagesSelected(@NotNull List<? extends Uri> list, @Nullable String s) {
        Uri uri = list.get(0);
        image.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        image.setImageURI(uri);
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