package com.example.mypet.activity;

import android.app.AlertDialog;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypet.R;
import com.example.mypet.utils.InfoPrefs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;

public class BlueToothInfoActivity extends AppCompatActivity {
        private static final String TAG = "BlueToothInfoActivity";
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
    private String name;
    //选择的device
    BluetoothDevice device;
    private int REQUEST_ENABLE_BT=1;

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_blt);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InfoPrefs.init("pet_info");
        name=InfoPrefs.getData("pet_name");
        setContentView(R.layout.activity_bltinfo);

        initToolbar();
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
        }

    }

    private class ButtonClickListenser implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.as_server:
                    //开启服务器
                    startServer();
                    break;
                case R.id.as_client:
                    //开启客户端
                    startClient();
                    break;
            }
        }
    }

    private Handler handler =  new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //执行消息放入文本框
            output.append(msg.getData().getString("msg"));
            return true;
        }

    });

    public void mkmsg(String str) {
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
        new Thread(new AcceptThread()).start();

    }

    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private final BluetoothDevice mmDevice;
        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            //尝试获取套接字
            try {
                tmp = device.createRfcommSocketToServiceRecord(BlueToothInfoActivity.MY_UUID);
            } catch (IOException e) {
                mkmsg("获取套接字失败，错误信息： "+e.getMessage()+"\n");
            }
            socket = tmp;
        }
        public void run() {
            bltAdapter.cancelDiscovery();
            try {
                socket.connect();
            } catch (IOException e) {
                mkmsg("连接失败...\n");
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e2) {
                    mkmsg("关闭套接字失败，错误信息： "+e2.getMessage()+"\n");
                    socket = null;
                }
                // Start the service over to restart listening mode
            }
            // If a connection was accepted
            if (socket != null) {
                mkmsg("对方蓝牙设备地址: "+socket.getRemoteDevice().getAddress()+"\n");
                try {
                    PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
                    mkmsg("发送宠物信息...\n");
                    out.println("宠物姓名："+name);
                    out.flush();
                    mkmsg("发送完成...\n");

                    mkmsg("接收消息 ...\n");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String str = in.readLine();
                    mkmsg("接收到的消息:\n" + str+"\n");
                    mkmsg("连接结束\n");
                } catch(Exception e) {
                    mkmsg("发送接收出现错误\n");
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        mkmsg("关闭套接字失败，错误信息： "+e.getMessage()+"\n");
                    }
                }
            } else {
                mkmsg("以建立连接，但套接字为null\n");
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                mkmsg( "关闭套接字连接失败： "+e.getMessage() +"\n");
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
                tmp = bltAdapter.listenUsingRfcommWithServiceRecord(BlueToothInfoActivity.NAME, BlueToothInfoActivity.MY_UUID);
            } catch (IOException e) {
                mkmsg("启动服务器失败\n");
            }
            mmServerSocket = tmp;
        }

        public void run() {
            mkmsg("等待连接");
            BluetoothSocket socket = null;
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                mkmsg("接受请求失败\n");
            }

            // If a connection was accepted
            if (socket != null) {
                mkmsg("对方蓝牙设备地址： " + socket.getRemoteDevice().getAddress() + "\n");
                //Note this is copied from the TCPdemo code.
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String str = in.readLine();
                    mkmsg("接收到的信息: " + str + "\n");
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    mkmsg("发送信息...\n");
                    out.println("宠物姓名："+name);
                    out.flush();
                    mkmsg("已发送...\n");

                    mkmsg("连接关闭\n");
                } catch (Exception e) {
                    mkmsg("发送或接收信息出现问题\n");

                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        mkmsg("无法关闭套接字： " + e.getMessage() + "\n");
                    }
                }

            } else {
                mkmsg("连接已建立，但套接字为null\n");
            }
            mkmsg("服务器端结束\n");
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

    //获取已绑定设备列表,alertDialog显示
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
            builder.setTitle("请选择蓝牙设备:");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    dialog.dismiss();
                    if (item >= 0 && item <blueDev.length) {
                        device = blueDev[item];//当前选择的蓝牙设备
                        //设置设备信息
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
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    break;
                case BluetoothAdapter.STATE_OFF:
                    break;
                default:
            }
        }
    };

//    //管理得到的socket
//    private void manageSocket(BluetoothSocket socket){
//
//    }
//
//    //关闭套接字
//    private void closeSocket(BluetoothSocket socket){
//        try {
//            socket.close();
//        } catch (IOException e) {
//            mkmsg("无法关闭套接字： " + e.getMessage() + "\n");
//        }
//    }

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
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
