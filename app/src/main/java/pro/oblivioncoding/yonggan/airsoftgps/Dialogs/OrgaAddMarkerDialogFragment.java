package pro.oblivioncoding.yonggan.airsoftgps.Dialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import netty.client.NettyClient;
import pro.oblivioncoding.yonggan.airsoftgps.MainActivity;
import pro.oblivioncoding.yonggan.airsoftgps.R;

public class OrgaAddMarkerDialogFragment extends DialogFragment {

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public OrgaAddMarkerDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static OrgaAddMarkerDialogFragment newInstance(String title) {
        OrgaAddMarkerDialogFragment frag = new OrgaAddMarkerDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.orga_add_marker_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Spinner spinner = (Spinner) getView().findViewById(R.id.orgaaddmarkertype);
        ArrayList<String> spinnerItems = new ArrayList<String>(Arrays.asList("Tactical Marker", "Mission Marker", "Respawn Marker", "HQ Marker", "Flag Marker"));
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinnerItems);
        int counter = 0;
        if (!MainActivity.tacticalMarker) {
            adapter.remove(adapter.getItem(0 - counter));
            adapter.notifyDataSetChanged();
            counter++;
        }
        if (!MainActivity.missionMarker) {
            adapter.remove(adapter.getItem(1 - counter));
            adapter.notifyDataSetChanged();
            counter++;
        }
        if (!MainActivity.respawnMarker) {
            adapter.remove(adapter.getItem(2 - counter));
            adapter.notifyDataSetChanged();
            counter++;
        }
        if (!MainActivity.hqMarker) {
            adapter.remove(adapter.getItem(3 - counter));
            adapter.notifyDataSetChanged();
            counter++;
        }
        if (!MainActivity.flagMarker) {
            adapter.remove(adapter.getItem(4 - counter));
            adapter.notifyDataSetChanged();
            counter++;
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getView().findViewById(R.id.orgaaddmarkerteamnamelabel).setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.orgaaddmarkerteamname).setVisibility(View.VISIBLE);
                } else {
                    getView().findViewById(R.id.orgaaddmarkerteamnamelabel).setVisibility(View.INVISIBLE);
                    getView().findViewById(R.id.orgaaddmarkerteamname).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final TextView latitudeTextView = (TextView) getView().findViewById(R.id.orgaaddmarkerdialoglatitude);
        final TextView longitudeTextView = (TextView) getView().findViewById(R.id.orgaaddmarkerdialoglongitude);
        //Current Lcoation Button
        ((Button) getView().findViewById(R.id.orgaaddmarkercurrentlocation)).setOnClickListener(view1 ->
        {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                latitudeTextView.setText(String.valueOf(MainActivity.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()));
                longitudeTextView.setText(String.valueOf(MainActivity.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude()));
            }
        });
        ((Button) getView().findViewById(R.id.orgaaddmarker)).setOnClickListener(view1 ->
        {
            switch (spinner.getSelectedItemPosition()) {
                case 0:
                    NettyClient.sendAddTacticalMarkerOUTPackage(Double.valueOf(latitudeTextView.getText().toString()), Double.valueOf(longitudeTextView.getText().toString()),
                            ((TextView) getView().findViewById(R.id.orgaaddmarkerteamname)).getText().toString(), ((TextView) getView().findViewById(R.id.orgaaddmarkertitle)).getText().toString(),
                            ((TextView) getView().findViewById(R.id.orgaaddmarkerdescription)).getText().toString());
                    break;
                case 1:
                    NettyClient.sendAddMissionMarkerOUTPackage(Double.valueOf(latitudeTextView.getText().toString()), Double.valueOf(longitudeTextView.getText().toString()),
                            ((TextView) getView().findViewById(R.id.orgaaddmarkertitle)).getText().toString(),
                            ((TextView) getView().findViewById(R.id.orgaaddmarkerdescription)).getText().toString());
                    break;
                case 2:
                    NettyClient.sendAddRespawnMarkerOUTPackage(Double.valueOf(latitudeTextView.getText().toString()), Double.valueOf(longitudeTextView.getText().toString()),
                            ((TextView) getView().findViewById(R.id.orgaaddmarkertitle)).getText().toString(),
                            ((TextView) getView().findViewById(R.id.orgaaddmarkerdescription)).getText().toString(), own);
                    break;
                case 3:
                    NettyClient.sendAddHQMarkerOUTPackage(Double.valueOf(latitudeTextView.getText().toString()), Double.valueOf(longitudeTextView.getText().toString()),
                            ((TextView) getView().findViewById(R.id.orgaaddmarkertitle)).getText().toString(),
                            ((TextView) getView().findViewById(R.id.orgaaddmarkerdescription)).getText().toString());
                    break;
                case 4:
                    NettyClient.sendAddFlagMarkerOUTPackage(Double.valueOf(latitudeTextView.getText().toString()), Double.valueOf(longitudeTextView.getText().toString()),
                            ((TextView) getView().findViewById(R.id.orgaaddmarkertitle)).getText().toString(),
                            ((TextView) getView().findViewById(R.id.orgaaddmarkerdescription)).getText().toString());
                    break;
            }
        });
    }

}
