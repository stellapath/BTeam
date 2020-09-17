package com.bteam.project.user.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bteam.project.Common;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* 이미지 가져오는 곳 */
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String user_email;
    private ImageView drawer_image;

    public ImageLoadTask(String user_email, ImageView drawer_image) {
        this.user_email = user_email;
        this.drawer_image = drawer_image;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        String param = "user_email=" + user_email;
        Bitmap bitmap = null;

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

            // 파라미터 전달
            OutputStream out = conn.getOutputStream();
            out.write(param.getBytes("UTF-8"));
            out.flush();
            out.close();

            // 이미지 가져오기
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        drawer_image.setImageBitmap(bitmap);

        super.onPostExecute(bitmap);
    }
}
