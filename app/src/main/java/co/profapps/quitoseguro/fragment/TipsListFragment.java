package co.profapps.quitoseguro.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.profapps.quitoseguro.R;
import co.profapps.quitoseguro.TipDetailActivity;
import co.profapps.quitoseguro.firebase.FirebaseHelper;
import co.profapps.quitoseguro.model.Tip;

public class TipsListFragment extends ListFragment {
    public static final String TAG = TipsListFragment.class.getSimpleName();

    List<Tip> data = new ArrayList<>();

    public static TipsListFragment newInstance() {
        return new TipsListFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(getString(R.string.security_tips));

        getListView().setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.colorPrimaryDarkest));
        getListView().setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(),
                R.color.colorPrimary)));
        getListView().setDividerHeight(2);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reloadData();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Tip tip = (Tip) l.getItemAtPosition(position);

        Intent intent = new Intent(getActivity(), TipDetailActivity.class);
        intent.putExtra(TipDetailActivity.EXTRA_TIP, tip);

        startActivity(intent);
    }

    private void reloadData() {
        final Query query = FirebaseHelper.getTipsDataRef();
        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    data.clear();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Tip tip = child.getValue(Tip.class);
                        data.add(tip);
                    }

                    setListAdapter(new ArrayAdapter<Tip>(getActivity(), R.layout.item_stats, data) {
                        @Override
                        public View getView(final int position, View convertView,
                                            final ViewGroup parent) {
                            if (convertView == null) {
                                convertView = LayoutInflater.from(getActivity())
                                        .inflate(R.layout.item_tips, parent, false);
                            }

                            Locale locale = getResources().getConfiguration().locale;
                            Tip tip = getItem(position);

                            TextView titleTextView = (TextView) convertView
                                    .findViewById(R.id.titleTextView);
                            titleTextView.setText(locale.getLanguage().equalsIgnoreCase("es") ?
                                    tip.getTitleES() : tip.getTitleEN());

                            TextView summaryTextView = (TextView) convertView
                                    .findViewById(R.id.summaryTextView);
                            summaryTextView.setText(locale.getLanguage().equalsIgnoreCase("es") ?
                                    tip.getSummaryES() : tip.getSummaryEN());

                            SmartImageView imageView = (SmartImageView) convertView
                                    .findViewById(R.id.imageView);
                            imageView.setImageUrl(tip.getThumbnail());

                            return convertView;
                        }
                    });

                    query.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        query.addValueEventListener(valueEventListener);
    }
}
