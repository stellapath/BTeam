package com.hanul.project.ui.user.task;

import android.os.AsyncTask;
import android.util.Log;

import com.hanul.project.CommonMethod;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 로그인 정보를 가져오는 AsyncTask
 */
public class LogInTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "LogInTask";

    private String user_id, user_pw;

    public LogInTask(String user_id, String user_pw) {
        this.user_id = user_id;
        this.user_pw = user_pw;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL(CommonMethod.url);

            // URL 연결 객체 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            Log.d(TAG, "doInBackground: " + response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
