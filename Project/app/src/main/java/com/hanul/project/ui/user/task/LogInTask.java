package com.hanul.project.ui.user.task;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.hanul.project.network.CommonMethod;
import com.hanul.project.network.RequestHttpUrlConnection;
import com.hanul.project.ui.user.LoginActivity;
import com.hanul.project.ui.user.model.UserDTO;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* 로그인 정보를 가져오는 AsyncTask */
public class LoginTask extends AsyncTask<Void, Void, String> {

    private String url;
    private ContentValues params;

    public LoginTask(String url, ContentValues params) {
        this.url = url;
        this.params = params;
    }

    @Override
    protected String doInBackground(Void... values) {
        RequestHttpUrlConnection connection = new RequestHttpUrlConnection();
        String result = connection.request(url, params);

        return result;
    }
/*

    private UserDTO getJson(InputStream is) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        UserDTO dto = new UserDTO();

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("user_id"))            dto.setUser_id(reader.nextString());
            else if (key.equals("user_pw"))       dto.setUser_pw(reader.nextString());
            else if (key.equals("user_name"))     dto.setUser_name(reader.nextString());
            else if (key.equals("user_nickname")) dto.setUser_nickname(reader.nextString());
            else if (key.equals("user_email"))    dto.setUser_email(reader.nextString());
            else if (key.equals("user_phone"))    dto.setUser_phone(reader.nextString());
            else if (key.equals("user_birth"))    dto.setUser_birth(reader.nextString());
            else if (key.equals("user_key"))      dto.setUser_key(reader.nextString());
            else reader.skipValue();
        }
        reader.endObject();

        return dto;
    }
*/

}
