package com.bteam.project.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.Common;
import com.bteam.project.R;
import com.bteam.project.network.Singleton;
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
                final String id = login_id.getText().toString();
                final String pw = login_pw.getText().toString();

                if (id.length() == 0) {
                    showToast("아이디를 입력하세요.");
                }
                if (pw.length() == 0) {
                    showToast("비밀번호를 입력하세요");
                }

                sendLoginRequest(id, pw);

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
                showToast("서버와의 연결이 원활하지 않습니다.");
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
        Singleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void onPostExecute(UserVO s) {
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
}