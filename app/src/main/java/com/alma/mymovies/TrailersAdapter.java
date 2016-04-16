package com.alma.mymovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alon on 4/16/2016.
 */
public class TrailersAdapter<T> extends ArrayAdapter<Trailer> {

    private Context mContext;
    private int mLayoutResourceId;

    public TrailersAdapter(Context context, int resource, ArrayList<Trailer> trailersList) {
        super(context, resource, trailersList);
        mContext = context;
        mLayoutResourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrailerHolder holder = null;
        Trailer trailer = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new TrailerHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.trailerNumber_textView);

            convertView.setTag(holder);
        } else {
            holder = (TrailerHolder) convertView.getTag();
        }

        holder.mTextView.setText(trailer.mTrailerName);

        return convertView;
    }

    static class TrailerHolder {
        ImageView mImageView;
        TextView mTextView;
    }
}
