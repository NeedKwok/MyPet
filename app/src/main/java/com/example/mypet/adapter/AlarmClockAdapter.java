package com.example.mypet.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.mypet.R;
import com.example.mypet.bean.AlarmClockItemInfo;
import com.example.mypet.utils.FormatUtil;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;

import static com.example.mypet.utils.Constants.FALSE;
import static com.example.mypet.utils.Constants.TRUE;

public class AlarmClockAdapter extends RecyclerView.Adapter<AlarmClockAdapter.ViewHolder> implements View.OnClickListener{
    private static final String TAG = "AlarmClockAdapter";
    private List<AlarmClockItemInfo> mAlarmList;
    private AlarmClockAdapter.OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(/*View prevSelectedView,*/ View selectedView , int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View alarmClockItemView;
        TextView alarmClockTime;
        TextView alarmClockRepeat;
        TextView alarmClockLabel;
        SwitchButton switchButton;
        public ViewHolder(View itemView) {
            super(itemView);
            alarmClockItemView = itemView;
            alarmClockTime = itemView.findViewById(R.id.clock_time);
            alarmClockRepeat = itemView.findViewById(R.id.clock_repeat);
            alarmClockLabel =  itemView.findViewById(R.id.clock_label);
            switchButton = itemView.findViewById(R.id.clock_switch);
        }
    }

    public AlarmClockAdapter(List<AlarmClockItemInfo> mAlarmList) {
        this.mAlarmList = mAlarmList;
    }

    public void setOnItemClickListener(AlarmClockAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public AlarmClockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_clock_item, parent,false);
        final AlarmClockAdapter.ViewHolder holder = new AlarmClockAdapter.ViewHolder(view);
        holder.alarmClockItemView.setOnClickListener(this);
        holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = holder.getAdapterPosition();

            }
        });
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
            mOnItemClickListener.onItemClick(v,position);
        }else{
            Log.e(TAG,"you forgot add listener!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmClockAdapter.ViewHolder holder, int position) {
        AlarmClockItemInfo alarmClockItemInfo = mAlarmList.get(position);
        holder.alarmClockTime.setText(
                FormatUtil.addTimeSeparator(alarmClockItemInfo.getHour(),alarmClockItemInfo.getMinute()));
        holder.alarmClockLabel.setText(FormatUtil.lessThan8Words(alarmClockItemInfo.getLabel()));
        holder.alarmClockRepeat.setText(FormatUtil.getRepeatText(alarmClockItemInfo.getRepeat()));

        if(alarmClockItemInfo.getIsEnable() == TRUE){
            holder.switchButton.setChecked(true);
        } else{
            holder.switchButton.setChecked(false);
        }
        holder.alarmClockItemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mAlarmList.size();
    }
}
