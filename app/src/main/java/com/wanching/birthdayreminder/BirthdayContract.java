package com.wanching.birthdayreminder;

import android.provider.BaseColumns;

/**
 * Created by WanChing on 4/8/2017.
 */

public class BirthdayContract {

    public BirthdayContract(){}

    public static class BirthdayEntry implements BaseColumns{
        public static final String TABLE_NAME="birthday";
        public static final String COLUMN_NAME_NAME="name";
        public static final String COLUMN_NAME_DATE="date";
        public static final String COLUMN_NAME_EMAIL="email";
        public static final String COLUMN_NAME_NOTIFY="notify";
    }
}
