package com.example.photogallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageAdapter extends BaseAdapter {

    // Context of the activity or application
    private final Context context;

    // Array of image files to display in the grid
    private final File[] images;

    // Constructor that takes in the context and image file array
    public ImageAdapter(Context context, File[] images) {
        this.context = context;
        this.images = images;
    }

    // Returns the total number of items (images)
    @Override
    public int getCount() {
        return images.length;
    }

    // Returns the image file at a specific position
    @Override
    public Object getItem(int position) {
        return images[position];
    }

    // Returns the ID of the item (in this case, just the position)
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Returns the view for each item (grid cell) in the GridView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // If there's no reusable view, inflate a new one from the grid_item layout
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
        }

        // Find the ImageView inside the grid item layout
        ImageView imageView = convertView.findViewById(R.id.gridItemImage);

        // Use Picasso to load the image from file into the ImageView efficiently
        Picasso.get().load(images[position]).into(imageView);

        // Return the completed view to be displayed
        return convertView;
    }
}
