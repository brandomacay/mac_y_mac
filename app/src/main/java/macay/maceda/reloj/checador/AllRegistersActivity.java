package macay.maceda.reloj.checador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import macay.maceda.reloj.checador.Adapters.Person_detail_activivities;
import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;

public class AllRegistersActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseOpenHelper dbConnector;
    private Person_detail_activivities adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_registers);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_getall);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        dbConnector = new DatabaseOpenHelper(this);
        getAlllActivities("_id");
    }

    private void getAlllActivities(String filter){
        dbConnector = new DatabaseOpenHelper(this);
        adapter = new Person_detail_activivities(dbConnector.getAllActividades(filter), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }

}
