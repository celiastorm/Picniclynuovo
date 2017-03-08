package com.picnicly.picnic_ly;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Giovy on 05/03/2017.
 */

public class PuntiPicnic {

    private String nome;
    private Double lat;
    private Double lon;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private PuntiPicnic() {
    }

    PuntiPicnic(String nome, Double lat, Double lon) {
        this.nome = nome;
        this.lat = lat;
        this.lon = lon;
    }

    public String getNome() {
        return nome;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }
}
