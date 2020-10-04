package com.project.bteam.board.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.bteam.R;
import com.project.bteam.board.BoardListActivity;
import com.project.bteam.board.model.CategoryVO;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<CategoryVO> list;

    public CategoryAdapter(Context context, List<CategoryVO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryVO vo = list.get(position);
        holder.title.setText(vo.getTitle());
        Glide.with(context).load(vo.getImageURL()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, BoardListActivity.class);
                        String category = list.get(position).getCategory();
                        intent.putExtra("category", category);
                        context.startActivity(intent);
                    }
                }
            });

            title = itemView.findViewById(R.id.category_title);
            image = itemView.findViewById(R.id.category_image);
        }
    }
}
