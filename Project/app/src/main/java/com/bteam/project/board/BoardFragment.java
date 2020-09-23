package com.bteam.project.board;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bteam.project.Common;
import com.bteam.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class BoardFragment extends Fragment {

    private static final String TAG = "BoardFragment";

    private Fragment noticeFragment;
    private int page = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_board, container, false);

        // Fragment
        noticeFragment = new NoticeFragment();

        // TabLayout
        TabLayout tabs = root.findViewById(R.id.board_tabs);
        tabs.addTab(tabs.newTab().setText("공지사항"));

        // 처음 화면 설정
        changeFragment(noticeFragment);

        // Tab 선택 리스너
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                page = position;
                if (position == 0) changeFragment(noticeFragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // fab 버튼 클릭시 게시글 작성 화면
        FloatingActionButton fab = root.findViewById(R.id.board_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.login_info == null) {
                    Toast.makeText(getActivity(), "게시글을 작성하려면 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (page == 0 && !Common.login_info.getUser_email().equals("admin")) {
                    Toast.makeText(getActivity(), "공지사항은 관리자만 작성할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), BoardInsertActivity.class);
                intent.putExtra("category", page);
                startActivityForResult(intent, Common.REQUEST_BOARD_INSERT);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 글 작성 완료 시
        if (requestCode == Common.REQUEST_BOARD_INSERT && resultCode == Activity.RESULT_OK) {
            // TODO 글 목록 새로고침
        }
    }

    // 프래그먼트 전환
    private void changeFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.board_frameLayout, fragment).commit();
    }
}