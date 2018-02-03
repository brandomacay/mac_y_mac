package macay.maceda.reloj.checador;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.opencsv.CSVWriter;

import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cv,cv_pdf;
    ProgressDialog pd;
    private DatabaseOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cv = (CardView) findViewById(R.id.edit_pass);
        cv.setOnClickListener(this);
        cv_pdf = (CardView) findViewById(R.id.pdf_share);
        cv_pdf.setOnClickListener(this);
        setupActionBar();
        dbHelper = new DatabaseOpenHelper(this);


    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_pass:
                edit_password_dialog();
                break;
            case R.id.pdf_share:


                exportdb();


                //Toast.makeText(SettingActivity.this, "PDF CREADO!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void edit_password_dialog () {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_admin, null);
        final EditText password = (EditText) mView.findViewById(R.id.pass);
        final EditText repeatpassword = (EditText) mView.findViewById(R.id.repeatpass);
        password.setHint("new password");
        TextView tv = (TextView) mView.findViewById(R.id.textView);
        tv.setText("Cambiar contraseña");
        Button cancel = (Button) mView.findViewById(R.id.cancel);
        Button register = (Button) mView.findViewById(R.id.login);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (!password.getText().toString().isEmpty() && !repeatpassword.getText().toString().isEmpty()) {
                    if (password.getText().toString().equals(repeatpassword.getText().toString())) {

                        PreferenceManager.getDefaultSharedPreferences(SettingActivity.this)
                                .edit()
                                .putBoolean("isPasswordSet", true)
                                .putString("password", repeatpassword.getText().toString().trim())
                                .apply();
                        Toast.makeText(SettingActivity.this,
                                "Contraseña cambiada exitosamente!",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                    } else {
                        repeatpassword.setError("Las contraseñas no coinciden");
                    }
                } else {
                    Toast.makeText(SettingActivity.this,
                            "Ingresa todos los campos",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Habilita la acccion de volver a la actividad anterior xD.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //finalizo la actividad para no hacer intent ni startactivity ni mamadas xD
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void createCSV () {
        try {
            String path = "sdcard/relojchecador/archivos/";
            String fileName = "Listaempleados";
            String content = "Alejandro, Jorge, Brandon";
            File file = new File(path + fileName +".csv");
            // if file doesnt exists, then create it
            File f = new File(path);
            f.getAbsolutePath();

            if(f.mkdirs()) {
                //se ha creado bien
                //string.replace(" ", "\\ ");
                Toast.makeText(SettingActivity.this,
                        "Carpeta creada", Toast.LENGTH_LONG).show();

            }

            //file.mkdir();
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportdb() //throws IOException
    {
        pd = new ProgressDialog(SettingActivity.this);
        pd.setTitle("Guardando DB");
        pd.setMessage("Please Wait...");
        //	pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        createcarpet();
        new saveCsv().execute();
        //new UnZipTask().execute("", "");
    }
    private void createcarpet() {

        File f = new File("/sdcard/relojchecador/archivos/");
        f.getAbsolutePath();

        if(f.mkdirs()) {
            //se ha creado bien
            //string.replace(" ", "\\ ");
            Toast.makeText(SettingActivity.this,
                    "Carpeta creada", Toast.LENGTH_LONG).show();
            //pd.dismiss();

        }
        else {
            if (f.exists()) {
                //	((MainActivity)getActivity()).show_toast("La carpeta ya existe");
                //pd.dismiss();
            }
            else

            Toast.makeText(SettingActivity.this,
                    "Carpeta o SD no encontrada", Toast.LENGTH_LONG).show();
            pd.dismiss();
        }

    }

    public class saveCsv extends AsyncTask<String, String, Cursor>
    {

        @Override
        protected Cursor doInBackground(String... params)
        {
            return dbHelper.get_all_users();
        }

        @Override
        protected void onPostExecute(Cursor result)
        {


            boolean sdDisponible = false;
            boolean sdAccesoEscritura = false;

            //Comprobamos el estado de la memoria externa (tarjeta SD)
            String estado = Environment.getExternalStorageState();

            if (estado.equals(Environment.MEDIA_MOUNTED))
            {
                sdDisponible = true;
                sdAccesoEscritura = true;
            }
            else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
            {
                sdDisponible = true;
                sdAccesoEscritura = false;
            }
            else
            {
                sdDisponible = false;
                sdAccesoEscritura = false;
            }

            //Si la memoria externa está disponible y se puede escribir
            if (sdDisponible && sdAccesoEscritura)
            {
                try
                {
                    //File ruta_sd = Environment.getExternalStorageDirectory();
                    String	date = getCurrDate();
                    String filename = "empleados_" + date;

                    File f = new File("/sdcard/relojchecador/archivos/",
                            filename.toString().replace("/","")
                                    .replace(" ", "_")
                                    .replace(",", "")
                                    .replace(":", "")+".csv" );

                   // CSVWriter writer = new CSVWriter(new FileWriter(exportFile), ',');
                    CSVWriter writer = new CSVWriter(new FileWriter(f));


                    String[] columnNames = result.getColumnNames();

                   // Log.d(TAG, "column names: " + Arrays.asList(columnNames));

                    columnNames = deleteElement("password", columnNames);


                    writer.writeNext(columnNames);
                    // Write rows
                    result.moveToFirst();
                    while (!result.isAfterLast()) {
                        final String[] fields = getFieldsAsStringArray(result);
                        writer.writeNext(fields);
                        result.moveToNext();
                    }



                    writer.close();
                    Toast.makeText(SettingActivity.this, "EXITO: Archivo CSV creado", Toast.LENGTH_SHORT).show();


                }
                catch (Exception ex)
                {
                    Toast.makeText(SettingActivity.this, "Error al escribir fichero en la tarjeta SD", Toast.LENGTH_SHORT).show();
                    //	Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
                }
            }

            //	lv.setAdapter(adapter);
            dbHelper.close();
            result.close();
        }
    }

    public String getCurrDate() {
        String dt;
        Date cal = Calendar.getInstance().getTime();
        dt = cal.toLocaleString();
        return dt;
    }
    private String[] getFieldsAsStringArray(Cursor cursor) {
        final int columnCount = cursor.getColumnCount();
        final String[] result = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {

            // cursor = 11 is the column password, so i must delete that one
            if (i != 11) {
                result[i] = cursor.getString(i);
            }
        }
        return result;
    }
    private static String[] deleteElement(String stringToDelete, String[] array) {
        String[] result = new String[array.length];
        int index = 0;

        ArrayList<String> rm = new ArrayList<String>();

        for(int i = 0; i < array.length; i++) {
            rm.add(array[i]);
        }
        for(int i = 0; i < array.length; i++) {
            if(array[i].equals(stringToDelete)) {
                index = i;
            }
        }
        rm.remove(index);

        result = rm.toArray(new String[rm.size()]);

        return result;
    }

}
