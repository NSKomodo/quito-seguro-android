package co.profapps.quitoseguro.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import co.profapps.quitoseguro.R;
import co.profapps.quitoseguro.firebase.FirebaseHelper;

public class StatsListFragment extends ListFragment {
    public static final String TAG = StatsListFragment.class.getSimpleName();

    String[] data = {"off_robbery", "off_violence", "off_express_kidnapping",
            "off_missing_person", "off_murder", "off_house_robbery", "off_store_robbery",
            "off_grand_theft_auto", "off_credit_card_cloning", "off_public_disorder"};
    long total = 0;
    boolean statsFetched = false;

    public static StatsListFragment newInstance() {
        return new StatsListFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(getString(R.string.report_statistics));

        getListView().setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.colorPrimaryDarkest));
        getListView().setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(),
                R.color.colorPrimary)));
        getListView().setDividerHeight(2);

        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_stats) {
            @Override
            public int getCount() {
                return data.length;
            }

            @Override
            public View getView(final int position, View convertView, final ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_stats,
                            parent, false);
                }

                int offenseId = getActivity().getResources().getIdentifier(data[position],
                        "string", getActivity().getPackageName());

                TextView offenseTextView = (TextView) convertView
                        .findViewById(R.id.offenseTextView);
                offenseTextView.setText(getString(offenseId));

                final TextView numberTextView = (TextView) convertView
                        .findViewById(R.id.numberTextView);

                Query query = FirebaseHelper.getReportsQueryRef(data[position]);
                final View finalConvertView = convertView;

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        numberTextView.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        total += dataSnapshot.getChildrenCount();

                        getActivity().setTitle(getString(R.string.report_statistics) +
                                " | Total: " + total);

                        statsFetched = position == dataSnapshot.getChildrenCount() - 1;
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                };

                query.addValueEventListener(valueEventListener);

                if (statsFetched) {
                    query.removeEventListener(valueEventListener);
                }

                return convertView;
            }
        });
    }
}
