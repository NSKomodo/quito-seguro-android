package co.profapps.quitoseguro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Locale;

import co.profapps.quitoseguro.fragment.TipDetailFragment;
import co.profapps.quitoseguro.model.Tip;

public class TipDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TIP = "co.profapps.quitoseguro.EXTRA_TIP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_detail);

        setupUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Locale locale = getResources().getConfiguration().locale;

        Tip tip = (Tip) getIntent().getSerializableExtra(EXTRA_TIP);
        setTitle(locale.getLanguage().equalsIgnoreCase("es") ? tip.getTitleES() : tip.getTitleEN());

        loadTipDetailFragment();
    }

    private void loadTipDetailFragment() {
        TipDetailFragment fragment = (TipDetailFragment) getSupportFragmentManager()
                .findFragmentByTag(TipDetailFragment.TAG);

        if (fragment == null) {
            fragment = TipDetailFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.tipDetailContainerLayout,
                fragment, TipDetailFragment.TAG).commit();
    }
}
