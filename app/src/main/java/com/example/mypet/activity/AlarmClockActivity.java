package com.example.mypet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mypet.R;
import com.example.mypet.adapter.AlarmClockAdapter;
import com.example.mypet.adapter.MusicAdapter;
import com.example.mypet.bean.AlarmClockItemInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

public class AlarmClockActivity extends AppCompatActivity {
    private static final String TAG = "AlarmClockActivity";
    private static final int REQUEST_EDIT_ALARM_CLOCK = 1;
    private static final int REQUEST_ADD_ALARM_CLOCK = 2;

    private List<AlarmClockItemInfo> mAlarmList;
    private AlarmClockAdapter adapter;

    private int mPosition;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        initAll();
    }

    private void initAll() {
        initToolbar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        textView = findViewById(R.id.msg_empty_TV);
        mAlarmList = DataSupport.findAll(AlarmClockItemInfo.class);
        if(mAlarmList.isEmpty()){
            textView.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.GONE);
        }
        final RecyclerView recyclerView = findViewById(R.id.alarm_clock_RV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AlarmClockAdapter(mAlarmList);
        adapter.setOnItemClickListener(new AlarmClockAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mPosition = position;
                Intent intent = new Intent(AlarmClockActivity.this,EditAlarmClockActivity.class);
                intent.putExtra("position",position);
                startActivityForResult(intent,REQUEST_EDIT_ALARM_CLOCK);
            }

            @Override
            public void onItemLongClick(final int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AlarmClockActivity.this);
                dialog.setTitle("删除闹钟");
                dialog.setMessage("要删除选中的闹钟吗？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<AlarmClockItemInfo> info = DataSupport.limit(1)
                                .offset(position).find(AlarmClockItemInfo.class);
                        Log.d(TAG,"in delete->"+info.get(0).getMinute());
                        info.get(0).delete();
                        mAlarmList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position,mAlarmList.size()-position);
                        dialog.dismiss();
                        if(mAlarmList.size()<1||mAlarmList.isEmpty()){
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_alarm_clock);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.add:
                Intent intent = new Intent(AlarmClockActivity.this,NewAlarmClockActivity.class);
                startActivityForResult(intent,REQUEST_ADD_ALARM_CLOCK);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_ADD_ALARM_CLOCK:
                    AlarmClockItemInfo info = DataSupport.findLast(AlarmClockItemInfo.class);
                    mAlarmList.add(info);
                    adapter.notifyItemInserted(mAlarmList.size()-1);
                    adapter.notifyItemRangeChanged(mAlarmList.size()-1,1);
                    textView.setVisibility(View.GONE);
                    break;
                case REQUEST_EDIT_ALARM_CLOCK:
                    AlarmClockItemInfo info2 = DataSupport.limit(1).offset(mPosition).find(AlarmClockItemInfo.class).get(0);
                    mAlarmList.set(mPosition,info2);
                    adapter.notifyItemChanged(mPosition);
                    //adapter.notifyDataSetChanged();
                    break;
                default:
            }
        }
    }

}
