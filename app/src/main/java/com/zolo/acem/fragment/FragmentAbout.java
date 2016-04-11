package com.zolo.acem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zolo.acem.CityGuide;
import com.zolo.acem.MainActivity;
import com.zolo.acem.R;
import com.zolo.acem.about.AboutDataHolder;
import com.zolo.acem.adapter.AdapterAbout;
import com.zolo.acem.settings.AppSettings;
import com.zolo.acem.util.UtilView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AbsListView.LayoutParams;
import com.huewu.pla.lib.internal.PLA_AdapterView;

public class FragmentAbout extends Fragment {

    public static final String TAG = FragmentAbout.class.getSimpleName();
    private TextView mTitle;
    private TextView mSubTitle;
    private View mBackArrow;

    public static FragmentAbout newInstance() {

        FragmentAbout fragmentAbout = new FragmentAbout();

        return fragmentAbout;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fra_about, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View mMainContainer = getView().findViewById(R.id.fra_about_main_container);
        UtilView.setNoLollipopPadding(mMainContainer,10, 0);

        mBackArrow = getView().findViewById(R.id.fra_about_arrow_back);
        mBackArrow.setOnClickListener(new OnBackClickListener());

        mTitle = (TextView) getView().findViewById(R.id.fra_about_title);
        mTitle.setText(AppSettings.TOWN + ", " + AppSettings.COUNTRY);

        mSubTitle = (TextView) getView().findViewById(R.id.fra_about_subtitle);
        mSubTitle.setText(getString(R.string.about));

        MultiColumnListView listView = (MultiColumnListView) getView().findViewById(R.id.list);

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.fra_about_header, null);

        TextView headerTextView = (TextView) header.findViewById(R.id.fra_about_header_text);
        headerTextView.setText(AboutDataHolder.getInstance().getAbout().getDescription().getContent());
        headerTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        listView.addHeaderView(headerTextView);

        AdapterAbout adapterAbout = new AdapterAbout(AboutDataHolder.getInstance().getAbout().getPhotos(), getActivity());

        listView.setAdapter(adapterAbout);

        listView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, final int position, long id) {
                if (position > 0) {
                    ((MainActivity) getActivity()).showPhotoDialog(AboutDataHolder.getInstance().getAbout().getPhotos().get(position - 1));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        CityGuide application = (CityGuide)getActivity().getApplication();
        final Tracker tracker = application.getTracker();
        if(tracker != null){
            tracker.setScreenName(getClass().getSimpleName());
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    private class OnBackClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
