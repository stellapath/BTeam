# BTeam
안드로이드 프로젝트를 올려주세요.

# 테이블 구성
<pre>
<code>
CREATE TABLE bUser (
    user_id       VARCHAR2(15)  PRIMARY KEY NOT NULL,   // 아이디
    user_pw       VARCHAR2(15)  NOT NULL,               // 비밀번호
    user_name     VARCHAR2(15),                         // 이름
    user_nickname VARCHAR2(30),                         // 닉네임
    user_email    VARCHAR2(30),                         // 이메일
    user_phone    VARCHAR2(15),                         // 전화번호
    user_birth    DATE,                                 // 생일
    user_key      VARCHAR2(30)                          // 이메일 인증키 (인증 완료시 OK로 바뀜)
);
</code>
</pre>
