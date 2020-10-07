package com.bteam.project.user;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bteam.project.R;
import com.bteam.project.network.FileUploadHelper;
import com.bteam.project.util.Common;
import com.bteam.project.util.MyMotionToast;
import com.kroegerama.imgpicker.BottomSheetImagePicker;
import com.kroegerama.imgpicker.ButtonType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MyPageActivity extends AppCompatActivity implements BottomSheetImagePicker.OnImagesSelectedListener {

    private static final String TAG = "MyPageActivity";

    private ImageView profileImage, backButton;
    private TextView name, email;
    private TextView tab_myInfo;
    private TextView myInfo_email, myInfo_nickname, myInfo_tel, myInfo_addr, myInfo_birth;
    private LinearLayout myInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        if (Common.login_info == null) {
            MyMotionToast.errorToast(MyPageActivity.this, "로그인 정보가 존재하지 않습니다.");
            finish();
            return;
        }

        initView();
        initProfile();

        // 뒤로가기 버튼 클릭 시
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // 프로필 이미지 클릭 시
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
                new BottomSheetImagePicker.Builder("fileProvider")
                        .cameraButton(ButtonType.Button)
                        .galleryButton(ButtonType.Button)
                        .singleSelectTitle(R.string.image_picker_title)
                        .requestTag("single")
                        .show(getSupportFragmentManager(), null);
            }
        });

        // 내 정보 클릭 시
        tab_myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 하단에 내 정보 표시
            }
        });
    }

    private void initView() {
        backButton = findViewById(R.id.profile_backButton);
        profileImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);

        tab_myInfo = findViewById(R.id.profile_tab_myInfo);
        myInfo = findViewById(R.id.profile_myInfo);
        myInfo_email = findViewById(R.id.profile_myInfo_email);
        myInfo_nickname = findViewById(R.id.profile_myInfo_nickname);
        myInfo_tel = findViewById(R.id.profile_myInfo_tel);
        myInfo_addr = findViewById(R.id.profile_myInfo_addr);
        myInfo_birth = findViewById(R.id.profile_myInfo_birth);
    }

    private void initProfile() {
        if (Common.login_info == null) {
            MyMotionToast.errorToast(MyPageActivity.this, "로그인 정보를 불러오는 데 실패했습니다.");
            finish();
        }
        profileImage.setImageBitmap(Common.login_info.getProfile_image());
        name.setText(Common.login_info.getUser_nickname());
        email.setText(Common.login_info.getUser_email());
    }

    @Override
    public void onImagesSelected(@NotNull List<? extends Uri> list, @Nullable String s) {
        Uri profileImageUri = list.get(0);
        Log.i(TAG, "onImagesSelected: " + profileImageUri.toString());
        imageUpload(new File(FileUploadHelper.getPath(this, profileImageUri)));
    }

    private void imageUpload(File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", Common.login_info.getUser_email())
                .addFormDataPart("file", file.getName(), RequestBody.create(file, MultipartBody.FORM))
                .build();
        Request request = new Request.Builder()
                .url(Common.SERVER_URL + "andImageUpload")
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response);
            }
        });
    }

    private void requestStoragePermission() {
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