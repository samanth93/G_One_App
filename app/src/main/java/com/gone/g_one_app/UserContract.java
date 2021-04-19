package com.gone.g_one_app;

import android.provider.BaseColumns;

public class UserContract {
    private UserContract(){}
    public static class UsersTable implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_PASSWORD = "password";
    }
}
