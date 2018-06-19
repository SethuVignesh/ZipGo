package com.theatro.zipgo.view;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.theatro.zipgo.POJO.DistanceInfo;
import com.theatro.zipgo.POJO.PlaceList;
import com.theatro.zipgo.R;
import com.theatro.zipgo.view.adapters.MyAdapter;
import com.theatro.zipgo.viewmodel.NearestPointsViewModel;

import java.util.ArrayList;

public class NearestPointsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    ArrayList<DistanceInfo> distanceInfoArrayList;
    ArrayList<PlaceList> nearestPlaces = new ArrayList<>();
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_location);
        emptyView = findViewById(R.id.empty_view);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nearest Points");
        getSupportActionBar().setIcon(R.mipmap.image003);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        mRecyclerView = findViewById(R.id.distanceList);

        //LOADING DATA FROM VIEW MODEL
        NearestPointsViewModel nearestPointsViewModel = ViewModelProviders.of(this).get(NearestPointsViewModel.class);
        ArrayList<PlaceList> placeLists = nearestPointsViewModel.getplaceList();
        distanceInfoArrayList = nearestPointsViewModel.getNearestPlaceList();

        if (!distanceInfoArrayList.isEmpty()) {
            emptyView.setVisibility(View.GONE);
            nearestPlaces.add(distanceInfoArrayList.get(0).getSrcPlace());
            nearestPlaces.add(distanceInfoArrayList.get(0).getDestPlace());
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(nearestPlaces, NearestPointsActivity.this, false);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
