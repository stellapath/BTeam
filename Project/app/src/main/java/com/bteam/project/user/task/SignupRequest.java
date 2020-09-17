package com.bteam.project.user.task;

import android.os.AsyncTask;

import com.bteam.project.Common;
import com.bteam.project.user.model.UserVO;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignupRequest extends AsyncTask<Void, Void, String> {

    private UserVO vo;

    public SignupRequest(UserVO vo) {
        this.vo = vo;
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
                + "&user_birth=" + vo.getUser_birth()
                + "&user_key=" + vo.getUser_key() + "";
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}