package com.example.mypet.activity;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypet.R;
import com.example.mypet.bean.ChangeInfoBean;
import com.example.mypet.dialog.ItemsAlertDialogUtil;
import com.example.mypet.service.PetWindowService;
import com.example.mypet.utils.Constants;
import com.example.mypet.utils.InfoPrefs;
import com.kyleduo.switchbutton.SwitchButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetInfoActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    private static final String TAG = "PetInfoActivity";

    RelativeLayout relativeLayout_pet_theme;
    RelativeLayout relativeLayout_pet_name;
    SwitchButton switchButton_pet_isopen;
    TextView textView_pet_name;
    CircleImageView circleImageView_pet_theme;

    private static final int REQUEST_CHANGE_PET_NAME = 1;
    private static final int REQUEST_FLOAT_WINDOW = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info);
        init();
    }

    private void init() {
        InfoPrefs.init("pet_info");
        relativeLayout_pet_theme = findViewById(R.id.pet_theme);
        relativeLayout_pet_theme.setOnClickListener(this);
        relativeLayout_pet_name = findViewById(R.id.pet_name);
        relativeLayout_pet_name.setOnClickListener(this);
        switchButton_pet_isopen = findViewById(R.id.pet_isopen_switch);
        if(InfoPrefs.getIntData("pet_isopen") == 1){
            switchButton_pet_isopen.setChecked(true);
        }
        switchButton_pet_isopen.setOnCheckedChangeListener(this);
        textView_pet_name = findViewById(R.id.pet_name_TV);
        textView_pet_name.setText(InfoPrefs.getData("pet_name"));
        circleImageView_pet_theme = findViewById(R.id.pet_theme_iv);
        if(InfoPrefs.getIntData("pet_theme") == 0){
            circleImageView_pet_theme.setImageResource(R.drawable.fatqq);
        }else{
            circleImageView_pet_theme.setImageResource(R.drawable.ali);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pet_theme:
                new ItemsAlertDialogUtil(this).setItems(Constants.PETSELECT)
                        .setListener(new ItemsAlertDialogUtil.OnSelectFinishedListener() {
                    @Override
                    public void SelectFinished(int which) {
                        if(which == 0){
                            InfoPrefs.setIntData("pet_theme",0);
                            circleImageView_pet_theme.setImageResource(R.drawable.fatqq);
                        }else{
                            InfoPrefs.setIntData("pet_theme",1);
                            circleImageView_pet_theme.setImageResource(R.drawable.ali);
                        }
                    }
                }).showDialog();
                break;
            case R.id.pet_name:
                ChangeInfoBean bean = new ChangeInfoBean();
                bean.setTitle("修改昵称");
                bean.setInfo(InfoPrefs.getData("pet_name"));
                Intent intent = new Intent(PetInfoActivity.this,ChangeInfoActivity.class);
                intent.putExtra("data", bean);
                startActivityForResult(intent,REQUEST_CHANGE_PET_NAME);
                break;
            default:
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Intent it2 = new Intent(PetInfoActivity.this, PetWindowService.class);
        /*PetInfoActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    switchButton_pet_isopen.setEnabled(false);
                    Thread.sleep(1000);
                    switchButton_pet_isopen.setEnabled(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/
        if(isChecked){
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(getApplicationContext())) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, REQUEST_FLOAT_WINDOW);
                } else {
                    startService(it2);
                }
            }
            InfoPrefs.setIntData("pet_isopen",1);
        }else{
            stopService(it2);
            InfoPrefs.setIntData("pet_isopen",0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 获取changeinfo销毁 后 回传的数据
                case REQUEST_CHANGE_PET_NAME:
                    String returnData = data.getStringExtra("data_return");
                    InfoPrefs.setData("pet_name",returnData);
                    textView_pet_name.setText(InfoPrefs.getData("pet_name"));
                    break;
                case REQUEST_FLOAT_WINDOW:
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (!Settings.canDrawOverlays(getApplicationContext())) {
                            Toast.makeText(this, "未开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent it = new Intent(PetInfoActivity.this, PetWindowService.class);
                            startService(it);
                        }
                    }
                    break;
                default:
            }
        }else{
            Log.e(TAG,"result = "+resultCode+",request = "+requestCode);
        }
    }
}
