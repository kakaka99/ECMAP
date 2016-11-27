package com.example.xdeveloper.simplewebstuff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xdeveloper.simplewebstuff.ItemFragment.OnListFragmentInteractionListener;

import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    public final List<Station> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;
    Activity activity;

    public MyItemRecyclerViewAdapter(List<Station> station_list, OnListFragmentInteractionListener listener , Activity activity) {
        mValues = station_list;
        mListener = listener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,  int position) {
        holder.mItem = mValues.get(position);


        holder.mContentView.setText(mValues.get(position).get_name_tc()+" "+mValues.get(position).get_name_en());

        holder.mDescriptionView.setText(mValues.get(position).get_packing_no());
        holder.mDistance.setText(String.valueOf((int)mValues.get(position).get_m_distance()));

        Glide.with(context)
                .load(mValues.get(position).get_image())
                .into(holder.mImageView);

        final Station marker = mValues.get(position);

        holder.mView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity, StationDetailActivity.class);


                in.putExtra("id" , marker.get_string_id());
                in.putExtra("name_tc" , marker.get_name_tc());
                in.putExtra("name_en" , marker.get_name_en());
                in.putExtra("packing_no" , marker.get_packing_no());
                in.putExtra("image" , marker.get_image());
                in.putExtra("distance" , String.valueOf((int)marker.get_m_distance()));
                in.putExtra("type" , marker.get_type());
                in.putExtra("provider" , marker.get_provider());
                in.putExtra("address_en" , marker.get_address_en());
                in.putExtra("address_tc" , marker.get_address_tc());


                activity.startActivity(in);
            }
        });
    }






    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mDescriptionView;
        public final TextView mDistance;
        public final ImageView mImageView;
        public Station mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mDescriptionView = (TextView) view.findViewById(R.id.description);
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            mDistance = (TextView) view.findViewById(R.id.distance);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
