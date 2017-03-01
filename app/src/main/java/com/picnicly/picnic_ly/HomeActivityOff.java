package com.picnicly.picnic_ly;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.text.Html;
import android.text.Spanned;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.BitmapDescriptor;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.zip.Inflater;

import android.widget.TextView;




/**
 * Created by Giovy on 28/01/2017.
 */


import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.picnicly.picnic_ly.dummy.DummyContent;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import static android.R.attr.data;




public class HomeActivityOff extends AppCompatActivity implements OnMapReadyCallback, LocationListener, NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener{
    GoogleMap m;
    Marker marker;
    Marker prevmark;
    private GoogleApiClient client;
    private SupportMapFragment map;
    public static final String TAG = "HomeActivityOff";
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    SupportMapFragment mMapFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private Button search;
    int PLACE_PICKER_REQUEST = 1;
    String placeName, address;
    private static final float ALPHA_DIM_VALUE = 0.1f;
    private SlidingUpPanelLayout mLayout;
    List<Marker> markers = new ArrayList<Marker>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Inflater inflater = null;
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_home_off);
        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        /*map.getMapAsync(this);*/
        initializeMap();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.app.FragmentManager fm = getFragmentManager();




        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomeActivityOff.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });

        List<String> your_array_list = Arrays.asList(
                "This",
                "Is",
                "An",
                "Example",
                "ListView",
                "That",
                "You",
                "Can",
                "Scroll",
                ".",
                "It",
                "Shows",
                "How",
                "Any",
                "Scrollable",
                "View",
                "Can",
                "Be",
                "Included",
                "As",
                "A",
                "Child",
                "Of",
                "SlidingUpPanelLayout"
        );

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
                textView.setTextColor(Color.WHITE);

                return view;
            }
        };

        lv.setAdapter(arrayAdapter);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
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
                if(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED){
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });



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


    }



    private void initializeMap() {
        if (m == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);


            // check if map is created successfully or not
            /*if (m == null) {
                Toast.makeText(MainActivity.this,  "Creando la mappa", Toast.LENGTH_SHORT).show();
            }*/
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
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

        /*map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(47.17, 27.5699), 16));
        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)).anchor(0.0f, 1.0f) //
Anchors the marker on the bottom left
                .position(new LatLng(47.17, 27.5699))); //Iasi, Romania*/
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                final EditText editText = (EditText) findViewById(R.id.editText);
                //Toast.makeText(getApplicationContext(), latLng.toString(), Toast.LENGTH_LONG).show();
                editText.clearFocus();
                hideSoftKeyboard(HomeActivityOff.this);
            }
        });

        //To setup location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //To request location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
        LatLng sydney = new LatLng(27.746974, 85.301582);
        LatLng tv = new LatLng(45.6662855, 12.2420720);
        LatLng quinto = new LatLng(45.6562880, 12.166667);
        m.addMarker(new MarkerOptions().position(sydney).title("Kathmandu, Nepal"));
        //m.addMarker(new MarkerOptions().position(tv).title("Tv, tv"));
        //m.addMarker(new MarkerOptions().position(quinto).title("Tv, tvv"));
        //MarkerOptions marker = new MarkerOptions().position(quinto).title("Hello Maps");
        markers.add(m.addMarker(new MarkerOptions().position(sydney).title("Kathmandu, Nepal")));
        //m.addMarker(new MarkerOptions().position(tv).title("Tv, tv"));
        markers.add(m.addMarker(new MarkerOptions().position(tv).title("Tv, tv")));

// Changing marker icon
        //marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.picnic_table));
        //m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.picnic_table)).anchor(0.0f, 1.0f).position(quinto).title("Quinto"));
        //m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.forest)).anchor(0.0f, 1.0f).position(tv).title("Tv"));
        markers.add(m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.forest)).anchor(0.0f, 1.0f).position(tv).title("Tv")));
        markers.add(m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.picnic_table)).anchor(0.0f, 1.0f).position(quinto).title("Quinto")));
// adding marker
        //m.addMarker(marker);
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

        //if (mark.equals(marker))
        if(markers.contains(mark))
        {
            if(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN){
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            mLayout.findViewById(R.id.dragView).setVisibility(View.VISIBLE);
            mLayout.setPanelHeight(150);
            final String address = mark.getTitle()+"\n"+getCompleteAddressString(mark.getPosition().latitude, mark.getPosition().longitude);
            //mLayout.findViewById(R.id.panel).setVisibility(View.VISIBLE);
            TextView t = (TextView) findViewById(R.id.name);
            t.setText(address);

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
            Log.w("My Current location", "Canont get Address!");
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
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
        int id = item.getItemId();


        /*if (id == R.id.nav_home) {
            map.getView().setVisibility(View.VISIBLE);
            sbutton.setVisibility(View.VISIBLE);
            edtext.setVisibility(View.VISIBLE);

        }*/
        if (id == R.id.nav_help) {
            /*sbutton.setVisibility(View.GONE);
            map.getView().setVisibility(View.GONE);
            edtext.setVisibility(View.GONE);
            fm.beginTransaction().replace(R.id.content_frame, new HelpFragment()).commit();*/
            Intent openHelp = new Intent(HomeActivityOff.this,HelpActivity.class);
            openHelp.putExtra("activity","homeoff");
            startActivity(openHelp);

        }
        else if (id == R.id.footer_spacer_1) {
            sbutton.setVisibility(View.GONE);
            map.getView().setVisibility(View.GONE);
            edtext.setVisibility(View.GONE);
            fm.beginTransaction().replace(R.id.content_frame, new InfoFragment()).commit();

        }
        else if (id == R.id.nav_login) {
            SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", false);
            ed.commit();
            Intent intent = new Intent(HomeActivityOff.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

}