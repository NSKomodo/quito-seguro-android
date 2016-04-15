package co.profapps.quitoseguro.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import co.profapps.quitoseguro.R;
import co.profapps.quitoseguro.model.Report;
import co.profapps.quitoseguro.util.AppUtils;

public class CreateReportFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    public static final String TAG = CreateReportFragment.class.getSimpleName();
    private static final int RC_PERMISSION_LOCATION = 0x0001;

    GoogleMap map;
    MenuItem sendItem;

    LocationManager locationManager;
    Location myLocation;
    boolean canAccessLocation;
    Report report = new Report();

    public static CreateReportFragment newInstance() {
        return new CreateReportFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        canAccessLocation = Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeLocationManager();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create, menu);
        sendItem = menu.findItem(R.id.action_send);
        sendItem.getIcon().setColorFilter(ContextCompat.getColor(getContext(),
                R.color.colorLightGrayText), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().finish();
        }

        if (id == R.id.action_send) {
            sendReport();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION_LOCATION) {
            canAccessLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }

        if (canAccessLocation) {
            initializeLocationManager();
        }
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
    }

    private void drawBounds() {
        map.clear();

        PolylineOptions options = new PolylineOptions();
        options.add(new LatLng(-0.0413704, -78.5881530), new LatLng(-0.3610194, -78.5881530),
                new LatLng(-0.3610194, -78.4078789), new LatLng(-0.0413704, -78.4078789),
                new LatLng(-0.0413704, -78.5881530))
                .color(ContextCompat.getColor(getActivity(), R.color.colorAccent))
                .geodesic(true);

        Polyline polyline = map.addPolyline(options);
    }

    private void sendReport() {
        // TODO: implement
    }

    private void initializeLocationManager() {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,
                        this);
            }

            setupMap();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        RC_PERMISSION_LOCATION);
            }
        }
    }

    // region Map ready callback

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }

        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        if (myLocation != null && AppUtils.isValidLocation(myLocation)) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(),
                    myLocation.getLongitude()), 17));
        } else {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(AppUtils.QUITO_LAT_LNG, 11));
        }

        drawBounds();
    }

    // endregion

    // region Location listener

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;

        if (AppUtils.isValidLocation(myLocation)) {
            map.setMyLocationEnabled(true);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(),
                    myLocation.getLongitude()), 17));
        }

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
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

    // endregion
}
