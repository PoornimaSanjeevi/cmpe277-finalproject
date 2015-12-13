package tania277.project_final;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import tania277.project_final.DataAccess.AsyncTask.AcceptEventAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetEventDetailsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetEventsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetFriendRequestsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.UpdateCurrentLocationAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.UpdatePathCoordinatesAsyncTask;
import tania277.project_final.Models.CurrentLocationAllParticipants;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.LatLang;
import tania277.project_final.Models.RunRecord;
import tania277.project_final.Models.User;
import tania277.project_final.util.PermissionUtils;
import tania277.project_final.util.PrefUtil;

public class MapActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    protected static final String TAG = "location-updates";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;
    EventItem item;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    float colors[] = {BitmapDescriptorFactory.HUE_AZURE, BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_CYAN, BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_MAGENTA, BitmapDescriptorFactory.HUE_ORANGE, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_ROSE, BitmapDescriptorFactory.HUE_VIOLET, BitmapDescriptorFactory.HUE_YELLOW};
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    // Labels.
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;
    String eventId;
    List<String> participants;
    GetEventDetailsAsyncTask getEventDetailsAsyncTask = new GetEventDetailsAsyncTask();
    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;
    boolean firstLoc = true;
    private GoogleMap mMap;
    Map<String, Marker> markers = new HashMap<String, Marker>();
    Map<String, String> latlongs = new HashMap<String, String>();

    List<String> locHistory = new ArrayList<String>();
    long startTS;
    long endTS;
    double distance = 0.0;
    String eventName;
    String currUser;
    String latlongstr;
    GetEventsAsyncTask task = new GetEventsAsyncTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setHasOptionsMenu(true);
        eventId = this.getIntent().getStringExtra("eventId");
        eventName = this.getIntent().getStringExtra("eventName");
        participants = this.getIntent().getStringArrayListExtra("plist");
        buildGoogleApiClient();
        startTS = System.currentTimeMillis();
        PrefUtil pu = new PrefUtil(this);
        currUser = pu.getEmailId();

