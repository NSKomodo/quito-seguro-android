package co.profapps.quitoseguro.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import co.profapps.quitoseguro.R;
import co.profapps.quitoseguro.firebase.FirebaseHelper;
import co.profapps.quitoseguro.model.Report;
import co.profapps.quitoseguro.util.AppUtils;

public class ReportsMapFragment extends SupportMapFragment implements OnMapReadyCallback,
        LocationListener {
    public static final String TAG = ReportsMapFragment.class.getSimpleName();
    private static final int RC_PERMISSION_LOCATION = 0x0001;

    Query reportQuery;
    ValueEventListener valueListener;

    LocationManager locationManager;
    Location myLocation;
    GoogleMap map;
    boolean canAccessLocation;

    String offenseFilter = null;

    public static ReportsMapFragment newInstance() {
        return new ReportsMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        canAccessLocation = Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.reported_offenses));
        getMapAsync(this);
        initializeLocationManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (reportQuery != null && valueListener != null) {
            reportQuery.removeEventListener(valueListener);
        }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter_all) {
            offenseFilter = null;
        }

        if (id == R.id.action_filter_robbery) {
            offenseFilter = "off_robbery";
        }

        if (id == R.id.action_filter_violence) {
            offenseFilter = "off_violence";
        }

        if (id == R.id.action_filter_express_kidnapping) {
            offenseFilter = "off_express_kidnapping";
        }
        if (id == R.id.action_filter_missing_person) {
            offenseFilter = "off_missing_person";
        }
        if (id == R.id.action_filter_murder) {
            offenseFilter = "off_murder";
        }
        if (id == R.id.action_filter_house_robbery) {
            offenseFilter = "off_house_robbery";
        }
        if (id == R.id.action_filter_store_robbery) {
            offenseFilter = "off_store_robbery";
        }
        if (id == R.id.action_filter_grand_theft_auto) {
            offenseFilter = "off_grand_theft_auto";
        }
        if (id == R.id.action_filter_credit_card_cloning) {
            offenseFilter = "off_credit_card_cloning";
        }
        if (id == R.id.action_filter_public_disorder) {
            offenseFilter = "off_public_disorder";
        }

        populateMap(map);
        return super.onOptionsItemSelected(item);
    }

    private void initializeLocationManager() {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,
                        this);
            } else {
                prepareMap();
            }

            if (map != null) {
                populateMap(map);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        RC_PERMISSION_LOCATION);
            }
        }
    }

    private void prepareMap() {
        if (map != null) {
            map.getUiSettings().setMapToolbarEnabled(false);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(AppUtils.QUITO_LAT_LNG, 11));
        }

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
    }

    private void populateMap(@NonNull final GoogleMap googleMap) {
        reportQuery = FirebaseHelper.getReportsQueryRef(offenseFilter);
        valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                googleMap.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Report report = child.getValue(Report.class);
                    int offenseId = getResources().getIdentifier(report.getOffense(), "string",
                            getContext().getPackageName());

                    MarkerOptions options = new MarkerOptions()
                            .position(new LatLng(report.getLat(), report.getLng()))
                            .title(getString(offenseId))
                            .snippet(report.getDate())
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.pin_2));

                    googleMap.addMarker(options);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, "onCancelled: " +  firebaseError.getMessage());
            }
        };

        reportQuery.addValueEventListener(valueListener);
    }

    // region On map ready callback

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }

        populateMap(map);
        prepareMap();
    }

    // endregion

    // region Location listener

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        prepareMap();

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
