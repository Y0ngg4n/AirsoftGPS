package pro.oblivioncoding.yonggan.airsoftgps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.SupportMapFragment;

import netty.client.NettyClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapFragment.OnFragmentInteractionListener, RadioFragment.OnFragmentInteractionListener {

    public static LocationManager locationManager;
    private static LocationListener locationListener;

    private MapFragment mapFragment;
    private RadioFragment radioFragment;
    private AdvancedMapFragment advancedMapFragment;

    private NettyClient nettyClient;

    private enum FragmentAttached {
        mapFragment,
        radioFragment,
        advancedMapFragment
    }

    private FragmentAttached mainMenuFragmentAttached;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Connect to Server
        nettyClient = new NettyClient("test", "test", "192.168.1.29", 12345);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //Set Accuracy to best
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.getBestProvider(criteria, true);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (mapFragment != null) {
                    Log.i("LocationBla", "Setting own marker");
                    mapFragment.setOwnMarker(location);
                }
//                if (nettyClient != null) {
//                    nettyClient.sendClientPositionOUTPackage(location.getLatitude(), location.getLongitude());
//                }
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
        };

        //Create new Object of Fragments
        this.mapFragment = new MapFragment();
        this.radioFragment = new RadioFragment();
        this.advancedMapFragment = new AdvancedMapFragment();

        requestLocationPermissions();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (!fragmentManager.getFragments().contains(mapFragment)) {
            fragmentTransaction.add(R.id.content_main, mapFragment);
            fragmentTransaction.detach(mapFragment);
        }
        if (!fragmentManager.getFragments().contains(advancedMapFragment)) {
            fragmentTransaction.add(R.id.content_main, advancedMapFragment);
            fragmentTransaction.detach(advancedMapFragment);
        }
        if (!fragmentManager.getFragments().contains(radioFragment)) {
            fragmentTransaction.add(R.id.content_main, radioFragment);
            fragmentTransaction.detach(radioFragment);
        }


        switch (id){
            case R.id.nav_map:
                switch (mainMenuFragmentAttached){
                    case mapFragment:
                        return false;
                    case advancedMapFragment:
                        fragmentTransaction.detach(advancedMapFragment);
                        break;
                    case radioFragment:
                        fragmentTransaction.detach(radioFragment);
                }
                fragmentTransaction.attach(mapFragment);
                mainMenuFragmentAttached = FragmentAttached.mapFragment;
                break;
            case R.id.advancedmap:
                switch (mainMenuFragmentAttached){
                    case advancedMapFragment:
                        return false;
                    case mapFragment:
                        fragmentTransaction.detach(mapFragment);
                        break;
                    case radioFragment:
                        fragmentTransaction.detach(radioFragment);
                        break;
                }
                mainMenuFragmentAttached = FragmentAttached.advancedMapFragment;
                fragmentTransaction.attach(advancedMapFragment);
                break;
            case R.id.radio:{
                switch (mainMenuFragmentAttached){
                    case radioFragment:
                        return false;
                    case mapFragment:
                        fragmentTransaction.detach(mapFragment);
                        break;
                    case advancedMapFragment:
                        fragmentTransaction.detach(advancedMapFragment);
                        break;
                }
                mainMenuFragmentAttached = FragmentAttached.radioFragment;
                fragmentTransaction.attach(radioFragment);
                break;
            }
        }






        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static LocationManager getLocationManager() {
        return locationManager;
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestLocationPermissions();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            } else {
                requestLocation();
            }
        }
    }

}

