package macay.maceda.reloj.checador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageButton workin;
    CircleImageView imagen;
    TextView nombres, textViewin;
    private String mCurrentPhotoPath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        getSupportActionBar().hide();
        dbHelper = new DatabaseOpenHelper(this);
        imagen = (CircleImageView) findViewById(R.id.avatar);
        nombres = (TextView) findViewById(R.id.my_name);
        textViewin = (TextView) findViewById(R.id.textViewin);
        workin = (ImageButton) findViewById(R.id.working_button);

        try {
            receivedPersonId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Empleados_admin receivedPerson = dbHelper.getPerson(receivedPersonId);
        mCurrentPhotoPath = receivedPerson.getImage();
        nombres.setText(receivedPerson.getName()+" "+ receivedPerson.getLastname());

        if (dbHelper.already_workin_today(String.valueOf(receivedPersonId), datex())) {
            workin.setVisibility(View.GONE);
            textViewin.setVisibility(View.GONE);

        }
        else {
            workin.setVisibility(View.VISIBLE);
            textViewin.setVisibility(View.GONE);

        }
        Picasso.with(this).load(new File(receivedPerson.getImage())).placeholder(R.mipmap.ic_launcher).into(imagen);

        workin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(UserPanelActivity.this,
                  //      "userid=" + String.valueOf(receivedPersonId) + " date=" +datex(),
                    //    Toast.LENGTH_SHORT).show();

                if (dbHelper.already_workin_today(String.valueOf(receivedPersonId), datex() )) {

                    Toast.makeText(UserPanelActivity.this,
                            "La entrada fue registrada anteriormente",
                            Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.insert_user_assistance(receivedPersonId, datex(), datetimex());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String today = formatter.format(date);

        return today;
    }
    public static String timex () {
        Date date = Calendar.getInstance().getTime();

        DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        String today = formatter.format(date);

        return today;
    }



}
