package com.zolo.acem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.zolo.acem.callbacks.CallbackSearchDone;
import com.zolo.acem.db.DatabaseManager;
import com.zolo.acem.googleplaces.Constants;
import com.zolo.acem.googleplaces.GooglePlaces;
import com.zolo.acem.googleplaces.models.Place;
import com.zolo.acem.googleplaces.models.PlacesResult;
import com.zolo.acem.settings.AppSettings;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dobrikostadinov on 6/15/15.
 */
public class OnSearchEditTextFetching extends AsyncTask<Void, Void, Void> {

    private CallbackSearchDone callbackSearchDone;
    List<String> types;
    String keyword;
    private Set<Place> filteredResultItems;

    public OnSearchEditTextFetching(String keyword, List<String> types, CallbackSearchDone callbackSearchDone) {
        this.keyword = keyword;
        this.types = types;
        this.callbackSearchDone = callbackSearchDone;
    }

    @Override
    protected Void doInBackground(Void... params) {

        GooglePlaces googlePlaces = new GooglePlaces(new LatLng(AppSettings.LATITUDE, AppSettings.LONGITUDE));

        try {
            if (types.get(0).equalsIgnoreCase(Constants.PLACE_TYPES.GOOGLE_PLACES_FAV.getType())) {

                filteredResultItems = new HashSet<>();

                Set<Place> favItemsAsSet = DatabaseManager.getInstance().getRealFavPlace();

                List<Place> favItems = new ArrayList<>(favItemsAsSet);

                for (int i = 0; i < favItems.size(); i++) {
                    if (favItems.get(i).getName().toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
                        filteredResultItems.add(favItems.get(i));
                    }
                }
            } else {
                PlacesResult placesResult = googlePlaces.getPlacesSearch(keyword, types);
                filteredResultItems = placesResult.getPlaces();
            }

        } catch (Exception e) {
            Log.i("Search_Query", "Exception - " + e.getMessage());
        }

        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callbackSearchDone.showSearchLoading();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (filteredResultItems != null) {

            callbackSearchDone.getSearchResult(filteredResultItems);
        }
        callbackSearchDone.hideSearchLoading();
    }
}