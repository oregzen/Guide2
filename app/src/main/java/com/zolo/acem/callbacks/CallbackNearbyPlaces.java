package com.zolo.acem.callbacks;

import com.zolo.acem.googleplaces.Constants;
import com.zolo.acem.googleplaces.models.Place;

import java.util.Set;

public interface CallbackNearbyPlaces {

    void onPlaceClicked(Place place);

    void onPlacesLoaded(Set<Place> places, Constants.PLACE_TYPES type);
}
