package com.myapplicationdev.android.mydatabook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DrawerItemAdapter extends ArrayAdapter<DrawerItem> {

    private Context context;
    private int layoutID;
    private DrawerItem[] drawerItems;

    public DrawerItemAdapter(Context context, int layoutID, DrawerItem[] drawerItems){
        super(context, layoutID, drawerItems);
        this.context = context;
        this.layoutID = layoutID;
        this.drawerItems = drawerItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(layoutID, parent, false);
        ImageView imageView = rowView.findViewById(R.id.ivDrawerItem);
        TextView textView = rowView.findViewById(R.id.tvDrawerItem);
        DrawerItem drawerItem = drawerItems[position];

        textView.setText(drawerItem.getTitle());
        imageView.setImageResource(drawerItem.getImageID());

        return rowView;
    }
}
