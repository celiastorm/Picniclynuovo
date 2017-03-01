package com.picnicly.picnic_ly;


import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;



public class HelpActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void Faq(View view){
        Intent FaqActivity = new Intent(HelpActivity.this,FaqActivity.class);
        startActivity(FaqActivity);

    }

    public void Guida(View view){
        Intent GuidaActivity = new Intent(HelpActivity.this,GuidaActivity.class);
        startActivity(GuidaActivity);
    }

    public void Credits(View view){
        Intent CreditsActivity = new Intent(HelpActivity.this,CreditsActivity.class);
        startActivity(CreditsActivity);
    }

    public void Contatti(View view){
        Intent ContattiActivity = new Intent(HelpActivity.this,ContattiActivity.class);
        startActivity(ContattiActivity);
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
        inflater.inflate(R.menu.activity_help, menu);
        return true;
    }
}
