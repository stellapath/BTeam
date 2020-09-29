package com.bteam.project.board;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bteam.project.util.Common;
import com.bteam.project.R;
import com.bteam.project.board.model.BoardVO;
import com.bteam.project.util.MyMotionToast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BoardInsertActivity extends AppCompatActivity {

    private int category;

    private EditText title, content;
    private ImageButton close;
    private TextView writer, date, filename, filesize;
    private LinearLayout attachment;
    private Button insertButton;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_insert);

        initView();

        // 글 작성을 위한 카테고리를 받아온다
        if (getIntent() != null) {
            Intent intent = getIntent();
            category = intent.getIntExtra("category", 0);
        }

        // 작성자는 로그인 정보에서 가져온다
        writer.setText(Common.login_info.getUser_nickname());

        // 날짜는 현재 시간으로 자동 생성한다
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분");
        writer.setText(sdf.format(date));

        // 파일 첨부 클릭 시
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 외부저장소 접근 권한 체크
                checkPermission();

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                // intent.setType("image/*"); <---- 이미지만 선택 가능
                intent.setType("*/*"); // <---- 모든파일 선택가능
                /* setType을 하지 않으면 예외 발생 */
                startActivityForResult(intent, Common.REQUEST_BOARD_FILE);
            }
        });

        // TODO 파일 삭제 버튼

        // 작성 버튼 클릭 시
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 글 작성 요청
                sendBoardInsertRequest();
                finish();
            }
        });

    }

    private void sendBoardInsertRequest() {
        BoardVO vo = getBoardVO();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("board_email", vo.getBoard_email())
                .addFormDataPart("board_nickname", vo.getBoard_nickname())
                .addFormDataPart("board_category", vo.getBoard_category() + "")
                .addFormDataPart("board_title", vo.getBoard_title())
                .addFormDataPart("board_content", vo.getBoard_content())
                .addFormDataPart("file", getFileName(fileUri),
                        RequestBody.create(MultipartBody.FORM, new File(fileUri.getPath())))
                .build();

        Request request = new Request.Builder()
                .url(Common.SERVER_URL + "andBoardInsert")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                MyMotionToast.errorToast(BoardInsertActivity.this, "서버와의 연결이 원활하지 않습니다.");
                finish();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                MyMotionToast.successToast(BoardInsertActivity.this, "게시글이 작성되었습니다.");
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 파일 선택 후 파일 정보를 받는 곳
        if (requestCode == Common.REQUEST_BOARD_FILE && resultCode == RESULT_OK) {
            fileUri = data.getData();
            // ↓ 비트맵으로 받는 방법 ↓
            /*
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(in);
                imgVwSelected.setImageBitmap(image);
                in.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            */
            // 파일 선택 후 파일 이름 변경
            filename.setText(getFileName(fileUri));
            // TODO 파일크기 구하기
        }

    }

    private void initView() {
        title = findViewById(R.id.board_insert_title);
        content = findViewById(R.id.board_insert_content);
        close = findViewById(R.id.board_insert_close);
        writer = findViewById(R.id.board_insert_writer);
        date = findViewById(R.id.board_insert_date);
        filename = findViewById(R.id.board_insert_file_name);
        filesize = findViewById(R.id.board_insert_file_size);
        attachment = findViewById(R.id.board_insert_attachment);
        insertButton = findViewById(R.id.board_insert_button);
    }

    // 작성한 글의 정보들을 객체에 넣는다
    private BoardVO getBoardVO() {
        BoardVO vo = new BoardVO();
        vo.setBoard_category(category);
        vo.setBoard_title(title.getText().toString());
        vo.setBoard_content(content.getText().toString());
        vo.setBoard_email(Common.login_info.getUser_email());
        vo.setBoard_nickname(Common.login_info.getUser_nickname());
        return vo;
    }

    // Uri로 파일 이름 구하기
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void checkPermission() {
        String temp = "";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        if (TextUtils.isEmpty(temp) == false) { // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
        }
    }
}