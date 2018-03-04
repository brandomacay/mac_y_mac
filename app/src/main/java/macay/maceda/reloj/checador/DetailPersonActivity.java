package macay.maceda.reloj.checador;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import macay.maceda.reloj.checador.Adapters.Person_detail_activivities;
import macay.maceda.reloj.checador.Adapters.User_detail_admin;
import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.Model.Actividades_empleados;
import macay.maceda.reloj.checador.Model.Empleados_admin;

public class DetailPersonActivity extends AppCompatActivity {
    private long receivedPersonId;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseOpenHelper dbConnector;
    private Person_detail_activivities adapter;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_person);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        dbConnector = new DatabaseOpenHelper(this);

        try {
            receivedPersonId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //toolbar.setTitle("Hola que hace");
        final Empleados_admin person = dbConnector.getPerson(receivedPersonId);
        getSupportActionBar().setTitle(""+receivedPersonId+"-"+person.getName()+" "+person.getLastname());
        toolbar.setTitleTextColor(getResources().getColor(R.color.negro));
        startviewuser("_id",receivedPersonId);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activities_person_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.edit_user:
                goToUpdateActivity(receivedPersonId);
                return true;
            case R.id.delete_person:
                deletUser();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void startviewuser(String filter,long userid){
        dbConnector = new DatabaseOpenHelper(this);
        adapter = new Person_detail_activivities(dbConnector.getActividades(filter,userid), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }

    private void goToUpdateActivity(long personId){
        Intent goToUpdate = new Intent(DetailPersonActivity.this, EditUser.class);
        goToUpdate.putExtra("USER_ID",personId);
        startActivity(goToUpdate);
    }

    private void deletUser(){
        AlertDialog.Builder myBulid = new AlertDialog.Builder(this).setCancelable(false);
        myBulid.setMessage("En verdad deseas eliminar a esta persona?");
        myBulid.setIcon(R.drawable.delete);
        myBulid.setTitle("Eliminar Usuario");
        myBulid.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(DetailPersonActivity.this);
                dbHelper.deletePerson(receivedPersonId, DetailPersonActivity.this);
                finish();
            }
        });
        myBulid.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = myBulid.create();
        dialog.show();
    }

}
