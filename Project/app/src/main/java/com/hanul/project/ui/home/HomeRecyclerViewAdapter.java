package com.hanul.project.ui.home;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanul.project.R;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TextView textView_homeItem1 = itemView.findViewById(R.id.textView_homeItem1);
            TextView textView_homeItem1_2 = itemView.findViewById(R.id.textView_homeItem1_2);
            ImageView imageView_homeItem = itemView.findViewById(R.id.imageView_homeItem1);
        }

    }

}
