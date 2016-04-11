package com.zolo.acem.about;

import android.content.Context;

import org.simpleframework.xml.Element;

public class Photo {

    @Element
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public int getDrawableId(Context context) {
        return context.getResources().getIdentifier(path, "drawable",
                context.getPackageName());
    }
}
