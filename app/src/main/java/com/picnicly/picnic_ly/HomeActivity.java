package com.picnicly.picnic_ly;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Address;
import android.location.Geocoder;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import android.net.Uri;
import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import static android.graphics.Color.WHITE;


public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMarkerClickListener {
    GoogleMap m;
    Marker marker;
    Marker prevmark;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private SupportMapFragment map;
    public static final String TAG = "HomeActivity";
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private GoogleApiClient mGoogleApiClient;
    private SlidingUpPanelLayout mLayout;
    HashMap<Marker, String> myMarks = new HashMap<Marker, String>();
    HashMap<String, String> myIndex = new HashMap<String, String>();
    HashMap<String, Luogo> myPlaces = new HashMap<String, Luogo>();
    HashMap<String, Float> myRates = new HashMap<String, Float>();
    List<String> votedId = new ArrayList<String>();
    Rate token = new Rate();
    private SmoothActionBarDrawerToggle drawerTogg;
    private DatabaseReference mDatabase;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        PrefManager prefManager = new PrefManager(getApplicationContext());
        // make first time launch TRUE
        //prefManager.setFirstTimeLaunch(true);
        if(prefManager.isFirstTimeLaunch()) {
            //prefManager.setFirstTimeLaunch(false);
            startActivity(new Intent(HomeActivity.this, WelcSlider.class));
            finish();
        }

        setContentView(R.layout.activity_home);



        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        /*map.getMapAsync(this);*/
        initializeMap();



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mGoogleApiClient = ((MyApplication) getApplication()).getGoogleApiClient(HomeActivity.this, this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            Bitmap bm = getCircleBitmap(getBitmapFromURL(photoUrl.toString()));


            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View hView =  navigationView.getHeaderView(0);
            TextView nav_user = (TextView)hView.findViewById(R.id.user);
            TextView nav_mail = (TextView)hView.findViewById(R.id.mail);
            ImageView nav_pic = (ImageView)hView.findViewById(R.id.imageView);
            nav_user.setText(name);
            nav_mail.setText(email);
            nav_pic.setImageBitmap(bm);
            //Picasso.with(HomeActivity.this).load(photoUrl).into(nav_pic);

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerTogg = new SmoothActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerTogg);
        drawerTogg.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        android.app.FragmentManager fm = getFragmentManager();

        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomeActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });

        List<String> your_array_list = Arrays.asList();

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list ){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

            /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(WHITE);

                return view;
            }
        };

        lv.setAdapter(arrayAdapter);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        //mLayout.findViewById(R.id.dragView).setVisibility(View.INVISIBLE);
        mLayout.setPanelHeight(0);



        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                if(slideOffset==0.0){
                    FrameLayout img = (FrameLayout) findViewById(R.id.frm);
                    //mLayout.findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                    img.setVisibility(View.GONE);
                    mLayout.setPanelHeight(150);
                }
                else {
                    FrameLayout img = (FrameLayout) findViewById(R.id.frm);
                    //mLayout.findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                    img.setVisibility(View.VISIBLE);
                    mLayout.setPanelHeight(150);
                }

            }


            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                    //mLayout.findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                if(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED){
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }

            }
        });

        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

            }
        });



        //Togliere apparizione tastiera all'inizio e fare in modo che appaia quando si clicca sull'edittext
        final EditText editText = (EditText) findViewById(R.id.editText);
        editText.setInputType(InputType.TYPE_NULL);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do this
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    //Toast.makeText(HelloFormStuff.this, editText.getText(), Toast.LENGTH_SHORT).show();
                    onMapSearch(editText);
                    return true;
                }
                return false;
            }
        });

        DrawerLayout touchInterceptor = (DrawerLayout) findViewById(R.id.drawer_layout);
        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (editText.isFocused()) {
                        Rect outRect = new Rect();
                        editText.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                            editText.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }

                }

                return false;
            }
        });
        touchInterceptor.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                editText.clearFocus();
                InputMethodManager imm = (InputMethodManager) drawerView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
                mLayout.setPanelHeight(0);
            }
            @Override
            public void onDrawerClosed(View drawerView){
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

            }
        });
        Intent intent = getIntent();
        if(intent.getStringExtra("activity")!=null) {
            if (intent.getStringExtra("activity").equals("main")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        HomeActivity.this);
                builder.setTitle("RISPETTA L'AMBIENTE");
                builder.setMessage("Ricordati di non abbandonare rifiuti, impegnandoti sempre a mantenere pulito l'ambiente che ti circonda.");
                builder.setNeutralButton("Ok, ricevuto!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Toast.makeText(getApplicationContext(), "OK is clicked", Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();
            }
        }

    }



    private void initializeMap() {
        if (m == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        //DATABASE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRootRef = database.getReference();

        m = map;
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new
                LatLng(49.39, -124.83), 20));

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(this);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                final EditText editText = (EditText) findViewById(R.id.editText);
                editText.clearFocus();
                hideSoftKeyboard(HomeActivity.this);

            }
        });

        //To setup location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //To request location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);



        final Query puntiPicnic = mRootRef.child("d").child("__count")
                .orderByValue();

        puntiPicnic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot placeSnapshot: dataSnapshot.getChildren()) {
                    String num = String.valueOf(placeSnapshot.getKey());
                    String nome = (String) placeSnapshot.child("Nome").getValue();
                    Long lat = (Long)(placeSnapshot.child("Latitudine").getValue());
                    Long lon = (Long)(placeSnapshot.child("Longitudine").getValue());
                    String gid = String.valueOf(placeSnapshot.child("gid").getValue());
                    Long s = ((placeSnapshot.child("Voto").getValue(Long.class)));
                    Long n = ((placeSnapshot.child("Nvoti").getValue(Long.class)));
                    float somma = s.floatValue();
                    int nvoti = n.intValue();
                    Luogo place = new Luogo(gid,num,somma,nvoti);
                    myPlaces.put(gid,place);
                    //mRootRef.child("d").child("__count").child(gid);
                    Double la = lat.doubleValue();
                    Double lo = lon.doubleValue();

                    if(la/1000000 >= 4d && la/1000000 <= 5d){
                        la = la/100000;
                    }
                    else if(la/10000000 >= 4d && la/10000000 <= 5d){
                        la = la/1000000;
                    }
                    else if(la/100000000 >= 4d && la/100000000 <= 5d){
                        la = la/10000000;
                    }
                    else {
                        la = la / 1000000000;
                        la = la / 10000;
                    }

                    if(lo/1000000 >= 1d && lo/1000000 <= 2d){
                        lo = lo/100000;
                    }
                    else if(lo/10000000 >= 1d && lo/10000000 <= 2d){
                        lo = lo/1000000;
                    }
                    else if(lo/100000000 >= 1d && lo/100000000 <= 2d){
                        lo = lo/10000000;
                    }
                    else {
                        lo = lo / 1000000000;
                        lo = lo / 10000;
                    }

                    LatLng latLng = new LatLng(la, lo);
                    Log.i(TAG, "onChildAdded:" + nome);
                    Log.i(TAG, "onChildAdded:" + la);
                    myMarks.put((m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.bskt)).anchor(0.0f, 1.0f).position(latLng).title(nome))),gid);
                    myIndex.put(gid,num);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        final Query puntiCamp = mRootRef.child("d").child("camp")
                .orderByValue();

        puntiCamp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot placeSnapshot: dataSnapshot.getChildren()) {
                    String num = String.valueOf(placeSnapshot.getKey());
                    String nome = (String) placeSnapshot.child("Nome").getValue();
                    Long lat = (Long)(placeSnapshot.child("Latitudine").getValue());
                    Long lon = (Long)(placeSnapshot.child("Longitudine").getValue());
                    String gid = String.valueOf(placeSnapshot.child("gid").getValue());
                    Long s = ((placeSnapshot.child("Voto").getValue(Long.class)));
                    Long n = ((placeSnapshot.child("Nvoti").getValue(Long.class)));
                    float somma = s.floatValue();
                    int nvoti = n.intValue();
                    Luogo place = new Luogo(gid,num,somma,nvoti);
                    myPlaces.put(gid,place);
                    Double la = lat.doubleValue();
                    Double lo = lon.doubleValue();

                    if(la/1000000 >= 4d && la/1000000 <= 5d){
                        la = la/100000;
                    }
                    else if(la/10000000 >= 4d && la/10000000 <= 5d){
                        la = la/1000000;
                    }
                    else if(la/100000000 >= 4d && la/100000000 <= 5d){
                        la = la/10000000;
                    }
                    else {
                        la = la / 1000000000;
                        la = la / 10000;
                    }

                    if(lo/1000000 >= 1d && lo/1000000 <= 2d){
                        lo = lo/100000;
                    }
                    else if(lo/10000000 >= 1d && lo/10000000 <= 2d){
                        lo = lo/1000000;
                    }
                    else if(lo/100000000 >= 1d && lo/100000000 <= 2d){
                        lo = lo/10000000;
                    }
                    else {
                        lo = lo / 1000000000;
                        lo = lo / 10000;
                    }



                    LatLng latLng = new LatLng(la, lo);
                    Log.i(TAG, "onChildAdded:" + nome);
                    Log.i(TAG, "onChildAdded:" + la);
                    //markers.add(m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.tent)).anchor(0.0f, 1.0f).position(latLng).title(nome)));
                    myMarks.put((m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.tent)).anchor(0.0f, 1.0f).position(latLng).title(nome))),gid);
                    myIndex.put(gid,num);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onMarkerClick(final Marker mark) {

        if(myMarks.containsKey(mark))
        {
            final String gd = myMarks.get(mark);
            database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference();
            if(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN){
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            mLayout.findViewById(R.id.dragView).setVisibility(View.VISIBLE);
            mLayout.setPanelHeight(150);
            final String address = mark.getTitle()+"\n"+getCompleteAddressString(mark.getPosition().latitude, mark.getPosition().longitude);
            final Double lat = mark.getPosition().latitude;
            final Double lon = mark.getPosition().longitude;
            //mLayout.findViewById(R.id.panel).setVisibility(View.VISIBLE);
            TextView t = (TextView) findViewById(R.id.name);
            t.setText(address);

            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final RatingBar ratingBar;
            ratingBar = (RatingBar) findViewById(R.id.stars);
            Luogo place = myPlaces.get(gd);
            float voto;
            float somma = place.getVoto();
            int nvoti = place.getNvoti();
            final Rate ratings = new Rate(somma, nvoti);
            TextView testo = (TextView) findViewById(R.id.numero);
            float sum = ratings.getVoto();
            int numero = ratings.getNvoti();

            if (numero == 1) {
                testo.setText("1 utente ha valutato questo luogo.");
            } else if (numero > 1) {
                testo.setText("" + numero + " utenti hanno valutato questo luogo.");
            } else {
                testo.setText("Nessuna valutazione presente. Sii il primo a votare!");
            }

            if (numero != 0) {
                voto = sum / numero;
                ratingBar.setRating(voto);
            } else {
                ratingBar.setRating(0);
            }


                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(final RatingBar rating, float v, boolean b) {


                    final Query voted = mDatabase.child("users").child(uid).child("voted")
                            .orderByValue();

                    voted.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            votedId.clear();
                            myRates.clear();
                            for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                                String gid = (String) (placeSnapshot.getKey());
                                Long st = ((placeSnapshot.child("stars").getValue(Long.class)));
                                float stars = st.floatValue();
                                Log.i("POSTO VOTATO", ""+gid+" "+stars);
                                votedId.add(gid);
                                myRates.put(gid, stars);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                            // ...
                        }
                    });
                    if(b) {
                        if (votedId.contains(gd)) {
                            mDatabase.child("users").child(uid).child("voted").child(gd).removeValue();
                            final String num = myIndex.get(gd);
                            int ind = Integer.parseInt(num);
                            float stars = myRates.get(gd);
                            Log.i("GIA VOTATOOOOO", "" + stars);
                            if (Integer.parseInt(gd) > 394) {
                                Luogo place = myPlaces.get(gd);
                                float voto;
                                float somma = place.getVoto();
                                int nvoti = place.getNvoti();
                                //final Rate rate = new Rate(somma, nvoti);
                                somma = somma - stars;
                                nvoti--;
                                ratings.setVoto(somma);
                                ratings.setNvoti(nvoti);
                                token.setNvoti(nvoti);
                                token.setVoto(-stars);
                                mDatabase.child("d").child("__count").child(num).child("Voto").setValue(somma);
                                mDatabase.child("d").child("__count").child(num).child("Nvoti").setValue(nvoti);
                            /*if (nvoti != 0) {
                                voto = somma / nvoti;
                                ratingBar.setRating(voto);

                            }*/
                                TextView testo = (TextView) findViewById(R.id.numero);
                                if (nvoti != 0) {
                                    voto = somma / nvoti;
                                    ratingBar.setRating(voto);
                                    if (nvoti == 1) {
                                        testo.setText("1 utente ha valutato questo luogo.");
                                    } else if (nvoti > 1) {
                                        testo.setText("" + nvoti + " utenti hanno valutato questo luogo.");
                                    } else {
                                        testo.setText("Nessuna valutazione presente. Sii il primo a votare!");
                                    }

                                }
                                else{
                                    ratingBar.setRating(0);
                                    testo.setText("Nessuna valutazione presente. Sii il primo a votare!");
                                }

                            } else {
                                Luogo place = myPlaces.get(gd);
                                float voto;
                                float somma = place.getVoto();
                                int nvoti = place.getNvoti();
                                //final Rate rate = new Rate(somma, nvoti);
                                somma = somma - stars;
                                nvoti--;
                                ratings.setVoto(somma);
                                ratings.setNvoti(nvoti);
                                token.setNvoti(nvoti);
                                token.setVoto(-stars);
                                mDatabase.child("d").child("camp").child(num).child("Voto").setValue(somma);
                                mDatabase.child("d").child("camp").child(num).child("Nvoti").setValue(nvoti);
                            /*if (nvoti != 0) {
                                voto = somma / nvoti;
                                ratingBar.setRating(voto);

                            }*/
                                TextView testo = (TextView) findViewById(R.id.numero);
                                if (nvoti != 0) {

                                    voto = somma / nvoti;
                                    ratingBar.setRating(voto);
                                    if (nvoti == 1) {
                                        testo.setText("1 utente ha valutato questo luogo.");
                                    } else if (nvoti > 1) {
                                        testo.setText("" + nvoti + " utenti hanno valutato questo luogo.");
                                    } else {
                                        testo.setText("Nessuna valutazione presente. Sii il primo a votare!");
                                    }

                                }
                                else{
                                    ratingBar.setRating(0);
                                    testo.setText("Nessuna valutazione presente. Sii il primo a votare!");
                                }

                            }

                        } else {
                            mDatabase.child("users").child(uid).child("voted").child(gd).child("stars").setValue(rating.getRating());
                            final String num = myIndex.get(gd);
                            int ind = Integer.parseInt(num);
                            Log.i("ANCORA NON VOTATOOOOO", "" + rating.getRating() + ind);
                            //final float stars = ((Long) (dataSnapshot.child(gd).child("stars").getValue())).floatValue();
                            //mDatabase.child("users").child(uid).child("voted").child(gd).removeValue();
                            if (Integer.parseInt(gd) > 394) {
                                Luogo place = myPlaces.get(gd);
                                float voto;
                                float somma = place.getVoto();
                                int nvoti = place.getNvoti();
                                Log.i("VOTAZIONII", " " + somma + " " + nvoti);
                                //final Rate rate = new Rate(somma, nvoti);
                                somma = somma + rating.getRating();
                                nvoti++;
                                token.setNvoti(nvoti);
                                token.setVoto(rating.getRating());
                                ratings.setVoto(somma);
                                ratings.setNvoti(nvoti);
                                mDatabase.child("d").child("__count").child(num).child("Voto").setValue(somma);
                                mDatabase.child("d").child("__count").child(num).child("Nvoti").setValue(nvoti);
                            if (nvoti != 0) {
                                TextView testo = (TextView) findViewById(R.id.numero);
                                voto = somma / nvoti;
                                ratingBar.setRating(voto);
                                if(token.getVoto()>=0) {
                                    if (nvoti == 1) {
                                        testo.setText("1 utente ha valutato questo luogo.");
                                    } else if (nvoti > 1) {
                                        testo.setText("" + nvoti + " utenti hanno valutato questo luogo.");
                                    } else {
                                        testo.setText("Nessuna valutazione presente. Sii il primo a votare!");
                                    }
                                }

                            }

                            } else {
                                Luogo place = myPlaces.get(gd);
                                float voto;
                                float somma = place.getVoto();
                                int nvoti = place.getNvoti();
                                //final Rate rate = new Rate(somma, nvoti);
                                somma = somma + rating.getRating();
                                nvoti++;
                                token.setNvoti(nvoti);
                                token.setVoto(rating.getRating());
                                ratings.setVoto(somma);
                                ratings.setNvoti(nvoti);
                                mDatabase.child("d").child("camp").child(num).child("Voto").setValue(somma);
                                mDatabase.child("d").child("camp").child(num).child("Nvoti").setValue(nvoti);
                            /*if (nvoti != 0) {
                                voto = somma / nvoti;
                                ratingBar.setRating(voto);

                            }*/
                                if (nvoti != 0) {
                                    TextView testo = (TextView) findViewById(R.id.numero);
                                    voto = somma / nvoti;
                                    ratingBar.setRating(voto);
                                    if (token.getVoto() >= 0) {
                                        if (nvoti == 1) {
                                            testo.setText("1 utente ha valutato questo luogo.");
                                        } else if (nvoti > 1) {
                                            testo.setText("" + nvoti + " utenti hanno valutato questo luogo.");
                                        } else {
                                            testo.setText("Nessuna valutazione presente. Sii il primo a votare!");
                                        }
                                    }
                                }

                            }

                        }
                    }

                }
                //}
            });
                    //TextView testo = (TextView) findViewById(R.id.numero);
                    //float sum = ratings.getVoto();
                    //int numero = ratings.getNvoti();

                    if (numero == 1) {
                        testo.setText("1 utente ha valutato questo luogo.");
                    } else if (numero > 1) {
                        testo.setText("" + numero + " utenti hanno valutato questo luogo.");
                    } else {
                        testo.setText("Nessuna valutazione presente. Sii il primo a votare!");
                    }

                    if (numero != 0) {
                        voto = sum / numero;
                        ratingBar.setRating(voto);
                    } else {
                        ratingBar.setRating(0);
                    }

            Button f = (Button) findViewById(R.id.condividi);
            //f.setMovementMethod(LinkMovementMethod.getInstance());
            f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, address);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                }
            });

            final Button pref = (Button) findViewById(R.id.preferiti);
            final TextView txt = (TextView)findViewById(R.id.pref);

            final Query prefe = mDatabase.child("users").child(uid).child("pref")
                    .orderByValue();
            final List<String> p = new ArrayList<String>();
            prefe.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    p.clear();
                    for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                        String gid = (String) (placeSnapshot.child("id").getValue());
                        p.add(gid);
                        Log.i(TAG, "IDDDDDDDDDDDDDDDDDDDD" + gid);
                    }
                    if(p.contains(gd)){
                        pref.setBackground(getResources().getDrawable(R.drawable.ic_picgreen));
                        txt.setTextColor(Color.parseColor("#91DC5A"));
                    }
                    else{
                        pref.setBackground(getResources().getDrawable(R.drawable.ic_pic));
                        txt.setTextColor(WHITE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            });


            //f.setMovementMethod(LinkMovementMethod.getInstance());
            pref.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Button pref = (Button) findViewById(R.id.preferiti);
                    final TextView txt = (TextView)findViewById(R.id.pref);
                    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    database = FirebaseDatabase.getInstance();
                    final String gd = myMarks.get(mark);
                    mDatabase = database.getReference();
                    if(p.contains(gd)){
                        pref.setBackground(getResources().getDrawable(R.drawable.ic_pic));
                        txt.setTextColor(WHITE);
                        mDatabase.child("users").child(uid).child("pref").child(gd).removeValue();
                    }
                    else{
                        pref.setBackground(getResources().getDrawable(R.drawable.ic_picgreen));
                        txt.setTextColor(Color.parseColor("#91DC5A"));
                        Pref pf = new Pref(gd,mark.getTitle(),lat,lon);
                        mDatabase.child("users").child(uid).child("pref").child(gd).setValue(pf);
                    }

                }
            });

            final Button vis = (Button) findViewById(R.id.visitato);
            final TextView tx = (TextView)findViewById(R.id.vis);
            final Query visit = mDatabase.child("users").child(uid).child("vis")
                    .orderByValue();
            final List<String> vi = new ArrayList<String>();
            visit.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    vi.clear();
                    for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                        String gid = (String) (placeSnapshot.child("id").getValue());
                        vi.add(gid);
                    }
                    if(vi.contains(gd)){
                        vis.setBackground(getResources().getDrawable(R.drawable.ic_visgreen));
                        tx.setTextColor(Color.parseColor("#91DC5A"));
                    }
                    else{
                        vis.setBackground(getResources().getDrawable(R.drawable.ic_vis));
                        tx.setTextColor(WHITE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            });


            //f.setMovementMethod(LinkMovementMethod.getInstance());
            vis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Button vis = (Button) findViewById(R.id.visitato);
                    final TextView tx = (TextView)findViewById(R.id.vis);
                    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    database = FirebaseDatabase.getInstance();
                    final String gd = myMarks.get(mark);
                    mDatabase = database.getReference();
                    if(vi.contains(gd)){
                        vis.setBackground(getResources().getDrawable(R.drawable.ic_vis));
                        tx.setTextColor(WHITE);
                        mDatabase.child("users").child(uid).child("vis").child(gd).removeValue();
                    }
                    else{
                        vis.setBackground(getResources().getDrawable(R.drawable.ic_visgreen));
                        tx.setTextColor(Color.parseColor("#91DC5A"));
                        Vis vs = new Vis(gd,mark.getTitle(),lat,lon);
                        mDatabase.child("users").child(uid).child("vis").child(gd).setValue(vs);
                    }

                }
            });

            Button d = (Button) findViewById(R.id.direct);
            //d.setMovementMethod(LinkMovementMethod.getInstance());
            d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Double lon = mark.getPosition().longitude;
                    Double lat = mark.getPosition().latitude;
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f, %f", lat, lon);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            });



            return true;
        }
        else{
            return false;
        }
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current location", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current location", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current location", "Cannot get Address!");
        }
        return strAdd;
    }

    @Override
    public void onLocationChanged(Location location) {
        //m.clear();
        //To clear map data
        //To hold location
        if(prevmark!=null){
            prevmark.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //To create marker in map
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("My Location");
        //adding marker to the map
        prevmark = m.addMarker(markerOptions);

        //opening position with some zoom level in the map
        m.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        if(marker!=null){
            marker.remove();
        }

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                marker = m.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                m.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            } else {
                Toast toast = Toast.makeText(this, "Per favore, inserisci un altro indirizzo.",
                        Toast.LENGTH_SHORT);
                toast.show();
            }

        }


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.main, menu);
        return true;*/
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (mLayout != null) {
            if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
                item.setTitle(R.string.action_show);
            } else {
                item.setTitle(R.string.action_hide);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);*/
        switch (item.getItemId()){
            case R.id.action_toggle: {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        item.setTitle(R.string.action_show);
                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_hide);
                    }
                }
                return true;
            }
            case R.id.action_anchor: {
                if (mLayout != null) {
                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(0.7f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        item.setTitle(R.string.action_anchor_disable);
                    } else {
                        mLayout.setAnchorPoint(1.0f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_anchor_enable);
                    }
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        android.app.FragmentManager fm = getFragmentManager();
        EditText edtext = (EditText) findViewById(R.id.editText);
        Button sbutton = (Button) findViewById(R.id.search_button);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        int id = item.getItemId();

        if (id == R.id.nav_pref) {
            /*sbutton.setVisibility(View.GONE);
            map.getView().setVisibility(View.GONE);
            edtext.setVisibility(View.GONE);
            fm.beginTransaction().replace(R.id.content_frame, new PreferitiFragment()).commit();*/
            //Intent openPreferiti = new Intent(HomeActivity.this,Preferiti.class);
            //startActivity(openPreferiti);
            drawerTogg.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(HomeActivity.this, Preferiti.class);
                    startActivity(intent);
                }
            });
            mDrawerLayout.closeDrawers();

        } else if (id == R.id.nav_visit) {
            drawerTogg.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(HomeActivity.this, Visitati.class);
                    startActivity(intent);
                }
            });
            mDrawerLayout.closeDrawers();

        }
          else if (id == R.id.nav_help) {
            drawerTogg.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
                    startActivity(intent);
                }
            });
            mDrawerLayout.closeDrawers();

                }
          else if (id == R.id.footer_spacer_1) {
            sbutton.setVisibility(View.GONE);
            map.getView().setVisibility(View.GONE);
            edtext.setVisibility(View.GONE);
            fm.beginTransaction().replace(R.id.content_frame, new InfoFragment()).commit();

                }
          else if (id == R.id.nav_logout) {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            FirebaseAuth.getInstance().signOut();
            if(accessToken!=null){
                LoginManager.getInstance().logOut();
            }

            drawerTogg.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(HomeActivity.this, HomeActivityOff.class);
                    startActivity(intent);
                }
            });
            mDrawerLayout.closeDrawers();
            finish();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }



}