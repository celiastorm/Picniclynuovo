package com.picnicly.picnic_ly;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Giovy on 11/03/2017.
 */

public class Rate {

    private float voto;
    private int nvoti;


    // Required default constructor for Firebase object mapping

    public Rate() {
    }

    Rate(float voto, int nvoti) {
        this.voto = voto;
        this.nvoti = nvoti;
    }

    public float getVoto() {
        return voto;
    }

    public int getNvoti() {
        return nvoti;
    }

    public void setVoto(float voto) {this.voto = voto;}

    public void setNvoti(int nvoti) {
        this.nvoti = nvoti;
    }

}
