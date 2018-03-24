package macay.maceda.reloj.checador;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.opencsv.CSVWriter;
import android.print.PrintAttributes;

import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cv,cv_pdf,cv_cvs,comunica;
    ProgressDialog pd;
    private DatabaseOpenHelper dbHelper;
    File f;
    private static boolean datestart_picked = false;
    private static boolean dateend_picked = false;
    private static int mYear, mMonth, mDay;
    private static TextView initialdate;
    private static TextView finaldate;
    private static String initial_d, final_d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cv = (CardView) findViewById(R.id.edit_pass);
        cv.setOnClickListener(this);
        cv_pdf = (CardView) findViewById(R.id.pdf_share);
        cv_pdf.setOnClickListener(this);
        cv_cvs = (CardView) findViewById(R.id.cvs_share);
        cv_cvs.setOnClickListener(this);
        comunica = (CardView) findViewById(R.id.comunicate);
        comunica.setOnClickListener(this);
        setupActionBar();

        dbHelper = new DatabaseOpenHelper(this);


    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_pass:
                edit_password_dialog();
                break;
            case R.id.pdf_share:


                //createPdf();
               // stringtopdf("Prueba de sonido\n Joder 123");
               // exportdb();
              //  new reportTask().execute();
                create_alertDialog_report();
                //generateHtmlOnSD("prueba.html", create_html_report());


                //Toast.makeText(SettingActivity.this, "PDF CREADO!!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cvs_share:
                exportdb();
               // Toast.makeText(SettingActivity.this, "CVS CREADO!!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.comunicate:
                editarcomunicado();
                break;

        }
    }
    public void editarcomunicado(){
        LayoutInflater layoutinflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View promptUserView = layoutinflater.inflate(R.layout.alert_comunicado, null);

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptUserView);

        final EditText cominicado_a = (EditText) promptUserView.findViewById(R.id.comunicado_edit);

        alertDialogBuilder.setTitle("Comunicado para los empleados");

        String comuni = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this)
                .getString("comunicado", "");
        if (comuni.equals("")){
            cominicado_a.setText("No puedes acceder al sistema, pasa a Recursos Humanos para consultar tu situacion");
        }else{
            cominicado_a.setText(comuni);
        }
        alertDialogBuilder.setPositiveButton("Enviar",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                PreferenceManager.getDefaultSharedPreferences(SettingActivity.this)
                        .edit()
                        .putBoolean("mensaje", true)
                        .putString("comunicado", cominicado_a.getText().toString().trim())
                        .apply();

            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void edit_password_dialog () {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_admin, null);
        final EditText password = (EditText) mView.findViewById(R.id.pass);
        final EditText repeatpassword = (EditText) mView.findViewById(R.id.repeatpass);
        password.setHint("new password");
        TextView tv = (TextView) mView.findViewById(R.id.textView);
        tv.setText("Cambiar contraseña");
        Button cancel = (Button) mView.findViewById(R.id.cancel);
        Button register = (Button) mView.findViewById(R.id.login);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        register.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (!password.getText().toString().isEmpty() && !repeatpassword.getText().toString().isEmpty()) {
                    if (password.getText().toString().equals(repeatpassword.getText().toString())) {

                        PreferenceManager.getDefaultSharedPreferences(SettingActivity.this)
                                .edit()
                                .putBoolean("isPasswordSet", true)
                                .putString("password", repeatpassword.getText().toString().trim())
                                .apply();
                        Toast.makeText(SettingActivity.this,
                                "Contraseña cambiada exitosamente!",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                    } else {
                        repeatpassword.setError("Las contraseñas no coinciden");
                    }
                } else {
                    Toast.makeText(SettingActivity.this,
                            "Ingresa todos los campos",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Habilita la acccion de volver a la actividad anterior xD.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //finalizo la actividad para no hacer intent ni startactivity ni mamadas xD
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void createCSV () {
        try {
            String path = "sdcard/relojchecador/archivos/";
            String fileName = "Listaempleados";
            String content = "Alejandro, Jorge, Brandon";
            File file = new File(path + fileName +".csv");
            // if file doesnt exists, then create it
            File f = new File(path);
            f.getAbsolutePath();

            if(f.mkdirs()) {
                //se ha creado bien
                //string.replace(" ", "\\ ");
                Toast.makeText(SettingActivity.this,
                        "Carpeta creada", Toast.LENGTH_LONG).show();

            }

            //file.mkdir();
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportdb() //throws IOException
    {
        pd = new ProgressDialog(SettingActivity.this);
        pd.setTitle("Guardando DB");
        pd.setMessage("Please Wait...");
        //	pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        createcarpet();
        new saveCsv().execute();
        //new UnZipTask().execute("", "");
    }
    private void createcarpet() {

        File f = new File("/sdcard/relojchecador/archivos/");
        f.getAbsolutePath();

        if(f.mkdirs()) {
            //se ha creado bien
            //string.replace(" ", "\\ ");
            Toast.makeText(SettingActivity.this,
                    "Carpeta creada", Toast.LENGTH_LONG).show();
            //pd.dismiss();

        }
        else {
            if (f.exists()) {
                //	((MainActivity)getActivity()).show_toast("La carpeta ya existe");
                //pd.dismiss();
            }
            else

            Toast.makeText(SettingActivity.this,
                    "Carpeta o SD no encontrada", Toast.LENGTH_LONG).show();
            pd.dismiss();
        }

    }

    public class saveCsv extends AsyncTask<String, String, Cursor>
    {

        @Override
        protected Cursor doInBackground(String... params)
        {
            return dbHelper.get_all_users();
        }

        @Override
        protected void onPostExecute(Cursor result)
        {



            boolean sdDisponible = false;
            boolean sdAccesoEscritura = false;

            //Comprobamos el estado de la memoria externa (tarjeta SD)
            String estado = Environment.getExternalStorageState();

            if (estado.equals(Environment.MEDIA_MOUNTED))
            {
                sdDisponible = true;
                sdAccesoEscritura = true;
            }
            else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
            {
                sdDisponible = true;
                sdAccesoEscritura = false;
            }
            else
            {
                sdDisponible = false;
                sdAccesoEscritura = false;
            }

            //Si la memoria externa está disponible y se puede escribir
            if (sdDisponible && sdAccesoEscritura)
            {
                try
                {
                    //File ruta_sd = Environment.getExternalStorageDirectory();
                    String	date = getCurrDate();
                    String filename = "empleados_" + date;

                     f = new File("/sdcard/relojchecador/archivos/",
                            filename.toString().replace("/","")
                                    .replace(" ", "_")
                                    .replace(",", "")
                                    .replace(":", "")+".csv" );

                   // CSVWriter writer = new CSVWriter(new FileWriter(exportFile), ',');
                    CSVWriter writer = new CSVWriter(new FileWriter(f));


                    String[] columnNames = result.getColumnNames();

                   // Log.d(TAG, "column names: " + Arrays.asList(columnNames));

                    columnNames = deleteElement("password", columnNames);
                    columnNames = deleteElement("image", columnNames);


                    writer.writeNext(columnNames);
                    // Write rows
                    result.moveToFirst();
                    while (!result.isAfterLast()) {
                        final String[] fields = getFieldsAsStringArray(result);
                        writer.writeNext(fields);
                        result.moveToNext();
                    }



                    writer.close();
                    Toast.makeText(SettingActivity.this, "EXITO: Archivo CSV creado", Toast.LENGTH_SHORT).show();


                }
                catch (Exception ex)
                {
                    Toast.makeText(SettingActivity.this, "Error al escribir fichero en la tarjeta SD", Toast.LENGTH_SHORT).show();
                    //	Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
                }
            }

            //	lv.setAdapter(adapter);
            dbHelper.close();
            result.close();

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(SettingActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(SettingActivity.this);
            }
            builder.setTitle("Compartir")
                    .setCancelable(false)
                    .setMessage("Compartir o guardar el archivo")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            //sharingIntent.setType("text/comma_separated_values/csv");
                            sharingIntent.setType("application/octet-stream");
                            //sharingIntent.setData(csv);
                            //sharingIntent.putExtra(Intent.EXTRA_STREAM, csv);
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" +
                                    f.getAbsolutePath().toString()) );
                            startActivity(Intent.createChooser(sharingIntent, ""));
                            //startActivity(Intent.createChooser(sharingIntent, getResources().getText(R.string.send_to)));

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }


    }

    public String getCurrDate() {
        String dt;
        Date cal = Calendar.getInstance().getTime();
        dt = cal.toLocaleString();
        return dt;
    }
    private String[] getFieldsAsStringArray(Cursor cursor) {
        int columnCount = cursor.getColumnCount();
        //I remove 2 items, image and password.
        columnCount = (columnCount - 2);
        final String[] result = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {

            // cursor = 11 is the column password, 10 = image so i must delete that one
            if (i < 10) {
                result[i] = cursor.getString(i);
            }
        }
        return result;
    }
    private static String[] deleteElement(String stringToDelete, String[] array) {
        String[] result = new String[array.length];
        int index = 0;

        ArrayList<String> rm = new ArrayList<String>();

        for(int i = 0; i < array.length; i++) {
            rm.add(array[i]);
        }
        for(int i = 0; i < array.length; i++) {
            if(array[i].equals(stringToDelete)) {
                index = i;
            }
        }
        rm.remove(index);

        result = rm.toArray(new String[rm.size()]);

        return result;
    }


    private void createPdf(){
        // create a new document
        PdfDocument document = new PdfDocument();

        // * PageInfo pageInfo = new PageInfo.Builder(new Rect(0, 0, 100, 100), 1).create();

        // crate a page description
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(595, 842, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        View content = this.findViewById(R.id.edit_pass);
        Canvas can = new Canvas();
        can.setDensity(240);

        can = page.getCanvas();

        content.draw(can);

        Paint paint = new Paint();
        // canvas.drawPaint(paint);
        //paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        can.drawText("mamadas", 20, 200, paint);

        paint.setTextSize(16);

        can.drawText("Joder, esto es muy trabajoso :(",
                20 , 250, paint);

        can.drawLine(0, 117, 596, 117, paint);
        can.drawLine(0, 115, 596, 115, paint);
        can.drawLine(325, 400, 325, 600, paint);
        //can.drawRect(120, 180, 400, 400, paint);

       // can.drawRect(0, 50, 100, 100, paint);



        // finish the page
        document.finishPage(page);


        // write the document content
        String targetPdf = "/sdcard/relojchecador/archivos/reporte.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }

    public void stringtopdf(String data)  {
        String extstoragedir = "/sdcard/relojchecador/archivos/";
        File fol = new File(extstoragedir);
        File folder=new File(fol,"pdf");
        if(!folder.exists()) {
            boolean bool = folder.mkdir();
        }
        try {
            final File file = new File(folder, "sample.pdf");
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);


            PdfDocument document = new PdfDocument();


            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(400, 400, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
           // canvas.drawPaint(paint);
            //paint.setColor(Color.BLACK);
            paint.setTextSize(16);
            canvas.drawText(data, 10, 10, paint);

            paint.setTextSize(12);

            canvas.drawText("Joder, esto es muy trabajoso :(",
                    10 , 30, paint);




            document.finishPage(page);
            document.writeTo(fOut);
            document.close();

        }catch (IOException e){
            Log.i("error",e.getLocalizedMessage());
        }
    }


    public void generateHtmlOnSD(String sFileName, StringBuilder sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "/relojchecador/archivos/");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(SettingActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder create_html_report () {

        StringBuilder sb = new StringBuilder();


       sb.append("<!DOCTYPE html> \n");
    sb.append("<html>");
    sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"> ");
    sb.append("<head>");
    sb.append("<style>");
    sb.append("table {");
    sb.append("width:100%;");
    sb.append("}");
    sb.append("table, th, td {");
    sb.append("border: 1px solid black;");
        sb.append("border-collapse: collapse;");
        sb.append("}");
        sb.append(" th, td {");
        sb.append("padding: 5px;");
        sb.append("text-align: left;");
        sb.append(" }");
        sb.append("table#t01 tr:nth-child(even) {");
        sb.append("background-color: #eee;");
        sb.append("}");
        sb.append("table#t01 tr:nth-child(odd) {");
        sb.append("background-color:#fff;");
        sb.append("}");
        sb.append("table#t01 th {");
        sb.append("background-color: black;");
        sb.append("color: white;");
        sb.append("}");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append(" ");
        sb.append("<table id=\"t01\">");
        sb.append("<tr>");
        sb.append("<th>Nombre: </th>");
        sb.append("<th>Alejandro Maceda </th>");
        sb.append("<th>Cargo: El mas mera verga </th>");
        sb.append("<th> </th>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th>Entrada</th>");
        sb.append("<th>Salida</th>");
        sb.append("<th>Salida comida</th>");
        sb.append("<th>Llegada comida</th>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>12-04-2018\n4:06:56 </td>");
        sb.append("<td>12-04-2018\n10:06:56 </td>");
        sb.append("<td>12-04-2018\n6:06:56 </td>");
        sb.append("<td>12-04-2018\n7:06:56 </td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>13-04-2018\n4:06:56 </td>");
        sb.append("<td>13-04-2018\n10:06:56 </td>");
        sb.append("<td>13-04-2018\n6:06:56 </td>");
        sb.append("<td>13-04-2018\n7:06:56 </td>");
        sb.append("</tr> ");
        sb.append("</table> ");
        sb.append("</body> ");
        sb.append("</html> ");


        sb.append(" ");
        sb.append(" ");





/*










  <tr>
    <td>John</td>
    <td>Doe</td>
    <td>80</td>
  </tr>
</table>
<br>

<table id="t01">
  <tr>
    <th>Firstname</th>
    <th>Lastname</th>
    <th>Age</th>
  </tr>
  <tr>
    <td>Jill</td>
    <td>Smith</td>
    <td>50</td>
  </tr>
  <tr>
    <td>Eve</td>
    <td>Jackson</td>
    <td>94</td>
  </tr>
  <tr>
    <td>John</td>
    <td>Doe</td>
    <td>80</td>
  </tr>




*/



        return sb;
    }

    public class reportTask extends AsyncTask<String, String, Cursor> {

        @Override
        protected Cursor doInBackground(String... params) {
            return dbHelper.get_all_users_report();
        }

        @Override
        protected void onPostExecute(Cursor result) {

            if (result.moveToFirst()) {

                StringBuilder sb = new StringBuilder();


                sb.append("<!DOCTYPE html> \n");
                sb.append("<html>");
                sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"> ");
                sb.append("<head>");
                sb.append("<style>");
                sb.append("table {");
                sb.append("width:100%;");
                sb.append("}");
                sb.append("table, th, td {");
                sb.append("border: 1px solid black;");
                sb.append("border-collapse: collapse;");
                sb.append("}");
                sb.append(" th, td {");
                sb.append("padding: 5px;");
                sb.append("text-align: left;");
                sb.append(" }");
                sb.append("table#t01 tr:nth-child(even) {");
                sb.append("background-color: #eee;");
                sb.append("}");
                sb.append("table#t01 tr:nth-child(odd) {");
                sb.append("background-color:#fff;");
                sb.append("}");
                sb.append("table#t01 th {");
                sb.append("background-color: black;");
                sb.append("color: white;");
                sb.append("}");
                sb.append("</style>");
                sb.append("</head>");
                sb.append("<body>");
                sb.append(" ");
                do {
                    sb.append("<table id=\"t01\">");
                    sb.append("<tr>");
                    sb.append("<th>Nombre: </th>");
                    sb.append("<th>" + result.getString(result.getColumnIndex("name"))
                            + " " + result.getString(result.getColumnIndex("lastname")) + "</th>");
                    sb.append("<th>Cargo: " + result.getString(result.getColumnIndex("occupation")) + "</th>");
                    sb.append("<th> </th>");
                    sb.append("</tr>");
                    sb.append("<tr>");
                    sb.append("<th>Entrada</th>");
                    sb.append("<th>Salida</th>");
                    sb.append("<th>Salida comida</th>");
                    sb.append("<th>Llegada comida</th>");
                    sb.append("</tr>");
                    Cursor mc = dbHelper.user_activity_from_date(result.getString(result.getColumnIndex("_id")),
                            initial_d, final_d);
                    //Toast.makeText(SettingActivity.this, initial_d, Toast.LENGTH_SHORT).show();

//                    Toast.makeText(SettingActivity.this, final_d, Toast.LENGTH_SHORT).show();



                    if (mc.moveToFirst()) {
                       // Toast.makeText(SettingActivity.this, "inside movetofirst", Toast.LENGTH_SHORT).show();

                        do {
                            sb.append("<tr>");
                            sb.append("<td>" + mc.getString(mc.getColumnIndex("workin")) + "</td>");
                            sb.append("<td>" + mc.getString(mc.getColumnIndex("workout")) + "</td>");
                            sb.append("<td>" + mc.getString(mc.getColumnIndex("breakin")) + "</td>");
                            sb.append("<td>" + mc.getString(mc.getColumnIndex("breakout")) + "</td>");
                            sb.append("</tr>");
                            //Toast.makeText(SettingActivity.this, "registro encontrado", Toast.LENGTH_SHORT).show();


                        } while (mc.moveToNext());
                    }
                    else {
                        sb.append("<td> No hay registros </td>");
                    }



                    sb.append("<br/>");
                   /*
                    Toast.makeText(SettingActivity.this,
                            result.getString(result.getColumnIndex("name")) + " " +
                                    result.getString(result.getColumnIndex("lastname")),
                                     Toast.LENGTH_LONG).show();
                    */

                } while (result.moveToNext());

                sb.append("</table> ");
                sb.append("</body> ");
                sb.append("</html> ");

                String sFileName = "prueba.html";

                try {
                    File root = new File(Environment.getExternalStorageDirectory(), "/relojchecador/archivos/");
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File gpxfile = new File(root, sFileName);
                    FileWriter writer = new FileWriter(gpxfile);
                    writer.append(sb);
                    writer.flush();
                    writer.close();
                    Uri uri = Uri.fromFile(gpxfile);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                   // browserIntent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                //    browserIntent.setDataAndType(uri, "text/html");
                    browserIntent.setDataAndType(uri, "multipart/related");

                    //  browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                    startActivity(browserIntent);
                    Toast.makeText(SettingActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(SettingActivity.this, "Aun no ha registrado usuarios", Toast.LENGTH_SHORT).show();

            }

        }

    }


    private void create_alertDialog_report () {

        datestart_picked = false;
        dateend_picked = false;


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_report, null);
        initialdate = (TextView) mView.findViewById(R.id.report_initialdate_tv);
       finaldate = (TextView) mView.findViewById(R.id.report_finaldate_tv);
        //TextView tv = (TextView) mView.findViewById(R.id.textView);
        //  tv.setVisibility(View.GONE);
        //tv.setText("Crear una contraseña");
        Button cancel = (Button) mView.findViewById(R.id.cancel);
        Button register = (Button) mView.findViewById(R.id.login);

        initialdate.setText("Fecha inicial");
        initialdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newfrag = new DateStart();
                newfrag.show(getFragmentManager(), "datePicker");
            }
        });
        finaldate.setText("Fecha final");
        finaldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newfrag = new DateEnd();
                newfrag.show(getFragmentManager(), "datePicker");
            }
        });

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

                if (datestart_picked && dateend_picked) {
                    //dothething
                    if (finaldate.getText().toString().compareTo(initialdate.getText().toString()) > 0 ||
                            finaldate.getText().toString().equals(initialdate.getText().toString())) {
                       // DotheThing
                        dialog.dismiss();
                        new reportTask().execute();

                    }
                    else{


                        AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
                        alert.setTitle("Fecha erronea");
                        alert.setMessage("La fecha final no debe ser menor a la inicial");
                        alert.setPositiveButton("Aceptar", null);
                        alert.show();


                    }

                }
                else {
                    Toast.makeText(SettingActivity.this, "Seleccione un rango valido", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }


    public static class DateStart extends DialogFragment implements
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
            DecimalFormat mFormat = new DecimalFormat("00");


            initialdate.setText(new StringBuilder()
                    .append(mYear).append("-")
                    .append(mFormat.format(mMonth + 1)).append("-")
                    .append(mFormat.format(mDay)));
            datestart_picked = true;
            initial_d = initialdate.getText().toString();

        }
    }

    public static class DateEnd extends DialogFragment implements
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
            DecimalFormat mFormat = new DecimalFormat("00");

            finaldate.setText(new StringBuilder()
                    .append(mYear).append("-")
                    .append(mFormat.format(mMonth + 1)).append("-")
                    .append(mFormat.format(mDay)));
            dateend_picked = true;
            final_d = finaldate.getText().toString();

        }
    }

}
