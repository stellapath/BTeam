package com.hanul.project.ui.home.model;

public class Trip {

    private int tripImage;
    private String tripTitle, trip;

    public Trip(int tripImage, String tripTitle, String trip) {
        this.tripImage = tripImage;
        this.tripTitle = tripTitle;
        this.trip = trip;
    }

    public int getTripImage() {
        return tripImage;
    }

    public void setTripImage(int tripImage) {
        this.tripImage = tripImage;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

}
