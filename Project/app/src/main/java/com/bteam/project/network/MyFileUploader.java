package com.bteam.project.network;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class MyFileUploader extends AsyncTask<Void, Void, String> {

    String serverURL;
    Map<String, String> params;
    Uri fileUri;

    public MyFileUploader(String serverURL, Map<String, String> params, Uri fileUri) {
        this.serverURL = serverURL;
        this.params = params;
        this.fileUri = fileUri;
    }

    String twoHyphens = "--";
    String boundary = "*****" + System.currentTimeMillis() + "*****";
    String lineEnd = "\r\n";
    String result = "";

    String[] q = fileUri.toString().split("/");
    int idx = q.length - 1;

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // 서버 연결
            URL url = new URL(serverURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            conn.setRequestProperty("Content-Type", "multipart/form-data; charset=utf-8; boundary=" + boundary);

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            // 파라미터 전송
            StringBuffer outputData = new StringBuffer();
            for (Map.Entry<String, String> map : params.entrySet()) {
                outputData.append(twoHyphens + boundary + lineEnd);
                outputData.append("Content-Disposition: form-data; name=\"" + map.getKey() + "\"" + lineEnd);
                outputData.append("Content-Type: text/plain; charset=utf-8;" + lineEnd);
                outputData.append(lineEnd + map.getValue() + lineEnd);
            }

            // 파일 정보 전송
            if (fileUri != null) {
                outputData.append(twoHyphens + boundary + lineEnd);
                outputData.append("Content-Disposition: form-data; name=\"file\";");
                outputData.append("filename=\"" + q[idx] + "\"" + lineEnd);
                outputData.append("Content-Type: " + URLConnection.guessContentTypeFromName(q[idx]) + lineEnd);
                outputData.append("Content-Transfer-Encoding: binary" + lineEnd);
            }

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(outputData.toString());

            // 파일 전송
            if (fileUri != null) {
                FileInputStream fis = new FileInputStream(fileUri.getPath());
                byte[] buffer = new byte[1 * 1024 * 1024];
                int bytesRead = -1;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }
                fis.close();
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.flush();
            dos.close();

            // 결과 받아오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
