package macay.maceda.reloj.checador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.Model.Empleados_admin;

public class UserPanelActivity extends AppCompatActivity {
    private long receivedPersonId;
    private DatabaseOpenHelper dbHelper;
    CircleImageView imagen;
    TextView nombres;
    private String mCurrentPhotoPath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        getSupportActionBar().hide();
        imagen = (CircleImageView) findViewById(R.id.avatar);
        nombres = (TextView) findViewById(R.id.my_name);
        dbHelper = new DatabaseOpenHelper(this);
        try {
            receivedPersonId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Empleados_admin receivedPerson = dbHelper.getPerson(receivedPersonId);
        mCurrentPhotoPath = receivedPerson.getImage();
        nombres.setText(receivedPerson.getName()+" "+ receivedPerson.getLastname());
        Picasso.with(this).load(new File(receivedPerson.getImage())).placeholder(R.mipmap.ic_launcher).into(imagen);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
