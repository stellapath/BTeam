package com.hanul.project.ui.user.model;

/**
 * 유저 정보를 저장하는 DTO
 */
public class UserDTO {

    private String user_id, user_pw, user_name, user_nickname,
            user_email, user_phone, user_birth, user_key;

    public UserDTO() {}

    public UserDTO(String user_id, String user_pw, String user_name, String user_nickname, String user_email,
                   String user_phone, String user_birth, String user_key) {
        super();
        this.user_id = user_id;
        this.user_pw = user_pw;
        this.user_name = user_name;
        this.user_nickname = user_nickname;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.user_birth = user_birth;
        this.user_key = user_key;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_birth() {
        return user_birth;
    }

    public void setUser_birth(String user_birth) {
        this.user_birth = user_birth;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

}
