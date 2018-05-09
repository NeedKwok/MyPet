package com.example.mypet.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypet.R;
import com.example.mypet.bean.MusicInfo;
import com.example.mypet.utils.RingUtil;

import java.util.List;

import static com.example.mypet.utils.Constants.FALSE;
import static com.example.mypet.utils.Constants.TRUE;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> implements View.OnClickListener{
    private static final String TAG = "MusicAdapter";
    private List<MusicInfo> mMusicList;
    private int prevSelectedMusic;
    private RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener = null;

    public void setPrevSelectedMusic(int prevSelectedMusic) {
        this.prevSelectedMusic = prevSelectedMusic;
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View musicItemView;
        TextView musicName;
        TextView musicLength;
        ImageView musicIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            musicItemView = itemView;
            musicName = itemView.findViewById(R.id.music_name);
            musicLength = itemView.findViewById(R.id.music_length);
            musicIcon =  itemView.findViewById(R.id.music_icon);
        }
    }

    public MusicAdapter(List<MusicInfo> mMusicList,RecyclerView mRecyclerView) {
        this.mMusicList = mMusicList;
        this.mRecyclerView = mRecyclerView;
        prevSelectedMusic = -1;
        for(int i =0; i<mMusicList.size(); i++){
            if(mMusicList.get(i).getIsSelected() == TRUE){
                prevSelectedMusic = i;
                break;
            }
        }
    }
    public MusicAdapter(List<MusicInfo> mMusicList,RecyclerView mRecyclerView,int prevSelectedMusic) {
        this.mMusicList = mMusicList;
        this.mRecyclerView = mRecyclerView;
        this.prevSelectedMusic = prevSelectedMusic;
        mMusicList.get(prevSelectedMusic).setIsSelected(TRUE);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item, parent,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    /**
     *  还要补充
     * @param v
     */
    @Override
    public void onClick(View v) {
        int position = (int)v.getTag();
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(position);
            //当前选择与之前选择不同才执行
            if(position != prevSelectedMusic){
                if(prevSelectedMusic >= 0){
                    ViewHolder viewHolder = (ViewHolder)mRecyclerView.findViewHolderForLayoutPosition(prevSelectedMusic);
                    if(viewHolder != null){
                        viewHolder.musicIcon.setVisibility(View.GONE);
                    }else{
                        notifyItemChanged(prevSelectedMusic);
                    }
                    mMusicList.get(prevSelectedMusic).setIsSelected(FALSE);
                }
                prevSelectedMusic = position;
                mMusicList.get(prevSelectedMusic).setIsSelected(TRUE);
                v.findViewById(R.id.music_icon).setVisibility(View.VISIBLE);
                /*viewHolder = (ViewHolder)mRecyclerView.findViewHolderForLayoutPosition(prevSelectedMusic);
                viewHolder.musicIcon.setVisibility(View.VISIBLE);*/
            }

        }else{
            Log.e(TAG,"you forgot add listener!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicInfo musicInfo = mMusicList.get(position);
        holder.musicName.setText(RingUtil.formatTitle(musicInfo.getMusicName()));
        holder.musicLength.setText(musicInfo.getDuration());
        holder.musicIcon.setImageResource(R.drawable.ic_add_alert_black_24dp);
        if(musicInfo.getIsSelected() == TRUE){
            holder.musicIcon.setVisibility(View.VISIBLE);
        } else{
            holder.musicIcon.setVisibility(View.GONE);
        }
        holder.musicItemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }


}
