package com.bteam.project.board;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.R;
import com.bteam.project.board.adapter.PopularAdapter;
import com.bteam.project.board.adapter.TrafficListAdapter;
import com.bteam.project.board.model.TrafficVO;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.util.Common;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class BoardFragment extends Fragment {

    private static final String TAG = "BoardFragment";

    private RecyclerView popular_recyclerView, traffic_recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_board, container, false);

        initView(root);

        getPopular();
        getTraffic();

        return root;
    }

    private void initView(View root) {
        popular_recyclerView = root.findViewById(R.id.board_popular_recyclerView);
        traffic_recyclerView = root.findViewById(R.id.board_traffic_recyclerView);
    }

    private void getPopular() {
        final String url = Common.SERVER_URL + "andPopular";
        StringRequest request = new StringRequest(Request.Method.POST, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                showPopular(Arrays.asList(gson.fromJson(response.trim(), TrafficVO[].class)));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
            }
        });
        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);
    }

    private void showPopular(List<TrafficVO> list) {
        popular_recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        popular_recyclerView.setAdapter(new PopularAdapter(getActivity(), list));
    }

    private void getTraffic() {
        final String url = Common.SERVER_URL + "andTraffic";
        StringRequest request = new StringRequest(Request.Method.POST, url,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                showTraffic(Arrays.asList(gson.fromJson(response.trim(), TrafficVO[].class)));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
            }
        });
        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);
    }

    private void showTraffic(List<TrafficVO> list) {
        traffic_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        traffic_recyclerView.setAdapter(new TrafficListAdapter(getActivity(), list));
    }
}