package com.bteam.project.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.bteam.project.user.model.UserVO;
import com.bteam.project.util.Common;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.HashMap;
import java.util.Map;

public class EmailVerifyActivity extends AppCompatActivity {

    private UserVO s;
    private EditText input;
    private Button verify, send;
    private ProgressWheel wheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        Toolbar toolbar = findViewById(R.id.email_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("이메일 인증");

        if (getIntent() != null) {
            s = (UserVO) getIntent().getSerializableExtra("vo");
        } else {
            Toast.makeText(this, "로그인 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }

        initView();

        wheel.setVisibility(View.GONE);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendKey();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_txt = input.getText().toString();
                if (TextUtils.isEmpty(input_txt)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EmailVerifyActivity.this);
                    builder.setTitle("인증번호 확인");
                    builder.setMessage("인증번호를 입력하세요.");
                    builder.setPositiveButton("확인", null);
                    builder.show();
                    return;
                }
                if (input_txt.equals(s.getUser_key())) {
                    verifyEmail();
                } else {
                    Toast.makeText(EmailVerifyActivity.this,
                            "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        input = findViewById(R.id.email_input);
        verify = findViewById(R.id.email_verify);
        send = findViewById(R.id.email_send);
        wheel = findViewById(R.id.email_wheel);
    }

    private void sendKey() {
        wheel.setVisibility(View.VISIBLE);
        final String url = Common.SERVER_URL + "andSendKey";
        StringRequest request = new StringRequest(Request.Method.POST, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                wheel.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(EmailVerifyActivity.this);
                builder.setTitle("이메일 발송");
                builder.setMessage("이메일로 인증번호가 발송되었습니다.\n인증번호를 확인해 주세요.");
                builder.setPositiveButton("확인", null);
                builder.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                wheel.setVisibility(View.GONE);
                Toast.makeText(EmailVerifyActivity.this,
                        "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", s.getUser_email());
                map.put("key", s.getUser_key());
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void verifyEmail() {
        wheel.setVisibility(View.VISIBLE);
        final String url = Common.SERVER_URL + "andVerifyEmail";
        StringRequest request = new StringRequest(Request.Method.POST, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    wheel.setVisibility(View.GONE);
                    Toast.makeText(EmailVerifyActivity.this,
                            "이메일이 인증되었습니다.", Toast.LENGTH_SHORT).show();
                    Common.login_info = s;
                    setResult(RESULT_OK);
                    finish();
                } else {
                    wheel.setVisibility(View.GONE);
                    Toast.makeText(EmailVerifyActivity.this,
                            "이메일 인증에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                wheel.setVisibility(View.GONE);
                Toast.makeText(EmailVerifyActivity.this,
                        "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", s.getUser_email());
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}