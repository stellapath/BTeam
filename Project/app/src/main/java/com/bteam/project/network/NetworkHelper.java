package com.bteam.project.network;

import android.app.Activity;
import android.content.Context;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bteam.project.util.Common;
import com.bteam.project.util.MyMotionToast;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NetworkHelper {

    private Context context;

    public NetworkHelper(Context context) {
        this.context = context;
    }

    public void getProfileImage() {
        if (Common.login_info == null) return;
        final String url = Common.SERVER_URL + Common.login_info.getUser_imagepath();
        ImageLoader imageLoader = VolleySingleton.getInstance(context.getApplicationContext())
                .getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (Common.login_info != null) Common.login_info.setProfile_image(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                MyMotionToast.errorToast((Activity) context, "프로필 이미지를 불러오는 데 실패했습니다.");
            }
        });
    }
}
