package com.bteam.project.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;
import com.bteam.project.home.adapter.DongAdapter;
import com.bteam.project.home.model.City;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 날씨 지역 설정 화면
 */
public class WeatherSettingFragment extends Fragment {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private EditText dong;
    private Button dongSearch;
    private RecyclerView recyclerView;
    private DongAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weather_setting, container, false);

        preferences = getActivity().getSharedPreferences("Weather", Context.MODE_PRIVATE);
        editor = preferences.edit();

        dong = root.findViewById(R.id.weather_dong);
        dongSearch = root.findViewById(R.id.weather_dongSearch);
        recyclerView = root.findViewById(R.id.dong_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dongSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 검색한 동이 들어오는 문자열
                String searchData = dong.getText().toString();

                if (searchData.length() == 0) {
                    Toast.makeText(getActivity(), "검색할 읍/면/동을 입력하세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (searchData.length() == 1) {
                    Toast.makeText(getActivity(), "두 글자 이상 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // asset에 있는 json 파일 읽기
                AssetManager assetManager = getResources().getAssets();
                StringBuffer json = new StringBuffer();
                try {
                    InputStream is = assetManager.open("code.json");
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        json.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 읽어들인 문자열 타입의 json을 City 타입으로 변환
                Gson gson = new Gson();
                City[] cities = gson.fromJson(json.toString(), City[].class);

                // 동 검색 결과를 ArrayList로 반환
                ArrayList<City> searchResult = new ArrayList<>();
                for (int i = 0; i < cities.length; i++) {
                    if (cities[i].getDong().contains(searchData)) {
                        searchResult.add(cities[i]);
                    }
                }

                adapter = new DongAdapter(searchResult);
                recyclerView.setAdapter(adapter);

                // 지역 설정 버튼을 눌렀을 때
                adapter.setOnItemClickListener(new DongAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String code = adapter.get(position).getCode();
                        editor.putString("zone", code).apply();
                        Toast.makeText(getActivity(), "지역이 변경되었습니다.",
                                Toast.LENGTH_SHORT).show();
                        // 키보드 내리기
                        InputMethodManager imm = (InputMethodManager) getActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        // 화면 refresh
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                });
            }
        });

        return root;
    }

}
