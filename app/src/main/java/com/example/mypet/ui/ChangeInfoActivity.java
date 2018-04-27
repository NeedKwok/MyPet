package com.example.mypet.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.mypet.R;
import com.example.mypet.bean.ChangeInfoBean;

public class ChangeInfoActivity extends AppCompatActivity {
    ChangeInfoBean settings;
    EditText editText;
    //SharedPreferences sp;

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinfo);

        Intent intent = getIntent();
        settings = intent.getParcelableExtra("data");

        editText = findViewById(R.id.info_ET);
        //InfoPrefs.init("user_info");
        /*sp = MyPetApplication.getContext()
                .getSharedPreferences(settings.getConfigName(), Context.MODE_PRIVATE);
        editText.setText(sp.getString(settings.getInfoName(),""));*/
        editText.setText(settings.getInfo());

        Toolbar toolbar = findViewById(R.id.toolbar_changeinfo);
        toolbar.setTitle(settings.getTitle());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.confirm:
                Intent intent = new Intent();
                intent.putExtra("data_return",editText.getText().toString());
                setResult(RESULT_OK,intent);
                //InfoPrefs.setData(settings.getInfoName(),editText.getText().toString());
                //Toast.makeText(ChangeInfoActivity.this,editText.getText().toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(ChangeInfoActivity.this,sp.getString(settings.getInfoName(),"nothing!"),Toast.LENGTH_LONG).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
