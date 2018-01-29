package macay.maceda.reloj.checador;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.Model.Empleados_admin;

public class EditUser extends AppCompatActivity {
    private EditText eName,eLastName,eEmail,ePhone,eAddress,eOccupation,eArea;
    private TextView eBirthday,eDateWork;
    private ImageView photoUser;

    private DatabaseOpenHelper dbHelper;
    private long receivedPersonId;
    private String mCurrentPhotoPath = "";
    private String path = "/sdcard/relojchecador/fotos/";
    Bitmap bitmappost;
    boolean imageIsSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        photoUser = (ImageView) findViewById(R.id.user_edit_foto);
        eName = (EditText) findViewById(R.id.nombres);
        eLastName = (EditText) findViewById(R.id.apellidos);
        eBirthday = (TextView) findViewById(R.id.fecha_nacimiento);
        eEmail = (EditText) findViewById(R.id.correo);
        ePhone = (EditText) findViewById(R.id.numero_celular);
        eAddress = (EditText) findViewById(R.id.direccion);
        eOccupation = (EditText) findViewById(R.id.cargo);
        eArea = (EditText) findViewById(R.id.area);
        eDateWork = (TextView) findViewById(R.id.trabajo);
        dbHelper = new DatabaseOpenHelper(this);

        photoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //image_chooser_dialog();
                File filex = new File("");

                try {
                    filex = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Toast.makeText(EditUser.this,
                            "Error creando el archivo", Toast.LENGTH_LONG).show();
                }

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropMenuCropButtonTitle("Elegir")
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setOutputCompressQuality(75)

                        .setOutputUri(Uri.fromFile(filex))
                        .start(EditUser.this);

            }
        });

        try {
            receivedPersonId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Empleados_admin receivedPerson = dbHelper.getPerson(receivedPersonId);
        eName.setText(receivedPerson.getName());
        eLastName.setText(receivedPerson.getLastname());
        eBirthday.setText(receivedPerson.getBirthday());
        eEmail.setText(receivedPerson.getEmail());
        ePhone.setText(receivedPerson.getNumber_phone());
        eAddress.setText(receivedPerson.getAddress());
        eOccupation.setText(receivedPerson.getOccupation());
        eArea.setText(receivedPerson.getArea());
        eDateWork.setText(receivedPerson.getDatework());
        mCurrentPhotoPath = receivedPerson.getImage();

        Picasso.with(this).load(new File(receivedPerson.getImage())).placeholder(R.mipmap.ic_launcher).into(photoUser);



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

        Empleados_admin updatedPerson = new Empleados_admin(name, lastname,cellphone,occupation,area,email, birthday,address,datawork,mCurrentPhotoPath);

        dbHelper.updatePerson(receivedPersonId, this, updatedPerson);

        finish();
    }

    private File createImageFile() throws IOException {
        mCurrentPhotoPath = getPhotoPathDirectory();
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(
        //        Environment.DIRECTORY_PICTURES);
        File storageDir = new File (mCurrentPhotoPath);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        // mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getPhotoPathDirectory () {




        File f = new File(path);
        f.getAbsolutePath();

        if(f.mkdirs()) {
            //se ha creado bien
            //string.replace(" ", "\\ ");
            Toast.makeText(EditUser.this,
                    "Carpeta creada", Toast.LENGTH_LONG).show();


        }
        else {
            if (f.exists()) {
                //	show_toast("La carpeta ya existe");
            }
            else
                Toast.makeText(EditUser.this,
                        "Carpeta o SD no encontrada", Toast.LENGTH_LONG).show();

        }


        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmappost = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    photoUser.setImageBitmap(bitmappost);
                    mCurrentPhotoPath = resultUri.getPath();
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

}
