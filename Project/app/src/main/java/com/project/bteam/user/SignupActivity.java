package com.project.bteam.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.bteam.R;
import com.project.bteam.util.MyMotionToast;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText email, pw, pw2, username;
    private Button submit;
    private FirebaseAuth auth;
    private DatabaseReference refer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        initView();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = pw.getText().toString();
                String txt_password2 = pw2.getText().toString();

                if (TextUtils.isEmpty(txt_username)) {
                    MyMotionToast.warningToast(SignupActivity.this,
                            "별명을 입력하세요.");
                    username.requestFocus();
                } else if (TextUtils.isEmpty(txt_email)) {
                    MyMotionToast.warningToast(SignupActivity.this,
                            "이메일을 입력하세요.");
                    email.requestFocus();
                } else if (!Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", txt_email)) {
                    MyMotionToast.warningToast(SignupActivity.this,
                            "유효하지 않은 이메일 입니다.");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(txt_password)) {
                    MyMotionToast.warningToast(SignupActivity.this,
                            "비밀번호를 입력하세요.");
                    pw.requestFocus();
                } else if (txt_password.length() < 6) {
                    MyMotionToast.warningToast(SignupActivity.this,
                            "비밀번호는 6자 이상 입력하세요.");
                    pw.requestFocus();
                } else if (!txt_password.equals(txt_password2)) {
                    MyMotionToast.warningToast(SignupActivity.this,
                            "비밀번호 확인을 정확히 입력하세요.");
                    pw2.requestFocus();
                }

                else register(txt_username, txt_email, txt_password);
            }
        });
    }

    private void initView() {
        email = findViewById(R.id.signup_email);
        pw = findViewById(R.id.signup_pw);
        pw2 = findViewById(R.id.signup_pw2);
        username = findViewById(R.id.signup_username);
        submit = findViewById(R.id.signup_submit);
    }

    private void register(final String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            refer = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            Map<String, String> map = new HashMap<>();
                            map.put("id", userid);
                            map.put("username", username);
                            map.put("imageURL", "default");

                            refer.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        MyMotionToast.successToast(SignupActivity.this,
                                                "회원가입에 성공했습니다.");
                                        finish();
                                    }
                                }
                            });
                        } else {
                            MyMotionToast.errorToast(SignupActivity.this,
                                    "회원가입에 실패했습니다.");
                        }
                    }
                });
    }

}