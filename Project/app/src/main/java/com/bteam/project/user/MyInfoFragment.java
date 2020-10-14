package com.bteam.project.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bteam.project.MainActivity;
import com.bteam.project.R;
import com.bteam.project.user.model.UserVO;
import com.bteam.project.util.Common;

public class MyInfoFragment extends Fragment {

    private TextView email, nickname, tel, addr, birth, modify;
    private LinearLayout myInfo;
    private UserVO info;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_info, container, false);

        initView(root);
        info = Common.login_info;
        getProfile();

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyPageActivity) getActivity()).modifyInfo();
            }
        });

        return root;
    }

    private void initView(View root) {
        myInfo = root.findViewById(R.id.profile_myInfo);
        email = root.findViewById(R.id.profile_myInfo_email);
        nickname = root.findViewById(R.id.profile_myInfo_nickname);
        tel = root.findViewById(R.id.profile_myInfo_tel);
        addr = root.findViewById(R.id.profile_myInfo_addr);
        birth = root.findViewById(R.id.profile_myInfo_birth);
        modify = root.findViewById(R.id.profile_myInfo_modify);
    }

    private void getProfile() {
        if (!TextUtils.isEmpty(info.getUser_email()))
            email.setText("이메일 : " + info.getUser_email());
        if (!TextUtils.isEmpty(info.getUser_nickname()))
            nickname.setText("별명 : " + info.getUser_nickname());
        if (!TextUtils.isEmpty(info.getUser_phone()))
            tel.setText("연락처 : " + info.getUser_phone());
        if (!TextUtils.isEmpty(info.getUser_address()))
            addr.setText("주소 : " + info.getUser_address());
        if (!TextUtils.isEmpty(info.getUser_birth()))
            birth.setText("생일 : " + info.getUser_birth());
    }
}
