package com.bteam.project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bteam.project.user.LoginActivity;
import com.bteam.project.user.MyPageActivity;
import com.bteam.project.util.Common;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

/* 메인 액티비티 */
public class MainActivity extends AppCompatActivity {

    private final int LOGIN_REQUEST_CODE = 100;
    private final int MYPAGE_REQUEST_CODE = 103;
    private AppBarConfiguration mAppBarConfiguration;

    private DrawerLayout drawer;
    private CircleImageView drawer_image;
    private TextView drawer_nickname, drawer_id;
    private MenuItem login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationSetting();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 로그인 처리
        if (requestCode == LOGIN_REQUEST_CODE) {
            // 로그인이 완료 되었을 때
            if (resultCode == RESULT_OK) {
                // 프로필 이미지 및 닉네임 변경 처리
                String user_nickname = Common.login_info.getUser_nickname();
                String user_email = Common.login_info.getUser_email();
                drawer_nickname.setText(user_nickname);
                drawer_id.setText(user_email);
                if (TextUtils.isEmpty(Common.login_info.getUser_imagepath())) {
                    drawer_image.setImageResource(R.drawable.ic_user);
                } else {
                    Glide.with(this)
                            .load(Common.SERVER_URL + Common.login_info.getUser_imagepath())
                            .into(drawer_image);
                }
                // 로그인 텍스트 -> 로그아웃
                login.setTitle("로그아웃");
            }
        }

        if (requestCode == MYPAGE_REQUEST_CODE) {
            Glide.with(this)
                    .load(Common.SERVER_URL + Common.login_info.getUser_imagepath())
                    .into(drawer_image);
        }
    }

    // 네비게이션 설정
    private void navigationSetting() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 하단 네비게이션 설정
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_alarm, R.id.navigation_direction, R.id.navigation_board
        ).build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // 사이드 네비게이션 (네비게이션 드로어) 설정
        drawer = findViewById(R.id.drawer_layout);

        /*
        NavigationView navigationView = findViewById(R.id.side_nav_view);
        mAppBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(drawerLayout)
                        .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        */

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar,
                        R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // 사이드 네비게이션 클릭 이벤트
        NavigationView navigationView = findViewById(R.id.side_nav_view);
        View headView = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        drawer_image = headView.findViewById(R.id.drawer_image);
        drawer_nickname = headView.findViewById(R.id.drawer_nickname);
        drawer_id = headView.findViewById(R.id.drawer_id);
        login = menu.findItem(R.id.nav_login);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    // 내가 올린 게시글
                    case R.id.nav_login :
                        if (Common.login_info == null) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent, LOGIN_REQUEST_CODE);
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            Common.login_info = null;
                            startActivity(getIntent());
                            finish();
                        }
                        break;

                    case R.id.nav_info :
                        if (Common.login_info == null) {
                            Toast.makeText(MainActivity.this,
                                    "이 메뉴는 로그인한 사용자만 이용할 수 있습니다.",
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        } else {
                            Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                            startActivity(intent);
                            break;
                        }

                    case R.id.nav_post :
                        if (Common.login_info == null) {
                            Toast.makeText(MainActivity.this,
                                    "이 메뉴는 로그인한 사용자만 이용할 수 있습니다.",
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        } else {
                            Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                            intent.putExtra("myPost", true);
                            startActivity(intent);
                        }
                        break;

                    case R.id.nav_home :
                        if (drawer.isDrawerOpen(GravityCompat.START))
                            drawer.closeDrawer(GravityCompat.START);
                        navController.navigate(R.id.navigation_home);
                        break;

                    case R.id.nav_alarm :
                        if (drawer.isDrawerOpen(GravityCompat.START))
                            drawer.closeDrawer(GravityCompat.START);
                        navController.navigate(R.id.navigation_alarm);
                        break;

                    case R.id.nav_board :
                        if (drawer.isDrawerOpen(GravityCompat.START))
                            drawer.closeDrawer(GravityCompat.START);
                        navController.navigate(R.id.navigation_board);
                        break;

                    case R.id.nav_iot :
                        if (drawer.isDrawerOpen(GravityCompat.START))
                            drawer.closeDrawer(GravityCompat.START);
                        navController.navigate(R.id.navigation_direction);
                        break;
                }
                return true;
            }
        });

        // 사이드 네비게이션 헤더 클릭 시 이벤트
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.login_info == null) {
                    // 로그인 상태가 아니면 로그인 화면으로 넘기기
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_REQUEST_CODE);
                } else {
                    // 로그인 상태이면 마이페이지로 넘기기
                    Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                    startActivityForResult(intent, MYPAGE_REQUEST_CODE);
                }
            }
        });


    }

    // 액션바에 옵션메뉴 추가하기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // 옵션메뉴 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings :
                Toast.makeText(this, "Settings 클릭됨", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // 뒤로가기 버튼이 눌렸을 때 앱 종료가 아닌 드로어가 닫히도록 설정
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}