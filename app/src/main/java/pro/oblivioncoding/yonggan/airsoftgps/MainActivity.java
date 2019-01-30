package pro.oblivioncoding.yonggan.airsoftgps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;

import netty.client.NettyClient;
import netty.client.NetworkHandler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapFragment.OnFragmentInteractionListener, RadioFragment.OnFragmentInteractionListener, AdvancedMapFragment.OnFragmentInteractionListener {

    public static LocationManager locationManager;
    private static LocationListener locationListener;

    public static MapFragment mapFragment;
    private static RadioFragment radioFragment;
    private static AdvancedMapFragment advancedMapFragment;

    private Fragment currentFragment;

    public static NettyClient nettyClient;

    public static MapFragment getMapFragment() {
        return mapFragment;
    }

    public static boolean alive = true, mission, underFire, support;

    //Floating Buttons
    private static FloatingActionButton hitFloatingButton;
    private static FloatingActionButton supportFloatingButton;
    private static FloatingActionButton underfireFloatingButton;
    private static FloatingActionButton missionFloatingButton;

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

        //############ Floating Buttons ####################
        hitFloatingButton = (FloatingActionButton) findViewById(R.id.hitfb);
        supportFloatingButton = (FloatingActionButton) findViewById(R.id.supportfb);
        underfireFloatingButton = (FloatingActionButton) findViewById(R.id.underfirefb);
        missionFloatingButton = (FloatingActionButton) findViewById(R.id.missionfb);
        hitFloatingButton.setOnClickListener(view -> {
            alive = !alive;
            nettyClient.sendClientStatusPositionOUTPackage(alive, underFire, mission, support);
            setAliveIcon();
            mapFragment.setIcon(mapFragment.ownMarker);
        });

        supportFloatingButton.setOnClickListener(view -> {
            support = !support;
            nettyClient.sendClientStatusPositionOUTPackage(alive, underFire, mission, support);
            setSupportIcon();
            mapFragment.setIcon(mapFragment.ownMarker);

        });

        underfireFloatingButton.setOnClickListener(view -> {
            underFire = !underFire;
            nettyClient.sendClientStatusPositionOUTPackage(alive, underFire, mission, support);
            setUnderFireIcon();
            mapFragment.setIcon(mapFragment.ownMarker);

        });

        missionFloatingButton.setOnClickListener(view -> {
            mission = !mission;
            nettyClient.sendClientStatusPositionOUTPackage(alive, underFire, mission, support);
            setMissionIcon();
            mapFragment.setIcon(mapFragment.ownMarker);
        });

        final FloatingActionButton currentlocationFloatingButton = (FloatingActionButton) findViewById(R.id.currentlocationfb);
        currentlocationFloatingButton.setOnClickListener(view -> mapFragment.setCamera(mapFragment.ownMarker.getPosition()));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_map);


        requestLocationPermissions();

        turnGPSOn();

        locationManager = (LocationManager) this.

                getSystemService(Context.LOCATION_SERVICE);

        //Set Accuracy to best
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.getBestProvider(criteria, true);

        if (mapFragment != null && locationManager != null) {
            Log.i("LocationBla", "Setting own marker");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mapFragment.setOwnMarker(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }

        locationListener = new

                LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i("LocationBla", "LocationChanged to " + location);

                        if (mapFragment != null) {
                            mapFragment.setOwnMarker(location);
                        }

                        if (nettyClient != null && NetworkHandler.loggedIN) {
                            nettyClient.sendClientPositionOUTPackage(location.getLatitude(), location.getLongitude());
                        }
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
                }
        ;

        requestLocation();

        //Create new Object of Fragment
        mapFragment = new

                MapFragment();

        advancedMapFragment = new

                AdvancedMapFragment();

        radioFragment = new

                RadioFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.content_main, mapFragment);
        fragmentTransaction.add(R.id.content_main, advancedMapFragment);
        fragmentTransaction.add(R.id.content_main, radioFragment);

        fragmentTransaction.detach(advancedMapFragment);
        fragmentTransaction.detach(radioFragment);
        fragmentTransaction.commit();

        currentFragment = mapFragment;

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (currentFragment != null) fragmentTransaction.detach(currentFragment);

        if (mapFragment == null) mapFragment = new MapFragment();
        if (advancedMapFragment == null) advancedMapFragment = new AdvancedMapFragment();
        if (radioFragment == null) radioFragment = new RadioFragment();


        if (id == R.id.nav_map) {
            fragmentTransaction.attach(mapFragment);
            currentFragment = mapFragment;
        } else if (id == R.id.nav_advancedmap) {
            fragmentTransaction.attach(advancedMapFragment);
            currentFragment = advancedMapFragment;
        } else if (id == R.id.nav_radio) {
            fragmentTransaction.attach(radioFragment);
            currentFragment = radioFragment;
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

            requestLocationPermissions();
            return;
        }
        if (locationManager != null)
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

    //Setting floating Button icons
    public static void setAliveIcon() {
        if (alive) {
            missionFloatingButton.show();
            supportFloatingButton.show();
            underfireFloatingButton.show();
            hitFloatingButton.setImageResource(R.drawable.ic_fb_hit);
            hitFloatingButton.hide();
            hitFloatingButton.show();
        }
        else {
            missionFloatingButton.hide();
            supportFloatingButton.hide();
            underfireFloatingButton.hide();
            hitFloatingButton.setImageResource(R.drawable.ic_fb_healed);
            hitFloatingButton.hide();
            hitFloatingButton.show();
        }
    }

    public static void setUnderFireIcon() {
        if (underFire) {
            missionFloatingButton.hide();
            supportFloatingButton.hide();
            underfireFloatingButton.setImageResource(R.drawable.ic_not_underfire);
            underfireFloatingButton.hide();
            underfireFloatingButton.show();
        }
        else {
            missionFloatingButton.show();
            supportFloatingButton.show();
            underfireFloatingButton.setImageResource(R.drawable.ic_under_fire);
            underfireFloatingButton.hide();
            underfireFloatingButton.show();
        }
    }

    public static void setMissionIcon() {
        if (mission) {
            underfireFloatingButton.hide();
            supportFloatingButton.hide();
            hitFloatingButton.hide();
            missionFloatingButton.setImageResource(R.drawable.ic_mission_success);
            missionFloatingButton.hide();
            missionFloatingButton.show();
        }
        else {
            underfireFloatingButton.show();
            supportFloatingButton.show();
            hitFloatingButton.show();
            missionFloatingButton.setImageResource(R.drawable.ic_fb_mission);
            missionFloatingButton.hide();
            missionFloatingButton.show();
        }
    }

    public static void setSupportIcon() {
        if (support) {
            missionFloatingButton.hide();
            supportFloatingButton.setImageResource(R.drawable.ic_no_support);
            supportFloatingButton.hide();
            supportFloatingButton.show();
        }
        else {
            missionFloatingButton.show();
            supportFloatingButton.setImageResource(R.drawable.ic_fb_support);
            supportFloatingButton.hide();
            supportFloatingButton.show();
        }
    }

    private void turnGPSOn() {
        //TODO: Automatically turn GPS on
    }

    private void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }
}

