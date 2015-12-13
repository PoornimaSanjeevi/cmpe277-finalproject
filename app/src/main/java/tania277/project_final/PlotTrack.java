package tania277.project_final;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class PlotTrack extends AppCompatActivity
        implements
        OnMapReadyCallback {
    protected static final String TAG = "plot-loc";

    private GoogleMap mMap;
    String latLongs;
    String distance;
    String name;
    String timeTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plot_track);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        latLongs = intent.getStringExtra("latlongs");
        distance = intent.getStringExtra("distance");
        name = intent.getStringExtra("name");
        timeTaken = intent.getStringExtra("timeTaken");
        TextView t = (TextView) findViewById(R.id.info);
        t.setText("Event: " + name + "\nDistance:" + distance + " mts\nTime: " + timeTaken + " mins");
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        String[] lls = latLongs.split("\\|");
        PolylineOptions rectOptions = new PolylineOptions();

        int i = 0;
        for (String ll : lls) {
            String[] parts = ll.split("=");
            LatLng l1 = new LatLng(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
            if (i == 0) {
                mMap.addMarker(new MarkerOptions()
                        .position(l1).draggable(false)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .title("START"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l1, 14));
            } else if (i == lls.length - 1) {
                mMap.addMarker(new MarkerOptions()
                        .position(l1).draggable(false)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title("END"));
            }
            i++;
            rectOptions.add(l1);
        }
        Polyline polyline = mMap.addPolyline(rectOptions);
        polyline.setVisible(true);
    }
}
