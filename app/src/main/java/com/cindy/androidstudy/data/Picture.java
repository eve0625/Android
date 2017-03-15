package com.cindy.androidstudy.data;

public class Picture {

    private int thumbID;
    private String thumbData;
    private int orientation;

    public Picture(int thumbID, String thumbData, int orientation) {
        this.thumbID = thumbID;
        this.thumbData = thumbData;
        this.orientation = orientation;
    }

    public int getThumbID() {
        return thumbID;
    }

    public void setThumbID(int thumbID) {
        this.thumbID = thumbID;
    }

    public String getThumbData() {
        return thumbData;
    }

    public void setThumbData(String thumbData) {
        this.thumbData = thumbData;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
