package com.bteam.project.direction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bteam.project.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DirectionFragment extends Fragment {

    public BluetoothAdapter mBluetoothAdapter;
    public Set<BluetoothDevice> mDevices;
    private BluetoothSocket bSocket;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private BluetoothDevice mRemoteDevice;
    public boolean onBT = false;
    public ProgressDialog asyncDialog;
    private static final int REQUEST_ENABLE_BT = 1;
    private ImageButton btButton,checkBattery,redLed, forwardLed;
    private TextView textView, receiveData, receiveData2;
    private int selfDis = 0;
    private String data;
    private double battery;

    IntentFilter stateFilter;
    //////////////////////////////////////////////////
    private static final String TAG = "main:DirectionFrag";

    char mCharDelimiter = '\n';

    Thread mWorkerThread = null;
    byte[] readBuffer;
    int readBufferPosition;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_direction, container, false);

        btButton = root.findViewById(R.id.dire_imgBtn);         // 블루투스 연결 img버튼
        // textView = root.findViewById(R.id.text_direction);   // 좌표값 출력 지도와 연동
        receiveData2 = root.findViewById(R.id.receiveData2);    // 아두이노로 부터 수신 배터리 용량
        checkBattery = root.findViewById(R.id.checkBattery);    // 배터리 확인 하기 위한 버튼
        redLed = root.findViewById(R.id.redLed);                // 상단 LED 조절 imgBtn
        forwardLed = root.findViewById(R.id.forwardLed);        // 전방 LED 조절 imgBtn


        // 리시버 등록
        regiserRec();

        btButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: onBT " + onBT);

                if (!onBT) { //Connect
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) { //장치가 블루투스를 지원하지 않는 경우.
                        Toast.makeText(getActivity(), "Bluetooth 지원을 하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
                    } else { // 장치가 블루투스를 지원하는 경우.
                        if (!mBluetoothAdapter.isEnabled()) {
                            // 블루투스를 지원하지만 비활성 상태인 경우
                            // 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요청
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        } else {
                            // 블루투스를 지원하며 활성 상태인 경우
                            // 페어링된 기기 목록을 보여주고 연결할 장치를 선택.
                            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                            if (pairedDevices.size() > 0) {
                                // 페어링 된 장치가 있는 경우.
                                selectDevice();
                                Toast.makeText(getActivity(), "연결된 장치가 있습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                // 페어링 된 장치가 없는 경우.
                                // 검색된 목록을 다이얼로그로 표시
                                Toast.makeText(getActivity(), "먼저 Bluetooth 설정에 들어가 페어링을 진행해 주세요.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                } else { //DisConnect
                    try {
                        //mWorkerThread.interrupt();   // 데이터 송신 쓰레드 종료
                        selfDis = 0;
                        mInputStream.close();
                        mOutputStream.close();
                        bSocket.close();
                        onBT = false;
                        Toast.makeText(getActivity(),"연결을 해제합니다.",Toast.LENGTH_SHORT).show();
                        btButton.setImageResource(R.drawable.grayumbrella);
                    } catch (Exception e) {
                        Log.d(TAG, "onClick: disconnect " + e.getMessage());
                    }

                }
            }
        });

        checkBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selfDis == 1) {
//                 sendData(sendEdit.getText().toString().trim());         // 상태 확인문자 우노보드로 보내기
                    sendData("i");         // 상태 확인문자 우노보드로 보내기
                    try {
                        Thread.sleep(1000); // 잠시대기
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    beginListenForData();  // 상태 확인

                    battery = Double.parseDouble(data);

                    if (battery >= 8.6) {
                        Toast.makeText(getActivity(), "배터리 상태가 아주 좋아요", Toast.LENGTH_SHORT).show();
                        checkBattery.setImageResource(R.drawable.ic_battery5);
                    } else if (battery < 8.6 && battery >= 6.6) {
                        Toast.makeText(getActivity(), "배터리 상태가 양호합니다.", Toast.LENGTH_SHORT).show();
                        checkBattery.setImageResource(R.drawable.ic_battery3);
                    } else if (battery < 6.6 && battery >= 6.2) {
                        Toast.makeText(getActivity(), "배터리가 20프로 이하입니다. \n교체를 준비해주세요", Toast.LENGTH_SHORT).show();
                        checkBattery.setImageResource(R.drawable.ic_battery1);
                    } else if (battery < 6.2) {
                        Toast.makeText(getActivity(), "배터리교체가 필요합니다.", Toast.LENGTH_SHORT).show();
                        checkBattery.setImageResource(R.drawable.ic_battery_chenge);
                    }
                }else {
                    Toast.makeText(getActivity(), "먼저 블루투스를 연결해 주세요.", Toast.LENGTH_SHORT).show();
                }




//                 sendEdit.setText("");                Edit 초기화

//                if(mThreadConnectedBluetooth != null) {
//                    mThreadConnectedBluetooth.write(sendEdit.getText().toString());
//
//                }
            }
        });

        // 상단 LED 버튼 클릭시
        redLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selfDis == 1){
                    redLedSelect();
                }else{
                    Toast.makeText(getActivity(),"먼저 블루투스를 연결해 주세요.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 전방 LED 버튼 클릭시
        forwardLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selfDis == 1){
                    forwardLedSelect();
                }else{
                    Toast.makeText(getActivity(),"먼저 블루투스를 연결해 주세요.",Toast.LENGTH_SHORT).show();
                }


            }
        });

