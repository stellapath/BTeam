package com.bteam.project.board.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;
import com.bteam.project.board.model.TrafficVO;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrafficListAdapter extends RecyclerView.Adapter<TrafficListAdapter.TrafficListViewHolder> {

    private Context context;
    private List<TrafficVO> list;

    public TrafficListAdapter(Context context, List<TrafficVO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TrafficListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrafficListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrafficListViewHolder holder, int position) {
        holder.setTraffic(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class TrafficListViewHolder extends RecyclerView.ViewHolder {

        TextView writer, title;
        CircleImageView profile;
        ImageView image;

        public TrafficListViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                    }
                }
            });

            writer = itemView.findViewById(R.id.traffic_writer);
            title = itemView.findViewById(R.id.traffic_title);
            profile = itemView.findViewById(R.id.traffic_profile);
            image = itemView.findViewById(R.id.traffic_image);
        }

        public void setTraffic(TrafficVO vo) {
            writer.setText(vo.getTra_username() + " - " + vo.getTra_time());
            title.setText(vo.getTra_content());
            if (!TextUtils.isEmpty(vo.getTra_user_image()))
                Glide.with(context).load(vo.getTra_user_image()).into(profile);
            if (!TextUtils.isEmpty(vo.getTra_content_image()))
                Glide.with(context).load(vo.getTra_content_image()).into(image);
        }
    }
}
