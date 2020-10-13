package com.bteam.project.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.util.Common;
import com.bteam.project.R;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.user.model.UserVO;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 네비게이션 드로어에서 헤더를 터치했을 때
 * 로그인 액티비티
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private final int SIGNUP_REQUEST_CODE = 101;
    private final int EMAIL_VERIFY_REQUEST_CODE = 105;
    private final int FORGOT_PASSWORD_REQUEST_CODE = 106;

    private EditText login_id, login_pw;
    private Button login_login, login_signup;
    private TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("로그인");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);
        login_login = findViewById(R.id.login_login);
        login_signup = findViewById(R.id.login_signup);
        forgot = findViewById(R.id.login_forgot);

        login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = login_id.getText().toString();
                final String pw = login_pw.getText().toString();

                if (id.length() == 0) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력하세요.",
                            Toast.LENGTH_SHORT).show();
                }
                if (pw.length() == 0) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요.",
                            Toast.LENGTH_SHORT).show();
                }

                sendLoginRequest(id, pw);

            }
        });

        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, SIGNUP_REQUEST_CODE);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivityForResult(intent, FORGOT_PASSWORD_REQUEST_CODE);
            }
        });
    }

    // 서버 통신
    private void sendLoginRequest(final String id, final String pw) {
        String url = Common.SERVER_URL + "andLogin";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        UserVO vo = gson.fromJson(response.trim(), UserVO.class);
                        onPostExecute(vo);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "서버와의 연결이 원활하지 않습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", id);
                params.put("user_pw", pw);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void onPostExecute(UserVO s) {
        if (s != null) {
            if (s.getUser_key().equals("OK")) {
                // 이메일 인증이 완료된 경우
                Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                Common.login_info = s;
                finish();
            } else {
                // 이메일 인증이 완료되지 않은 경우
                Intent intent = new Intent(LoginActivity.this, EmailVerifyActivity.class);
                intent.putExtra("vo", s);
                startActivityForResult(intent, EMAIL_VERIFY_REQUEST_CODE);
            }
        } else {
            Toast.makeText(this, "존재하지 않는 아이디이거나, 비밀번호를 잘못 입력하셨습니다.",
                    Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EMAIL_VERIFY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}