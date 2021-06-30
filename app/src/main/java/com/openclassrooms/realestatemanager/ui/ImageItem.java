package com.openclassrooms.realestatemanager.ui;

import android.widget.ImageView;

public class ImageItem {

    private String uri;

    public ImageItem(String imageUri) {
        uri = imageUri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
