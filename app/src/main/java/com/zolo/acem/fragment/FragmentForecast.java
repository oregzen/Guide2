package com.zolo.acem.fragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zolo.acem.CityGuide;
import com.zolo.acem.MainActivity;
import com.zolo.acem.R;
import com.zolo.acem.adapter.AdapterForecast;
import com.zolo.acem.callbacks.CallbackForecastLoaded;
import com.zolo.acem.settings.AppSettings;
import com.zolo.acem.tasks.LoadForecast;
import com.zolo.acem.util.UtilView;
import com.zolo.acem.weather.cmn.ForecastDay;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

public class FragmentForecast extends Fragment implements CallbackForecastLoaded {

    public static final String TAG = FragmentForecast.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mTitle;
    private View mBackArrow;

    public static FragmentForecast newInstance() {

        FragmentForecast fragmentForecast = new FragmentForecast();

        return fragmentForecast;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fra_forecast, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View mParentView = getView().findViewById(R.id.fra_forecast_main_container);
        UtilView.setNoLollipopPadding(mParentView, 10, 0);
        UtilView.setLollipopPadding(mParentView, 0, 30, 0, 0);


        mBackArrow = getView().findViewById(R.id.fra_forecast_arrow_back);
        mBackArrow.setOnClickListener(new OnBackClickListener());

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.fra_forecast_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mTitle = (TextView) getView().findViewById(R.id.fra_forecast_title);
        mTitle.setText(AppSettings.TOWN + ", " + AppSettings.COUNTRY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new LoadForecast((MainActivity) getActivity(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new LoadForecast((MainActivity) getActivity(), this).execute();
        }
    }

    @Override
    public void doneLoadingForecast(List<ForecastDay> result) {
        if (result != null && isAdded()) {
            AdapterForecast mAdapter = new AdapterForecast(getActivity().getLayoutInflater(), result, getActivity());

            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void showLoadingWeather() {

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
