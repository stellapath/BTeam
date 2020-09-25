package com.bteam.project.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bteam.project.R;
import com.bteam.project.util.Common;
import com.bteam.project.util.MyMotionToast;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import java.util.List;

public class MyPageActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        initView();
        initProfile();

        // 프로필 이미지 클릭 시
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 프로필 이미지 수정
                ImagePicker.create(MyPageActivity.this)
                        .returnMode(ReturnMode.ALL)
                        .folderMode(true)
                        .toolbarFolderTitle("폴더")
                        .toolbarImageTitle("이미지 선택")
                        .toolbarArrowColor(Color.BLACK)
                        .includeVideo(false)
                        .single()
                        .showCamera(true)
                        .start();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);

        }
    }
}