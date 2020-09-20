package com.bteam.project.board.model;

import android.graphics.Bitmap;

public class BoardVO {

    private String board_email, board_nickname, board_title, board_content, board_filename, board_filepath;
    private String board_date;
    private int board_num, board_category, board_like, board_readcnt, board_no;
    private Bitmap board_writer_image;

    public Bitmap getBoard_writer_image() {
        return board_writer_image;
    }

    public void setBoard_writer_image(Bitmap board_writer_image) {
        this.board_writer_image = board_writer_image;
    }

    public String getBoard_filename() {
        return board_filename;
    }

    public void setBoard_filename(String board_filename) {
        this.board_filename = board_filename;
    }

    public String getBoard_filepath() {
        return board_filepath;
    }

    public void setBoard_filepath(String board_filepath) {
        this.board_filepath = board_filepath;
    }

    public int getBoard_no() {
        return board_no;
    }

    public void setBoard_no(int board_no) {
        this.board_no = board_no;
    }

    public int getBoard_readcnt() {
        return board_readcnt;
    }

    public void setBoard_readcnt(int board_readcnt) {
        this.board_readcnt = board_readcnt;
    }

    public int getBoard_num() {
        return board_num;
    }

    public void setBoard_num(int board_num) {
        this.board_num = board_num;
    }

    public int getBoard_category() {
        return board_category;
    }

    public void setBoard_category(int board_category) {
        this.board_category = board_category;
    }

    public int getBoard_like() {
        return board_like;
    }

    public void setBoard_like(int board_like) {
        this.board_like = board_like;
    }

    public String getBoard_email() {
        return board_email;
    }

    public void setBoard_email(String board_email) {
        this.board_email = board_email;
    }

    public String getBoard_nickname() {
        return board_nickname;
    }

    public void setBoard_nickname(String board_nickname) {
        this.board_nickname = board_nickname;
    }

    public String getBoard_date() {
        return board_date;
    }

    public void setBoard_date(String board_date) {
        this.board_date = board_date;
    }

    public String getBoard_title() {
        return board_title;
    }

    public void setBoard_title(String board_title) {
        this.board_title = board_title;
    }

    public String getBoard_content() {
        return board_content;
    }

    public void setBoard_content(String board_content) {
        this.board_content = board_content;
    }

}
