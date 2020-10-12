package com.bteam.project.alarm.dialog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bteam.project.R;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.util.Common;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

/**
 * 구글 맵
 */
public class DestinationPopupActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "DestinationPopupActivit";

    private GoogleMap map;
    private AlarmSharedPreferencesHelper sharPrefHelper;
    private Button button;
    private LatLng placeLatLng;
    private String placeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_popup);

        sharPrefHelper = new AlarmSharedPreferencesHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.destination_map);
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);

        setTitle("목적지 설정");
        initView();

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.destination_autocomplete_begin);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID,
                Place.Field.NAME, Place.Field.ADDRESS));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                placeLatLng = place.getLatLng();
                placeAddress = place.getAddress();
                showSelectedPlace(place.getLatLng(), place.getName(), place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e(TAG, "onError: " + status);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeLatLng != null && placeAddress != null) {
                    sharPrefHelper.setLatitude(placeLatLng.latitude);
                    sharPrefHelper.setLongitude(placeLatLng.longitude);
                    sharPrefHelper.setAddress(placeAddress);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    // 위치 저장 실패!
                }
            }
        });

    }

    private void initView() {
        button = findViewById(R.id.destination_button);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Common.REQUEST_AUTOCOMPLETE_1) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(getSavedLocation()).title("저장된 위치"))
                .showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(getSavedLocation(), 15));
    }

    private void showSelectedPlace(LatLng latLng, String title, String addr) {
        map.clear();
        map.addMarker(new MarkerOptions().position(latLng).title(title).snippet(addr))
                .showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private LatLng getMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location currentLocation = locationManager.getLastKnownLocation(locationProvider);
        if (currentLocation != null) {
            double lng = currentLocation.getLongitude();
            double lat = currentLocation.getLatitude();
            return new LatLng(lat, lng);
        } else {
            return null;
        }
    }

    private LatLng getSavedLocation() {
        if (sharPrefHelper.getLatitude() == 0 || sharPrefHelper.getLongitude() == 0) {
            return getMyLocation();
        } else {
            return new LatLng(sharPrefHelper.getLatitude(), sharPrefHelper.getLongitude());
        }
    }
}