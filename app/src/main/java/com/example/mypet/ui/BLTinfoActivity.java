package com.example.mypet.ui;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypet.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Set;
import java.util.UUID;

public class BLTinfoActivity extends AppCompatActivity {
    private RelativeLayout name_show;
    private RelativeLayout number_show;
    private RelativeLayout blt_selectinfo;
    private RelativeLayout bltsc;
    private TextView output;
    private Button server;
    private Button client;
    private TextView device_name;
    private TextView device_num;
    private boolean blt_flag=false;
    private BluetoothAdapter bltAdapter;
    private TextView s_deviceInfo;
    public static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final String NAME = "Bluetooth";
    //选择的device
    BluetoothDevice device;
    private int REQUEST_ENABLE_BT=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bltinfo);
        name_show =findViewById(R.id.blt_name);
        number_show=findViewById(R.id.blt_num);
        blt_selectinfo=findViewById(R.id.blt_select);
        bltsc=findViewById(R.id.blt_sc);
        device_name=findViewById(R.id.device_name);
        device_num=findViewById(R.id.device_num);
        s_deviceInfo=findViewById(R.id.Sdevice_info);
        server=findViewById(R.id.as_server);
        client=findViewById(R.id.as_client);
        output=findViewById(R.id.outputInfo);
        server.setOnClickListener(new ButtonClickListenser());
        client.setOnClickListener(new ButtonClickListenser());
        name_show.setVisibility(View.INVISIBLE);
        number_show.setVisibility(View.INVISIBLE);
        blt_selectinfo.setVisibility(View.INVISIBLE);
        bltsc.setVisibility(View.INVISIBLE);
        bltAdapter = BluetoothAdapter.getDefaultAdapter();
        //注册蓝牙改变状态广播
        registerReceiver(bluetoothState,new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        //注册设备扫描到的广播
        //registerReceiver(deviceFound,new IntentFilter(BluetoothDevice.ACTION_FOUND));
        if (bltAdapter==null){
            Toast.makeText(this, "手机不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            blt_flag=bltAdapter.isEnabled();
        }
        if(!blt_flag){
            //申请打开蓝牙
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else{
            getMyDeviceInfo();
            Toast.makeText(this, "请先绑定设备", Toast.LENGTH_SHORT).show();
            //跳转至手机蓝牙设置界面用户自行绑定
            Intent intent_blt = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            startActivity(intent_blt);
            //getBoundDeviceInfo();
        }
//        IntentFilter intent = new IntentFilter();
//        intent.addAction(BluetoothDevice.ACTION_FOUND);//设备扫描到的广播
//        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙状态改变
//        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
//        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
//        registerReceiver(searchDevices, intent);

    }

    private class ButtonClickListenser implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.as_server:
                    //开启服务器
                    startServer();
                    Toast.makeText(BLTinfoActivity.this, "选择了server", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.as_client:
                    //开启客户端
                    startServer();
                    startClient();
                    Toast.makeText(BLTinfoActivity.this, "选择了client", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private Handler handler =  new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            output.append(msg.getData().getString("msg"));
            return true;
        }

    });

    public void mkmsg(String str) {
        //handler junk, because thread can't update screen!
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg", str);
        msg.setData(b);
        handler.sendMessage(msg);
    }

    //客户端
    public void startClient() {
        if (device != null) {
            new Thread(new ConnectThread(device)).start();
        }
    }

    //服务器端
    public void startServer() {
        BluetoothSocket socket;

        new Thread(new AcceptThread()).start();

    }

    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(BLTinfoActivity.MY_UUID);
            } catch (IOException e) {
                mkmsg("Client connection failed: "+e.getMessage()+"\n");
            }
            socket = tmp;

        }

        public void run() {
            mkmsg("Client running\n");
            // Always cancel discovery because it will slow down a connection
            bltAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket.connect();
            } catch (IOException e) {
                mkmsg("Connect failed\n");
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e2) {
                    mkmsg("unable to close() socket during connection failure: "+e2.getMessage()+"\n");
                    socket = null;
                }
                // Start the service over to restart listening mode
            }
            // If a connection was accepted
            if (socket != null) {
                mkmsg("Connection made\n");
                mkmsg("Remote device address: "+socket.getRemoteDevice().getAddress()+"\n");
                //Note this is copied from the TCPdemo code.
                try {
                    PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
                    mkmsg("Attempting to send message ...\n");
                    out.println("hello from Bluetooth Demo Client");
                    out.flush();
                    mkmsg("Message sent...\n");

                    mkmsg("Attempting to receive a message ...\n");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String str = in.readLine();
                    mkmsg("received a message:\n" + str+"\n");



                    mkmsg("We are done, closing connection\n");
                } catch(Exception e) {
                    mkmsg("Error happened sending/receiving\n");

                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        mkmsg("Unable to close socket"+e.getMessage()+"\n");
                    }
                }
            } else {
                mkmsg("Made connection, but socket is null\n");
            }
            mkmsg("Client ending \n");

        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                mkmsg( "close() of connect socket failed: "+e.getMessage() +"\n");
            }
        }
    }

    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = bltAdapter.listenUsingRfcommWithServiceRecord(BLTinfoActivity.NAME, BLTinfoActivity.MY_UUID);
            } catch (IOException e) {
                mkmsg("Failed to start server\n");
            }
            mmServerSocket = tmp;
        }

        public void run() {
            mkmsg("waiting on accept");
            BluetoothSocket socket = null;
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                mkmsg("Failed to accept\n");
            }

            // If a connection was accepted
            if (socket != null) {
                mkmsg("Connection made\n");
                mkmsg("Remote device address: " + socket.getRemoteDevice().getAddress() + "\n");
                //Note this is copied from the TCPdemo code.
                try {
                    mkmsg("Attempting to receive a message ...\n");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String str = in.readLine();
                    mkmsg("received a message:\n" + str + "\n");

                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    mkmsg("Attempting to send message ...\n");
                    out.println("Hi from Bluetooth Demo Server");
                    out.flush();
                    mkmsg("Message sent...\n");

                    mkmsg("We are done, closing connection\n");
                } catch (Exception e) {
                    mkmsg("Error happened sending/receiving\n");

                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        mkmsg("Unable to close socket" + e.getMessage() + "\n");
                    }
                }
            } else {
                mkmsg("Made connection, but socket is null\n");
            }
            mkmsg("Server ending \n");
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                mkmsg( "close() of connect socket failed: "+e.getMessage() +"\n");
            }
        }
    }


    //获取本地设备名和地址
    private void getMyDeviceInfo(){
        String deviceName=bltAdapter.getName();
        String deviceNum=bltAdapter.getAddress();
        device_num.setText(deviceNum);
        device_name.setText(deviceName);
    }

    //获取已绑定设备列表
    private void getBoundDeviceInfo(){
        Set<BluetoothDevice> boundDevices = bltAdapter.getBondedDevices();
        if (boundDevices.size()>0){
            final BluetoothDevice blueDev[] = new BluetoothDevice[boundDevices.size()];
            String[] items = new String[blueDev.length];
            int i =0;
            for (BluetoothDevice bltDevice : boundDevices){
                blueDev[i] = bltDevice;
                items[i] = blueDev[i].getName() + ": " + blueDev[i].getAddress();
                i++;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose Bluetooth:");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    dialog.dismiss();
                    if (item >= 0 && item <blueDev.length) {
                        device = blueDev[item];//当前选择的蓝牙设备
                        s_deviceInfo.setText(blueDev[item].getName()+"-->"+blueDev[item].getAddress());
                    }

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            Toast.makeText(this, "未找到已配对设备,请退出重试", Toast.LENGTH_SHORT).show();
        }
    }


//回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ENABLE_BT){
            switch (resultCode){
                case RESULT_OK:
                    bltAdapter.enable();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "此功能必须开启蓝牙", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //创建广播接收器
    private final BroadcastReceiver bluetoothState=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String stateExtra = BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(stateExtra, -1);
            switch (state){
                //打开中
                case BluetoothAdapter.STATE_TURNING_ON:
                    break;
                    //打开
                case BluetoothAdapter.STATE_ON:
                    unregisterReceiver(bluetoothState);
                    //获取本机设备信息
                    getMyDeviceInfo();
                    Toast.makeText(context, "请先配对设备", Toast.LENGTH_SHORT).show();
                    //跳转至手机蓝牙设置界面用户自行绑定
                    Intent intent_blt = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(intent_blt);
                    //getBoundDeviceInfo();
                    //设置设备可见性
//                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//                    startActivity(discoverableIntent);
                    //开始扫描
                    //bltAdapter.startDiscovery();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    break;
                case BluetoothAdapter.STATE_OFF:
                    break;
                default:
            }
        }
    };

    //创建蓝牙发现设备的广播接收器
//    private final BroadcastReceiver deviceFound= new BroadcastReceiver(){
////        @Override
////        public void onReceive(Context context, Intent intent) {
////            String action = intent.getAction();
////            // When discovery finds a device
////            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
////                // Get the 获得intent中的device对象
////                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
////                // 打印发现设备的名称和设备号
////                Log.e("Found device","DeviceInfo: "+device.getName()+"-->"+device.getAddress());
////            }
////        }
////    };




//  从系统设置界面返回后获取所有绑定的设备信息
    @Override
    protected void onRestart() {
        getBoundDeviceInfo();
        number_show.setVisibility(View.VISIBLE);
        name_show.setVisibility(View.VISIBLE);
        blt_selectinfo.setVisibility(View.VISIBLE);
        bltsc.setVisibility(View.VISIBLE);
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        //销毁时注销发现设备广播
        //unregisterReceiver(deviceFound);
        super.onDestroy();
    }
}
