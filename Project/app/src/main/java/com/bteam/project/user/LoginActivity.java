package com.bteam.project.user;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bteam.project.Common;
import com.bteam.project.R;
import com.bteam.project.user.model.UserVO;
import com.bteam.project.user.task.LoginRequest;

import java.util.concurrent.ExecutionException;

/**
 * 네비게이션 드로어에서 헤더를 터치했을 때
 * 로그인 액티비티
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    EditText login_id, login_pw;
    Button login_login, login_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);
        login_login = findViewById(R.id.login_login);
        login_signup = findViewById(R.id.login_signup);

        login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login_id.getText().toString().length() == 0) {
                    showToast("아이디를 입력하세요.");
                }
                if (login_pw.getText().toString().length() == 0) {
                    showToast("비밀번호를 입력하세요");
                }

                String id = login_id.getText().toString();
                String pw = login_pw.getText().toString();

                // 로그인 처리전 서버 연결확인
                // int 값 반환받으며 (연결실패 0,와이파이 = 1, 데이터 = 2)
                int status = isNetworkConnected(getApplicationContext());
                if(status == 1){
                    // 로그인 처리
                    LoginRequest request = new LoginRequest(id, pw);
                    request.execute();
                    UserVO vo = null;
                    try {
                        vo = request.get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    onPostExcute(vo);
                    Toast.makeText(LoginActivity.this, "와이파이 연결 확인",Toast.LENGTH_SHORT).show();
                }else if(status == 2){
                    Toast.makeText(LoginActivity.this, "모바일 연결 확인",Toast.LENGTH_SHORT).show();
                }else if(status == 0){
                    Toast.makeText(LoginActivity.this, "연결x 연결 확인",Toast.LENGTH_SHORT).show();
                }
            }
        });

        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, Common.REQUEST_SIGNUP);
            }
        });

    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void onPostExcute(UserVO s) {
        if (s != null) {
            Toast.makeText(LoginActivity.this, "로그인 되었습니다.",
                    Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            Common.login_info = s;
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "존재하지 않는 아이디이거나, 비밀번호가 잘못되었습니다.",
                    Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
        }
    }

    public int isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null) {
            int type = info.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(LoginActivity.this, "와이파이로 연결", Toast.LENGTH_SHORT).show();
                return 1;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(LoginActivity.this, "모바일로 연결", Toast.LENGTH_SHORT).show();
                return 2;
            }else {
                Toast.makeText(LoginActivity.this, "모르는 인터넷 연결", Toast.LENGTH_SHORT).show();
                return 5;
            }
        } else {
            Log.d("isconnected : ", "False => 데이터 통신 불가!!!");
            return 0;
        }
    }

}