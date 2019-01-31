package pro.oblivioncoding.yonggan.airsoftgps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import netty.client.NettyClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleMap googleMap = null;

    private SupportMapFragment mapFragment;

    private LocationManager locationManager;

    private static boolean firstTimeCamera;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public static Marker ownMarker;

    public static OwnMarkerData ownMarkerData;

    public static HashMap<Integer, Marker> userMarker = new HashMap<Integer, Marker>();

    public static HashMap<Marker, MarkerData> markerData = new HashMap<Marker, MarkerData>();

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        //This is for loading the Map
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this::onMapReady);
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("MapReady", "Map Loaded");
        clearMap();
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        locationManager = ((MainActivity) getActivity()).getLocationManager();

        setOwnMarker(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

        googleMap.setOnMarkerClickListener(marker -> {
            Log.i("Marker", "Marker: " + marker);
            if (marker.equals(ownMarker)) {
                Log.i("InfoWindow", "Own");
                googleMap.setInfoWindowAdapter(new CustomOwnMarkerInfoWindowAdapter(getContext(), ownMarkerData.getTitle(), ownMarkerData.getLatitude(), ownMarkerData.getLongitude()));
            } else {
                MarkerData markerData0 = markerData.get(marker);
                Log.i("InfoWindow", "User");
                googleMap.setInfoWindowAdapter(new CustomMarkerInfoWindowAdaper(getContext(), markerData0.getTitle(), markerData0.getLatitude(), markerData0.getLongitude(), markerData0.getTimestamp(), markerData0.getTeamname(), markerData0.isAlive(), markerData0.isUnderfire(), markerData0.isMission(), markerData0.isSupport()));
            }
            marker.showInfoWindow();
            return true;
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setOwnMarker(Location location) {

        if (googleMap != null && location != null) {
            if (ownMarker != null) ownMarker.remove();
            Log.i("LocationSet", "Setting your own Location");
            ownMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude())));
           ownMarkerData = new OwnMarkerData("You are here!", location.getLatitude(), location.getLongitude());
            if (!firstTimeCamera) {
                firstTimeCamera = !firstTimeCamera;
                setCamera(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }
    }

    public void clearMap() {
        if (googleMap != null) {
            googleMap.clear();
        }
    }

    public void setCamera(LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f));
    }

    public void createMarker(double latitude, double longitude, int userID, String username, Timestamp timestamp, String teamname, boolean alive, boolean underfire, boolean mission, boolean support) {
        if (googleMap != null) {
            getActivity().runOnUiThread(() -> {
                if (NettyClient.getUsername().equals(username)) {
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude)));
                    setIcon(marker);
                    if (userMarker.containsKey(userID)) userMarker.get(userID).remove();
                    userMarker.put(userID, marker);
                    Log.i("Marker", "Own Server-Marker created at " + marker.getPosition());
                    markerData.put(marker, new MarkerData(username + " (" + userID + ")", latitude, longitude, simpleDateFormat.format(new Date(timestamp.getTime())), teamname, alive, underfire, mission, support));
                } else {
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude)));
                    setIcon(marker);
                    if (userMarker.containsKey(userID)) userMarker.get(userID).remove();
                    userMarker.put(userID, marker);
                    Log.i("Marker", "Marker created at " + marker.getPosition());
                    markerData.put(marker, new MarkerData(username + " (" + userID + ")", latitude, longitude, simpleDateFormat.format(new Date(timestamp.getTime())), teamname, alive, underfire, mission, support));
                }
                Log.i("Marker", String.valueOf(userMarker));
            });
        } else {
            Log.i("Marker", "GoogleMap is null");
        }
    }

    private BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = getResources().getDrawable(id);
        int h = ((int) dpTopixel(getContext(), 50));
        int w = ((int) dpTopixel(getContext(), 50));
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    private static float dpTopixel(Context c, float dp) {
        float density = c.getResources().getDisplayMetrics().density;
        float pixel = dp * density;
        return pixel;
    }

    public void setIcon(Marker marker) {
        if (MainActivity.mission) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_alivemission));
        } else if (MainActivity.alive && MainActivity.underFire && MainActivity.support) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_aliveunderfiresupport));
        } else if (MainActivity.alive && MainActivity.underFire && !MainActivity.support) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_aliveunderfire));
        } else if (MainActivity.alive && !MainActivity.underFire && MainActivity.support) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_alivenotunderfiresupport));
        } else if (MainActivity.alive && !MainActivity.underFire && !MainActivity.support) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_alivenotunderfire));
        } else if (!MainActivity.alive) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_notalivenotunderfire));
        }
    }

}
