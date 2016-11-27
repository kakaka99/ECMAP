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

import com.bumptech.glide.Glide;
import com.example.xdeveloper.simplewebstuff.ItemFragment.OnListFragmentInteractionListener;

import java.util.List;


public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    public final List<Comment> mValues;
    //private final OnListFragmentInteractionListener mListener;
    private Context context;

    public CommentRecyclerViewAdapter(List<Comment> comment_list ) {
        mValues = comment_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,  int position) {


        holder.mComment.setText(mValues.get(position).get_comment());

        holder.mMark.setText(mValues.get(position).get_mark()+"/5 Mark");
        holder.mTime.setText(mValues.get(position).get_time());



    }






    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mIdView;
        public final TextView mComment;
        public final TextView mTime;
        public final TextView mMark;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            mComment = (TextView) view.findViewById(R.id.comment);
            mTime = (TextView) view.findViewById(R.id.created_at);
            mMark = (TextView) view.findViewById(R.id.mark);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mComment.getText() + "'";
        }
    }
}
