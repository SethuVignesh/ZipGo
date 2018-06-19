package com.theatro.zipgo;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.theatro.zipgo.POJO.DistanceInfo;
import com.theatro.zipgo.POJO.PlaceList;

import java.util.ArrayList;
import java.util.Collections;

public class Utils {


    private static double meterDistanceBetweenPoints(LatLng src, LatLng dest) {
        double lat_a = src.latitude;
        double lng_a = src.longitude;
        double lat_b = dest.latitude;
        double lng_b = dest.latitude;
        double pk = (double) (180.f / Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    private static double distance(LatLng src, LatLng dest) {

        float distance=0;
        Location crntLocation=new Location("crntlocation");
        crntLocation.setLatitude(src.latitude);
        crntLocation.setLongitude(src.longitude);

        Location newLocation=new Location("newlocation");
        newLocation.setLatitude(dest.latitude);
        newLocation.setLongitude(dest.longitude);


//float distance = crntLocation.distanceTo(newLocation);  in meters
        distance =crntLocation.distanceTo(newLocation) / 1000; // in km
        return distance;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static ArrayList<DistanceInfo> calculateDistanceMatrix(ArrayList<PlaceList> path) {
        ArrayList<DistanceInfo> distanceInfos = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            for (int k = i + 1; k < path.size(); k++) {
                if (path.get(i) != path.get(k)) {
                    distanceInfos.add(new DistanceInfo(distance(path.get(i).getLatLng(), path.get(k).getLatLng()), path.get(i), path.get(k)));
                }
            }
        }
        Collections.sort(distanceInfos);
        return distanceInfos;
    }
}
