package pro.oblivioncoding.yonggan.airsoftgps.InfoWindowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import pro.oblivioncoding.yonggan.airsoftgps.R;

public class CustomMissionMarkerWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View mWindow;
    private Context mContext;

    private double latitude, longitude;

    private String title, description, username;

    public CustomMissionMarkerWindowAdapter(Context context, double latitude, double longitude, String title, String description, String username) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_mission_marker_info_window, null);
        this.latitude = latitude;
        this.longitude = longitude;
        this.username = username;
        this.title = title;
        this.description = description;
    }

    private void renderWindowText(Marker marker, View view) {
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.latitude)).setText("Lat: " + String.valueOf(latitude));
        ((TextView) view.findViewById(R.id.longitude)).setText("Long: " + String.valueOf(longitude));
        ((TextView) view.findViewById(R.id.username)).setText("Creator: " + username);
        ((TextView) view.findViewById(R.id.description)).setText("Description: " + description);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return mWindow;
    }
}
