package com.bteam.project.board;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.bteam.project.board.adapter.NoticeRecyclerViewAdapter;
import com.bteam.project.board.model.BoardVO;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeFragment extends Fragment {

    private static final String TAG = "NoticeFragment";

    public static NoticeFragment newInstance() {
        return new NoticeFragment();
    }

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_board_recyclerview, container, false);

        recyclerView = root.findViewById(R.id.board_recyclerView);

        sendBoardListRequest();

        return root;
    }

    // 공지사항 정보를 불러와 BoardVO list를 만듦
    private void sendBoardListRequest() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Common.SERVER_URL + "andBoardList";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                BoardVO[] vo = gson.fromJson(response.trim(), BoardVO[].class);
                List<BoardVO> list = Arrays.asList(vo);
                Log.d(TAG, "listsize : " + list.size());
                onPostExcute(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "서버와의 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("category", "0");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void onPostExcute(List<BoardVO> list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        recyclerView.setAdapter(new NoticeRecyclerViewAdapter(list, getActivity()));
    }
}
