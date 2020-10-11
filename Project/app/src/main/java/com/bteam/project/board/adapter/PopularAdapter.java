package com.bteam.project.board.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;
import com.bteam.project.board.TrafficDetailActivity;
import com.bteam.project.board.model.TrafficVO;
import com.bteam.project.util.Common;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {

    private Context context;
    private List<TrafficVO> list;

    public PopularAdapter(Context context, List<TrafficVO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        holder.setPopular(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class PopularViewHolder extends RecyclerView.ViewHolder {

        TextView content, writer;
        ImageView image;
        CircleImageView userImage;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, TrafficDetailActivity.class);
                        intent.putExtra("num", list.get(position).getTra_num());
                        context.startActivity(intent);
                    }
                }
            });

            content = itemView.findViewById(R.id.popular_content);
            writer = itemView.findViewById(R.id.popular_writer);
            image = itemView.findViewById(R.id.popular_image);
            userImage = itemView.findViewById(R.id.popular_user_image);
        }

        public void setPopular(TrafficVO vo) {
            content.setText(vo.getTra_content());
            writer.setText(vo.getTra_username() + " - " + vo.getTra_time());
            if (!TextUtils.isEmpty(vo.getTra_content_image()))
                Glide.with(context).load(Common.SERVER_URL + vo.getTra_content_image()).into(image);
            if (!TextUtils.isEmpty(vo.getTra_user_image()))
                Glide.with(content).load(Common.SERVER_URL + vo.getTra_user_image()).into(userImage);
        }
    }
}
