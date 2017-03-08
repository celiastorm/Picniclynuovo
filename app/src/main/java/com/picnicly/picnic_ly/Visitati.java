package com.picnicly.picnic_ly;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class Visitati extends AppCompatActivity {

    private RecyclerView recView;
    private MyAdapterVisitati adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitati);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView img = (ImageView)findViewById(R.id.imageView);
        img.setImageResource(R.drawable.picnic);

        recView = (RecyclerView)findViewById(R.id.rec_list2);
        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapterVisitati(DerpData.getListData(), this);
        recView.setAdapter(adapter);

    }

    public void sendMePlace(View view){ //TODO: replace with "place me" action
        Snackbar.make(view, "TANTO andoid E poca BUFFY rendono PAOLO poco INCLINE", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    public void deleteVoice(View view){ //TODO: replace with delete action
        Snackbar.make(view, "Tanto Android e poca Buffy rendono Paolo poco incline", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_visitati, menu);
        return true;
    }
}
