package com.picnicly.picnic_ly;

//PER DATABASE

public class Vis {
    public String id;
    public String name;
    public Double lat;
    public Double lon;
    private int imageResId;
    private int imageResId2;

    public Vis() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Vis(String id, String name, Double lat, Double lon) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public int getImageResId2() { return imageResId2; }

    public void setImageResId2(int imageResId2) { this.imageResId2 = imageResId2; }

    public Double getLat() { return lat; }

    public Double getLon() { return  lon; }


}
