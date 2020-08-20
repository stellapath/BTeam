package com.hanul.project.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hanul.project.R;
import com.hanul.project.ui.user.model.UserDTO;
import com.hanul.project.ui.user.task.LogInTask;

/**
 * 로그인 화면
 */
public class LogInActivity extends AppCompatActivity {

    // 로그인 성공 시 유저 정보를 저장할 변수
    public static UserDTO logInState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        final EditText login_id = findViewById(R.id.login_id);
        final EditText login_pw = findViewById(R.id.login_pw);
        Button login_loginBtn = findViewById(R.id.login_loginBtn);
        Button login_singupBtn = findViewById(R.id.login_signupBtn);

        // 로그인 버튼 클릭 이벤트
        login_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_id = login_id.getText().toString();
                String input_pw = login_pw.getText().toString();

                // 아이디 입력을 하지 않았을 때
                if (input_id == null) {
                    Toast.makeText(LogInActivity.this, "아이디를 입력하세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 입력을 하지 않았을 때
                if (input_pw == null) {
                    Toast.makeText(LogInActivity.this, "비밀번호를 입력하세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // 로그인 처리
                LogInTask logInTask = new LogInTask(input_id, input_pw);
                logInTask.execute();

                // 로그인이 된 경우
                if (logInState != null) {
                    Snackbar.make(view, logInState.getUser_nickname() + "님 환영합니다.",
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogInActivity.this, "존재하지 않는 아이디이거나, " +
                            "비밀번호를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}