//        findViewById(R.id.btnPlot).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mGoogleApiClient.disconnect();
//                User user = new User();
//                try {
//                    user = new GetUserAsyncTask().execute(new PrefUtil(MapActivity.this).getEmailId()).get();
//
//                    for (RunRecord runRecord : user.getRunRecords()) {
//
//
//                        if (runRecord.getEventId().trim().equalsIgnoreCase(item.getEventId())) {
//                            Intent intent = new Intent(v.getContext(), PlotTrack.class);
//                            intent.putExtra("latlongs", runRecord.getPath());
//                            intent.putExtra("distance", runRecord.getDistanceRan());
//                            intent.putExtra("timeTaken", runRecord.getTimeRan());
//                            intent.putExtra("name", eventName);
//                            startActivity(intent);
//                            break;
//                        }
//
//
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                item = null;
            } else {
                item = (EventItem) extras.getSerializable("eventItem");
            }
        } else {
            item = (EventItem) savedInstanceState.getSerializable("eventItem");
        }
        getEventDetailsAsyncTask.setUserId(item.getEventId());
        try {
            getEventDetailsAsyncTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.btnStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTS = System.currentTimeMillis();
                for (int i = 0; i < locHistory.size() - 1; i++) {
                    distance += distFrom(locHistory.get(i), locHistory.get(i + 1));
                }
                distance = (double) Math.round(distance * 100.0) / 100.0;
                // TODO Update run record
                mGoogleApiClient.disconnect();
                locHistory.clear();

                item.getParticipants().remove(new PrefUtil(MapActivity.this).getEmailId());
               // item.getFinishedUsers().add(new PrefUtil(MapActivity.this).getEmailId());

                //Update DB
                User user = new User();
                try {
                    user = new GetUserAsyncTask().execute(new PrefUtil(MapActivity.this).getEmailId()).get();

                    for (RunRecord runRecord : user.getRunRecords()) {


                        if (runRecord.getEventId().trim().equalsIgnoreCase(item.getEventId())) {
                            runRecord.setDistanceRan(distance + "");
                            runRecord.setTimeRan("" + (endTS - startTS) / 60000);
                            latlongstr = runRecord.getPath();
                            break;
                        }


                    }
                    new AcceptEventAsyncTask().execute(item);
                    new UpdatePathCoordinatesAsyncTask().execute(user);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public static double distFrom(String ll1, String ll2) {
        String[] p1 = ll1.split(",");
        double lat1 = Double.parseDouble(p1[0]);
        double lng1 = Double.parseDouble(p1[1]);

        String[] p2 = ll2.split(",");
        double lat2 = Double.parseDouble(p2[0]);
        double lng2 = Double.parseDouble(p2[1]);
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);
        return dist;
    }

    @Override
    public void onBackPressed() {
    }

    public void addOrUpdateMarkers() {
        for (int i = 0; i < participants.size(); i++) {
            String currPart = participants.get(i);
            if (latlongs.containsKey(currPart)) {
                String[] ll = latlongs.get(currPart).split(",");
                double lat = Double.parseDouble(ll[0]);
                double lon = Double.parseDouble(ll[1]);
                LatLng l1 = new LatLng(lat, lon);
                Marker m1 = markers.get(currPart);
                if (m1 == null) {
                    m1 = mMap.addMarker(new MarkerOptions()
                            .position(l1).draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(colors[i % colors.length]))
                            .title(currPart));

                    markers.put(currPart, m1);

                } else {
                    m1.setPosition(l1);
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

//        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(provider);
        if (myLocation != null) {
            // Get latitude of the current location
            double latitude = myLocation.getLatitude();

            // Get longitude of the current location
            double longitude = myLocation.getLongitude();

            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Show the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
//        mRequestingLocationUpdates = true;
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }

//    @Override
//    public boolean onMyLocationButtonClick() {
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
//        // Return false so that we don't consume the event and the default behavior still occurs
//        // (the camera animates to the user's current position).
//        return false;
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//            updateUI();
        }
        startLocationUpdates();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        String cl = lat + "," + lon;
        if (!firstLoc) {
            locHistory.add(cl);
        }
        firstLoc = false;
        // TODO update current location to db and get others current location
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(location.getLatitude(), location.getLongitude()))
//                );
        Toast.makeText(this, currUser + ":" + cl, Toast.LENGTH_SHORT).show();


//Update DB
        User user = new User();
        try {
            user = new GetUserAsyncTask().execute(new PrefUtil(this).getEmailId()).get();
            user.setLatLang(new LatLang(location.getLatitude() + "", "" + location.getLongitude()));
            boolean present = false;
            for (RunRecord runRecord : user.getRunRecords()) {


                if (runRecord.getEventId().trim().equalsIgnoreCase(item.getEventId())) {
                    Log.i("message:latLang inside", "");
                    runRecord.setPath(runRecord.getPath() + "|" + location.getLatitude() + "=" + location.getLongitude());
                    Log.i("message:latLang", "" + runRecord.getPath());
                    present = true;
                }


            }
            if (!present) {
                RunRecord runRecord1 = new RunRecord();
                runRecord1.setEventId(item.getEventId());
                runRecord1.setEventName(item.getName());
                runRecord1.setDistanceRan("");
                runRecord1.setTimeRan("");
                runRecord1.setPath(location.getLatitude() + "=" + location.getLongitude());
                user.getRunRecords().add(runRecord1);
                Log.i("message:latLang", "" + runRecord1.getPath());
            }
            new UpdatePathCoordinatesAsyncTask().execute(user);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        new UpdateCurrentLocationAsyncTask().execute(user);

        //Update DB
//        boolean present = false;
//        for(int i=0;i<item.getCurrentLocationsAllParticipants().size();i++)
//        {
//            if(item.getCurrentLocationsAllParticipants().get(i).getEmailId().trim().equalsIgnoreCase(new PrefUtil(this).getEmailId()))
//            {
//                present=true;
//                item.getCurrentLocationsAllParticipants().get(i).setLongitude(location.getLongitude()+"");
//                item.getCurrentLocationsAllParticipants().get(i).setLatitude(location.getLatitude()+"");
//            }
//        }
//        if(!present)
//        {
//            Log.i("message: ","creating");
//            CurrentLocationAllParticipants participant = new CurrentLocationAllParticipants();
//            participant.setEmailId(new PrefUtil(this).getEmailId());
//            participant.setLatitude(location.getLatitude() + "");
//            participant.setLongitude(location.getLongitude() + "");
//            item.getCurrentLocationsAllParticipants().add(participant);
//        }
//
//        new UpdateCurrentLocationAsyncTask().execute(item);

        //Get other locations from DB

        List<User> users;
        try {
            users = new GetFriendRequestsAsyncTask().execute(item.getParticipants()).get();
            for (User user1 : users) {
                if (user1.getLatLang() != null) {
                    String currLocation = user1.getLatLang().getLatitude() + "," + user1.getLatLang().getLongitude();
                    latlongs.put(user1.getEmail(), currLocation);
                    Log.i("message:", "particiants" + user1.getEmail() + " - " + currLocation);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        addOrUpdateMarkers();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
}