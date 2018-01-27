package macay.maceda.reloj.checador;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_admin, null);
                final EditText password = (EditText) mView.findViewById(R.id.pass);
                final EditText repeatpassword = (EditText) mView.findViewById(R.id.repeatpass);
                Button cancel = (Button) mView.findViewById(R.id.cancel);
                Button login = (Button) mView.findViewById(R.id.login);

                mBuilder.setView(mView);
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
                        if(!password.getText().toString().isEmpty() && !repeatpassword.getText().toString().isEmpty()) {
                            if (password.getText().toString().equals(repeatpassword.getText().toString())) {
                                startActivity(new Intent(StartActivity.this, AdminActivity.class));
                                Toast.makeText(StartActivity.this,
                                        "Bienvenido!",
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else{
                                repeatpassword.setError("Las contraseñas no coinciden");
                            }
                        }else{
                            Toast.makeText(StartActivity.this,
                                    "Ingresa todos los campos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.user:
                startActivity(new Intent(StartActivity.this,MainActivity.class));
                overridePendingTransition(0,0);
                break;
        }
    }
}
