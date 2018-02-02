package macay.maceda.reloj.checador.DataBase;

import android.app.*;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import java.util.LinkedList;
import java.util.List;
import macay.maceda.reloj.checador.Model.Empleados_admin;
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "relojchecador";
    private static final int DATABASE_VERSION = 1;

    //Tabla para los usuarios
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PERSON_NAME = "name";
    public static final String COLUMN_PERSON_LASTNAME = "lastname";
    public static final String COLUMN_PERSON_BIRTHDAY = "birthday";
    public static final String COLUMN_PERSON_EMAIL = "email";
    public static final String COLUMN_PERSON_PHONE = "phone";
    public static final String COLUMN_PERSON_OCCUPATION = "occupation";
    public static final String COLUMN_PERSON_ADDRESS = "address";
    public static final String COLUMN_PERSON_AREA = "area";
    public static final String COLUMN_PERSON_STARTEDDATE = "started_date";
    public static final String COLUMN_PERSON_IMAGE = "image";
    public static final String COLUMN_PERSON_PASSWORD = "password";

    //Tabla para checar asistencia
    public static final String TABLE_CLOCKING_NAME = "clocking";
    public static final String COLUMN_CLOCKING_ID = "_id";
    public static final String COLUMN_CLOCKING_USERID = "userid";
    public static final String COLUMN_CLOCKING_IN = "workin";
    public static final String COLUMN_CLOCKING_OUT = "workout";
    public static final String COLUMN_CLOCKING_BREAKIN = "breakin";
    public static final String COLUMN_CLOCKING_BREAKOUT = "breakout";










    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PERSON_NAME + " TEXT NOT NULL, " +
                COLUMN_PERSON_LASTNAME + " TEXT  NOT NULL, " +
                COLUMN_PERSON_BIRTHDAY + " TEXT NOT NULL, " +
                COLUMN_PERSON_EMAIL + " TEXT NOT NULL, " +
                COLUMN_PERSON_PHONE + " NUMBER NOT NULL, " +
                COLUMN_PERSON_OCCUPATION + " TEXT NOT NULL, " +
                COLUMN_PERSON_ADDRESS + " TEXT NOT NULL, " +
                COLUMN_PERSON_AREA + " TEXT NOT NULL, " +
                COLUMN_PERSON_STARTEDDATE + " TEXT NOT NULL, " +
                COLUMN_PERSON_IMAGE + " TEXT  NOT NULL, " +
                COLUMN_PERSON_PASSWORD + " NUMBER NOT NULL);"

        );

        db.execSQL(" CREATE TABLE " + TABLE_CLOCKING_NAME + " (" +
                COLUMN_CLOCKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLOCKING_USERID + " NUMBER NOT NULL, " +
            //    COLUMN_CLOCKING_DATE + " DATE NOT NULL, " +

                COLUMN_CLOCKING_IN + " DATETIME , " +
                COLUMN_CLOCKING_OUT + " DATETIME , " +
                COLUMN_CLOCKING_BREAKIN + " DATETIME , " +
                COLUMN_CLOCKING_BREAKOUT + " DATETIME );"

        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
    public void insertPerson(Empleados_admin person) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSON_NAME, person.getName());
        values.put(COLUMN_PERSON_LASTNAME, person.getLastname());
        values.put(COLUMN_PERSON_BIRTHDAY, person.getBirthday());
        values.put(COLUMN_PERSON_EMAIL, person.getEmail());
        values.put(COLUMN_PERSON_PHONE, person.getNumber_phone());
        values.put(COLUMN_PERSON_OCCUPATION, person.getOccupation());
        values.put(COLUMN_PERSON_ADDRESS, person.getAddress());
        values.put(COLUMN_PERSON_AREA, person.getArea());
        values.put(COLUMN_PERSON_STARTEDDATE, person.getDatework());
        values.put(COLUMN_PERSON_IMAGE, person.getImage());
        values.put(COLUMN_PERSON_PASSWORD, person.getPassword());

        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    /**Query records, give options to filter results**/
    public List<Empleados_admin> peopleList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_NAME;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filter;
        }

        List<Empleados_admin> personLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Empleados_admin person;

        if (cursor.moveToFirst()) {
            do {
                person = new Empleados_admin();

                person.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                person.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME)));
                person.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_LASTNAME)));
                person.setNumber_phone(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PHONE)));
                person.setOccupation(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_OCCUPATION)));
                person.setArea(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_AREA)));
                person.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_EMAIL)));
                person.setBirthday(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_BIRTHDAY)));
                person.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_ADDRESS)));
                person.setDatework(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_STARTEDDATE)));
                person.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
                person.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PASSWORD)));

                personLinkedList.add(person);
            } while (cursor.moveToNext());
        }


        return personLinkedList;
    }

    /**Query only 1 record
     * @param id**/
    public Empleados_admin getPerson(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        Empleados_admin receivedPerson = new Empleados_admin();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedPerson.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME)));
            receivedPerson.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_LASTNAME)));
            receivedPerson.setNumber_phone(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PHONE)));
            receivedPerson.setOccupation(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_OCCUPATION)));
            receivedPerson.setArea(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_AREA)));
            receivedPerson.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_EMAIL)));
            receivedPerson.setBirthday(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_BIRTHDAY)));
            receivedPerson.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_ADDRESS)));
            receivedPerson.setDatework(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_STARTEDDATE)));
            receivedPerson.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
            receivedPerson.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PASSWORD)));
        }
        return receivedPerson;
    }

    //Query only 1 record by id and password

    public Empleados_admin getEmpleado(String id, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ id + " AND password=" + password;
        Cursor cursor = db.rawQuery(query, null);

        Empleados_admin receivedPerson = new Empleados_admin();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedPerson.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME)));
            receivedPerson.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_LASTNAME)));
            receivedPerson.setNumber_phone(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PHONE)));
            receivedPerson.setOccupation(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_OCCUPATION)));
            receivedPerson.setArea(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_AREA)));
            receivedPerson.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_EMAIL)));
            receivedPerson.setBirthday(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_BIRTHDAY)));
            receivedPerson.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_ADDRESS)));
            receivedPerson.setDatework(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_STARTEDDATE)));
            receivedPerson.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
            receivedPerson.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PASSWORD)));
            return receivedPerson;
        }
        return null;
    }


    /**delete record**/
    public void deletePerson(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+id+"'");
        Toast.makeText(context, "Empleado borrado", Toast.LENGTH_SHORT).show();

    }

    /**update record**/
    public void updatePerson(long personId, Context context, Empleados_admin updatedperson) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_NAME+
                " SET name ='"+ updatedperson.getName() +
                "', lastname ='" + updatedperson.getLastname()+
                "', image ='" + updatedperson.getImage()+
                "', email ='" + updatedperson.getEmail()+
                "', phone ='" + updatedperson.getNumber_phone()+
                "', address ='" + updatedperson.getAddress()+
                "', occupation ='" + updatedperson.getOccupation()+
                "', area ='"+ updatedperson.getArea() +
                "', password ='"+ updatedperson.getPassword() +
                "', started_date ='"+ updatedperson.getDatework() +
                "'  WHERE _id='" + personId + "'");
        Toast.makeText(context, "Datos actualizados!", Toast.LENGTH_SHORT).show();


    }

    //insertar entradas y salidas por fecha
    public void insert_user_workin(long userid, String datetimex) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CLOCKING_USERID, userid);
      //  values.put(COLUMN_CLOCKING_DATE, today);

        values.put(COLUMN_CLOCKING_IN, datetimex);
        /*
        values.put(COLUMN_PERSON_BIRTHDAY, person.getBirthday());
        values.put(COLUMN_PERSON_EMAIL, person.getEmail());
        values.put(COLUMN_PERSON_PHONE, person.getNumber_phone());
        values.put(COLUMN_PERSON_OCCUPATION, person.getOccupation());
        values.put(COLUMN_PERSON_ADDRESS, person.getAddress());
        values.put(COLUMN_PERSON_AREA, person.getArea());
        values.put(COLUMN_PERSON_STARTEDDATE, person.getDatework());
        values.put(COLUMN_PERSON_IMAGE, person.getImage());
        values.put(COLUMN_PERSON_PASSWORD, person.getPassword());
        */

        // insert
        db.insert(TABLE_CLOCKING_NAME,null, values);
        db.close();
    }


    //insertar entradas y salidas por fecha
    /**update record**/
    public void insert_user_workout (String user_id, String workin, String workout, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_CLOCKING_NAME+
                " SET workout ='" + workout +
                "'  WHERE userid='" + user_id + "' AND workin='" + workin +
                "'");
        Toast.makeText(context, "Salida registrada", Toast.LENGTH_LONG).show();


    }

    public void insert_user_breakout (String user_id, String workin, String breakout, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_CLOCKING_NAME+
                " SET breakout ='" + breakout +
                "'  WHERE userid='" + user_id + "' AND workin='" + workin +
                "'");
        Toast.makeText(context, "Salida registrada", Toast.LENGTH_LONG).show();

    }

    public Cursor already_workin_today(String id, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT  * FROM " + TABLE_CLOCKING_NAME + " WHERE userid="+ id;

     //   String query = "SELECT  * FROM " + TABLE_CLOCKING_NAME + " WHERE userid="+ id + " AND date='" + date + "'";
        Cursor cursor = db.rawQuery(query, null);

        //Empleados_admin receivedPerson = new Empleados_admin();
       // if(cursor.getCount() > 0) {


            return cursor;


    }



}

