package com.zolo.acem.callbacks;

import com.zolo.acem.googleplaces.models.Place;

import java.util.Set;


public interface CallbackSearchDone {

    void getSearchResult(Set<Place> places);

    void showSearchLoading();

    void hideSearchLoading();

}
