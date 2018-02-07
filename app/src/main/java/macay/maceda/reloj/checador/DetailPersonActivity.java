package macay.maceda.reloj.checador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import macay.maceda.reloj.checador.Adapters.Person_detail_activivities;
import macay.maceda.reloj.checador.Adapters.User_detail_admin;
import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.Model.Actividades_empleados;

public class DetailPersonActivity extends AppCompatActivity {
    private long receivedPersonId;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseOpenHelper dbConnector;
    private Person_detail_activivities adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_person);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        try {
            receivedPersonId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        startviewuser("_id",receivedPersonId);

    }
    private void startviewuser(String filter,long userid){
        dbConnector = new DatabaseOpenHelper(this);
        adapter = new Person_detail_activivities(dbConnector.getActividades(filter,userid), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }


}
