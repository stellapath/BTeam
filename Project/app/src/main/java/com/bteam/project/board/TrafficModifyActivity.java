package com.bteam.project.board;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bteam.project.R;
import com.bteam.project.board.model.TrafficVO;
import com.bteam.project.network.FileUploadHelper;
import com.bteam.project.util.Common;
import com.bumptech.glide.Glide;
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

public class TrafficModifyActivity extends AppCompatActivity
        implements BottomSheetImagePicker.OnImagesSelectedListener {

    private static final String TAG = "TrafficModifyActivity";

    private TrafficVO vo;

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
        setContentView(R.layout.activity_traffic_modify);

        if (getIntent() != null) {
            vo = (TrafficVO) getIntent().getSerializableExtra("vo");
        } else {
            Toast.makeText(this, "게시글 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        initView();
        getBoardInfo();

        // 이미지 첨부 클릭 시
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                modifyTraffic();
            }
        });
    }

    private void initView() {
        content = findViewById(R.id.traffic_modify_content);
        close = findViewById(R.id.traffic_modify_close);
        writer = findViewById(R.id.traffic_modify_writer);
        date = findViewById(R.id.traffic_modify_date);
        filename = findViewById(R.id.traffic_modify_file_name);
        attachment = findViewById(R.id.traffic_modify_attachment);
        insertButton = findViewById(R.id.traffic_modify_button);
        image = findViewById(R.id.traffic_modify_image);
        delete = findViewById(R.id.traffic_modify_delete);
    }

    private void getBoardInfo() {
        content.setText(vo.getTra_content());
        writer.setText(vo.getTra_username());
        date.setText(vo.getTra_time());
        if (TextUtils.isEmpty(vo.getTra_content_image())) {
            Glide.with(this).load(Common.SERVER_URL + vo.getTra_content_image())
                    .into(image);
            delete.setVisibility(View.VISIBLE);
        }
    }

    private void modifyTraffic() {
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
                .url(Common.SERVER_URL + "andTrafficModify")
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
}