package macay.maceda.reloj.checador;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class RegisterUserActivity extends AppCompatActivity {

    ImageView photo;
    //private String createUsers = "CREATE TABLE users (_id integer primary key autoincrement, name,"
    //      + " lastname, birthday, email, phone, address, ocupation, area, started_date, image);";
    private EditText name, lastname, email, phone, address, ocupation, area;
    private static TextView birthday, started_date;
    Bitmap bitmappost;
    boolean imageIsSet = false;
    private static boolean birthday_picked = false;
    private static boolean started_picked = false;
    private static int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_register_user);

        name = (EditText) findViewById(R.id.register_user_name_et);
        lastname = (EditText) findViewById(R.id.register_user_lastname_et);
        email = (EditText) findViewById(R.id.register_user_email_et);
        phone = (EditText) findViewById(R.id.register_user_phone_et);
        address = (EditText) findViewById(R.id.register_user_address_et);
        ocupation = (EditText) findViewById(R.id.register_user_ocupation_et);
        area = (EditText) findViewById(R.id.register_user_area_et);


        birthday = (TextView) findViewById(R.id.register_user_birthday_tv);
        started_date = (TextView) findViewById(R.id.register_user_started_date_tv);

        photo = (ImageView) findViewById(R.id.foto);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropMenuCropButtonTitle("Elegir")
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setOutputCompressQuality(75)
                        .start(RegisterUserActivity.this);
            }
        });
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newfrag = new DateBirthday();
                newfrag.show(getFragmentManager(), "datePicker");
            }
        });
        started_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newfrag = new DateStartWork();
                newfrag.show(getFragmentManager(), "datePicker");
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmappost = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    photo.setImageBitmap(bitmappost);
                    imageIsSet = true;

                } catch (IOException e) {
                    e.printStackTrace();
                    imageIsSet = false;
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                imageIsSet = false;
            }
        }

    }
    public static class DateBirthday extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;

            birthday.setText(new StringBuilder()
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(" "));
            birthday_picked = true;

        }
    }
    public static class DateStartWork extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;

            started_date.setText(new StringBuilder()
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(" "));
            started_picked = true;

        }
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

            validate_new_user();
            //Toast.makeText(RegisterUserActivity.this, "Datos guardado correctamente!", Toast.LENGTH_LONG).show();
            //finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void validate_new_user () {

        if (name.getText().toString().trim().isEmpty()) {
            name.setError("Nombre del usuario");
        }


        if (lastname.getText().toString().trim().isEmpty()) {
            lastname.setError("Apellidos del usuario");
        }



        if (!started_picked) {
           // started_date.requestFocus();
            started_date.setError("Fecha en que empezo a laborar");

        }
        else {
            started_date.setError(null);
        }


        if (!birthday_picked) {

            birthday.setError("Fecha de nacimiento");

        }
        else {
            birthday.setError(null);
        }

        if (!name.getText().toString().trim().isEmpty() && !name.getText().toString().trim().isEmpty()
                 && birthday_picked && started_picked) {

        }

    }

    private void save_user () {
        Toast.makeText(RegisterUserActivity.this,
                "Datos guardado correctamente!", Toast.LENGTH_LONG).show();
        clean_fields();

    }

    private void clean_fields () {

      //  private EditText name, lastname, email, phone, address, ocupation, area;
        birthday_picked = false;
        started_picked = false;
        name.setText("");
        lastname.setText("");
        email.setText("");
        phone.setText("");
        address.setText("");
        ocupation.setText("");
        area.setText("");

    }

}
