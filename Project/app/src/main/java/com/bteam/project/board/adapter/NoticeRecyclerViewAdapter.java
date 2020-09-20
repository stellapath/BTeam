package com.bteam.project.board.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;
import com.bteam.project.board.model.BoardVO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.NoticeRecyclerViewHolder> {

    List<BoardVO> list;

    public NoticeRecyclerViewAdapter(List<BoardVO> list) {
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

        public NoticeRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            writer = itemView.findViewById(R.id.board_writer);
            title = itemView.findViewById(R.id.board_title);
            image = itemView.findViewById(R.id.board_writer_image);
            file = itemView.findViewById(R.id.board_file);
        }

        void setBoardItems(BoardVO vo) {
            writer.setText(vo.getBoard_nickname() + " - " + vo.getBoard_date());
            title.setText(vo.getBoard_title());
            image.setImageBitmap(vo.getBoard_writer_image());
            file.setText(vo.getBoard_filename());
        }
    }

}
