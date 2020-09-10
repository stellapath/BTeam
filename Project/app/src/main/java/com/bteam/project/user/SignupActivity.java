package com.bteam.project.user;

import android.content.Context;
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
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity"; 
    
    private EditText signup_email, signup_pw, signup_pw2, signup_nickname, signup_phone,
                     signup_zipcode, signup_address, signup_detail, signup_birth;
    private Button signup_andAddress, signup_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_email = findViewById(R.id.signup_email);
        signup_pw = findViewById(R.id.signup_pw);
        signup_pw2 = findViewById(R.id.signup_pw2);
        signup_nickname = findViewById(R.id.signup_nickname);
        signup_phone = findViewById(R.id.signup_phone);
        signup_zipcode = findViewById(R.id.signup_zipcode);
        signup_address = findViewById(R.id.signup_address);
        signup_detail = findViewById(R.id.signup_detail);
        signup_birth = findViewById(R.id.signup_birth);

        signup_andAddress = findViewById(R.id.signup_andAddress);
        signup_submit = findViewById(R.id.signup_submit);

        signup_andAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                startActivityForResult(intent, Common.REQUEST_ADDRESS);
            }
        });

        signup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* 유효성 검사 */
                if (signup_email.getText().toString().length() == 0) {
                    Snackbar.make(view, "이메일을 입력하세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                
                if (signup_pw.getText().toString().length() == 0) {
                    Snackbar.make(view, "비밀번호를 입력하세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (signup_pw2.getText().toString().length() == 0) {
                    Snackbar.make(view, "비밀번호를 확인하세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                
                if (!signup_pw.getText().toString().equals(signup_pw2.getText().toString())) {
                    Snackbar.make(view, "비밀번호를 확인하세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (signup_nickname.getText().toString().length() == 0) {
                    Snackbar.make(view, "닉네임을 입력하세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                UserVO vo = getUserVO();
                SignupRequest request = new SignupRequest(vo, getApplicationContext());
                request.execute();
                finish();
            }
        });

    }

    private class SignupRequest extends AsyncTask<Void, Void, String> {

        private UserVO vo;
        private Context mContext;

        public SignupRequest(UserVO vo, Context context) {
            this.vo = vo;
            this.mContext = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String param = "user_email=" + vo.getUser_email()
                         + "&user_pw=" + vo.getUser_pw()
                         + "&user_nickname=" + vo.getUser_nickname()
                         + "&user_phone=" + vo.getUser_phone()
                         + "&user_zipcode=" + vo.getUser_zipcode()
                         + "&user_address=" + vo.getUser_address()
                         + "&detail_address=" + vo.getDetail_address()
                         + "&user_birth" + vo.getUser_birth()
                         + "&user_key" + vo.getUser_key() + "";
            String result = "";
            try {
                // 서버 연결
                URL url = new URL(Common.SERVER_URL + "andSignup");
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
                result = buffer.toString().trim();
                Log.d(TAG, "doInBackground: result=" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.contains("1")) {
                Toast.makeText(mContext, "회원가입이 완료되었습니다.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "회원가입에 실패했습니다. 잠시 후에 다시 시도해 주세요.",
                        Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }

    // EditText에서 값을 가져와 UserVO에 저장
    private UserVO getUserVO() {
        UserVO vo = new UserVO();
        vo.setUser_email(signup_email.getText().toString());
        vo.setUser_pw(signup_pw.getText().toString());
        vo.setUser_nickname(signup_nickname.getText().toString());
        vo.setUser_phone(signup_phone.getText().toString());
        vo.setUser_zipcode(signup_zipcode.getText().toString());
        vo.setUser_address(signup_address.getText().toString());
        vo.setDetail_address(signup_detail.getText().toString());
        vo.setUser_birth(signup_birth.getText().toString());
        vo.setUser_key(getKey(false, 10));
        return vo;
    }

    // 난수 생성
    private boolean lowerCheck;
    private int size;
    private String randomKey() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;

        do {
            num = random.nextInt(75) + 48;
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char) num);
            } else {
                continue;
            }

        } while (sb.length() < size);
        if (lowerCheck) {
            return sb.toString().toLowerCase();
        }
        return sb.toString();
    }

    public String getKey(boolean lowerCheck, int size) {
        this.lowerCheck = lowerCheck;
        this.size = size;
        return randomKey();
    }

}