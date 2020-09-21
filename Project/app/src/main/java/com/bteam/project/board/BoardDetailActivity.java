package com.bteam.project.board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bteam.project.Common;
import com.bteam.project.R;
import com.bteam.project.board.model.BoardVO;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BoardDetailActivity extends AppCompatActivity {

    TextView title, writer, date, content, filename, filesize;
    ImageButton close;
    CircleImageView profile;
    LinearLayout attachment;

    private int board_num, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);

        initView();

        // 클릭한 게시글의 번호를 넘겨 받음
        if (getIntent() != null) {
            Intent intent = getIntent();
            board_num = intent.getIntExtra("board_num", 0 );
            category = intent.getIntExtra("category", 0);
        }

        sendBoardDetailRequest(board_num, category);

    }

    private void initView() {
        title = findViewById(R.id.board_detail_title);
        writer = findViewById(R.id.board_detail_writer);
        date = findViewById(R.id.board_detail_date);
        content = findViewById(R.id.board_detail_content);
        filename = findViewById(R.id.board_detail_file_name);
        filesize = findViewById(R.id.board_detail_file_size);
        close = findViewById(R.id.board_detail_close);
        profile = findViewById(R.id.board_detail_profile);
        attachment = findViewById(R.id.board_detail_attachment);
    }

    private void sendBoardDetailRequest(final int board_num, final int category) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Common.SERVER_URL + "andBoardView";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                BoardVO vo = gson.fromJson(response.trim(), BoardVO.class);
                onPostExcute(vo);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BoardDetailActivity.this, "서버와의 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("board_num", board_num + "");
                params.put("category", category + "");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void onPostExcute(BoardVO vo) {
        title.setText( vo.getBoard_title() );
        writer.setText( vo.getBoard_nickname() );
        date.setText( vo.getBoard_date() );
        content.setText( vo.getBoard_content() );
        filename.setText( vo.getBoard_filename() );
        // filesize.settext
        profile.setImageBitmap( vo.getBoard_writer_image() );
        if (vo.getBoard_filename() == null) {
            attachment.setVisibility(View.GONE);
        }
    }
}