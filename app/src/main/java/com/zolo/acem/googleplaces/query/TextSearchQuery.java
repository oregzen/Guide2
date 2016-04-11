package com.zolo.acem.googleplaces.query;

import com.zolo.acem.MainActivity;

public class TextSearchQuery extends SearchQuery {

    public TextSearchQuery(String query) {

        setKey(MainActivity.getApiKey());

        setQuery(query);
    }

    public void setQuery(String query) {
        mQueryBuilder.addParameter("query", query);
    }

    @Override
    public String getUrl() {
        return "https://maps.googleapis.com/maps/api/place/textsearch/json";
    }
}
