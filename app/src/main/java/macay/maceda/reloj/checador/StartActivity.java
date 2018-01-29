package macay.maceda.reloj.checador;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    TextView vhour,vpuntos,vminutes,vppmm,datte;
    boolean isShowed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageButton btn_admin = (ImageButton) findViewById(R.id.admin);
        ImageButton btn_user = (ImageButton) findViewById(R.id.user);
        datte = (TextView) findViewById(R.id.fecha);
        vhour = (TextView) findViewById(R.id.hora);
        vpuntos = (TextView) findViewById(R.id.puntos);
        vminutes = (TextView) findViewById(R.id.minutos);
        vppmm = (TextView) findViewById(R.id.ppmm);
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long date = System.currentTimeMillis();
                                SimpleDateFormat hora = new SimpleDateFormat("hh");
                                String horass = hora.format(date);
                                vhour.setText(horass);

                                vpuntos.setText(":");

                                if (isShowed) {
                                    vpuntos.setVisibility(View.INVISIBLE);
                                    isShowed = false;

                                }
                                else {
                                    vpuntos.setVisibility(View.VISIBLE);
                                    isShowed = true;
                                }


                                SimpleDateFormat minuto = new SimpleDateFormat("mm");
                                String minutoss = minuto.format(date);
                                vminutes.setText(minutoss);

                                SimpleDateFormat aa = new SimpleDateFormat("a");
                                String aaaa = aa.format(date);
                                vppmm.setText(" "+aaaa);

                                SimpleDateFormat dateFormat = new SimpleDateFormat("E dd MMM  yyyy");
                                String dates = dateFormat.format(date);
                                datte.setText(dates);
                            }
                        });
                    }
                } catch (InterruptedException ignored) {
                }
            }
        };
        t.start();
        btn_admin.setOnClickListener(this);
        btn_user.setOnClickListener(this);
        getSupportActionBar().hide();
        isStoragePermissionGranted();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 9);
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin:

                     boolean isPasswordSet;

                     isPasswordSet = PreferenceManager.getDefaultSharedPreferences(StartActivity.this)
                     .getBoolean("isPasswordSet", false);

                     if (isPasswordSet) {
                         login_password_dialog();
                     }
                     else {
                         create_password_dialog();
                     }
                break;
            case R.id.user:
                startActivity(new Intent(StartActivity.this,MainActivity.class));
                overridePendingTransition(0,0);
                break;
        }
    }

    private void login_password_dialog () {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_admin, null);

        final EditText password = (EditText) mView.findViewById(R.id.pass);
        EditText repeatpassword = (EditText) mView.findViewById(R.id.repeatpass);
        repeatpassword.setVisibility(View.GONE);
        TextView tv = (TextView) mView.findViewById(R.id.textView);
      //  tv.setVisibility(View.GONE);
        tv.setText("Administrador");
        Button cancel = (Button) mView.findViewById(R.id.cancel);
        Button login = (Button) mView.findViewById(R.id.login);

        mBuilder.setView(mView);
        //mBuilder.setTitle("Crear una contraseña");
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (password.getText().toString().isEmpty()) {


                    password.setError("Escribe una contraseña valida");

                } else {
                    String realPassword = PreferenceManager.getDefaultSharedPreferences(StartActivity.this)
                            .getString("password", "");

                    if (password.getText().toString().trim().equals(realPassword)) {
                        dialog.dismiss();
                        Toast.makeText(StartActivity.this,
                                "Bienvenido",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StartActivity.this, AdminActivity.class));

                    }
                    else {
                        password.setError("Contraseña incorrecta");
                    }
                }

            }
        });
    }

    private void create_password_dialog () {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_admin, null);
        final EditText password = (EditText) mView.findViewById(R.id.pass);
        final EditText repeatpassword = (EditText) mView.findViewById(R.id.repeatpass);
        TextView tv = (TextView) mView.findViewById(R.id.textView);
        //  tv.setVisibility(View.GONE);
        tv.setText("Crear una contraseña");
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
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (!password.getText().toString().isEmpty() && !repeatpassword.getText().toString().isEmpty()) {
                    if (password.getText().toString().equals(repeatpassword.getText().toString())) {

                        PreferenceManager.getDefaultSharedPreferences(StartActivity.this)
                        .edit()
                        .putBoolean("isPasswordSet", true)
                        .putString("password", repeatpassword.getText().toString().trim())
                        .apply();
                        Toast.makeText(StartActivity.this,
                                "Bienvenido",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        startActivity(new Intent(StartActivity.this, AdminActivity.class));
                        overridePendingTransition(0,0);

                    } else {
                        repeatpassword.setError("Las contraseñas no coinciden");
                    }
                } else {
                    Toast.makeText(StartActivity.this,
                            "Ingresa todos los campos",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
               // Log.v(TAG, "Permission is granted");
            //    isGPSPermissionGranted();
                return true;
            } else {
              //  Log.v(TAG, "Permission is revoked");
                // context.startActivity(new Intent(context, ProfileActivity.class));
              //  isGPSPermissionGranted();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
          //  Log.v(TAG, "Permission is granted");
            return true;
        }
    }

}
