package co.profapps.quitoseguro.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.util.Locale;

import co.profapps.quitoseguro.R;
import co.profapps.quitoseguro.TipDetailActivity;
import co.profapps.quitoseguro.model.Tip;
import co.profapps.quitoseguro.util.AppUtils;

public class TipDetailFragment extends Fragment {
    public static final String TAG = TipsListFragment.class.getSimpleName();

    Tip tip;

    public static TipDetailFragment newInstance() {
        return new TipDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tip_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setupUI(view);
    }

    private void setupUI(@NonNull  View view) {
        Locale locale = getResources().getConfiguration().locale;
        tip = (Tip) getActivity().getIntent().getSerializableExtra(TipDetailActivity.EXTRA_TIP);

        SmartImageView imageView = (SmartImageView) view.findViewById(R.id.imageView);

        DisplayMetrics displayMetrics = getContext().getResources()
                .getDisplayMetrics();

        int size = displayMetrics.widthPixels;

        imageView.getLayoutParams().width = size;
        imageView.getLayoutParams().height = size / 2;
        imageView.setImageUrl(tip.getHeader());

        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(locale.getLanguage().equalsIgnoreCase("es") ?
                tip.getTextES().replaceAll("###", "\n\n") :
                tip.getTextEN().replaceAll("###", "\n\n"));

        Button shareButton = (Button) view.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeShare();
            }
        });
    }

    private void executeShare() {
        Locale locale = getResources().getConfiguration().locale;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(tip.getHeader()));
        intent.putExtra(Intent.EXTRA_SUBJECT, locale.getLanguage().equalsIgnoreCase("es") ?
                tip.getTitleES() : tip.getTitleEN());
        intent.putExtra(Intent.EXTRA_TEXT, locale.getLanguage().equalsIgnoreCase("es") ?
                tip.getTextES().replaceAll("###", "\n\n"):
                tip.getTextEN().replaceAll("###", "\n\n") + "\n\n" +
                        getString(R.string.download_app_at) + "\n\n" + AppUtils.PLAY_STORE_URL);

        getActivity().startActivity(Intent.createChooser(intent,
                getString(R.string.share_tip_title)));
    }
}
