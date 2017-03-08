package com.picnicly.picnic_ly;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.picnicly.picnic_ly.R.drawable.ic_delete_black_36dp;
import static com.picnicly.picnic_ly.R.drawable.ic_place_black_36dp;

public class Preferiti extends AppCompatActivity {

    private RecyclerView recView;
    private MyAdapterPreferiti adapter;
    private static int icon = ic_place_black_36dp;
    private static int icon2 = ic_delete_black_36dp;
    public static final String TAG = "Preferiti";
    public List <Pref> preferiti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferiti);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRootRef = database.getReference();
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final Query prefe = mRootRef.child("users").child(uid).child("pref")
                .orderByValue();
        final List<Pref> p = new ArrayList<Pref>();
        prefe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                    String nome = (String) placeSnapshot.child("name").getValue();
                    Double lat = (Double) (placeSnapshot.child("lat").getValue());
                    Double lon = (Double) (placeSnapshot.child("lon").getValue());
                    String gid = (String)(placeSnapshot.child("id").getValue());
                    Double la = lat.doubleValue();
                    Double lo = lon.doubleValue();
                    Pref pf = new Pref(gid,nome,lat,lon);
                    p.add(pf);
                    adapter.notifyItemInserted(p.size());
                    pf.setImageResId(icon);
                    pf.setImageResId2(icon2);
                    LatLng latLng = new LatLng(la, lo);
                    Log.i(TAG, "IDDDDDDDDDDDDDDDDDDDD" + pf.getId());
                    Log.i(TAG, "onChildAdded:" + la);
                    //markers.add(m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.basket)).anchor(0.0f, 1.0f).position(latLng).title(nome)));
                    //myMarks.put((m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.basket)).anchor(0.0f, 1.0f).position(latLng).title(nome))), gid);
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        ImageView img = (ImageView) findViewById(R.id.imageView);
        img.setImageResource(R.drawable.picnic2);

        recView = (RecyclerView) findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapterPreferiti(p, this);
        recView.setAdapter(adapter);
    }
    public void sendMePlace(View view){ //TODO: replace with "place me" action
        Snackbar.make(view, "TANTO android E poca BUFFY rendono PAOLO poco INCLINE", Snackbar.LENGTH_LONG)
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
        inflater.inflate(R.menu.menu_preferiti, menu);
        return true;
    }



}
