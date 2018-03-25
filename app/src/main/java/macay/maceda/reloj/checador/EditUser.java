package macay.maceda.reloj.checador;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
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
    int blocked =0;
    private Switch blocked_sw;
    private TextView eBirthday,eDateWork, ePassword;
    private ImageView photoUser;
    private FloatingActionButton fab_add;
    private DatabaseOpenHelper dbHelper;
    private long receivedPersonId;
    private String mCurrentPhotoPath = "";
    private String path = "/sdcard/relojchecador/fotos/";
    Bitmap bitmappost;
    boolean imageIsSet = false;
    private int screenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        photoUser = (ImageView) findViewById(R.id.user_edit_foto);
        fab_add = (FloatingActionButton) findViewById(R.id.add_photo);
        eName = (EditText) findViewById(R.id.nombres);
        eLastName = (EditText) findViewById(R.id.apellidos);
        eBirthday = (TextView) findViewById(R.id.fecha_nacimiento);
        eEmail = (EditText) findViewById(R.id.correo);
        ePhone = (EditText) findViewById(R.id.numero_celular);
        eAddress = (EditText) findViewById(R.id.direccion);
        eOccupation = (EditText) findViewById(R.id.cargo);
        eArea = (EditText) findViewById(R.id.area);
        eDateWork = (TextView) findViewById(R.id.trabajo);
        blocked_sw = (Switch) findViewById(R.id.user_blocked_switch);
        blocked_sw.setChecked(false);
        ePassword = (EditText) findViewById(R.id.password);


        dbHelper = new DatabaseOpenHelper(this);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //image_chooser_dialog();
                File filex = new File("");

                try {
                    filex = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Toast.makeText(EditUser.this,
                            getString(R.string.error_archivo), Toast.LENGTH_LONG).show();
                }

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropMenuCropButtonTitle(getString(R.string.elegir))
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
        final Empleados_admin receivedPerson = dbHelper.getPerson(receivedPersonId);
        eName.setText(receivedPerson.getName());
        eLastName.setText(receivedPerson.getLastname());
        eBirthday.setText(receivedPerson.getBirthday());
        eEmail.setText(receivedPerson.getEmail());
        ePhone.setText(receivedPerson.getNumber_phone());
        eAddress.setText(receivedPerson.getAddress());
        eOccupation.setText(receivedPerson.getOccupation());
        eArea.setText(receivedPerson.getArea());
        eDateWork.setText(receivedPerson.getDatework());
        blocked = receivedPerson.getBlocked();
        if (blocked == 0) {
           // blocked_sw.setChecked(false);
            blocked_sw.setText(getString(R.string.desbloqueado));
        }
        if (blocked == 1){
            dialogo_este_men_esta_bloqueado();
           // blocked_sw.setChecked(true);
        }
        blocked_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        if (blocked == 1){
                            blocked_sw.setChecked(true);
                        }else{
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    EditUser.this);
                            alertDialogBuilder.setTitle(getString(R.string.bloquear_persona));
                            alertDialogBuilder
                                    .setMessage(getString(R.string.no_acceso))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.bloquear),new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // if this button is clicked, close
                                            // current activity
                                            blocked_sw.setChecked(true);
                                            blocked_sw.setText("Bloqueado");
                                            blocked = 1;
                                        }
                                    })
                                    .setNegativeButton(getString(R.string.cancelar),new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            blocked_sw.setChecked(false);
                                            blocked_sw.setText(getString(R.string.desbloqueado));
                                            blocked = 0;
                                        }
                                    });

                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                        }

                    }
                    else {
                        //    blocked_sw.setText("All List");  //To change the text near to switch
                        // Log.d("You are :", " Not Checked");
                        blocked = 0;
                        blocked_sw.setText(getString(R.string.desbloqueado));
                    }


            }
        });
        ePassword.setText(receivedPerson.getPassword());
        mCurrentPhotoPath = receivedPerson.getImage();


        int height;
        if (this.receivedPersonId == 1 || receivedPerson.getImage() == null) {
            height = 700;
        } else {
            height = 600;
        }
        Picasso.with(this)
                .load(new File(receivedPerson.getImage()))
                .resize(screenWidth / 2, height)
                .placeholder(R.drawable.persona)
                .into(photoUser);

    }

    public void dialogo_este_men_esta_bloqueado(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                EditUser.this);
        alertDialogBuilder.setTitle(getString(R.string.persona_bloqueada));
        alertDialogBuilder
                .setMessage(getString(R.string.desea_desblo))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.si),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        blocked_sw.setChecked(false);
                        blocked_sw.setText(getString(R.string.desbloqueado));
                        blocked = 0;
                    }
                })
                .setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        blocked_sw.setChecked(true);
                        blocked = 1;
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
        String passwd = ePassword.getText().toString().trim();



        if(name.isEmpty()){
            Toast.makeText(this, getString(R.string.ingresar_nombre), Toast.LENGTH_SHORT).show();
        }

        if(lastname.isEmpty()){
            Toast.makeText(this, getString(R.string.ingresar_apellido), Toast.LENGTH_SHORT).show();
        }


        if (passwd.isEmpty() ||
                passwd.length() < 4 ) {
            ePassword.setError(getString(R.string.agregue_4_numeros));
        }

        if (!name.isEmpty() && !lastname.isEmpty()
                 && !passwd.isEmpty()) {

            if (passwd.length() < 4 ) {
                ePassword.setError(getString(R.string.agregue_4_numeros));

            }
            else {
                Empleados_admin updatedPerson = new Empleados_admin(name, lastname,cellphone,occupation,area,email, birthday,address,datawork,mCurrentPhotoPath, blocked, passwd);

                dbHelper.updatePerson(receivedPersonId, this, updatedPerson);

                finish();
            }

        }


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
                "0",         /* suffix */
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
                    getString(R.string.carpeta_creada), Toast.LENGTH_LONG).show();


        }
        else {
            if (f.exists()) {
                //	show_toast("La carpeta ya existe");
            }
            else
                Toast.makeText(EditUser.this,
                        getString(R.string.carpeta_no_creada), Toast.LENGTH_LONG).show();

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
