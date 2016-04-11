package com.zolo.acem.about;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class About {

    @ElementList
    private List<Photo> photos;

    @Element
    private Description description;

    public List<Photo> getPhotos() {
        return photos;
    }

    public Description getDescription() {

        if(description == null){
            description = new Description();
        }

        return description;
    }
}