# 메인화면
<img src="https://raw.githubusercontent.com/stellapath/BTeam/master/Project/%EC%B1%85%EC%9E%90/03.%20%EA%B5%AC%ED%98%84/4.1.%20%EB%A9%94%EC%9D%B8%ED%99%94%EB%A9%B4/01.jpg" width="200" /> <img src="https://raw.githubusercontent.com/stellapath/BTeam/master/Project/%EC%B1%85%EC%9E%90/03.%20%EA%B5%AC%ED%98%84/4.1.%20%EB%A9%94%EC%9D%B8%ED%99%94%EB%A9%B4/02.jpg" width="200" /> <img src="https://raw.githubusercontent.com/stellapath/BTeam/master/Project/%EC%B1%85%EC%9E%90/03.%20%EA%B5%AC%ED%98%84/4.1.%20%EB%A9%94%EC%9D%B8%ED%99%94%EB%A9%B4/03.jpg" width="200" />

# 실시간 교통 정보 공유 게시판 테이블
<pre>
<code>
create table traffic (
    tra_num             number constraint tra_num_pk primary key,
    tra_user_email      varchar2(30),
    tra_username        varchar2(30),
    tra_user_image      varchar2(300),
    tra_content         varchar2(1024),
    tra_content_image   varchar2(300),
    constraint tra_email_fk
    foreign key (tra_user_email)
    references bUser(user_email)
    on delete cascade
);

create sequence seq_traffic start with 1;

create or replace trigger trg_traffic
before insert on traffic for each row
begin
select seq_traffic.nextval into :new.tra_num from dual;
end;
/
</code>
</pre>

# 좋아요 테이블
<pre>
<code>
create table traffic_like (
    like_id number primary key,
    like_email varchar2(30) not null,
    like_board number not null,
    constraint like_email_fk foreign key (like_email)
    references bUser(user_email) on delete cascade,
    constraint like_board_fk foreign key (like_board)
    references traffic(tra_num) on delete cascade
);

create sequence seq_traffic_like start with 1;

create or replace trigger trg_traffic_like
before insert on traffic_like for each row
begin
select seq_traffic_like.nextval into :new.like_id from dual;
end;
/
</code>
</pre>

# 댓글 테이블
<pre>
<code>
create table traffic_comment (
    comment_id number primary key,
    comment_board number not null,
    comment_email varchar2(30) not null,
    comment_image varchar2(30),
    comment_content varchar2(30),
    comment_date varchar2(20) default to_char(sysdate, 'yyyy/mm/dd hh:mi:ss'),
    constraint comment_board_fk foreign key (comment_board)
    references traffic(tra_num) on delete cascade,
    constraint comment_email_fk foreign key (comment_email)
    references buser(user_email) on delete cascade
);

create sequence seq_traffic_comment start with 1;

create or replace trigger trg_traffic_comment
before insert on traffic_comment for each row
begin
select seq_traffic_comment.nextval into :new.comment_id from dual;
end;
/
</code>
</pre>