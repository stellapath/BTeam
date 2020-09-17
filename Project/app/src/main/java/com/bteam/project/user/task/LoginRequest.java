package com.bteam.project.user.task;

import android.os.AsyncTask;

import com.bteam.project.Common;
import com.bteam.project.user.model.UserVO;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginRequest extends AsyncTask<Void, Void, UserVO> {

    private String id, pw;

    public LoginRequest(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    @Override
    protected UserVO doInBackground(Void... voids) {

        String param = "user_email=" + id
                + "&user_pw=" + pw + "";
        String json = null;
        UserVO vo = null;
        try {
            // 서버 연결
            URL url = new URL(Common.SERVER_URL + "andLogin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.connect();

            // 서버 연결 확인
            if (conn.getResponseCode() != 200) {
                return null;
            }

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
            json = buffer.toString().trim();
            Gson gson = new Gson();
            vo = gson.fromJson(json, UserVO.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

}