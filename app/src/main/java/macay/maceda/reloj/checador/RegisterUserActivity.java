package macay.maceda.reloj.checador;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.Model.Empleados_admin;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class RegisterUserActivity extends AppCompatActivity {

    ImageView photo;
    //private String createUsers = "CREATE TABLE users (_id integer primary key autoincrement, name,"
    //      + " lastname, birthday, email, phone, address, ocupation, area, started_date, image);";
    private EditText name, lastname, email, phone, address, ocupation, area, password;
    int blocked = 0;
    private Switch blocked_sw;
    private static TextView birthday, started_date;
    Bitmap bitmappost;
    boolean imageIsSet = false;
    private static boolean birthday_picked = false;
    private static boolean started_picked = false;
    private static int mYear, mMonth, mDay;
    private DatabaseOpenHelper dbHelper;
    private String mCurrentPhotoPath = "";
    private String path = "/sdcard/relojchecador/fotos/";
    private FloatingActionButton photo_register;
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
        blocked_sw = (Switch) findViewById(R.id.user_blocked_switch);
        blocked_sw.setText("Desbloqueado");
        blocked_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                  //  blocked_sw.setText("Only Today's");  //To change the text near to switch
                   // Log.d("You are :", "Checked");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            RegisterUserActivity.this);

                    // set title
                    alertDialogBuilder.setTitle("Bloquear usuario");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("El usuario no podra acceder")
                            .setCancelable(false)
                            .setPositiveButton("Bloquear",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    blocked_sw.setChecked(true);
                                    blocked_sw.setText("Bloqueado");
                                    blocked = 1;
                                }
                            })
                            .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    blocked_sw.setChecked(false);
                                    blocked = 0;
                                    blocked_sw.setText("Desbloqueado");
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
                else {
                //    blocked_sw.setText("All List");  //To change the text near to switch
                   // Log.d("You are :", " Not Checked");
                    blocked_sw.setText("Desbloqueado");
                    blocked = 0;
                }
            }
        });

        password = (EditText) findViewById(R.id.register_user_password_et);




        birthday = (TextView) findViewById(R.id.register_user_birthday_tv);
        started_date = (TextView) findViewById(R.id.register_user_started_date_tv);

        photo = (ImageView) findViewById(R.id.foto);
        photo_register = (FloatingActionButton) findViewById(R.id.add_photo_r);
        photo_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //image_chooser_dialog();
                File filex = new File("");

                try {
                    filex = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Toast.makeText(RegisterUserActivity.this,
                            "Error creando el archivo", Toast.LENGTH_LONG).show();
                }

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropMenuCropButtonTitle("Elegir")
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setOutputCompressQuality(75)

                        .setOutputUri(Uri.fromFile(filex))
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

        dbHelper = new DatabaseOpenHelper(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case 1: {
                if (resultCode == Activity.RESULT_OK) {
                    //	handleBigCameraPhoto();
                    //show_toast(mCurrentPhotoPath);

                    Picasso.with(this)
                            .load(new File(mCurrentPhotoPath))
                            .fit()
                            //.resize(600,6000)
                            .centerInside()
                            //.placeholder(R.drawable.agregar_imagen)
                            //.error(R.drawable.vlover)
                            //.networkPolicy(NetworkPolicy.NO_STORE)
                            //.memoryPolicy(MemoryPolicy.NO_STORE)
                            .into(photo);

                     galleryAddPic();
                }

                break;
            } // ACTION_TAKE_PHOTO_B

            case 2: {
                if (resultCode == Activity.RESULT_OK) {

                    String curFileName = data.getStringExtra("GetFileName");
                    String curPath = data.getStringExtra("GetPath");
                    //String edittext.setText(curPath+curFileName);
                    //	show_toast(curPath+curFileName);

                    //	show_toast(rfrag.getTag());

                    //rfrag.setChangeableText(data.getDataString());0
                    mCurrentPhotoPath = curPath + curFileName;
                    Picasso.with(this)
                            .load(new File(mCurrentPhotoPath))
                            .fit()
                            //.resize(600,6000)
                            .centerInside()
                            //.placeholder(R.drawable.agregar_imagen)
                            //.error(R.drawable.vlover)
                            //.networkPolicy(NetworkPolicy.NO_STORE)
                            //.memoryPolicy(MemoryPolicy.NO_STORE)
                            .into(photo);
                    // setPic();
                }
                break;
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmappost = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    photo.setImageBitmap(bitmappost);
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

        if (password.getText().toString().trim().isEmpty() ||
                password.getText().toString().trim().length() < 4 ) {
            password.setError("Agrege al menos 4 numeros");
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
                 && birthday_picked && started_picked && !password.getText().toString().trim().isEmpty()) {
          if (password.getText().toString().trim().length() < 4 ) {
              password.setError("Agrege al menos 4 numeros");

          }
          else {
              save_user();
          }

        }

    }

    private void save_user () {

        @SuppressLint("StaticFieldLeak") AsyncTask<Object, Object, Cursor> insertTask =
                new AsyncTask<Object, Object, Cursor>() {
            @Override
            protected Cursor doInBackground(Object... params) {
                Empleados_admin person = new Empleados_admin(name.getText().toString(),
                        lastname.getText().toString(), phone.getText().toString(),
                        ocupation.getText().toString(), area.getText().toString(),
                        email.getText().toString(), birthday.getText().toString(),
                        address.getText().toString(), started_date.getText().toString(),
                        mCurrentPhotoPath, blocked, password.getText().toString().trim());
                dbHelper.insertPerson(person);

                return null;
            }

            @Override
            protected void onPostExecute (Cursor result) {
                Toast.makeText(RegisterUserActivity.this,
                          "Datos guardados correctamente!", Toast.LENGTH_LONG).show();
                clean_fields();

            }

                };
        insertTask.execute();

        // Toast.makeText(RegisterUserActivity.this,
              //  "Datos guardados correctamente!", Toast.LENGTH_LONG).show();
       // clean_fields();

    }

    private void clean_fields () {

      //  private EditText name, lastname, email, phone, address, ocupation, area;
        birthday_picked = false;
        started_picked = false;
        birthday.setText("Fecha de nacimiento");
        started_date.setText("Fecha de contrato");
        name.setText("");
        lastname.setText("");
        email.setText("");
        phone.setText("");
        address.setText("");
        ocupation.setText("");
        area.setText("");
        password.setText("");
        photo.setImageBitmap(null);


    }

    private void image_chooser_dialog ()
    {
        //((UserPanelActivity)getActivity()).show_toast(a);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);
        builder.setTitle("Obtener desde...");
        //	alert.setMessage(R.string.confirmMessage);
        LayoutInflater inflater = this.getLayoutInflater();
        View vi = (inflater.inflate(R.layout.chooser_image_dialog, null));
        builder.setView(vi);


		/*
		builder.setNegativeButton("Cancelar", null).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//Toda la diversion ocurre en el custom listener :)
					((UserPanelActivity)getActivity()).show_toast("mamadas");

				}
			});
			*/


       ImageButton dialog_camera_btn = (ImageButton) vi.findViewById(R.id.photo_camera);
        ImageButton dialog_file_btn = (ImageButton) vi.findViewById(R.id.photo_file);
        //AlertDialog ad = builder.create();
        //ad.show();
        //		load_spin_movs();
        //getButton(ad.BUTTON_POSITIVE)
        //	Button theButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        //theButton.setOnClickListener(new CustomListenerPhoto(ad));

        final AlertDialog dialog = builder.create();

        dialog_camera_btn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //	((UserPanelActivity)getActivity()).show_toast("Camara");
                imageFromCamera();
                dialog.dismiss();
            }
        });

        dialog_file_btn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //	((UserPanelActivity)getActivity()).show_toast("Galeria");
                imageFromGallery();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void imageFromGallery () {
	/*
	Intent intent = new Intent();
	intent.setType("image/*");
	intent.setAction(Intent.ACTION_GET_CONTENT);
	startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
	*/

        Intent chooseFile = new Intent (this, FileChooser.class);

        chooseFile.putExtra("option", 5);
        startActivityForResult(chooseFile, 2);
    }

    private void imageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(RegisterUserActivity.this,
                          "Error creando el archivo", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + "0", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                 uri);
                 //       Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 1);
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
            Toast.makeText(RegisterUserActivity.this,
                    "Carpeta creada", Toast.LENGTH_LONG).show();


        }
        else {
            if (f.exists()) {
                //	show_toast("La carpeta ya existe");
            }
            else
                Toast.makeText(RegisterUserActivity.this,
                        "Carpeta o SD no encontrada", Toast.LENGTH_LONG).show();

        }


        return path;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

    }



}
