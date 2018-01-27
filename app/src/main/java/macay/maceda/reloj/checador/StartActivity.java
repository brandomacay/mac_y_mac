package macay.maceda.reloj.checador;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageButton btn_admin = (ImageButton) findViewById(R.id.admin);
        ImageButton btn_user = (ImageButton) findViewById(R.id.user);
        btn_admin.setOnClickListener(this);
        btn_user.setOnClickListener(this);
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
                        .commit();
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
}
