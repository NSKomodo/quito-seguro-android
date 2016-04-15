package co.profapps.quitoseguro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;

import co.profapps.quitoseguro.LegalNoticesActivity;
import co.profapps.quitoseguro.R;

public class AboutFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = AboutFragment.class.getSimpleName();

    private GoogleMap map;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI(view);
    }

    private void setupUI(@NonNull View view) {
        getActivity().setTitle(getString(R.string.about_quito_seguro));
        view.findViewById(R.id.legalNoticesLayout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.legalNoticesLayout) {
            Intent intent = new Intent(getContext(), LegalNoticesActivity.class);
            startActivity(intent);
        }
    }
}
