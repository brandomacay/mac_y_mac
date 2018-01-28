package macay.maceda.reloj.checador;

import android.app.*;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private String createUsers = "CREATE TABLE users (_id integer primary key autoincrement, name,"
    + " lastname, birthday, email, phone, address, ocupation, area, started_date, image);";


    public DatabaseOpenHelper(Context context, String name,
                              CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(createUsers);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}

