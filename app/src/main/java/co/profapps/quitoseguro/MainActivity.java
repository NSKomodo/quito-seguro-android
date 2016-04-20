package co.profapps.quitoseguro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import co.profapps.quitoseguro.firebase.FirebaseHelper;
import co.profapps.quitoseguro.fragment.AboutFragment;
import co.profapps.quitoseguro.fragment.ReportsMapFragment;
import co.profapps.quitoseguro.fragment.StatsListFragment;
import co.profapps.quitoseguro.fragment.TipsListFragment;
import co.profapps.quitoseguro.util.AppUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseHelper.initialize(this);
        setupUI();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_reports) {
            loadReportsMapFragment();
        } else if (id == R.id.nav_stats) {
            loadStatsListFragment();
        } else if (id == R.id.nav_tips) {
            loadTipsListFragment();
        } else if (id == R.id.nav_about) {
            loadAboutFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupUI() {
        AppUtils.setAppBarColorOnRecentWindows(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateReportActivity();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadReportsMapFragment();
    }

    private void loadReportsMapFragment() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.show();

        ReportsMapFragment fragment = (ReportsMapFragment) getSupportFragmentManager()
                .findFragmentByTag(ReportsMapFragment.TAG);

        if (fragment == null) {
            fragment = ReportsMapFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, fragment,
                ReportsMapFragment.TAG).commit();
    }

    private void loadStatsListFragment() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        StatsListFragment fragment = (StatsListFragment) getSupportFragmentManager()
                .findFragmentByTag(StatsListFragment.TAG);

        if (fragment == null) {
            fragment = StatsListFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, fragment,
                StatsListFragment.TAG).commit();
    }

    private void loadAboutFragment() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        AboutFragment fragment = (AboutFragment) getSupportFragmentManager()
                .findFragmentByTag(AboutFragment.TAG);

        if (fragment == null) {
            fragment = AboutFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, fragment,
                AboutFragment.TAG).commit();
    }

    private void loadTipsListFragment() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        TipsListFragment fragment = (TipsListFragment) getSupportFragmentManager()
                .findFragmentByTag(TipsListFragment.TAG);

        if (fragment == null) {
            fragment = TipsListFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, fragment,
                TipsListFragment.TAG).commit();
    }

    private void startCreateReportActivity() {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }
}
