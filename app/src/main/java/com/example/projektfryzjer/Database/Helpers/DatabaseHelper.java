package com.example.projektfryzjer.Database.Helpers;

import static android.provider.Telephony.Carriers.PASSWORD;

import static com.example.projektfryzjer.Database.Migrations.Migrations.MIGRATION_1_2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projektfryzjer.Database.MyDatabase;
import com.example.projektfryzjer.Models.User;
import com.example.projektfryzjer.Models.UserFieldType;
import com.example.projektfryzjer.R;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static MyDatabase INSTANCE;
    private Cursor cursor = null;
    private static String DATABASE_NAME = "example_db";
    private static String DATABASE_VERSION = "1";
    @Override
    public void onCreate(SQLiteDatabase db){
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, Integer.parseInt(DATABASE_VERSION));
        INSTANCE = MyDatabase.getDatabaseInstance(context);
    }
    public static MyDatabase getDatabase(Context context){
        return MyDatabase.getDatabaseInstance(context);
    }
    public static String getUserFieldSQLQuery(Context context, UserFieldType userFieldType, String inputUsername) {
        MyDatabase myDatabase = getDatabase(context);
        User foundUser = myDatabase.userDao().findByUsername(inputUsername);
        if(foundUser != null) {
            switch (userFieldType) {
                case PASSWORD:
                    Toast.makeText(context, foundUser.toString(), Toast.LENGTH_LONG);
                    return foundUser.getPassword();
                case PASSWORD_SALT:
                    Toast.makeText(context, foundUser.toString(), Toast.LENGTH_LONG);
                    return foundUser.getPasswordSalt();
            }
        }
        Toast.makeText(
                context,
                "An user with that username was not found in the database!",
                Toast.LENGTH_LONG);
        return null;
    }
}