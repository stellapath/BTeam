package com.bteam.project.board;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bteam.project.Common;
import com.bteam.project.R;
import com.bteam.project.board.model.BoardVO;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                SendBoardInsertRequest request = new SendBoardInsertRequest(getBoardVO(), fileUri);
                request.execute();
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

    // 게시글 작성 요청 보내기
    private class SendBoardInsertRequest extends AsyncTask<Void, Void, String> {

        BoardVO vo;
        Uri fileUri;

        public SendBoardInsertRequest(BoardVO vo, Uri fileUri) {
            this.vo = vo;
            this.fileUri = fileUri;
        }

        @Override
        protected String doInBackground(Void... voids) {

            String result = null;

            // Multipart 전송시에 필요한 값 준비
            String twoHyphens = "--";
            String lineEnd = "\r\n";
            String boundary = "****" + System.currentTimeMillis() + "****";

            // 파라미터 준비
            String[] data = {
                    vo.getBoard_category() + "",
                    vo.getBoard_nickname(),
                    vo.getBoard_title(),
                    vo.getBoard_content(),
                    vo.getBoard_email()
            };
            String[] dataName = {
                    "board_category",
                    "board_nickname",
                    "board_title",
                    "board_content",
                    "board_email"
            };

            try {
                byte[] buffer;
                int maxBufferSize = 5 * 1024 * 1024;

                // 서버 연결
                URL url = new URL(Common.SERVER_URL + "andBoardInsert");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setReadTimeout(3000);
                conn.setConnectTimeout(3000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                // 파라미터
                String delimiter = twoHyphens + boundary + lineEnd;
                StringBuffer postDataBuilder = new StringBuffer();

                // 문자열 데이터
                for (int i = -1; i < data.length; i++) {
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append("Content-Disposition: form-data; name=\"" + dataName[i] +"\""+lineEnd+lineEnd+data[i]+lineEnd);
                }

                // 파일이 존재할 때에만 생성
                if (fileUri != null) {
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append("Content-Disposition: form-data; name=\"" + "file" + "\";filename=\"" + getFileName(fileUri) +"\"" + lineEnd);
                }

                // 파라미터 및 파일 전송
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.write(postDataBuilder.toString().getBytes());

                if (fileUri != null) {
                    dos.writeBytes(lineEnd);
                    FileInputStream fileInputStream = new FileInputStream(getFileFromUri(fileUri));
                    buffer = new byte[maxBufferSize];
                    int length = -1;
                    while ((length = fileInputStream.read(buffer)) != -1) {
                        dos.write(buffer, 0, length);
                    }
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    fileInputStream.close();
                } else {
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                }
                dos.flush();
                dos.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.contains("0")) {
                Toast.makeText(BoardInsertActivity.this, "게시글이 작성되었습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            } else {
                Toast.makeText(BoardInsertActivity.this, "게시글 작성에 실패했습니다. 잠시 후에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
            }
            finish();

            super.onPostExecute(s);
        }
    }

    // Uri로 파일 찾기
    private File getFileFromUri(Uri uri) {
        return new File(uri.getPath());
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