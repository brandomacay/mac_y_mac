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
    private static final int DATABASE_VERSION = 3 ;
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
                COLUMN_PERSON_IMAGE + " TEXT  NOT NULL);"
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
                personLinkedList.add(person);
            } while (cursor.moveToNext());
        }


        return personLinkedList;
    }

    /**Query only 1 record
     * @param id**/
    public Empleados_admin getPerson(String id){
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
        }
        return receivedPerson;
    }


    /**delete record**/
    public void deletePersonRecord(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+id+"'");
        Toast.makeText(context, "Empleado borrado", Toast.LENGTH_SHORT).show();

    }

    /**update record**/
    public void updatePersonRecord(long personId, Context context, Empleados_admin updatedperson) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_NAME+" SET name ='"+ updatedperson.getName() + "', age ='" + updatedperson.getLastname()+ "', occupation ='"+ updatedperson.getOccupation() + "', image ='"+ updatedperson.getImage() + "'  WHERE _id='" + personId + "'");
        Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();


    }
}
