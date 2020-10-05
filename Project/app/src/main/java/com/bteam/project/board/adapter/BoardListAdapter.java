package com.bteam.project.board.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;
import com.bteam.project.board.BoardDetailActivity;
import com.bteam.project.board.model.BoardVO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BoardListAdapter extends RecyclerView.Adapter<BoardListAdapter.NoticeRecyclerViewHolder> {

    private Context context;
    private List<BoardVO> list;

    public BoardListAdapter(Context context, List<BoardVO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoticeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoticeRecyclerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeRecyclerViewHolder holder, int position) {
        holder.setBoardItems( list.get(position) );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoticeRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView writer, title, file;
        CircleImageView image;

        public NoticeRecyclerViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        int board_num = list.get(position).getBoard_num();
                        int category = list.get(position).getBoard_category();
                        Intent intent = new Intent(context, BoardDetailActivity.class);
                        intent.putExtra("board_num", board_num);
                        intent.putExtra("category", category);
                        context.startActivity(intent);
                    }
                }
            });

            writer = itemView.findViewById(R.id.board_writer);
            title = itemView.findViewById(R.id.board_title);
            image = itemView.findViewById(R.id.board_profile);
            file = itemView.findViewById(R.id.board_file);
        }

        void setBoardItems(BoardVO vo) {
            writer.setText(vo.getBoard_nickname() + " - " + vo.getBoard_date());
            title.setText(vo.getBoard_title());
            image.setImageBitmap(vo.getBoard_writer_image());
            file.setText(vo.getBoard_filename());
            if (vo.getBoard_filename() == null) {
                file.setVisibility(View.GONE);
            }
        }
    }
}
