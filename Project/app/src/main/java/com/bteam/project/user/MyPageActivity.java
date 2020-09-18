package com.bteam.project.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bteam.project.R;

public class MyPageActivity extends AppCompatActivity {

    Button noticeBtn, myWritten, battery, led;
    ImageView myImg;
    TextView myNick, myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        noticeBtn = findViewById(R.id.noticeBtn);
        myWritten = findViewById(R.id.myWritten);
        battery = findViewById(R.id.battery);
        led = findViewById(R.id.led);
        myImg = findViewById(R.id.myImg);
        myNick = findViewById(R.id.myNick);
        myId = findViewById(R.id.myId);

        // 로그인시 프로필,닉네임,아이디 받아와서 다시 쓰기
//        myImg.setimage();
//        myNick.setText(.getInfo_nickname());
//        myId.setText(.getInfo_email());




        // 공지사항 클릭 메소드
        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 공지사항 클릭시 이동
                // Intent intent = new Intent(get(), .class);

            }
        });

        // 내가 쓴글 검색해서 보여주는 페이지 이동
        myWritten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 배터리 관리 페이지
        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 개인정보 수정
        led.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}