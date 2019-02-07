package pro.oblivioncoding.yonggan.airsoftgps.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import netty.client.NettyClient;
import pro.oblivioncoding.yonggan.airsoftgps.InfoWindowAdapter.CustomFlagMarkerWindowAdapter;
import pro.oblivioncoding.yonggan.airsoftgps.InfoWindowAdapter.CustomHQMarkerWindowAdapter;
import pro.oblivioncoding.yonggan.airsoftgps.InfoWindowAdapter.CustomMarkerInfoWindowAdaper;
import pro.oblivioncoding.yonggan.airsoftgps.InfoWindowAdapter.CustomMissionMarkerWindowAdapter;
import pro.oblivioncoding.yonggan.airsoftgps.InfoWindowAdapter.CustomOwnMarkerInfoWindowAdapter;
import pro.oblivioncoding.yonggan.airsoftgps.InfoWindowAdapter.CustomRespawnMarkerWindowAdapter;
import pro.oblivioncoding.yonggan.airsoftgps.InfoWindowAdapter.CustomTacticalMarkerWindowAdapter;
import pro.oblivioncoding.yonggan.airsoftgps.MainActivity;
import pro.oblivioncoding.yonggan.airsoftgps.MarkerData.FlagMarkerData;
import pro.oblivioncoding.yonggan.airsoftgps.MarkerData.HQMakerData;
import pro.oblivioncoding.yonggan.airsoftgps.MarkerData.MarkerData;
import pro.oblivioncoding.yonggan.airsoftgps.MarkerData.MissionMarkerData;
import pro.oblivioncoding.yonggan.airsoftgps.MarkerData.OwnMarkerData;
import pro.oblivioncoding.yonggan.airsoftgps.MarkerData.RespawnMarkerData;
import pro.oblivioncoding.yonggan.airsoftgps.MarkerData.TacticalMarkerData;
import pro.oblivioncoding.yonggan.airsoftgps.R;


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

    private static Polygon teamAreaPolygon;

    private static ArrayList<Circle> teamAreaCircleList = new ArrayList<Circle>();

    private static HashMap<Marker, HQMakerData> HQmarker = new HashMap();
    public static HashMap<Marker, TacticalMarkerData> tacticalmarker = new HashMap();
    private static HashMap<Marker, MissionMarkerData> missionmarker = new HashMap();
    private static HashMap<Marker, RespawnMarkerData> respawnmarker = new HashMap();
    private static HashMap<Marker, FlagMarkerData> flagmarker = new HashMap();


    public MapFragment() {
        // Required empty public constructor
    }

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
            } else if (markerData.containsKey(marker)) {
                MarkerData markerData0 = markerData.get(marker);
                Log.i("InfoWindow", "User");
                googleMap.setInfoWindowAdapter(new CustomMarkerInfoWindowAdaper(getContext(), markerData0.getTitle(), markerData0.getLatitude(), markerData0.getLongitude(), markerData0.getTimestamp(), markerData0.getTeamname(), markerData0.isAlive(), markerData0.isUnderfire(), markerData0.isMission(), markerData0.isSupport()));
            } else if (tacticalmarker.containsKey(marker)) {
                TacticalMarkerData tacticalMarkerData = tacticalmarker.get(marker);
                googleMap.setInfoWindowAdapter(new CustomTacticalMarkerWindowAdapter(getContext(), tacticalMarkerData.getLatitude(), tacticalMarkerData.getLongitude(), tacticalMarkerData.getTeamname(), tacticalMarkerData.getTitle(), tacticalMarkerData.getDescription(), tacticalMarkerData.getUsername()));
            } else if (missionmarker.containsKey(marker)) {
                MissionMarkerData missionMarkerData = missionmarker.get(marker);
                googleMap.setInfoWindowAdapter(new CustomMissionMarkerWindowAdapter(getContext(), missionMarkerData.getLatitude(), missionMarkerData.getLongitude(), missionMarkerData.getTitle(), missionMarkerData.getDescription(), missionMarkerData.getUsername()));
            } else if (respawnmarker.containsKey(marker)) {
                RespawnMarkerData respawnMarkerData = respawnmarker.get(marker);
                googleMap.setInfoWindowAdapter(new CustomRespawnMarkerWindowAdapter(getContext(), respawnMarkerData.getLatitude(), respawnMarkerData.getLongitude(), respawnMarkerData.getTitle(), respawnMarkerData.getDescription(), respawnMarkerData.getUsername()));
            } else if (HQmarker.containsKey(marker)) {
                HQMakerData hqMakerData = HQmarker.get(marker);
                googleMap.setInfoWindowAdapter(new CustomHQMarkerWindowAdapter(getContext(), hqMakerData.getLatitude(), hqMakerData.getLongitude(), hqMakerData.getTitle(), hqMakerData.getDescription(), hqMakerData.getUsername()));
            } else if (flagmarker.containsKey(marker)) {
                FlagMarkerData flagMarkerData = flagmarker.get(marker);
                googleMap.setInfoWindowAdapter(new CustomFlagMarkerWindowAdapter(getContext(), flagMarkerData.getLatitude(), flagMarkerData.getLongitude(), flagMarkerData.getTitle(), flagMarkerData.getDescription(), flagMarkerData.getUsername()));
            }

            marker.showInfoWindow();
            return true;
        });

        Toast.makeText(getContext(), "You may have to move or manual reload to see the other Players", Toast.LENGTH_LONG).show();
    }

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
                    if (userMarker.containsKey(userID)) userMarker.get(userID).remove();
                    userMarker.put(userID, marker);
                    Log.i("Marker", "Own Server-Marker created at " + marker.getPosition());
                    MarkerData playerMarkerData = new MarkerData(username + " (" + userID + ")", latitude, longitude, simpleDateFormat.format(new Date(timestamp.getTime())), teamname, alive, underfire, mission, support);
                    markerData.put(marker, playerMarkerData);
                    setIcon(marker, playerMarkerData);
                } else {
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude)));
                    if (userMarker.containsKey(userID)) userMarker.get(userID).remove();
                    userMarker.put(userID, marker);
                    Log.i("Marker", "Marker created at " + marker.getPosition());
                    MarkerData playerMarkerData = new MarkerData(username + " (" + userID + ")", latitude, longitude, simpleDateFormat.format(new Date(timestamp.getTime())), teamname, alive, underfire, mission, support);
                    markerData.put(marker, playerMarkerData);
                    setIcon(marker, playerMarkerData);
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

    public void setIcon(Marker marker, MarkerData markerData) {
        Log.i("Marker", "Setting Icon of Marker");
        if (markerData.isMission()) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_alivemission));
        } else if (markerData.isAlive() && markerData.isUnderfire() && markerData.isSupport()) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_aliveunderfiresupport));
        } else if (markerData.isAlive() && markerData.isUnderfire() && !markerData.isSupport()) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_aliveunderfire));
        } else if (markerData.isAlive() && !markerData.isUnderfire() && markerData.isSupport()) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_alivenotunderfiresupport));
        } else if (markerData.isAlive() && !markerData.isUnderfire() && !markerData.isSupport()) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_alivenotunderfire));
        } else if (!markerData.isAlive()) {
            marker.setIcon(getBitmapDescriptor(R.drawable.ic_pin_notalivenotunderfire));
        }
    }

    public void setOrgaMarkerIcon(Marker marker, MarkerData markerData) {

    }

    public void setAreaPolygons() {
        PolygonOptions polygonOptions = new PolygonOptions();
        for (Marker marker : userMarker.values()) {
            polygonOptions.add(marker.getPosition());
        }
        polygonOptions.fillColor(Color.BLUE);
        polygonOptions.strokeColor(Color.BLUE);
        teamAreaPolygon = googleMap.addPolygon(polygonOptions);
    }

    public void removeAreaPolygons() {
        if (teamAreaPolygon != null) {
            teamAreaPolygon.remove();
        }
    }

    public void setAreaCircles() {
        for (Marker marker : userMarker.values()) {
            teamAreaCircleList.add(googleMap.addCircle(new CircleOptions().center(marker.getPosition()).radius(100).fillColor(Color.GREEN).strokeColor(Color.GREEN)));
        }
    }

    public void removeAreaCircles() {
        for (Circle circle : teamAreaCircleList) {
            if (circle != null) {
                circle.remove();
            }
        }
        teamAreaCircleList.clear();
    }

    public void addTacticalMarker(double latitude, double longitude, int id, String title, String description, String teamname, String username) {
        if (googleMap != null) {
            getActivity().runOnUiThread(() -> {
                tacticalmarker.put(googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))), new TacticalMarkerData(latitude, longitude, id, title, teamname, description, username));
                Log.i("Pin", "Setting Tactical Marker");
            });
        }
    }

    public void addMissionMarker(double latitude, double longitude, String title, String description, String username) {
        if (googleMap != null) {
            getActivity().runOnUiThread(() -> {
                missionmarker.put(googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))), new MissionMarkerData(latitude, longitude, title, description, username));
                Log.i("Pin", "Setting Mission Marker");
            });
        }
    }

    public void addRespawnMarker(double latitude, double longitude, String title, String description, String username) {
        if (googleMap != null) {
            getActivity().runOnUiThread(() -> {
                respawnmarker.put(googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))), new RespawnMarkerData(latitude, longitude, title, description, username));
                Log.i("Pin", "Setting Respawn Marker");
            });
        }
    }

    public void addHQMarker(double latitude, double longitude, String title, String description, String username) {
        if (googleMap != null) {
            getActivity().runOnUiThread(() -> {
                HQmarker.put(googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))), new HQMakerData(latitude, longitude, title, description, username));
                Log.i("Pin", "Setting HQ Marker");
            });
        }
    }

    public void addFlagMarker(double latitude, double longitude, String title, String description, String username) {
        if (googleMap != null) {
            getActivity().runOnUiThread(() -> {
                flagmarker.put(googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))), new FlagMarkerData(latitude, longitude, title, description, username));
                Log.i("Pin", "Setting Flag Marker");
            });
        }
    }


}
