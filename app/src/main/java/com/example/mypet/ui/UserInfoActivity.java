package com.example.mypet.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypet.utils.AlertDialogUtil;
import com.example.mypet.utils.Constants;
import com.example.mypet.R;
import com.example.mypet.bean.ChangeInfoBean;
import com.example.mypet.utils.InfoPrefs;
//import com.example.mypet.utils.InfoPrefs;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{
    RelativeLayout relativeLayout_user_nick_name;
    RelativeLayout relativeLayout_user_gender;
    TextView textView_user_nick_name;
    TextView textView_user_gender;
    //SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        textView_user_nick_name = findViewById(R.id.user_nick_name_TV);
        textView_user_gender = findViewById(R.id.user_gender_TV);
        init();

        relativeLayout_user_nick_name = findViewById(R.id.user_nick_name);
        relativeLayout_user_nick_name.setOnClickListener(this);

        relativeLayout_user_gender = findViewById(R.id.user_gender);
        relativeLayout_user_gender.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar_userinfo);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator();
        }
    }

    public void init(){
        InfoPrefs.init("user_info");
        textView_user_nick_name.setText(InfoPrefs.getData("user_nick_name"));
        //InfoPrefs.setData("user_gender","男");
        textView_user_gender.setText(InfoPrefs.getData("user_gender"));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.user_nick_name:
                ChangeInfoBean bean = new ChangeInfoBean();
                bean.setTitle("修改昵称");
                bean.setConfigName("user_info");
                bean.setInfoName("user_nick_name");
                Intent intent = new Intent(UserInfoActivity.this,ChangeInfoActivity.class);
                intent.putExtra("data", bean);
                startActivity(intent);
                break;
            case R.id.user_gender:
                new AlertDialogUtil(UserInfoActivity.this,-1,"修改性别")
                        .setSettings("user_info","user_gender")
                        .setTextView(textView_user_gender).setItems(Constants.GENDER).showDialog();
                //refresh(textView_user_gender);
                break;
            default:
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Toast.makeText(UserInfoActivity.this,"you press back!",Toast.LENGTH_LONG).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }
}

