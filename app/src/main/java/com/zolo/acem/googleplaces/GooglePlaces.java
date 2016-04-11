package com.zolo.acem.googleplaces;

import android.util.Log;

import com.zolo.acem.MainActivity;
import com.zolo.acem.callbacks.CallbackNearbyPlaces;
import com.zolo.acem.db.DatabaseManager;
import com.zolo.acem.db.cmn.DbNetwork;
import com.zolo.acem.googleplaces.models.DetailsResult;
import com.zolo.acem.googleplaces.models.Place;
import com.zolo.acem.googleplaces.models.PlaceDetails;
import com.zolo.acem.googleplaces.models.PlaceReview;
import com.zolo.acem.googleplaces.models.PlacesResult;
import com.zolo.acem.googleplaces.query.DetailsQuery;
import com.zolo.acem.googleplaces.query.GooglePlusQuery;
import com.zolo.acem.googleplaces.query.NearbySearchQuery;
import com.zolo.acem.googleplaces.query.TextSearchQuery;
import com.zolo.acem.network.NetworkFetcher;
import com.zolo.acem.settings.AppSettings;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GooglePlaces {

    private final LatLng mLocation;

    public GooglePlaces(LatLng location) {
        this.mLocation = location;
    }

    Set<Place> resultPlaces = new HashSet<Place>();

    public void getPlacesNearby(NearbySearchQuery query, CallbackNearbyPlaces callbackNearbyPlaces, Constants.PLACE_TYPES type)
            throws JSONException, IOException, InterruptedException {

        Log.i("Loaded_Markers", "Executed query = " + query.toString());

        PlacesResult result = new PlacesResult(NetworkFetcher.executeRequest(query.toString(), false));

        callbackNearbyPlaces.onPlacesLoaded(result.getPlaces(), type);

        Log.i("Loaded_Markers", "NextPageToken " + result.getNextPageToken());
    }



    public PlacesResult getPlacesSearch(String searchText, List<String> types)
            throws JSONException, IOException {
        TextSearchQuery query = new TextSearchQuery(searchText);
        query.setLocation(mLocation.latitude, mLocation.longitude);
        query.setRadius(AppSettings.GOOGLE_PLACES_SEARCH_RADIUS);
        query.addTypes(types);

        PlacesResult result = new PlacesResult(NetworkFetcher.executeRequest(query.toString(), false));

        return result;
    }

    public DetailsResult getPlaceDetails(String reference)
            throws JSONException, IOException {

        DbNetwork dbNetwork = DatabaseManager.getInstance().findNetworkQuery(reference);


        DetailsQuery query = new DetailsQuery(reference);
        query.setKey(MainActivity.getApiKey());

        JSONObject response = NetworkFetcher.executeRequest(query.toString(), true);
        DetailsResult result = new DetailsResult(response);

        return result;
    }

    public void getPlaceDetailsReviews(PlaceDetails placeDetails)
            throws JSONException, IOException {
        try {
            List<PlaceReview> reviews = placeDetails.getReviews();

            for (int i = 0; i < reviews.size(); i++) {

                GooglePlusQuery googlePlusQuery = new GooglePlusQuery();
                googlePlusQuery.setGooglePlacePersonId(reviews.get(i).getAuthorPhotoUrl());

                JSONObject googlePlusObject = NetworkFetcher.executeRequest(googlePlusQuery.toString(), true);

                String actualPhotoUrl = googlePlusObject.getJSONObject("image").getString("url");
                actualPhotoUrl = actualPhotoUrl.replace("50", "100");

                reviews.get(i).setAuthorPhotoUrl(actualPhotoUrl);
            }
        } catch (Exception e) {
            Log.i("LOG_PLACE_DETAIL", "Error getting avatar image");
        }
    }

    public NearbySearchQuery getDefaultNearbySearchQuery(List<String> types) {

        NearbySearchQuery nearbySearchQuery = new NearbySearchQuery(mLocation.latitude, mLocation.longitude);

        nearbySearchQuery.setRadius(AppSettings.GOOGLE_PLACES_LOCATION_RADIUS);

        nearbySearchQuery.addTypes(types);

        nearbySearchQuery.setKey(MainActivity.getApiKey());

        return nearbySearchQuery;
    }

}