//        mBluetoothHandler = new Handler(){
//            public void handleMessage(android.os.Message msg){
//                if(msg.what == BT_MESSAGE_READ){
//                    String readMessage = null;
//                    try {
//                        readMessage = new String((byte[]) msg.obj, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    receiveData.setText(readMessage);
//                }
//            }
//        };



        return root;
    }

    // 상단 LED 클릭시 선택
    void redLedSelect(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("상단 LED 조절");

        final CharSequence[] items = { "켜기", "깜박이기", "끄기"};

        builder.setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(id == 0){
                            sendData("e");
                            redLed.setImageResource(R.drawable.ic_emergency_on);
                        }else if(id == 1){
                            sendData("f");
                            redLed.setImageResource(R.drawable.ic_emergency_on);
                        }else if(id == 2){
                            sendData("g");
                            redLed.setImageResource(R.drawable.ic_emergency_off);
                        }
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    // 전방 LED 클릭시 선택
    void forwardLedSelect(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("전방 LED 조절");

        final CharSequence[] items = { "1단계", "2단계", "3단계", "LED 끄기"};

        builder.setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(id == 0){
                            sendData("a");
                            forwardLed.setImageResource(R.drawable.ic_flashlight_on);
                        }else if( id == 1){
                            sendData("b");
                            forwardLed.setImageResource(R.drawable.ic_flashlight_on);
                        }else if( id == 2){
                            sendData("c");
                            forwardLed.setImageResource(R.drawable.ic_flashlight_on);
                        }else if( id == 3){
                            sendData("d");
                            forwardLed.setImageResource(R.drawable.ic_flashlight_off);
                        }
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

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
        getActivity().registerReceiver(mBluetoothStateReceiver, stateFilter);
    }

    BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();   //입력된 action
            //입력된 action에 따라서 함수를 처리한다
            if(selfDis == 1 && action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {   //블루투스 기기 끊어짐
//                    Log.d("Bluetooth", "ACTION_ACL_DISCONNECTED");
                Toast.makeText(getActivity(),"연결 끊어짐",Toast.LENGTH_SHORT).show();
                startLocationService();     // 좌표값 가져오는 메소드
                btButton.setImageResource(R.drawable.disconumbrella);
                selfDis = 0;

            }

        }
    };
    private void startLocationService() {
        // 위치관리자 객체 참조
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            Location lastLocation =
                    manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation != null){
                Double latitude = lastLocation.getLatitude(); // 위도
                Double longitude = lastLocation.getLongitude(); // 경도

                String msg = "Latitude : " + latitude + "\nLongitude : " + longitude;
                Log.d("main:location", msg);

                // textView.setText("트라이 위치1 : " + latitude +", " + longitude);
            }

        }catch (SecurityException e){
            Log.d("main:sException", e.getMessage());
        }


    }



    public void selectDevice() {
        mDevices = mBluetoothAdapter.getBondedDevices();
        final int mPairedDeviceCount = mDevices.size();

        if (mPairedDeviceCount == 0) {
            //  페어링 된 장치가 없는 경우
            Toast.makeText(getActivity(),"장치를 페어링 해주세요!",Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("블루투스 장치 선택");


        // 페어링 된 블루투스 장치의 이름 목록 작성
        List<String> listItems = new ArrayList<>();
        for(BluetoothDevice device : mDevices) {
            listItems.add(device.getName());
        }
        listItems.add("취소");    // 취소 항목 추가

        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(item == mPairedDeviceCount) {
                    // 연결할 장치를 선택하지 않고 '취소'를 누른 경우
//                    finish();
                }
                else {
                    // 연결할 장치를 선택한 경우
                    // 선택한 장치와 연결을 시도함
                    connectToSelectedDevice(items[item].toString());
                }
            }
        });


        builder.setCancelable(false);    // 뒤로 가기 버튼 사용 금지
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void connectToSelectedDevice(final String selectedDeviceName) {

        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);

        //Progress Dialog
        asyncDialog = new ProgressDialog(getActivity());
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage(selectedDeviceName+"블루투스 연결중..");
        asyncDialog.show();
