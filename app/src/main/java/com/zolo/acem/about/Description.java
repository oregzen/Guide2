package com.zolo.acem.about;

import org.simpleframework.xml.Element;

public class Description {

    @Element
    private String content;

    public String getContent() {

        if (content == null) {
            content = "";
        }

        return content;
    }
}
