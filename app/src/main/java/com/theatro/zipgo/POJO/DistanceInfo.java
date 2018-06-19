package com.theatro.zipgo.POJO;

import com.google.android.gms.maps.model.LatLng;

public class DistanceInfo implements Comparable<DistanceInfo> {
    LatLng src;
    LatLng dest;
    double distance;
    PlaceList srcPlace;

    public PlaceList getSrcPlace() {
        return srcPlace;
    }

    public void setSrcPlace(PlaceList srcPlace) {
        this.srcPlace = srcPlace;
    }

    public PlaceList getDestPlace() {
        return destPlace;
    }

    public void setDestPlace(PlaceList destPlace) {
        this.destPlace = destPlace;
    }

    public DistanceInfo(double distance, PlaceList srcPlace, PlaceList destPlace) {

        this.distance = distance;
        this.srcPlace = srcPlace;
        this.destPlace = destPlace;
    }

    PlaceList destPlace;

    public DistanceInfo(LatLng src, LatLng dest, double distance) {
        this.src = src;
        this.dest = dest;
        this.distance = distance;
    }

    public LatLng getSrc() {

        return src;
    }

    public void setSrc(LatLng src) {
        this.src = src;
    }

    public LatLng getDest() {
        return dest;
    }

    public void setDest(LatLng dest) {
        this.dest = dest;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(DistanceInfo o) {
        if (this.distance < o.getDistance()) {
            return -1;
        }
        if (this.distance == o.getDistance()) {
            return 0;
        }
        return 1;
    }
}
