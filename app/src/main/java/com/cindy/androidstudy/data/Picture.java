package com.cindy.androidstudy.data;

public class Picture {

    private String thumbID;
    private String thumbData;
    private int orientation;

    public Picture(String thumbID, String thumbData, int orientation) {
        this.thumbID = thumbID;
        this.thumbData = thumbData;
        this.orientation = orientation;
    }

    public String getThumbID() {
        return thumbID;
    }

    public void setThumbID(String thumbID) {
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
