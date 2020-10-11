package com.bteam.project.user;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.R;
import com.bteam.project.network.FileUploadHelper;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.util.Common;
import com.bumptech.glide.Glide;
import com.kroegerama.imgpicker.BottomSheetImagePicker;
import com.kroegerama.imgpicker.ButtonType;
import com.pnikosis.materialishprogress.ProgressWheel;

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
    private TextView tab_myInfo, tab_myPost;
    private MyInfoFragment myInfoFragment;
    private MyPostFragment myPostFragment;
    private ProgressWheel wheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        if (Common.login_info == null) {
            Toast.makeText(this, "로그인 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT)
                    .show();
            finish();
            return;
        }

        initView();
        initProfile();

        myInfoFragment = new MyInfoFragment();
        myPostFragment = new MyPostFragment();

        // 내 정보 표시
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_container, myInfoFragment)
                .commit();

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
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.profile_container, myInfoFragment).commit();
                tab_myInfo.setTextColor(Color.BLUE);
                tab_myPost.setTextColor(Color.GRAY);
            }
        });

        // 내가 쓴 글 클릭 시
        tab_myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.profile_container, myPostFragment).commit();
                tab_myPost.setTextColor(Color.BLUE);
                tab_myInfo.setTextColor(Color.GRAY);
            }
        });

    }

    private void initView() {
        backButton = findViewById(R.id.profile_backButton);
        profileImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        tab_myInfo = findViewById(R.id.profile_tab_myInfo);
        wheel = findViewById(R.id.profile_progress);
        tab_myPost = findViewById(R.id.profile_tab_myPost);
    }

    private void initProfile() {
        if (TextUtils.isEmpty(Common.login_info.getUser_imagepath())) {
            profileImage.setImageResource(R.drawable.ic_user);
        } else {
            Glide.with(MyPageActivity.this)
                    .load(Common.SERVER_URL + Common.login_info.getUser_imagepath())
                    .into(profileImage);
        }
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
        wheel.setVisibility(View.VISIBLE);
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
                wheel.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response);
                reloadImage();
            }
        });
    }

    private void reloadImage() {
        String url = Common.SERVER_URL + "andGetImage?email=" + Common.login_info.getUser_email();
        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url,
        new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Common.login_info.setUser_imagepath(response.trim());
                Glide.with(MyPageActivity.this).load(Common.SERVER_URL + response.trim())
                        .into(profileImage);
                wheel.setVisibility(View.GONE);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                wheel.setVisibility(View.GONE);
            }
        });
        VolleySingleton.getInstance(MyPageActivity.this).addToRequestQueue(request);
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