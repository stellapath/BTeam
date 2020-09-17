package com.bteam.project.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bteam.project.Common;
import com.bteam.project.R;
import com.bteam.project.user.model.UserVO;
import com.bteam.project.user.task.SignupRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private Context context;

    private EditText signup_email, signup_pw, signup_pw2, signup_nickname, signup_phone,
                     signup_zipcode, signup_address, signup_detail, signup_birth;
    private Button signup_andAddress, signup_submit;

    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        context = getApplicationContext();

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
                    signup_email.requestFocus();
                    return;
                }else if(!Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",signup_email.getText())){
                    Snackbar.make(view, "유효한 이메일을 입력하세요.", Snackbar.LENGTH_SHORT).show();
                    signup_email.requestFocus();
                    return;
                }

                if (signup_pw.getText().toString().length() == 0) {
                    Snackbar.make(view, "비밀번호를 입력하세요.", Snackbar.LENGTH_SHORT).show();
                    signup_pw.requestFocus();
                    return;
                }else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{6,15}$", signup_pw.getText())){
                    Snackbar.make(view, "6~15자리 숫자.영어,특수문자를 사용하세요", Snackbar.LENGTH_SHORT).show();
                    signup_pw.requestFocus();
                    return;
                }

                if (signup_pw2.getText().toString().length() == 0) {
                    Snackbar.make(view, "비밀번호를 확인을 입력하세요.", Snackbar.LENGTH_SHORT).show();
                    signup_pw2.requestFocus();
                    return;
                }

                if (!signup_pw.getText().toString().equals(signup_pw2.getText().toString())) {
                    Snackbar.make(view, "비밀번호가 일치하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                    signup_pw.setText("");
                    signup_pw2.setText("");
                    signup_pw.requestFocus();
                    return;
                }

                if (signup_nickname.getText().toString().length() == 0) {
                    Snackbar.make(view, "닉네임을 입력하세요.", Snackbar.LENGTH_SHORT).show();
                    signup_nickname.requestFocus();
                    return;
                } else if (!Pattern.matches("^[가-힣a-zA-Z0-9]{2,6}$", signup_nickname.getText())){
                    /*닉네임 유효성 검사 한글,영어,숫자 2~6자리로 설정*/
                    Snackbar.make(view, "한글,영어 2~6자리를 사용하세요.", Snackbar.LENGTH_SHORT).show();
                    signup_nickname.requestFocus();
                    return;

                }

                UserVO vo = getUserVO();
                SignupRequest request = new SignupRequest(vo);
                request.execute();
                String result = "";
                try {
                    result = request.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                onPostExcute(result);
            }
        });

        /* 비밀번호 확인란에 입력하면서 같은지 실시간 확인*/
        signup_pw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = signup_pw.getText().toString();
                String passwordConfirm = signup_pw2.getText().toString();
                /* 비밀번호가 일치하면 둘다 초록색, 추후 비밀번호 일치하지 않음 글씨로 변경할까? */
                if(password.equals(passwordConfirm)){
                    signup_pw.setBackgroundColor(Color.GREEN);
                    signup_pw2.setBackgroundColor(Color.GREEN);
                }else{      /* 다르면 비밀번호 확인창 빨강색 */
                    signup_pw.setBackgroundColor(Color.GREEN);
                    signup_pw2.setBackgroundColor(Color.RED);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void onPostExcute(String s) {
        if (s.contains("1")) {
            Toast.makeText(context, "회원가입이 완료되었습니다.",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "회원가입에 실패했습니다. 잠시 후에 다시 시도해 주세요.",
                    Toast.LENGTH_SHORT).show();
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