package macay.maceda.reloj.checador;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.Model.Empleados_admin;

public class UserPanelActivity extends AppCompatActivity {
    private long receivedPersonId;
    private DatabaseOpenHelper dbHelper;
    private CardView workin, workout, workback;
    TextView chekin_tv, checkout_tv, breakin_tv, breakout_tv;
    FloatingActionButton bt;
    CircleImageView imagen;
    TextView nombres;
    private String mCurrentPhotoPath = "";
    private String mWorkin, mWorkout, mBreakin, mBreakout;
    //private Handler _handler;
    private static final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        getSupportActionBar().hide();
        dbHelper = new DatabaseOpenHelper(this);
        imagen = (CircleImageView) findViewById(R.id.avatar);
        nombres = (TextView) findViewById(R.id.my_name);
        bt = (FloatingActionButton) findViewById(R.id.b_edit);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_pin_dialog();
            }
        });
        workin = (CardView) findViewById(R.id.inicio_trabajo);
        workout = (CardView) findViewById(R.id.salida_trabajo);
        workback = (CardView) findViewById(R.id.regreso_trabajo);

        chekin_tv = (TextView) findViewById(R.id.checkin_tv);
        checkout_tv = (TextView) findViewById(R.id.checkout_tv);
        breakin_tv = (TextView) findViewById(R.id.breakin_tv);
        breakout_tv = (TextView) findViewById(R.id.breakout_tv);

        try {
            receivedPersonId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Empleados_admin receivedPerson = dbHelper.getPerson(receivedPersonId);
        mCurrentPhotoPath = receivedPerson.getImage();
        nombres.setText(receivedPerson.getName()+" "+ receivedPerson.getLastname());

        Cursor cursor = dbHelper.already_workin_today(String.valueOf(receivedPersonId), datex() );

        if(cursor.getCount() > 0) {



            if (cursor.moveToLast()) {// data?
                mWorkin = cursor.getString(cursor.getColumnIndex("workin"));
                mWorkout = cursor.getString(cursor.getColumnIndex("workout"));
                mBreakin = cursor.getString(cursor.getColumnIndex("breakin"));
                mBreakout = cursor.getString(cursor.getColumnIndex("breakout"));


                //cursor.getString(0)

                /*
                Toast.makeText(UserPanelActivity.this,
                        "ENTRADA: " + mWorkin,
                        Toast.LENGTH_SHORT).show();
                        */
                chekin_tv.setVisibility(View.VISIBLE);
                chekin_tv.setText("ENTRADA: " +mWorkin);

                if (mWorkout == null) {

                    workin.setVisibility(View.GONE);
                    workout.setVisibility(View.VISIBLE);
                    checkout_tv.setVisibility(View.GONE);


                    /*
                    Toast.makeText(UserPanelActivity.this,
                            "Salida aun no registrada: ",
                            Toast.LENGTH_SHORT).show();
                            */


                } else {
                    workout.setVisibility(View.GONE);
                    workin.setVisibility(View.VISIBLE);
                    checkout_tv.setVisibility(View.VISIBLE);
                    checkout_tv.setText("SALIDA: " + mWorkout);


                    /*
                    Toast.makeText(UserPanelActivity.this,
                            "La salida ya fue registrada previamente",
                            Toast.LENGTH_SHORT).show();
                            */
                }

                if (mBreakout == null) {


                }
                else {
                    breakout_tv.setVisibility(View.VISIBLE);
                    breakout_tv.setText("SALIDA A COMER: " +mBreakout);
                    workout.setVisibility(View.GONE);
                    workback.setVisibility(View.VISIBLE);
                }

                if (mBreakin == null) {

                    /*
                    Toast.makeText(UserPanelActivity.this,
                            "La llegada de comer aun no esta registrada",
                            Toast.LENGTH_SHORT).show();
                            */



                }
                else {
                    breakin_tv.setVisibility(View.VISIBLE);
                    breakin_tv.setText("ENTRADA DE COMIDA: " +mBreakin);

                    if (mWorkout == null) {
                        workin.setVisibility(View.GONE);
                        workout.setVisibility(View.VISIBLE);
                        workback.setVisibility(View.GONE);
                    }
                    else {
                        workout.setVisibility(View.GONE);
                        workin.setVisibility(View.VISIBLE);
                        workback.setVisibility(View.GONE);
                    }

                }

            }

        }
        else {
            workout.setVisibility(View.GONE);
            workin.setVisibility(View.VISIBLE);


        }


        Picasso.with(this).load(new File(receivedPerson.getImage())).placeholder(R.mipmap.ic_launcher).into(imagen);

        workback.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.insert_user_breakin(String.valueOf(receivedPersonId), mWorkin, datetimex(), UserPanelActivity.this);
                finish();

            }


        });

        workin.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(UserPanelActivity.this,
                  //      "userid=" + String.valueOf(receivedPersonId) + " date=" +datex(),
                    //    Toast.LENGTH_SHORT).show();

                /*

                Cursor cursor = dbHelper.already_workin_today(String.valueOf(receivedPersonId), datex() );

                if(cursor.getCount() > 0) {

                    Toast.makeText(UserPanelActivity.this,
                            "La entrada fue registrada anteriormente",
                            Toast.LENGTH_SHORT).show();
                  //  mWorkin = cursor.getString(cursor.getColumnIndex("workin"));
                } else {
                    dbHelper.insert_user_workin(receivedPersonId, datex(), timex(), datetimex());

                    Toast.makeText(UserPanelActivity.this,
                            "La entrada fue registrada correctamente",
                            Toast.LENGTH_LONG).show();
                    finish();
                }


                */

                if (mBreakout == null) {
                    dbHelper.insert_user_workin(receivedPersonId, datex(), datetimex());
                    Toast.makeText(UserPanelActivity.this,
                            "La entrada fue registrada correctamente",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                else {

                        dbHelper.insert_user_workin(receivedPersonId, datex(), datetimex());
                        Toast.makeText(UserPanelActivity.this,
                                "La entrada fue registrada correctamente",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }

                   /*
                    Toast.makeText(UserPanelActivity.this,
                            "La entrada fue registrada correctamente",
                            Toast.LENGTH_LONG).show();
                            */

            }
        });





        workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(UserPanelActivity.this,
                //      "userid=" + String.valueOf(receivedPersonId) + " date=" +datex(),
                //    Toast.LENGTH_SHORT).show();


                if (mBreakin == null) {
                    options_exit();
                }
                else {
                    dbHelper.insert_user_workout(String.valueOf(receivedPersonId), mWorkin, datetimex(),
                            UserPanelActivity.this );
                    Toast.makeText(UserPanelActivity.this,"Cuminacion de trabajo exitosa!",Toast.LENGTH_LONG).show();
                   // dialog.dismiss();
                    finish();
                }

            }
        });
    }

    private void edit_pin_dialog() {
        handler.removeCallbacks(textRunnable);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(UserPanelActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_admin,null);
        final EditText password = (EditText) mView.findViewById(R.id.pass);
        final EditText repeatpassword = (EditText) mView.findViewById(R.id.repeatpass);
        password.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        repeatpassword.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        password.setHint("new pin");
        repeatpassword.setHint("repeat pin");
        TextView tv = (TextView) mView.findViewById(R.id.textView);
        //  tv.setVisibility(View.GONE);
        tv.setText("Cambiar Pin");
        Button cancel = (Button) mView.findViewById(R.id.cancel);
        Button register = (Button) mView.findViewById(R.id.login);

        mBuilder.setView(mView);
        //mBuilder.setTitle("Crear una contraseña");
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                handler.postDelayed(textRunnable, 10000);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Empleados_admin receivedPerson = dbHelper.getPerson(receivedPersonId);

                if (!password.getText().toString().isEmpty() && !repeatpassword.getText().toString().isEmpty()) {
                    if (password.getText().toString().equals(repeatpassword.getText().toString())) {
                        if (password.length() < 4 && repeatpassword.length() < 4){
                            password.setError("Agrege al menos 4 numeros");
                            repeatpassword.setError("Agrege al menos 4 numeros");
                        }else {
                            Empleados_admin updatedPerson = new Empleados_admin(receivedPerson.getName(),
                                    receivedPerson.getLastname(),receivedPerson.getNumber_phone(),
                                    receivedPerson.getOccupation(),receivedPerson.getArea(),
                                    receivedPerson.getEmail(), receivedPerson.getBirthday(),receivedPerson.getAddress(),
                                    receivedPerson.getDatework(),mCurrentPhotoPath, receivedPerson.getBlocked(), password.getText().toString().trim());
                            dbHelper.updatePasswordPerson(receivedPersonId, UserPanelActivity.this, updatedPerson);
                            dialog.dismiss();
                            handler.postDelayed(textRunnable, 10000);
                        }




                    } else {
                        repeatpassword.setError("Las contraseñas no coinciden");
                    }
                } else {
                    Toast.makeText(UserPanelActivity.this,
                            "Ingresa todos los campos",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
    }







    private void options_exit () {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(UserPanelActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_option_exit, null);

        ImageView person_culminate = (ImageView) mView.findViewById(R.id.culminate);
        ImageView person_food = (ImageView) mView.findViewById(R.id.food);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        person_culminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.insert_user_workout(String.valueOf(receivedPersonId), mWorkin, datetimex(),
                    UserPanelActivity.this );
                Toast.makeText(UserPanelActivity.this,"Culminacion de trabajo exitosa!",Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        person_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHelper.insert_user_breakout(String.valueOf(receivedPersonId), mWorkin, datetimex(),
                        UserPanelActivity.this );

                Toast.makeText(UserPanelActivity.this,"Hora de comida",Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });
    }


    public String diferenciaFechas(String inicio, String llegada){

        Date fechaInicio = null;
        Date fechaLlegada = null;

        // configuramos el formato en el que esta guardada la fecha en
        //  los strings que nos pasan
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            // aca realizamos el parse, para obtener objetos de tipo Date de
            // las Strings
            fechaInicio = formato.parse(inicio);
            fechaLlegada = formato.parse(llegada);

        } catch (ParseException e) {
            // Log.e(TAG, "Funcion diferenciaFechas: Error Parse " + e);
        } catch (Exception e){
            // Log.e(TAG, "Funcion diferenciaFechas: Error " + e);
        }

        // tomamos la instancia del tipo de calendario
        Calendar calendarInicio = Calendar.getInstance();
        Calendar calendarFinal = Calendar.getInstance();

        // Configramos la fecha del calendatio, tomando los valores del date que
        // generamos en el parse
        calendarInicio.setTime(fechaInicio);
        calendarFinal.setTime(fechaLlegada);

        // obtenemos el valor de las fechas en milisegundos
        long milisegundos1 = calendarInicio.getTimeInMillis();
        long milisegundos2 = calendarFinal.getTimeInMillis();

        // tomamos la diferencia
        long diferenciaMilisegundos = milisegundos2 - milisegundos1;

        // Despues va a depender en que formato queremos  mostrar esa
        // diferencia, minutos, segundo horas, dias, etc, aca van algunos
        // ejemplos de conversion

        // calcular la diferencia en segundos
        long diffSegundos =  Math.abs (diferenciaMilisegundos / 1000);

        // calcular la diferencia en minutos
        long diffMinutos =  Math.abs (diferenciaMilisegundos / (60 * 1000));
        long restominutos = diffMinutos%60;

        // calcular la diferencia en horas
        long diffHoras =   (diferenciaMilisegundos / (60 * 60 * 1000));

        // calcular la diferencia en dias
        long diffdias = Math.abs ( diferenciaMilisegundos / (24 * 60 * 60 * 1000) );

        // devolvemos el resultado en un string
        return String.valueOf(diffHoras + "H " + restominutos + "m ");
    }

    public static String datex () {
        Date date = Calendar.getInstance().getTime();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(date);

        return today;
    }

    public static String datetimex () {
        Date date = Calendar.getInstance().getTime();

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String today = formatter.format(date);

        return today;
    }
    public static String timex () {
        Date date = Calendar.getInstance().getTime();

        DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        String today = formatter.format(date);

        return today;
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        handler.postDelayed(textRunnable, 10000);

    }

    private final Runnable textRunnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

}
