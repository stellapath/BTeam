package com.bteam.project.user;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
                LoginRequest request = new LoginRequest();
                request.execute();
            }
        });

        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    private class LoginRequest extends AsyncTask<Void, Void, UserVO> {

        @Override
        protected UserVO doInBackground(Void... voids) {

            String param = "user_email=" + login_id.getText().toString()
                         + "&user_pw=" + login_pw.getText().toString() + "";
            String json = null;
            UserVO vo = null;
            try {
                // 서버 연결
                URL url = new URL(Common.SERVER_URL + "andLogin");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                conn.connect();

                // 파라미터 전달
                OutputStream out = conn.getOutputStream();
                out.write(param.getBytes("UTF-8"));
                out.flush();
                out.close();

                // 결과 가져오기
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                json = buffer.toString().trim();
                Log.d(TAG, "doInBackground: json=" + json);
                Gson gson = new Gson();
                vo = gson.fromJson(json, UserVO.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return vo;
        }

        @Override
        protected void onPostExecute(UserVO s) {
            if (s != null) {
                Toast.makeText(LoginActivity.this, "로그인 되었습니다.",
                        Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            } else {
                Toast.makeText(LoginActivity.this, "로그인에 실패했습니다. 잠시 후에 다시 시도해 주세요.",
                        Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
            }
            Common.login_info = s;

            finish();
            super.onPostExecute(s);
        }
    }
}