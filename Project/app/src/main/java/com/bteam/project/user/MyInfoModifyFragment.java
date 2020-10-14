package com.bteam.project.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.R;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.user.model.UserVO;
import com.bteam.project.util.Common;

import java.util.HashMap;
import java.util.Map;

public class MyInfoModifyFragment extends Fragment {

    private EditText nickname, tel, addr, birth;
    private TextView email, submit;
    private UserVO info;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_info_modify, container, false);

        initView(root);
        info = Common.login_info;

        setDefaultInfo();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo();
            }
        });

        return root;
    }

    private void initView(View v) {
        email = v.findViewById(R.id.myInfo_modify_email);
        nickname = v.findViewById(R.id.myInfo_modify_nickname);
        tel = v.findViewById(R.id.myInfo_modify_tel);
        addr = v.findViewById(R.id.myInfo_modify_addr);
        birth = v.findViewById(R.id.myInfo_modify_birth);
        submit = v.findViewById(R.id.myInfo_modify_submit);
    }

    private void setDefaultInfo() {
        email.setText("이메일 : " + info.getUser_email());
        nickname.setText(info.getUser_nickname());
        if (!TextUtils.isEmpty(info.getUser_phone()))
            tel.setText(info.getUser_phone());
        if (!TextUtils.isEmpty(info.getUser_address()))
            tel.setText(info.getUser_address());
        if (!TextUtils.isEmpty(info.getUser_birth()))
            tel.setText(info.getUser_birth());
    }

    private void updateUserInfo() {
        final String url = Common.SERVER_URL + "andUserUpdate";
        final String nickname_txt = nickname.getText().toString();
        final String tel_txt = tel.getText().toString();
        final String addr_txt = addr.getText().toString();
        final String birth_txt = birth.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    Common.login_info.setUser_nickname(nickname_txt);
                    Common.login_info.setUser_phone(tel_txt);
                    Common.login_info.setUser_address(addr_txt);
                    Common.login_info.setUser_birth(birth_txt);
                } else {
                    Toast.makeText(getActivity(), "업데이트에 실패했습니다.", Toast.LENGTH_SHORT)
                            .show();
                }
                ((MyPageActivity) getActivity()).saveInfo();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "업데이트에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show();
                ((MyPageActivity) getActivity()).saveInfo();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_email", info.getUser_email());
                map.put("user_nickname", nickname_txt);
                map.put("user_phone", tel_txt);
                map.put("user_address", addr_txt);
                map.put("user_birth", birth_txt);
                return map;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}
