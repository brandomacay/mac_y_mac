package macay.maceda.reloj.checador;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import macay.maceda.reloj.checador.Adapters.User_detail_admin;
import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.Model.Empleados_admin;

public class AdminActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseOpenHelper dbConnector;
    private User_detail_admin adapter;
    String filter = "started_date";
    private android.support.v7.widget.SearchView searchView = null;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.view_recycler);
        setSupportActionBar(toolbar);
         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,RegisterUserActivity.class));
                overridePendingTransition(0,0);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                try {
                    if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                        fab.hide();
                    } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                        fab.show();
                    } else {
                        //Toast.makeText(AdminActivity.this, "error detectado en el fab", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });


    }
    private void startviewuser(String filter){
        dbConnector = new DatabaseOpenHelper(this);
        adapter = new User_detail_admin(dbConnector.peopleList(filter), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }

    private void startviewuser_by_search(String filterx) {

        dbConnector = new DatabaseOpenHelper(this);
        adapter = new User_detail_admin(dbConnector.peopleList_by_search(filterx), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        // SearchManager searchManager = (SearchManager) AdminActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            //  assert searchManager != null;
            //   searchView.setSearchableInfo(searchManager.getSearchableInfo(AdminActivity.this.getComponentName()));
            searchView.setIconified(false);
        }

        //
        //SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final android.support.v7.widget.SearchView search = (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search).getActionView();

        //  search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String query) {

                //loadHistory(query);
                if (query.isEmpty()) {
                    startviewuser(filter);

                }else{
                    startviewuser_by_search(query.trim());
                }

                return true;

            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                startviewuser_by_search(query.trim());
                return false;
            }


        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.setting) {
            startActivity(new Intent(AdminActivity.this,SettingActivity.class));
            overridePendingTransition(0,0);
            return true;
        }
        if (id == R.id.acerca){
            acerca_de_macymax();
            return true;
        }
        if (id == R.id.timeline){
            startActivity(new Intent(AdminActivity.this,AllRegistersActivity.class));
            overridePendingTransition(0,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void acerca_de_macymax(){

        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(AdminActivity.this);
        //Button yes = (Button) mView.findViewById(R.id.si);
        //Button no = (Button) mView.findViewById(R.id.no);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        final android.app.AlertDialog dialog = mBuilder.create();
        dialog.setTitle("Creditos Macymax");
        dialog.setMessage("Creado por: " +
                "                                       Alejandro Maceda"+
                "                                       Brandon Macay" +
                "                                       Mexico-Ecuador");

        dialog.show();

        //
    }



    @Override
    protected void onResume() {
        super.onResume();
        startviewuser(filter);

    }
}
