package com.zolo.acem.googleplaces.query;

import com.zolo.acem.MainActivity;
import com.zolo.acem.googleplaces.Constants;

public class GooglePlusQuery extends Query {

    private String googlePlacePersonId;

    public void setField() {

        mQueryBuilder.addParameter("fields", "image");
    }

    public void setGooglePlacePersonId(String googlePlacePersonId) {
        this.googlePlacePersonId = googlePlacePersonId;
    }

    @Override
    public String toString() {
        return getUrl();
    }

    @Override
    public String getUrl() {
        setField();
        setKey(MainActivity.getApiKey());

        return Constants.GOOGLE_PLUS_URL + googlePlacePersonId + mQueryBuilder.toString();
    }
}
