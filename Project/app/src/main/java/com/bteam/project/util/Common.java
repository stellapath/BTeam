package com.bteam.project.util;

import com.bteam.project.user.model.UserVO;

public class Common {

    /* Spring URL */
    public static final String SERVER_URL = "http://192.168.0.165/bteam/";

    /* 로그인 정보가 들어오는 곳 */
    public static UserVO login_info;

    /* PERMISSON */
    public static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 45;

    /* Weather RequestCode */
    public static final int REQUEST_WEATHER = 10;

    /* Intent RequestCode */
    public static final int REQUEST_LOGIN = 100;
    public static final int REQUEST_SIGNUP = 101;
    public static final int REQUEST_ADDRESS = 102;
    public static final int REQUEST_MYPAGE = 103;

    /* Alarm RequestCode */
    public static final int REQUEST_WAKEUP_ALARM = 0;
    public static final int REQUEST_ARRIVAL_ALARM = 1;
    public static final int REQUEST_NOTIFICATION_ALARM = 2;
    public static final int REQUEST_RINGTONE = 201;
    public static final int REQUEST_MEMO = 202;
    public static final int REQUEST_DESTINATION = 203;
    public static final int REQUEST_AUTOCOMPLETE_1 = 204;
    public static final int REQUEST_AUTOCOMPLETE_2 = 205;

    /* Board RequestCode */
    public static final int REQUEST_BOARD_INSERT = 300;
    public static final int REQUEST_BOARD_FILE = 301;

    /* Permission */
    public static final int REQUEST_OVERRAY_PERMISSION = 2323;
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 2424;

}
