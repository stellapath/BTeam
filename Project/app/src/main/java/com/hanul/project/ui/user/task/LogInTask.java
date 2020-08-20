package com.hanul.project.ui.user.task;

import android.os.AsyncTask;
import android.util.Log;

import com.hanul.project.CommonMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        try {
            // 연결 객체 생성
            url = new URL(CommonMethod.url);
            conn = (HttpURLConnection) url.openConnection();

            // 설정 단계
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);

            // 파라미터 보내기
            StringBuffer sb = new StringBuffer();
            sb.append("/andLogin");
            sb.append("?user_id=" + user_id + "&user_pw=" + user_pw);

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),
                    "UTF-8"));
            pw.write(sb.toString());
            pw.close();

            // 결과를 저장
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                    "UTF-8"));
            String line;
            while((line = br.readLine()) != null) {
                result += line;
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 연결 종료
            conn.disconnect();
        }

        Log.d(TAG, "doInBackground: " + result);
        
        return null;
    }

}
