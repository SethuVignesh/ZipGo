package com.theatro.zipgo.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theatro.zipgo.POJO.PlaceList;
import com.theatro.zipgo.R;
import com.theatro.zipgo.model.PrefUtils;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<PlaceList> placeList;
    private String[] mDataset;
    private Context cxt;
    private boolean showDelBtn;

    public MyAdapter(ArrayList<PlaceList> placeLists, Context cxt, boolean showDelBtn) {
        this.placeList = placeLists;
        this.cxt = cxt;
        this.showDelBtn = showDelBtn;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mName;
        public TextView mLat;
        public TextView mLng;
        public ImageView mDelete;

        public ViewHolder(View v) {
            super(v);
            mName = v.findViewById(R.id.name_of_data);
            mLat = v.findViewById(R.id.latitude);
            mLng = v.findViewById(R.id.longitude);
            mDelete = v.findViewById(R.id.deleteRow);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.row_item_data, parent, false);
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_data, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mName.setText(placeList.get(position).getName());
        holder.mLat.setText(placeList.get(position).getLatLng().latitude + "");
        holder.mLng.setText(placeList.get(position).getLatLng().longitude + "");
        if (showDelBtn)
            holder.mDelete.setVisibility(View.VISIBLE);
        else
            holder.mDelete.setVisibility(View.INVISIBLE);
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeList.remove(position);
                notifyDataSetChanged();
                PrefUtils.savePlaceList(placeList, cxt);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return placeList.size();
    }
}