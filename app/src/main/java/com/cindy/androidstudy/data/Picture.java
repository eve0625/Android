package com.cindy.androidstudy.data;

public class Picture {

    private String thumbID;
    private String thumbData;

    public Picture(String thumbID, String thumbData) {
        this.thumbID = thumbID;
        this.thumbData = thumbData;
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

}
