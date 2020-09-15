package com.bteam.project;

import com.bteam.project.user.model.UserVO;

public class Common {

    /* Spring URL */
    public static final String SERVER_URL = "http://192.168.0.165/bteam/";

    /* 로그인 정보가 들어오는 곳 */
    public static UserVO login_info;

    /* Weather RequestCode */
    public static final int REQUEST_WEATHER = 1;

    /* Intent RequestCode */
    public static final int REQUEST_LOGIN = 100;
    public static final int REQUEST_SIGNUP = 101;
    public static final int REQUEST_ADDRESS = 102;
    public static final int REQUEST_MYPAGE = 103;

    /* Alarm RequestCode */
    public static final int REQUEST_RINGTONE = 200;

}
