# BTeam

# 테이블 구성
<pre>
<code>
CREATE TABLE bUser (
    user_id       VARCHAR2(15)  PRIMARY KEY NOT NULL,
    user_pw       VARCHAR2(15)  NOT NULL,
    user_name     VARCHAR2(15),
    user_nickname VARCHAR2(30),
    user_email    VARCHAR2(30),
    user_phone    VARCHAR2(15),
    user_birth    DATE,
    user_key      VARCHAR2(30)
);
</code>
</pre>
