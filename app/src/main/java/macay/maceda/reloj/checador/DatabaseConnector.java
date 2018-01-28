package macay.maceda.reloj.checador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseConnector {

    private static final String DB_NAME = "relojchecador";
    private SQLiteDatabase database;
    private DatabaseOpenHelper dbOpenhelper;
    private int DB_VERSION = 1;
    private Cursor cursor;

    public DatabaseConnector (Context context) {
        dbOpenhelper = new DatabaseOpenHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void open () throws SQLException {
        database = dbOpenhelper.getWritableDatabase();
    }

    public void close () {
        if (database != null) {
            database.close();
        }
    }

    //private String createUsers = "CREATE TABLE users (_id integer primary key autoincrement, name,"
    //      + " lastname, birthday, email, phone, address, ocupation, area, started_date, image);";
    public void insertUser (String name, String lastname, String birthday, String email, String phone,
                           String address, String ocupation, String area, String started_date,
                           String image ) {

        ContentValues newCon = new ContentValues();
        newCon.put("name", name);
        newCon.put("lastname", lastname);
        newCon.put("birthday", birthday);
        newCon.put("email", email);
        newCon.put("phone", phone);
        newCon.put("address", address);
        newCon.put("ocupation", ocupation);
        newCon.put("area", area);
        newCon.put("started_date", started_date);
        newCon.put("image", image);

        open();
        database.insert("users", null, newCon);
        close();

    }


}