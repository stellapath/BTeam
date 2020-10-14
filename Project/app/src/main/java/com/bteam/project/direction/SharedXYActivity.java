package com.bteam.project.direction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bteam.project.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SharedXYActivity extends AppCompatActivity {

    private TextView textView, textView2;
    private EditText editText;
    private Button setBtn, getBtn;
    private SharedPreferences sf;
    //private LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    //private Location location;
    private static final int REQUEST_ENABLE_BT = 3;
    public BluetoothAdapter mBluetoothAdapter = null;
    Set<BluetoothDevice> bluetooth_device;
    int mPairedDeviceCount;
    BluetoothDevice mRemoteDevice;
    BluetoothSocket mSocket;
    InputStream mInputStream;
    OutputStream mOutputStream;
    Thread mWorkerThread;
    int readBufferPositon;      //버퍼 내 수신 문자 저장 위치
    byte[] readBuffer;      //수신 버퍼
    byte mDelimiter = 10;
    String myName = "hanul";

    IntentFilter stateFilter;
    // 연결되었는가
    int isConnect = 0;
    int isContinue = 0;
    int connCount = 0;
    // 일정시간마다 실행
    CountDownTimer CDT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_x_y);

        editText = findViewById(R.id.dire_edit);
        textView = findViewById(R.id.dire_text2);
        setBtn = findViewById(R.id.dire_setShared);
        getBtn = findViewById(R.id.dire_getShared);


        bluetooth_device = new HashSet<>();

        // 리시버 등록
        regiserRec();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();   //블루투스 adapter 획득
        boolean isStart = mBluetoothAdapter.startDiscovery(); //블루투스 기기 검색 시작
        Log.d("main", "onCreate: isStart " + isStart);



//        // 데이터 송신하기
//        setBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendData(editText.getText().toString());
//
//            }
//        });
    }

//    // 데이터 송신
//    public void sendData(String sendText){
//        sendText += "\n";
//        try{
//            mOutputStream.write(sendText.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void regiserRec(){
        stateFilter = new IntentFilter();
        stateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); //BluetoothAdapter.ACTION_STATE_CHANGED : 블루투스 상태변화 액션
        stateFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        stateFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED); //연결 확인
        stateFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED); //연결 끊김 확인
        stateFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        stateFilter.addAction(BluetoothDevice.ACTION_FOUND);    //기기 검색됨
        stateFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);   //기기 검색 시작
        stateFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);  //기기 검색 종료
        stateFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(mBluetoothStateReceiver, stateFilter);
    }

    BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();   //입력된 action
            final BluetoothDevice device =  intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //입력된 action에 따라서 함수를 처리한다
            if(action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {   //블루투스 기기 끊어짐
//                    Log.d("Bluetooth", "ACTION_ACL_DISCONNECTED");
                textView.append("받은 액션 : " + "연결끊어짐" + "\n");
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        //regiserRec();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 리시버 해제
        //unregisterReceiver(mBluetoothStateReceiver);
    }

}