package com.zolo.acem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zolo.acem.about.AboutDataLoader;
import com.zolo.acem.about.Photo;
import com.zolo.acem.db.DatabaseManager;
import com.zolo.acem.fragment.DialogFragmentLoading;
import com.zolo.acem.fragment.DialogFragmentPhoto;
import com.zolo.acem.fragment.FragmentAbout;
import com.zolo.acem.fragment.FragmentForecast;
import com.zolo.acem.fragment.FragmentHome;
import com.zolo.acem.fragment.FragmentListPlaces;
import com.zolo.acem.fragment.FragmentNearby;
import com.zolo.acem.fragment.FragmentPlaceDetail;
import com.zolo.acem.fragment.FragmentReviews;
import com.zolo.acem.googleplaces.Constants;
import com.zolo.acem.googleplaces.NearbyPlacesManager;
import com.zolo.acem.googleplaces.models.Place;
import com.zolo.acem.googleplaces.models.PlaceDetails;
import com.zolo.acem.network.NetworkFetcher;
import com.zolo.acem.settings.AppSettings;
import com.zolo.acem.util.ImageOptionsBuilder;
import com.zolo.acem.util.ThemeUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.ImageLoader;


public class MainActivity extends FragmentActivity {

    public static float density;

    public static Context context;

    protected AdView mAdmobView;

    public static String getApiKey() {
        return context.getString(R.string.google_api_key);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        //Fabric.with(this, new Crashlytics());

        ThemeUtil.setTranslucentTheme(this);

        density = getResources().getDisplayMetrics().density;

        setContentView(R.layout.activity_main);

        DatabaseManager.initWithContext(this);

        ImageLoader.getInstance().init(
                ImageOptionsBuilder.createImageLoaderConfiguration(this));

        Constants.DEFAULT_PHOTO_WIDTH = getResources().getDisplayMetrics().widthPixels;

        showHomeFragment();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NearbyPlacesManager.getInstance();
            }
        }, 1000);

        new AboutDataLoader().loadData(this);

        initAdmob();
    }




    public void showNearbyFragment(Constants.PLACE_TYPES type, Place placeDetail, boolean showCurrentLocation) {
        showScreen(FragmentNearby.newInstance(type, placeDetail, showCurrentLocation), FragmentNearby.TAG, true, false);
    }

    public boolean isNearbyFragmentShowed() {
        return getSupportFragmentManager().findFragmentByTag(FragmentNearby.TAG) != null;
    }

    public void showListFragment(Constants.PLACE_TYPES type) {
        showScreen(FragmentListPlaces.newInstance(type), FragmentListPlaces.TAG, true, false);
    }

    private void showHomeFragment() {
        showScreen(FragmentHome.newInstance(), FragmentHome.TAG, false, false);
    }

    public void showForecastFragment() {

        showScreen(FragmentForecast.newInstance(), FragmentForecast.TAG, true, false);
    }

    public void showAboutFragment() {

        showScreen(FragmentAbout.newInstance(), FragmentAbout.TAG, true, false);
    }

    public void showLoadingDialog() {
        DialogFragmentLoading.newInstance().show(getSupportFragmentManager(), DialogFragmentLoading.TAG);
    }

    public void showPhotoDialog(Photo photo) {
        DialogFragmentPhoto.newInstance(photo).show(getSupportFragmentManager(), DialogFragmentPhoto.TAG);
    }


    public void showPlaceDetailFragment(Place place, Constants.PLACE_TYPES type) {
        FragmentPlaceDetail fragmentPlaceDetail = FragmentPlaceDetail.newInstance(place, type);

        showScreen(fragmentPlaceDetail, FragmentPlaceDetail.TAG, true, false);
    }

    public void showReviewsScreen(PlaceDetails placeDetails, Constants.PLACE_TYPES type) {
        FragmentReviews fragmentReviews = FragmentReviews.newInstance(placeDetails, type);

        showScreen(fragmentReviews, FragmentReviews.TAG, true, false);
    }


    public void hideLoadingDialog() {

        Fragment loadingFragment = getSupportFragmentManager().findFragmentByTag(DialogFragmentLoading.TAG);

        if (loadingFragment != null) {
            ((DialogFragmentLoading) loadingFragment).dismiss();
        }
    }

    protected void initAdmob() {
        mAdmobView = (AdView) findViewById(R.id.home_admob);
        if (AppSettings.ENABLE_ADMOB) {
            mAdmobView.setVisibility(View.VISIBLE);
            AdRequest.Builder builder =  new AdRequest.Builder();
            AdRequest adRequest = builder.build();

            // Start loading the ad in the background.
            mAdmobView.loadAd(adRequest);

        } else {
            mAdmobView.setVisibility(View.GONE);
        }
    }

    private void showScreen(Fragment content,
                            String contentTag, boolean addToBackStack, boolean clearBackStack) {

        if (!NetworkFetcher.isNetworkConnected(MainActivity.this) && !contentTag.equalsIgnoreCase(FragmentHome.TAG)) {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

            return;
        }

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.left_slide_in, R.anim.left_slide_out,
                R.anim.right_slide_in, R.anim.right_slide_out);


        ft.replace(R.id.activity_main_content, content, contentTag);


        if (clearBackStack) {
            fm.popBackStackImmediate(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (addToBackStack) {
            ft.addToBackStack(String.valueOf(System.identityHashCode(content)));
        }

        ft.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }
}
