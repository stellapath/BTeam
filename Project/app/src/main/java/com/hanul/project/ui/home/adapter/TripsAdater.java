package com.hanul.project.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanul.project.R;
import com.hanul.project.ui.home.model.Ads;
import com.hanul.project.ui.home.model.Item;
import com.hanul.project.ui.home.model.News;
import com.hanul.project.ui.home.model.Trip;

import java.util.List;

public class TripsAdater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> items;

    public TripsAdater(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 0=Trip, 1=Ads, 2=News

        if (viewType == 0) {
            return new TripViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_container_trip,
                            parent,
                            false
                    )
            );
        } else if (viewType == 1) {
            return new AdsViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_container_ads,
                            parent,
                            false
                    )
            );
        } else {
            return new NewsViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_container_news,
                            parent,
                            false
                    )
            );
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            Trip trip = (Trip) items.get(position).getObject();
            ((TripViewHolder) holder).setTripData(trip);
        } else if (getItemViewType(position) == 1) {
            Ads ads = (Ads) items.get(position).getObject();
            ((AdsViewHolder) holder).setAdsData(ads);
        } else {
            News news = (News) items.get(position).getObject();
            ((NewsViewHolder) holder).setNewsdata(news);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageTrip;
        private TextView textTripTitle, textTrip;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTrip = itemView.findViewById(R.id.imageTrip);
            textTripTitle = itemView.findViewById(R.id.textTripTitle);
            textTrip = itemView.findViewById(R.id.textTrip);
        }

        void setTripData(Trip trip) {
            imageTrip.setImageResource(trip.getTripImage());
            textTripTitle.setText(trip.getTripTitle());
            textTrip.setText(trip.getTrip());
        }
    }

    static class AdsViewHolder extends RecyclerView.ViewHolder {

        private TextView textAdsTitle, textAds;

        AdsViewHolder(@NonNull View itemView) {
            super(itemView);
            textAdsTitle = itemView.findViewById(R.id.textAdsTitle);
            textAds = itemView.findViewById(R.id.textAds);
        }

        void setAdsData(Ads ads) {
            textAdsTitle.setText(ads.getAdsTitle());
            textAds.setText(ads.getAds());
        }
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        private TextView textNewsTitle, textNews;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            textNewsTitle = itemView.findViewById(R.id.textNewsTitle);
            textNews = itemView.findViewById(R.id.textNews);
        }

        void setNewsdata(News news) {
            textNewsTitle.setText(news.getNewsTitle());
            textNews.setText(news.getNews());
        }
    }

}
