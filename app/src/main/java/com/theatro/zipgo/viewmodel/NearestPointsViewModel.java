package com.theatro.zipgo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.theatro.zipgo.POJO.DistanceInfo;
import com.theatro.zipgo.POJO.PlaceList;
import com.theatro.zipgo.Utils;
import com.theatro.zipgo.model.PrefUtils;

import java.util.ArrayList;

public class NearestPointsViewModel extends AndroidViewModel {
    private ArrayList<PlaceList> placeList;
    ArrayList<DistanceInfo> distanceInfoArrayList;

    public NearestPointsViewModel(@NonNull Application application) {
        super(application);
    }

    public ArrayList<PlaceList> getplaceList() {
        if (placeList == null) {
            placeList = loadPlaces();
        }
        return placeList;
    }

    public ArrayList<DistanceInfo> getNearestPlaceList() {
        if (distanceInfoArrayList == null) {
            distanceInfoArrayList = loadNearestPlaces();
        }
        return distanceInfoArrayList;
    }

    private ArrayList<DistanceInfo> loadNearestPlaces() {
        return Utils.calculateDistanceMatrix(getplaceList());
    }


    private ArrayList<PlaceList> loadPlaces() {
        return PrefUtils.getPlaceList(getApplication());
    }

    private void savePlaces(ArrayList<PlaceList> placeLists) {
        PrefUtils.savePlaceList(placeLists, getApplication());
    }
}
