package com.bteam.project.board;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.R;
import com.bteam.project.board.model.TrafficVO;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.util.Common;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class TrafficDetailActivity extends AppCompatActivity {

    private static final String TAG = "TrafficDetailActivity";
    private final int MODIFY_REQUEST_CODE = 303;
    private String num;

    private ImageButton close;
    private TextView writer, date, content;
    private ImageView image;

    private TrafficVO vo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_detail);

        if (getIntent() != null) {
            num = getIntent().getStringExtra("num");
        }

        initView();
        getTrafficDetail();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(TrafficDetailActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.traffic_detail_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.traffic_detail_modify :
                                // 수정화면
                                if (Common.login_info == null) {
                                    Toast.makeText(TrafficDetailActivity.this,
                                            "게시글을 수정하시려면 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                if (!Common.login_info.getUser_email().equals(vo.getTra_user_email())) {
                                    Toast.makeText(TrafficDetailActivity.this,
                                            "작성자만 수정이 가능합니다.", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                Intent intent = new Intent(TrafficDetailActivity.this,
                                        TrafficModifyActivity.class);
                                intent.putExtra("vo", vo);
                                startActivityForResult(intent, MODIFY_REQUEST_CODE);
                                break;

                            case R.id.traffic_detail_delete :
                                // 삭제처리
                                if (Common.login_info == null) {
                                    Toast.makeText(TrafficDetailActivity.this,
                                            "게시글을 삭제하시려면 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                if (!Common.login_info.getUser_email().equals(vo.getTra_user_email())) {
                                    Toast.makeText(TrafficDetailActivity.this,
                                            "작성자만 삭제가 가능합니다.", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(TrafficDetailActivity.this);
                                builder.setTitle("게시글 삭제");
                                builder.setMessage("게시글을 삭제하시겠습니까?");
                                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteTraffic();
                                    }
                                });
                                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    private void initView() {
        close = findViewById(R.id.traffic_detail_close);
        writer = findViewById(R.id.traffic_detail_writer);
        date = findViewById(R.id.traffic_detail_date);
        content = findViewById(R.id.traffic_detail_content);
        image = findViewById(R.id.traffic_detail_image);
    }

    private void getTrafficDetail() {
        final String url = Common.SERVER_URL + "andTrafficView?num= " + num;
        StringRequest request = new StringRequest(Request.Method.GET, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                vo = gson.fromJson(response.trim(), TrafficVO.class);
                showTrafficDetail(vo);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void showTrafficDetail(TrafficVO vo) {
        writer.setText(vo.getTra_username());
        date.setText(vo.getTra_time());
        content.setText(vo.getTra_content());
        if (!TextUtils.isEmpty(vo.getTra_content_image()))
            Glide.with(this).load(Common.SERVER_URL + vo.getTra_content_image())
                    .into(image);
    }

    private void deleteTraffic() {
        final String url = Common.SERVER_URL + "andTrafficDelete?num=" + vo.getTra_num();
        StringRequest request = new StringRequest(Request.Method.GET, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
                Toast.makeText(TrafficDetailActivity.this,
                        "게시글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}