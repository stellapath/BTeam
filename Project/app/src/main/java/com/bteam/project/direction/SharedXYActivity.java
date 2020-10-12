package com.bteam.project.direction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bteam.project.R;

public class SharedXYActivity extends AppCompatActivity {

    private TextView textView1;
    private EditText editText;
    private Button setBtn, getBtn;
    private SharedPreferences sf;
    //private LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    //private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_direction);

        editText = findViewById(R.id.dire_edit);
        textView1 = findViewById(R.id.dire_text2);
        setBtn = findViewById(R.id.dire_setShared);
        getBtn = findViewById(R.id.dire_getShared);



        sf = getSharedPreferences("xyLocation", MODE_PRIVATE);

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sf.edit();
                editor.putString("xy",editText.getText().toString());
                editor.commit();

                // location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

               GpsXY gpsxy = new GpsXY(SharedXYActivity.this);
               double latitude = gpsxy.getLatitude();       // 위도
               double longitude = gpsxy.getLongitude();     // 경도








            }
        });
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = sf.getString("xy","");
                textView1.setText(inputText);

            }
        });


    }
}