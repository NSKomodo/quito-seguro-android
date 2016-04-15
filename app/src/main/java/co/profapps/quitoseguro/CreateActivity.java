package co.profapps.quitoseguro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import co.profapps.quitoseguro.fragment.CreateReportFragment;
import co.profapps.quitoseguro.util.AppUtils;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        setupUI();
    }

    private void setupUI() {
        AppUtils.setAppBarColorOnRecentWindows(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadCreateReportFragment();
    }

    private void loadCreateReportFragment() {
        CreateReportFragment fragment = (CreateReportFragment) getSupportFragmentManager()
                .findFragmentByTag(CreateReportFragment.TAG);

        if (fragment == null) {
            fragment = CreateReportFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.createContainerLayout, fragment,
                CreateReportFragment.TAG).commit();
    }
}
