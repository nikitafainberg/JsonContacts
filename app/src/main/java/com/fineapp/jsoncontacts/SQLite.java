package com.fineapp.jsoncontacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLite extends SQLiteOpenHelper{


    public static final int VERSION = 1;
    public static final String NAME = "jsonData";
    public static final String CONTACTS = "contacts";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_PHONE = "phone";

    public SQLite(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + CONTACTS + "(" + KEY_ID + " integer primary key," + KEY_NAME + " text," + KEY_EMAIL + " text," + KEY_ADDRESS + " text," + KEY_GENDER + " text," + KEY_PHONE + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVers, int newVers) {
        sqLiteDatabase.execSQL("drop table if exists" + CONTACTS);
        onCreate(sqLiteDatabase);
    }
}
