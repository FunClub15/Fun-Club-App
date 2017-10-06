package com.techminds.funclub.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.techminds.funclub.R;
import com.techminds.funclub.entity.ImageModel;

/**
 * Created by mask on 8/27/17.
 */

public class ContentGalleryAdapter extends ArrayAdapter<ImageModel> {
    public ContentGalleryAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null ) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_item, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext()).load(getItem(position).getUrl()).into(holder.image);

        return convertView;
    }

    class ViewHolder {
        ImageView image;
    }
}
