package pro.oblivioncoding.yonggan.airsoftgps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomOwnMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View mWindow;
    private Context mContext;

    private double latitude;
    private double longitude;
    private String title;

    public CustomOwnMarkerInfoWindowAdapter(Context context, String title, double latitude, double longitude) {
        mContext = context;
        if(context != null) {
            mWindow = LayoutInflater.from(context).inflate(R.layout.custom_own_marker_info_window, null);
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }

    private void renderWindowText(Marker marker, View view) {
        TextView latitudeText = (TextView) view.findViewById(R.id.latitude);
        TextView longitudeText = (TextView) view.findViewById(R.id.longitude);
        TextView titleText = (TextView) view.findViewById(R.id.title);
        titleText.setText(title);
        latitudeText.setText("Lat: " + String.valueOf(latitude));
        longitudeText.setText("Long: " + String.valueOf(longitude));
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
