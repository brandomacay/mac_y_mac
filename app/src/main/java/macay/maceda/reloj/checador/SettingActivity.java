package macay.maceda.reloj.checador;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.opencsv.CSVWriter;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cv,cv_pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cv = (CardView) findViewById(R.id.edit_pass);
        cv.setOnClickListener(this);
        cv_pdf = (CardView) findViewById(R.id.pdf_share);
        cv_pdf.setOnClickListener(this);
        setupActionBar();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_pass:
                edit_password_dialog();
                break;
            case R.id.pdf_share:

                createCSV();



                Toast.makeText(SettingActivity.this, "PDF CREADO!!", Toast.LENGTH_SHORT).show();
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
}
