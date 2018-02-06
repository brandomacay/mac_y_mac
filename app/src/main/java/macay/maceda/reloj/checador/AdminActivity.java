package macay.maceda.reloj.checador;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import macay.maceda.reloj.checador.Adapters.User_detail_admin;
import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.Model.Empleados_admin;

public class AdminActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseOpenHelper dbConnector;
    private User_detail_admin adapter;
    private String filter = "";
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
                    startviewuser(query);
                }

                return true;

            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                startviewuser_by_search(query.trim());
                Toast.makeText(AdminActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }


        });


        MenuItem item = menu.findItem(R.id.filterSpinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filterOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = parent.getSelectedItem().toString();
                startviewuser(filter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                startviewuser(filter);
            }
        });

        spinner.setAdapter(adapter);

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
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startviewuser(filter);

    }
}
