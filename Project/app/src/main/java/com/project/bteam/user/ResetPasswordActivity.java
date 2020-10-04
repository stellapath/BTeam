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
import com.google.firebase.auth.FirebaseAuth;
import com.project.bteam.R;
import com.project.bteam.util.MyMotionToast;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Button reset;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        firebaseAuth = FirebaseAuth.getInstance();
        initView();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();

                if (TextUtils.isEmpty(txt_email)) {
                    MyMotionToast.warningToast(ResetPasswordActivity.this, "이메일을 입력하세요.");
                } else {
                    firebaseAuth.sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                MyMotionToast.successToast(ResetPasswordActivity.this,
                                        "이메일이 전송되었습니다.");
                                finish();
                            } else {
                                MyMotionToast.errorToast(ResetPasswordActivity.this,
                                        "이메일 전송에 실패했습니다.");
                            }
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        email = findViewById(R.id.forgot_email);
        reset = findViewById(R.id.forgot_button);
    }
}