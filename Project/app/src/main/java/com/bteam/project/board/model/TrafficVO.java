package com.bteam.project.board.model;

import java.io.Serializable;

public class TrafficVO implements Serializable {

	private String tra_num, tra_user_email, tra_username, tra_user_image,
			tra_content, tra_content_image, tra_time;

	public String getTra_num() {
		return tra_num;
	}

	public void setTra_num(String tra_num) {
		this.tra_num = tra_num;
	}

	public String getTra_user_email() {
		return tra_user_email;
	}

	public void setTra_user_email(String tra_user_email) {
		this.tra_user_email = tra_user_email;
	}

	public String getTra_username() {
		return tra_username;
	}

	public void setTra_username(String tra_username) {
		this.tra_username = tra_username;
	}

	public String getTra_user_image() {
		return tra_user_image;
	}

	public void setTra_user_image(String tra_user_image) {
		this.tra_user_image = tra_user_image;
	}

	public String getTra_content() {
		return tra_content;
	}

	public void setTra_content(String tra_content) {
		this.tra_content = tra_content;
	}

	public String getTra_content_image() {
		return tra_content_image;
	}

	public void setTra_content_image(String tra_content_image) {
		this.tra_content_image = tra_content_image;
	}

	public String getTra_time() {
		return tra_time;
	}

	public void setTra_time(String tra_time) {
		this.tra_time = tra_time;
	}
}
