package co.profapps.quitoseguro.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Calendar;
import java.util.GregorianCalendar;

import co.profapps.quitoseguro.R;
import co.profapps.quitoseguro.firebase.FirebaseHelper;
import co.profapps.quitoseguro.model.Report;
import co.profapps.quitoseguro.util.AppUtils;

public class CreateReportFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnCameraChangeListener, LocationListener {
    public static final String TAG = CreateReportFragment.class.getSimpleName();
    private static final int RC_PERMISSION_LOCATION = 0x0001;

    GoogleMap map;
    MenuItem sendItem;

    LocationManager locationManager;
    Location myLocation;
    LatLng selectedLatLng;
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

        setupUI();
        initializeLocationManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(this);
            }
        }
    }

    private void setupUI() {
        if (getView() != null) {
            View view = getView();

            view.findViewById(R.id.offenseLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOffensePopupMenu();
                }
            });

            view.findViewById(R.id.dateLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog();
                }
            });
        }
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
                .width(2)
                .color(ContextCompat.getColor(getActivity(), R.color.colorAccent))
                .geodesic(true);

        map.addPolyline(options);
    }

    private void sendReport() {
        report.setPlatform("Android");

        FirebaseHelper.getReportsDataRef().push().setValue(report);

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.report_sent))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

    private void showOffensePopupMenu() {
        if (getView() != null) {
            PopupMenu popup = new PopupMenu(getContext(), getView()
                    .findViewById(R.id.offenseLayout));
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.offenses, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.action_filter_robbery) {
                        report.setOffense("off_robbery");
                    }

                    if (id == R.id.action_filter_violence) {
                        report.setOffense("off_violence");
                    }

                    if (id == R.id.action_filter_express_kidnapping) {
                        report.setOffense("off_express_kidnapping");
                    }

                    if (id == R.id.action_filter_missing_person) {
                        report.setOffense("off_missing_person");
                    }

                    if (id == R.id.action_filter_murder) {
                        report.setOffense("off_murder");
                    }

                    if (id == R.id.action_filter_house_robbery) {
                        report.setOffense("off_house_robbery");
                    }

                    if (id == R.id.action_filter_store_robbery) {
                        report.setOffense("off_store_robbery");
                    }

                    if (id == R.id.action_filter_grand_theft_auto) {
                        report.setOffense("off_grand_theft_auto");
                    }

                    if (id == R.id.action_filter_credit_card_cloning) {
                        report.setOffense("off_credit_card_cloning");
                    }

                    if (id == R.id.action_filter_public_disorder) {
                        report.setOffense("off_public_disorder");
                    }

                    TextView offenseTextView = (TextView) getView()
                            .findViewById(R.id.offenseTextView);
                    int offenseId = getResources().getIdentifier(report.getOffense(), "string",
                            getContext().getPackageName());
                    offenseTextView.setText(getString(offenseId));

                    validate();
                    return false;
                }
            });

            popup.show();
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar gregorianCalendar =
                                new GregorianCalendar(year, month, dayOfMonth);

                        report.setDate(gregorianCalendar.getTime().getTime());

                        if (getView() != null) {
                            TextView dateTextView = (TextView) getView().findViewById(R.id.dateTextView);
                            dateTextView.setText(AppUtils.getFormattedDate(report.getDate()));
                        }

                        validate();
                    }
                }, year, month, date);

        datePickerDialog.show();
    }

    private void validate() {
        boolean isValid = report.getOffense() != null && report.getDate() != 0 &&
                AppUtils.isValidLocation(selectedLatLng);

        if (isValid) {
            sendItem.setEnabled(true);
            sendItem.getIcon().setColorFilter(ContextCompat.getColor(getContext(),
                    android.R.color.white), PorterDuff.Mode.MULTIPLY);
        } else {
            sendItem.setEnabled(false);
            sendItem.getIcon().setColorFilter(ContextCompat.getColor(getContext(),
                    R.color.colorLightGrayText), PorterDuff.Mode.MULTIPLY);
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

        map.setOnCameraChangeListener(this);

        drawBounds();
    }



    // endregion

    // region Camera change listener

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        selectedLatLng = new LatLng(cameraPosition.target.latitude,
                cameraPosition.target.longitude);

        report.setLat(cameraPosition.target.latitude);
        report.setLng(cameraPosition.target.longitude);

        validate();
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
