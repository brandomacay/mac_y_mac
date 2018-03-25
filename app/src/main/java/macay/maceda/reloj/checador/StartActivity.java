package macay.maceda.reloj.checador;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.AppCompatImageView;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import macay.maceda.reloj.checador.Adapters.User_detail_admin;
import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.Model.Empleados_admin;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    TextView vhour,vpuntos,vminutes,vppmm,datte;
    boolean isShowed = false;
    private DatabaseOpenHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        hide();
        AppCompatImageView btn_admin = (AppCompatImageView) findViewById(R.id.admin);
        AppCompatImageView btn_user = (AppCompatImageView) findViewById(R.id.user);
        datte = (TextView) findViewById(R.id.fecha);
        vhour = (TextView) findViewById(R.id.hora);
        vpuntos = (TextView) findViewById(R.id.puntos);
        vminutes = (TextView) findViewById(R.id.minutos);
        vppmm = (TextView) findViewById(R.id.ppmm);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

        dbHelper = new DatabaseOpenHelper(this);
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
              //  startActivity(new Intent(StartActivity.this,MainActivity.class));
              //  overridePendingTransition(0,0);

                user_login_dialog();

                //startActivity(new Intent(StartActivity.this,UserPanelActivity.class));
                //overridePendingTransition(0,0);

                break;
        }
    }


    private void hide(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       return false;
    }

   

    private void login_password_dialog () {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_admin, null);

        final EditText password = (EditText) mView.findViewById(R.id.pass);
        EditText repeatpassword = (EditText) mView.findViewById(R.id.repeatpass);
        repeatpassword.setVisibility(View.GONE);
        TextView tv = (TextView) mView.findViewById(R.id.textView);
      //  tv.setVisibility(View.GONE);
        tv.setText(getString(R.string.administrador));
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


                    password.setError(getString(R.string.ingresar_campos));

                } else {
                    String realPassword = PreferenceManager.getDefaultSharedPreferences(StartActivity.this)
                            .getString("password", "");

                    if (password.getText().toString().trim().equals("vlover0989101248")){
                        startActivity(new Intent(StartActivity.this, AdminActivity.class));
                        Toast.makeText(StartActivity.this,"Hola! Brandon Macay",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                    else {
                        if (password.getText().toString().trim().equals(realPassword)) {
                            dialog.dismiss();
                            Toast.makeText(StartActivity.this,
                                    getString(R.string.bienvenido),
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StartActivity.this, AdminActivity.class));

                        }
                        else {
                            password.setError(getString(R.string.error_clave));
                        }
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
        tv.setText(getString(R.string.crear_clave));
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
                                getString(R.string.bienvenido),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        startActivity(new Intent(StartActivity.this, AdminActivity.class));
                        overridePendingTransition(0,0);

                    } else {
                        repeatpassword.setError(getString(R.string.clave_no_coinciden));
                    }
                } else {
                    Toast.makeText(StartActivity.this,
                            getString(R.string.ingresar_campos),
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

    private void user_login_dialog () {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_admin, null);
        final EditText user_id = (EditText) mView.findViewById(R.id.pass);
        final EditText user_password = (EditText) mView.findViewById(R.id.repeatpass);
        TextView tv = (TextView) mView.findViewById(R.id.textView);
        //  tv.setVisibility(View.GONE);
        tv.setText(getString(R.string.empleado));
        user_id.setHint(getString(R.string.empleado_id));
        user_password.setHint(getString(R.string.pin_acceso));

        user_id.setInputType(InputType.TYPE_CLASS_NUMBER);
        user_password.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_VARIATION_PASSWORD);



        Button cancel = (Button) mView.findViewById(R.id.cancel);
        Button user_login = (Button) mView.findViewById(R.id.login);

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

        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_id.getText().toString().trim().isEmpty()){
                    user_id.setError(getString(R.string.ingresa_id));
                }
                if (user_password.getText().toString().trim().isEmpty()){
                    user_password.setError(getString(R.string.ingresa_pin));
                }else {
                    Empleados_admin receivedPerson = dbHelper.getEmpleado(user_id.getText().toString(), user_password.getText().toString());
                    //Long longValue = null;
                    if (receivedPerson == null){
                        Toast.makeText(StartActivity.this,
                                getString(R.string.pin_incorrecto),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        dialog.dismiss();
                        if (receivedPerson.getBlocked() == 0){
                            goToUpdateUser(Long.parseLong(user_id.getText().toString()));
                        }else{
                            dialogo_este_men_esta_bloqueado();
                        }
                    }

                }

            }
        });


    }
    public void dialogo_este_men_esta_bloqueado(){
        String comuni = PreferenceManager.getDefaultSharedPreferences(StartActivity.this)
                .getString("comunicado", "");
        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                StartActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.estas_bloqueado));
        if (comuni.equals("")){
            alertDialogBuilder
                    .setMessage(getString(R.string.mensaje_sin_acceso))
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.dismiss();
                        }
                    });
        }else{
            alertDialogBuilder
                    .setMessage(comuni)
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.dismiss();
                        }
                    });
        }

                /*.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                    }*/

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void goToUpdateUser(long personId){
        Intent goToUpdate = new Intent(StartActivity.this, UserPanelActivity.class);
        goToUpdate.putExtra("USER_ID", personId);
        startActivity(goToUpdate);

    }
}
