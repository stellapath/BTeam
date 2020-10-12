package com.bteam.project.user;

import android.os.Bundle;
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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.R;
import com.bteam.project.board.adapter.TrafficListAdapter;
import com.bteam.project.board.model.TrafficVO;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.util.Common;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPostFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_post, container, false);

        recyclerView = root.findViewById(R.id.myPost_recyclerView);

        getMyPost();

        return root;
    }

    private void getMyPost() {
        String url = Common.SERVER_URL + "andMyPost";
        StringRequest request = new StringRequest(Request.Method.POST, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                onPostExecute(Arrays.asList(gson.fromJson(response.trim(), TrafficVO[].class)));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "내가 쓴 글 목록을 불러오는 데 실패했습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", Common.login_info.getUser_email());
                return map;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void onPostExecute(List<TrafficVO> list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new TrafficListAdapter(getActivity(), list));
    }
}
