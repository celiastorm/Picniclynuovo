package com.picnicly.picnic_ly;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Giovy on 05/03/2017.
 */

public class Luogo {

    private String gd;
    private String ind;
    private float voto;
    private int nvoti;



    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Luogo() {
    }

    Luogo(String gd, String ind, float voto, int nvoti) {
        this.gd = gd;
        this.ind = ind;
        this.voto = voto;
        this.nvoti = nvoti;
    }

    public String getGd() {
        return gd;
    }

    public String getInd() {
        return ind;
    }

    public float getVoto() {
        return voto;
    }

    public int getNvoti() {
        return nvoti;
    }
}
