package com.theatro.zipgo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.theatro.zipgo.POJO.PlaceList;
import com.theatro.zipgo.model.PrefUtils;

import java.util.ArrayList;

public class HomeActivityViewModel extends AndroidViewModel {
    private ArrayList<PlaceList> placeList;

    public HomeActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public ArrayList<PlaceList> getplaceList() {
        if (placeList == null) {
            placeList = loadPlaces();
        }
        return placeList;
    }

    public ArrayList<PlaceList> setplaceList(ArrayList<PlaceList> placeLists) {
        if (placeList == null) {
            placeList = loadPlaces();
        }
        savePlaces(placeLists);
        return placeList;
    }

    private ArrayList<PlaceList> loadPlaces() {
        return PrefUtils.getPlaceList(getApplication());
    }

    private void savePlaces(ArrayList<PlaceList> placeLists) {
        PrefUtils.savePlaceList(placeLists, getApplication());
    }
}
