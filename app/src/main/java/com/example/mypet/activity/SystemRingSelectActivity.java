package com.example.mypet.activity;

import android.content.Intent;
import android.database.Cursor;

import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.mypet.R;
import com.example.mypet.adapter.MusicAdapter;
import com.example.mypet.bean.MusicInfo;
import com.example.mypet.utils.AudioPlayer;
import com.example.mypet.utils.Constants;
import com.example.mypet.utils.MyPetApplication;
import com.example.mypet.utils.RingUtil;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SystemRingSelectActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int REQUEST_CHANGE_ALARM_CLOCK_MUSIC = 10;
    private static final int LOADER = 1;
    List<MusicInfo> systemRingList = new ArrayList<>();

    String title,selectedLocalMusicTitle;
    String uri,selectedLocalMusicUri;
    int from,selectedLocalMusicFrom;

    MusicAdapter adapter;
    RelativeLayout selectedLocalMusicRelativeLayout;
    private int position = -1;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_ring_select);

        initToolbar();
        Intent intent = getIntent();
        selectedLocalMusicTitle = title = intent.getStringExtra("ring_title");
        selectedLocalMusicUri = uri = intent.getStringExtra("ring_uri");
        selectedLocalMusicFrom = from = intent.getIntExtra("ring_from",0);
        initSelectedLocalMusic();
        initMusicList();
        initJumping();
        //initRecyclerView();
    }

    private void initSelectedLocalMusic() {
        selectedLocalMusicRelativeLayout = findViewById(R.id.selected_local_music_RL);
        if(selectedLocalMusicFrom == 0){
            selectedLocalMusicRelativeLayout.setVisibility(View.GONE);
        }
        else{
            selectedLocalMusicRelativeLayout.setVisibility(View.VISIBLE);
            TextView titleTextView = selectedLocalMusicRelativeLayout.findViewById(R.id.selected_local_music_name);
            titleTextView.setText(RingUtil.formatTitle(selectedLocalMusicTitle));
            TextView fromTextView = selectedLocalMusicRelativeLayout.findViewById(R.id.selected_local_music_duration);
            fromTextView.setText("来自本地音乐");
            final ImageView selectedImageView = selectedLocalMusicRelativeLayout.findViewById(R.id.selected_local_music_icon);
            selectedLocalMusicRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioPlayer.getInstance(MyPetApplication.getContext())
                            .play(selectedLocalMusicUri,false,false);
                    title = selectedLocalMusicTitle;
                    uri = selectedLocalMusicUri;
                    from = selectedLocalMusicFrom;
                    if(getPosition()>=0){
                        selectedImageView.setVisibility(View.VISIBLE);
                        systemRingList.get(position).setIsSelected(Constants.FALSE);
                        adapter.notifyItemChanged(getPosition());
                    }
                    adapter.setPrevSelectedMusic(-1);
                    setPosition(-1);
                }
            });
        }

    }

    private void initJumping() {
        RelativeLayout relativeLayout = findViewById(R.id.goto_local_music_select_RL);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SystemRingSelectActivity.this,LocalMusicSelectActivity.class);
                intent1.putExtra("ring_title",title);
                intent1.putExtra("ring_uri",uri);
                intent1.putExtra("ring_from",from);
                startActivityForResult(intent1,REQUEST_CHANGE_ALARM_CLOCK_MUSIC);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_system_ring_select);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
    }

    private void initMusicList() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(LOADER,null,this);
        /*
        MusicInfo musicInfo = intent.getParcelableExtra("data");
        RingUtil util = new RingUtil(this);*/
        //systemRingList = util.getMusicInfoList(musicInfo.getMusicUri());
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
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.confirm:
                if(title == null ){
                    setResult(RESULT_CANCELED);
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("ring_title",title);
                    intent.putExtra("ring_uri",uri);
                    intent.putExtra("ring_from",from);
                    setResult(RESULT_OK,intent);
                }
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioPlayer.getInstance(MyPetApplication.getContext()).stop();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, new String[]{
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA}, null, null,
                MediaStore.Audio.Media.TITLE);
    }
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.system_ring_select_RV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
         adapter = new MusicAdapter(systemRingList,recyclerView);
        adapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View selectedView, int position) {
                if(selectedLocalMusicRelativeLayout.hasOnClickListeners()&&getPosition()<0){
                    selectedLocalMusicRelativeLayout.findViewById(R.id.selected_local_music_icon)
                            .setVisibility(View.GONE);
                }
                setPosition(position);
                AudioPlayer.getInstance(MyPetApplication.getContext())
                        .play(systemRingList.get(position).getMusicUri(),false,false);
                title = systemRingList.get(position).getMusicName();
                uri = systemRingList.get(position).getMusicUri();
                from = 0;
                //Toast.makeText(SystemRingSelectActivity.this,systemRingList.get(position).getMusicName(),Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        if(position >= 0){
            if(position<=2){
                recyclerView.scrollToPosition(0);
            }else{
                recyclerView.scrollToPosition(position-2);
            }
        }
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch(loader.getId()){
            case LOADER:
                int i = 0;
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                        .moveToNext()) {
                    // 音频文件名
                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    if(duration < 2000)
                        continue;
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setMusicName(cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    musicInfo.setFrom(0);
                    musicInfo.setMusicUri(cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA)));
                    musicInfo.setDuration(RingUtil.formatDuration(duration));
                    if(musicInfo.getMusicName().equals(title)){
                        musicInfo.setIsSelected(Constants.TRUE);
                        position = i;
                    }else{
                        musicInfo.setIsSelected(Constants.FALSE);
                    }
                    i++;
                    systemRingList.add(musicInfo);
                }
                initRecyclerView();
                break;
            default:
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case REQUEST_CHANGE_ALARM_CLOCK_MUSIC:
                    uri = data.getStringExtra("ring_uri");
                    title = data.getStringExtra("ring_title");
                    from = data.getIntExtra("ring_from",0);
                    if(title == null ){
                        setResult(RESULT_CANCELED);
                    }
                    else{
                        Intent intent = new Intent();
                        intent.putExtra("ring_title",title);
                        intent.putExtra("ring_uri",uri);
                        intent.putExtra("ring_from",from);
                        setResult(RESULT_OK,intent);
                    }
                    finish();
                    break;
                default:
            }
        }
    }
}


