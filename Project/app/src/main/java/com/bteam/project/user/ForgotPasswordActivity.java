package com.bteam.project.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.R;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.util.Common;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = findViewById(R.id.forgot_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("비밀번호 찾기");

        initView();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        final String url = Common.SERVER_URL + "andResetPassword";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int responseCode = Integer.parseInt(response);
                switch (responseCode) {
                    case -1 :
                        showAlert("이메일 확인", "이메일이 존재하지 않습니다.");
                        break;
                    case 0 :
                        showAlert("비밀번호 재설정 실패", "알 수 없는 오류가 발생했습니다.");
                        break;

                    case 1 :
                        showAlert("비밀번호 재설정 완료", "재설정된 비밀번호가 이메일로 전송되었습니다.");
                        setResult(RESULT_OK);
                        finish();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ForgotPasswordActivity.this,
                        "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", email.getText().toString());
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void initView() {
        email = findViewById(R.id.forgot_email);
        send = findViewById(R.id.forgot_send);
    }

    private void showAlert(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("에", null);
        builder.show();
    }
}