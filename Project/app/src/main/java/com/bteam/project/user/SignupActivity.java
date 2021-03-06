package com.bteam.project.user;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private final int AUTOCOMPLETE_REQUEST_CODE = 401;

    private EditText signup_email, signup_pw, signup_pw2, signup_nickname, signup_phone,
                     signup_zipcode, signup_address, signup_detail, signup_birth;
    private Button address, submit, emailCheck;
    private LinearLayout expand;
    private TextView expandBtn;

    private boolean isChecked = false;
    private boolean isAvailable = false;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("회원가입");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);

        initView();

        expand.setVisibility(View.GONE);

        expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpanded) {
                    isExpanded = false;
                    expand.setVisibility(View.GONE);
                } else {
                    isExpanded = true;
                    expand.setVisibility(View.VISIBLE);
                }
            }
        });

        emailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(signup_email.getText().toString())) {
                    showAlert("이메일 확인", "이메일을 입력하세요.");
                    signup_email.requestFocus();
                    return;
                } else if(!Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",signup_email.getText())){
                    showAlert("이메일 확인", "올바른 이메일 형식이 아닙니다.");
                    signup_email.requestFocus();
                    return;
                }
                emailCheck();
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields =
                        Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(SignupActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* 유효성 검사 */
                if (signup_email.getText().toString().length() == 0) {
                    showAlert("이메일 확인", "이메일을 입력하세요.");
                    signup_email.requestFocus();
                    return;
                } else if(!Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",signup_email.getText())){
                    showAlert("이메일 확인", "올바른 이메일 형식이 아닙니다.");
                    signup_email.requestFocus();
                    return;
                } else if (!isChecked) {
                    showAlert("이메일 확인", "이메일 중복확인을 해주세요.");
                    signup_email.requestFocus();
                    return;
                } else if (!isAvailable) {
                    showAlert("이메일 확인", "이미 사용중인 이메일 입니다.");
                    signup_email.requestFocus();
                    return;
                }

                if (signup_pw.getText().toString().length() == 0) {
                    showAlert("비밀번호 확인", "비밀번호를 입력하세요.");
                    signup_pw.requestFocus();
                    return;
                }else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{6,15}$", signup_pw.getText())){
                    showAlert("비밀번호 확인","비밀번호는 6 ~ 15자리로\n숫자, 영어, 특수문자를 모두 포함해야 합니다.");
                    signup_pw.requestFocus();
                    return;
                }

                if (signup_pw2.getText().toString().length() == 0) {
                    showAlert("비밀번호 확인", "비밀번호 확인을 입력하세요.");
                    signup_pw2.requestFocus();
                    return;
                }

                if (!signup_pw.getText().toString().equals(signup_pw2.getText().toString())) {
                    showAlert("비밀번호 확인", "비밀번호가 일치하지 않습니다.");
                    signup_pw2.setText("");
                    signup_pw.requestFocus();
                    return;
                }

                if (signup_nickname.getText().toString().length() == 0) {
                    showAlert("별명 확인", "별명을 입력하세요.");
                    signup_nickname.requestFocus();
                    return;
                } else if (!Pattern.matches("^[가-힣a-zA-Z0-9]{2,6}$", signup_nickname.getText())){
                    showAlert("별명 확인", "2 ~ 6자리의 한글, 영어, 숫자만 사용가능합니다.");
                    signup_nickname.requestFocus();
                   return;

                }

                UserVO vo = getUserVO();
                sendSignupRequest(vo);

            }
        });

        // 비밀번호를 바꿀 시 테두리 효과 초기
        signup_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    signup_pw.setBackgroundResource(R.drawable.custom_input);
                    signup_pw2.setBackgroundResource(R.drawable.custom_input);
                }
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
                if(password.equals(passwordConfirm)){
                    signup_pw.setBackgroundResource(R.drawable.custom_input_green);
                    signup_pw2.setBackgroundResource(R.drawable.custom_input_green);
                } else {
                    signup_pw.setBackgroundResource(R.drawable.custom_input_red);
                    signup_pw2.setBackgroundResource(R.drawable.custom_input_red);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void initView() {
        signup_email = findViewById(R.id.signup_email);
        signup_pw = findViewById(R.id.signup_pw);
        signup_pw2 = findViewById(R.id.signup_pw2);
        signup_nickname = findViewById(R.id.signup_nickname);
        signup_phone = findViewById(R.id.signup_phone);
        signup_zipcode = findViewById(R.id.signup_zipcode);
        signup_address = findViewById(R.id.signup_address);
        signup_detail = findViewById(R.id.signup_detail);
        signup_birth = findViewById(R.id.signup_birth);
        address = findViewById(R.id.signup_andAddress);
        submit = findViewById(R.id.signup_submit);
        expand = findViewById(R.id.signup_expand);
        expandBtn = findViewById(R.id.signup_expandBtn);
        emailCheck = findViewById(R.id.signup_emailCheck);
    }

    // 이메일 중복확인
    private void emailCheck() {
        final String url = Common.SERVER_URL + "andEmailCheck";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    showAlert("중복확인", "사용가능한 이메일 입니다.");
                    isAvailable = true;
                } else {
                    showAlert("중복확인", "이미 사용중인 이메일 입니다.");
                    isAvailable = false;
                }
                isChecked = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this,
                        "서버와의 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", signup_email.getText().toString());
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    // 주소 검색 api 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> politicals = new ArrayList<>();
            Place place = Autocomplete.getPlaceFromIntent(data);
            AddressComponents addressComponents = place.getAddressComponents();
            for (AddressComponent ac : addressComponents.asList()) {
                Log.i(TAG, "onActivityResult: " + ac.getTypes());
                if (ac.getTypes().contains("postal_code"))
                    signup_zipcode.setText(ac.getName());
                if (ac.getTypes().contains("political"))
                    politicals.add(ac.getName());
                if (ac.getTypes().contains("premise"))
                    politicals.add(ac.getName());
            }
            for (int i = politicals.size() - 1; i >= 0; i--) {
                if (i == politicals.size() - 1) continue;
                signup_address.append(politicals.get(i) + " ");
            }
        }
    }

    // 서버 통신
    private void sendSignupRequest(final UserVO vo) {
        String url = Common.SERVER_URL + "andSignup";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onPostExecute(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, "서버와의 연결이 원활하지 않습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", vo.getUser_email());
                params.put("user_pw", vo.getUser_pw());
                params.put("user_nickname", vo.getUser_nickname());
                params.put("user_phone", vo.getUser_phone());
                params.put("user_zipcode", vo.getUser_zipcode());
                params.put("user_address", vo.getUser_address());
                params.put("detail_address", vo.getDetail_address());
                params.put("user_birth", vo.getUser_birth());
                params.put("user_key", vo.getUser_key());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void onPostExecute(String s) {
        if (s.contains("true")) {
            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "회원가입에 실패했습니다. 잠시 후에 다시 시도해 주세요.",
                    Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlert(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("확인", null);
        builder.show();
    }
}