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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bteam.project.R;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.util.Common;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.Arrays;

/**
 * 구글 맵
 */
public class DestinationPopupActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "DestinationPopupActivit";

    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap map;
    private AlarmSharedPreferencesHelper sharPrefHelper;
    private Button button;
    private LatLng placeLatLng;
    private Location myLocation;
    private String placeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_popup);

        sharPrefHelper = new AlarmSharedPreferencesHelper(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.destination_map);
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);

        setTitle("목적지 설정");
        initView();

        if (ActivityCompat.checkSelfPermission(DestinationPopupActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DestinationPopupActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
            ActivityCompat.requestPermissions(DestinationPopupActivity.this, permissions, 2020);
            finish();
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(DestinationPopupActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        myLocation = location;
                        Log.i(TAG, "onSuccess: " + location);
                    }
                });

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
                if (placeLatLng == null) {
                    Toast.makeText(DestinationPopupActivity.this,
                            "지역이 선택 또는 변경되지 않았습니다.",
                            Toast.LENGTH_SHORT).show();
                } else if (myLocation != null) {
                    Location target = new Location("");
                    target.setLatitude(placeLatLng.latitude);
                    target.setLongitude(placeLatLng.longitude);
                    float distance = myLocation.distanceTo(target);
                    sharPrefHelper.setLatitude(placeLatLng.latitude);
                    sharPrefHelper.setLongitude(placeLatLng.longitude);
                    sharPrefHelper.setDistance(distance);
                    Toast.makeText(DestinationPopupActivity.this,
                            "목적지가 설정되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                    Log.i(TAG, "distance: " + distance);
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
        if (sharPrefHelper.getLatitude() == 0 || sharPrefHelper.getLongitude() == 0) {
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title("내 위치")
                    .snippet(myLocation.getProvider())).showInfoWindow();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } else {
            LatLng latLng = new LatLng(sharPrefHelper.getLatitude(), sharPrefHelper.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title("저장된 위치")).showInfoWindow();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    private void showSelectedPlace(LatLng latLng, String title, String addr) {
        map.clear();
        map.addMarker(new MarkerOptions().position(latLng).title(title).snippet(addr))
                .showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private void getMyLocation() {

    }
}