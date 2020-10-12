package com.bteam.project.direction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bteam.project.MainActivity;
import com.bteam.project.R;
import com.bteam.project.alarm.YoutubeActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public byte[] sendByte = new byte[4];
    public TextView tvBT;
    public ProgressDialog asyncDialog;
    private static final int REQUEST_ENABLE_BT = 1;
    private Button BTButton;
    private Button button;


    /**
     * 연결 끊기면 Receiver로 받기
     * 내 위치를 가져와서 일정 주기마다 저장하기 (xy좌표) --> SharedPreferences 쓰시면 될듯
     * 하세요
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_direction, container, false);

        BTButton = root.findViewById(R.id.dire_btn);





        button = root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), YoutubeActivity.class);
                startActivity(intent);


            }
        });

        BTButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                // selectDevice();
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
                        BTSend.interrupt();   // 데이터 송신 쓰레드 종료
                        mInputStream.close();
                        mOutputStream.close();
                        bSocket.close();
                        onBT = false;
                        BTButton.setText("connect");
                    } catch (Exception ignored) {
                    }

                }
            }
        });

        return root;
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
                    //finish();
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
        asyncDialog.setMessage("블루투스 연결중..");
        asyncDialog.show();
        asyncDialog.setCancelable(false);

        Thread BTConnect = new Thread(new Runnable() {
            public void run() {

                try {
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //HC-06 UUID
                    // 소켓 생성
                    bSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);

                    // RFCOMM 채널을 통한 연결
                    bSocket.connect();

                    // 데이터 송수신을 위한 스트림 열기
                    mOutputStream = bSocket.getOutputStream();
                    mInputStream = bSocket.getInputStream();


                    getActivity().runOnUiThread(new Runnable() {
                        @SuppressLint({"ShowToast", "SetTextI18n"})
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),selectedDeviceName + " 연결 완료",Toast.LENGTH_LONG).show();
                            tvBT.setText(selectedDeviceName + " Connected");
                            BTButton.setText("disconnect");
                            asyncDialog.dismiss();
                        }
                    });

                    onBT = true;


                }catch(Exception e) {
                    // 블루투스 연결 중 오류 발생
                    getActivity().runOnUiThread(new Runnable() {
                        @SuppressLint({"ShowToast", "SetTextI18n"})
                        @Override
                        public void run() {
                            tvBT.setText("연결 오류 -- BT 상태 확인해주세요.");
                            asyncDialog.dismiss();
                            Toast.makeText(getActivity(),"블루투스 연결 오류",Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });
        BTConnect.start();


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

    Thread BTSend  = new Thread(new Runnable() {
        public void run() {
            try {
                mOutputStream.write(sendByte);    // 프로토콜 전송
            } catch (Exception e) {
                // 문자열 전송 도중 오류가 발생한 경우.
            }
        }
    });

    //fixme : 데이터 전송
    public void sendbtData(int btLightPercent) throws IOException {
        //sendBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] bytes = new byte[4];
        bytes[0] = (byte) 0xa5;
        bytes[1] = (byte) 0x5a;
        bytes[2] = 1; //command
        bytes[3] = (byte) btLightPercent;
        sendByte = bytes;
        BTSend.run();
    }



}