package com.bteam.project.board;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bteam.project.R;
import com.bteam.project.board.model.TrafficVO;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.util.Common;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class TrafficDetailActivity extends AppCompatActivity {

    private static final String TAG = "TrafficDetailActivity";
    private final int MODIFY_REQUEST_CODE = 303;
    private String num;

    private ImageButton close;
    private TextView writer, date, content, like_count;
    private ImageView image;
    private Button solve, like;

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

        solve.setVisibility(View.GONE);

        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TrafficDetailActivity.this);
                builder.setTitle("해결");
                builder.setMessage("이 교통상황이 해결되었습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        solve();
                    }
                });
                builder.setNegativeButton("아니오", null);
                builder.show();
            }
        });

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
                                builder.setNegativeButton("아니오", null);
                                builder.show();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.login_info == null) {
                    showAlert("로그인 필요", "로그인이 필요한 기능입니다.");
                    return;
                }
                setLike();
            }
        });
    }

    private void initView() {
        close = findViewById(R.id.traffic_detail_close);
        writer = findViewById(R.id.traffic_detail_writer);
        date = findViewById(R.id.traffic_detail_date);
        content = findViewById(R.id.traffic_detail_content);
        image = findViewById(R.id.traffic_detail_image);
        solve = findViewById(R.id.traffic_detail_solve);
        like = findViewById(R.id.traffic_detail_like);
        like_count = findViewById(R.id.traffic_detail_like_count);
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

                if (Common.login_info != null) {
                    if (Common.login_info.getUser_email().equals(vo.getTra_user_email())
                            && vo.getTra_solve().equals("0")) {
                        solve.setVisibility(View.VISIBLE);
                    }
                }

                getLikeCount();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
                AlertDialog.Builder builder = new AlertDialog.Builder(TrafficDetailActivity.this);
                builder.setTitle("불러오기 실패");
                builder.setMessage("게시판을 불러오는 데 실패했습니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
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

    private void solve() {
        final String url = Common.SERVER_URL + "andTrafficSolve?num=" + vo.getTra_num();
        StringRequest request = new StringRequest(Request.Method.GET, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    showAlert("해결 완료", "해결되었습니다.");
                    solve.setVisibility(View.GONE);
                } else {
                    showAlert("해결 실패", "알 수 없는 오류가 발생했습니다.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TrafficDetailActivity.this,
                        "서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void getLikeCount() {
        final String url = Common.SERVER_URL + "andTrafficLikeSu?num=" + vo.getTra_num();
        StringRequest request = new StringRequest(Request.Method.GET, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                like_count.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                like_count.setText("ERROR");
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void setLike() {
        final String url = Common.SERVER_URL + "andTrafficLike";
        StringRequest request = new StringRequest(Request.Method.POST, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")) {
                    getLikeCount();
                } else {
                    Toast.makeText(TrafficDetailActivity.this,
                            "좋아요 처리에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TrafficDetailActivity.this,
                        "서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", Common.login_info.getUser_email());
                map.put("num", vo.getTra_num());
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void showAlert(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("확인", null);
        builder.show();
    }
}