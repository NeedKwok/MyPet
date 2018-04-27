package com.example.mypet.utils;

public final class Constants {

    public final class UserInfo{
        public static final String NAME = "user_nick_name";
        public static final String GENDER = "user_gender";
        public static final String HEAD_IMAGE = "user_head_uri";
    }
    public static final String DEFAULT_LABEL = "闹钟";
    /**
     * ItemsAlertDialogUtil gender
     */
    public static final String[] GENDER_ITEMS={"男","女","保密"};
    /**
     * ItemsAlertDialogUtil repeat
     */
    public static final String WEEKDAY = "工作日";
    public static final String WEEKEND = "周末";
    public static final String EVERYDAY = "每天";
    public static final String ONLY_ONCE = "仅一次";

    public static final int CODE_WEEKDAY = 0;
    public static final int CODE_WEEKEND = 1;
    public static final int CODE_EVERYDAY = 2;
    public static final int CODE_ONLY_ONCE = 3;

    public static final String[] DAY_ITEMS={WEEKDAY,WEEKEND,EVERYDAY,ONLY_ONCE};

    /**
     * ItemsAlertDialogUtil remind
     */
    public static final String RING = "响铃";
    public static final String SHOCK = "震动";
    public static final String RING_AND_SHOCK = "响铃及震动";

    public static  final int CODE_RING = 0;
    public static  final int CODE_SHOCK = 1;
    public static  final int CODE_RING_AND_SHOCK = 2;

    public static final String[] REMINDING = {"响铃","震动","响铃及震动"};

    /**
     * ItemsAlertDialogUtil snooze
     */
    public static final String NO_SNOOZE = "无";
    public static final String ONE_MINUTE = "1 分钟";
    public static final String THREE_MINUTES = "3 分钟";
    public static final String FIVE_MINUTES = "5 分钟";
    public static final String TEN_MINUTES = "10 分钟";
    public static final String QUARTER_HOUR = "15 分钟";
    public static final String HALF_HOUR = "30 分钟";

    public static final int CODE_NO_SNOOZE = 0;
    public static final int CODE_ONE_MINUTE = 1;
    public static final int CODE_THREE_MINUTES = 3;
    public static final int CODE_FIVE_MINUTES = 5;
    public static final int CODE_TEN_MINUTES = 10;
    public static final int CODE_QUARTER_HOUR = 15;
    public static final int CODE_HALF_HOUR = 30;

    public static final String[] SNOOZE = {NO_SNOOZE,ONE_MINUTE,THREE_MINUTES,FIVE_MINUTES,TEN_MINUTES,QUARTER_HOUR,HALF_HOUR};
}