//        asyncDialog.setCancelable(false);


                try {
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //HC-06 UUID
                    // 소켓 생성
                    bSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);


                    // RFCOMM 채널을 통한 연결 , 블루투스가 연결되지 않으면 여기서 Exception 발생
                    bSocket.connect();


                    // 데이터 송수신을 위한 스트림 열기
                    mOutputStream = bSocket.getOutputStream();
                    mInputStream = bSocket.getInputStream();

                    getActivity().runOnUiThread(new Runnable() {
                        @SuppressLint({"ShowToast", "SetTextI18n"})
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),selectedDeviceName + " 연결 완료",Toast.LENGTH_LONG).show();
                            btButton.setImageResource(R.drawable.blueumbrella);
                            asyncDialog.dismiss();
                        }
                    });
                    //mThreadConnectedBluetooth = new sendData(mOutputStream, mInputStream);
                    //mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();

                    selfDis = 1;
                    onBT = true;


                }catch(Exception e) {
                    // 블루투스 연결 중 오류 발생
                    Toast.makeText(getActivity(),"블루투스 연결 오류",Toast.LENGTH_SHORT).show();
                    asyncDialog.dismiss();
                }




    }


    public BluetoothDevice getDeviceFromBondedList(String name) {
        BluetoothDevice selectedDevice = null;

        for(BluetoothDevice device : mDevices) {
            if(name.equals(device.getName())) {
                selectedDevice = device;
                break;
            }
        }
        return selectedDevice;
    }

    // 우노보드로 데이터 보내기
    public void sendData(String msg) {
        try {
            this.mOutputStream.write(new StringBuilder(String.valueOf(msg)).toString().getBytes());
        } catch (Exception e) {
            Toast.makeText(getActivity(), "\ub370\uc774\ud130 \uc804\uc1a1 \uc911 \uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4.", Toast.LENGTH_SHORT).show();

        }
    }

    public void beginListenForData() {
        final Handler handler = new Handler();
        readBufferPosition = 0;                 // 버퍼 내 수신 문자 저장 위치.
        readBuffer = new byte[1024];            // 수신 버퍼.
        Log.d("beginListenForData", "1");
        // 문자열 수신 쓰레드.
        mWorkerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("beginListenForData", "2");
                // interrupt() 메소드를 이용 스레드를 종료시키는 예제이다.
                // interrupt() 메소드는 하던 일을 멈추는 메소드이다.
                // isInterrupted() 메소드를 사용하여 멈추었을 경우 반복문을 나가서 스레드가 종료하게 된다.
//                while (!Thread.currentThread().isInterrupted()) {
                try {
                    // InputStream.available() : 다른 스레드에서 blocking 하기 전까지 읽은 수 있는 문자열 개수를 반환함.
                    int byteAvailable = mInputStream.available();   // 수신 데이터 확인


                    if (byteAvailable > 0) {                        // 데이터가 수신된 경우.
                        byte[] packetBytes = new byte[byteAvailable];
                        // read(buf[]) : 입력스트림에서 buf[] 크기만큼 읽어서 저장 없을 경우에 -1 리턴.

                        //                       Log.d("byteAvailable", String.valueOf(byteAvailable));
                        Log.d("packetBytes", packetBytes.length + "");

                        mInputStream.read(packetBytes);
                        //                           readBufferPosition = 0;
                        for (int i = 0; i < byteAvailable; i++) {
                            byte b = packetBytes[i];
                            if (b == mCharDelimiter) {
                                Log.d("b", "b if : " + (int) b);
//                                readBuffer[readBufferPosition++] = b;
                                byte[] encodedBytes = new byte[readBufferPosition];
                                //  System.arraycopy(복사할 배열, 복사시작점, 복사된 배열, 붙이기 시작점, 복사할 개수)
                                //  readBuffer 배열을 처음 부터 끝까지 encodedBytes 배열로 복사.
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);

                                data = new String(encodedBytes, "US-ASCII");

                                //                                   Log.d("data", data + "\n");

                                readBufferPosition = 0;

                                handler.post(new Runnable() {
                                    // 수신된 문자열 데이터에 대한 처리.
                                    @Override
                                    public void run() {
                                        // mStrDelimiter = '\n';
//                                            receiveHeart.setText(data.substring(0, 3));
//                                            receiveTemperature.setText(data.substring(3, 8));
                                        Log.d("run", "문자 처리");
                                        Log.d("ACAC", "" + data.length());


                                        receiveData2.setText(data);
                                        Log.d("받은데이터",data);

                                        //String tempData =  data.substring(0, 1);
                                        //Log.d("tempData", tempData);

//                                            if(tempData.equals("c")){
//                                                Log.d("Main"," 거실에 등 상태는 켜진상태입니다");
//                                            }else if(tempData.equals("b")){
//                                                Log.d("Main"," 거실에 등 상태는 꺼진상태입니다");
//                                            }
//
//                                            if(tempData.equals("e")){
//                                                Log.d("Main","가스렌지 상태는 꺼진상태입니다");
//                                            }else if(tempData.equals("f")){
//                                                Log.d("Main","가스렌지  상태는 켜진상태입니다");
//                                            }
                                    }

                                });
                            } else {
                                readBuffer[readBufferPosition++] = b;
                                Log.d("b", "b else : " + (int) b + " / readBufferPosition : " + readBufferPosition);
                            }
                        }
                    }

                } catch (Exception e) {    // 데이터 수신 중 오류 발생.
                    Log.d("Error ", e.getMessage());
                    //                       Toast.makeText(getApplicationContext(), "데이터 수신 중 오류가 발생 했습니다.", Toast.LENGTH_LONG).show();
//                        finish();            // App 종료.
                }
                //               } //while문 종료
            }

        });
        mWorkerThread.start();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }




    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(getActivity(), permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0])) {
                Toast.makeText(getActivity(), "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}