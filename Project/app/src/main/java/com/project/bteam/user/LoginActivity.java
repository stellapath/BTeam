package com.project.bteam.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.bteam.R;
import com.project.bteam.util.MyMotionToast;

/**
 * 네비게이션 드로어에서 헤더를 터치했을 때
 * 로그인 액티비티
 */
public class LoginActivity extends AppCompatActivity {

    private EditText email, pw;
    private Button login, signup;
    private FirebaseAuth auth;
    private TextView forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        initView();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = pw.getText().toString();

                if (TextUtils.isEmpty(txt_email)) {
                    MyMotionToast.warningToast(LoginActivity.this, "이메일을 입력하세요.");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(txt_password)) {
                    MyMotionToast.warningToast(LoginActivity.this, "비밀번호를 입력하세요.");
                    pw.requestFocus();
                } else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        MyMotionToast.successToast(LoginActivity.this,
                                                "로그인 되었습니다.");
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        MyMotionToast.errorToast(LoginActivity.this,
                                                "아이디 또는 비밀번호가 잘못 입력되었습니다.");
                                    }
                                }
                            });
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    private void initView() {
        email = findViewById(R.id.login_id);
        pw = findViewById(R.id.login_pw);
        login = findViewById(R.id.login_login);
        signup = findViewById(R.id.login_signup);
        forgot_password = findViewById(R.id.login_forgot);
    }
}