package com.bteam.project.user;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bteam.project.R;
import com.bteam.project.network.MyFileUploader;
import com.bteam.project.network.MyImageLoader;
import com.bteam.project.util.Common;
import com.bteam.project.util.MyMotionToast;
import com.kroegerama.imgpicker.BottomSheetImagePicker;
import com.kroegerama.imgpicker.ButtonType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyPageActivity extends AppCompatActivity implements BottomSheetImagePicker.OnImagesSelectedListener {

    private static final String TAG = "MyPageActivity";

    private ImageView profileImage;
    private TextView name, email;

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

        // 프로필 이미지 클릭 시
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 프로필 이미지 수정
                new BottomSheetImagePicker.Builder("fileProvider")
                        .cameraButton(ButtonType.Button)
                        .galleryButton(ButtonType.Button)
                        .singleSelectTitle(R.string.image_picker_title)
                        .requestTag("single")
                        .show(getSupportFragmentManager(), null);
            }
        });
    }

    private void initView() {
        profileImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
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
        String url = Common.SERVER_URL + "andProfileImageUpload";
        Map<String, String> params = new HashMap<>();
        params.put("email", Common.login_info.getUser_email());

        MyFileUploader fileUploader = new MyFileUploader(url, params, profileImageUri);
        fileUploader.execute();

        MyImageLoader imageLoader = new MyImageLoader(this);
        imageLoader.getProfileImage();

        initProfile();
    }
}