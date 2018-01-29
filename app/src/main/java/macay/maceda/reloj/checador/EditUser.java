package macay.maceda.reloj.checador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.Model.Empleados_admin;

public class EditUser extends AppCompatActivity {
    private EditText eName,eLastName,eBirthday,eEmail,ePhone,eAddress,eOccupation,eArea,eDateWork;
    private ImageView photoUser;

    private DatabaseOpenHelper dbHelper;
    private long receivedPersonId;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        photoUser = (ImageView) findViewById(R.id.foto);
        eName = (EditText) findViewById(R.id.nombres);
        eLastName = (EditText) findViewById(R.id.apellidos);
        eBirthday = (EditText) findViewById(R.id.fecha_nacimiento);
        eEmail = (EditText) findViewById(R.id.correo);
        ePhone = (EditText) findViewById(R.id.numero_celular);
        eAddress = (EditText) findViewById(R.id.direccion);
        eOccupation = (EditText) findViewById(R.id.cargo);
        eArea = (EditText) findViewById(R.id.area);
        eDateWork = (EditText) findViewById(R.id.trabajo);
        dbHelper = new DatabaseOpenHelper(this);
        try {
            receivedPersonId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Empleados_admin receivedPerson = dbHelper.getPerson(receivedPersonId);
        eName.setText(receivedPerson.getName());
        eLastName.setText(receivedPerson.getLastname());
        eEmail.setText(receivedPerson.getEmail());
        ePhone.setText(receivedPerson.getNumber_phone());
        eAddress.setText(receivedPerson.getAddress());
        eOccupation.setText(receivedPerson.getOccupation());
        eArea.setText(receivedPerson.getArea());
        eDateWork.setText(receivedPerson.getDatework());


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.save) {

            validate_update_user();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void validate_update_user(){
        String name = eName.getText().toString().trim();
        String lastname = eLastName.getText().toString().trim();
        String birthday = eBirthday.getText().toString().trim();
        String email = eEmail.getText().toString().trim();
        String cellphone = ePhone.getText().toString().trim();
        String address = eAddress.getText().toString().trim();
        String occupation = eOccupation.getText().toString().trim();
        String area = eArea.getText().toString().trim();
        String datawork = eDateWork.getText().toString().trim();



        if(name.isEmpty()){
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
        }

        if(lastname.isEmpty()){
            Toast.makeText(this, "You must enter an lastname", Toast.LENGTH_SHORT).show();
        }

        if(occupation.isEmpty()){
            Toast.makeText(this, "You must enter an occupation", Toast.LENGTH_SHORT).show();
        }

        Empleados_admin updatedPerson = new Empleados_admin(name, lastname,cellphone,occupation,area,email, birthday,address,datawork,"");

        dbHelper.updatePerson(receivedPersonId, this, updatedPerson);

        finish();
    }
}
