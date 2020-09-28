package com.bteam.project.network;

import android.net.Uri;
import android.os.AsyncTask;

import com.bteam.project.util.Common;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class FileUploader extends AsyncTask<Void, Void, String> {

    Map<String, String> params;
    Uri fileUri;

    public FileUploader(Map<String, String> params, Uri fileUri) {
        this.params = params;
        this.fileUri = fileUri;
    }

    String twoHyphens = "--";
    String boundary = "*****" + System.currentTimeMillis() + "*****";
    String lineEnd = "\r\n";

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // 서버 연결
            URL url = new URL(Common.SERVER_URL + "andProfileImageUpload");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; charset=utf-8; boundary=" + boundary);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            // 파라미터 전송
            StringBuffer outputData = new StringBuffer();
            for (Map.Entry<String, String> map : params.entrySet()) {
                outputData.append(twoHyphens + )
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
