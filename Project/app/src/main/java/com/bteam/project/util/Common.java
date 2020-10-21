package com.bteam.project.util;

import com.bteam.project.user.model.UserVO;

public class Common {

    /* Spring URL */
    public static final String SERVER_URL = "http://112.164.58.7:8282/bteam/";

    /* 로그인 정보가 들어오는 곳 */
    public static UserVO login_info;

    /* Alarm RequestCode */
    public static final int REQUEST_WAKEUP_ALARM = 0;
    public static final int REQUEST_ARRIVAL_ALARM = 1;
    public static final int REQUEST_NOTIFICATION_ALARM = 2;
    public static final int REQUEST_RINGTONE = 201;
    public static final int REQUEST_MEMO = 202;
    public static final int REQUEST_DESTINATION = 203;
    public static final int REQUEST_AUTOCOMPLETE_1 = 204;
    public static final int REQUEST_AUTOCOMPLETE_2 = 205;

    /* Permission */
    public static final int REQUEST_OVERRAY_PERMISSION = 2323;
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 2424;

}
