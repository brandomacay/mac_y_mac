package macay.maceda.reloj.checador;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Habilita la acccion de volver a la actividad anterior xD.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle(getString(R.string.registro_todos));
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

}
