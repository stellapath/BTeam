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

import com.bteam.project.R;
import com.bteam.project.user.model.UserVO;
import com.bteam.project.util.Common;

public class MyInfoFragment extends Fragment {

    private TextView myInfo_email, myInfo_nickname, myInfo_tel, myInfo_addr, myInfo_birth;
    private LinearLayout myInfo;
    private UserVO info = Common.login_info;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_info, container, false);

        initView(root);
        getProfile();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView(View root) {
        myInfo = root.findViewById(R.id.profile_myInfo);
        myInfo_email = root.findViewById(R.id.profile_myInfo_email);
        myInfo_nickname = root.findViewById(R.id.profile_myInfo_nickname);
        myInfo_tel = root.findViewById(R.id.profile_myInfo_tel);
        myInfo_addr = root.findViewById(R.id.profile_myInfo_addr);
        myInfo_birth = root.findViewById(R.id.profile_myInfo_birth);
    }

    private void getProfile() {
        if (!TextUtils.isEmpty(info.getUser_email()))
            myInfo_email.append(info.getUser_email());
        if (!TextUtils.isEmpty(info.getUser_nickname()))
            myInfo_nickname.append(info.getUser_nickname());
        if (!TextUtils.isEmpty(info.getUser_phone()))
            myInfo_tel.append(info.getUser_phone());
        if (!TextUtils.isEmpty(info.getUser_address()))
            myInfo_addr.append(info.getUser_address());
        if (!TextUtils.isEmpty(info.getUser_birth()))
            myInfo_birth.append(info.getUser_birth());
    }
}
