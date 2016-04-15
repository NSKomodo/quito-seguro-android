package co.profapps.quitoseguro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;

public class LegalNoticesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_notices);

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

        TextView legalNoticesTextView = (TextView) findViewById(R.id.legalNoticesTextView);
        legalNoticesTextView.setText(GoogleApiAvailability.getInstance()
                .getOpenSourceSoftwareLicenseInfo(this));
    }
}
