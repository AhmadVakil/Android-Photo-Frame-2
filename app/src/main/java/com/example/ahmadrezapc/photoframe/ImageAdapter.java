package com.example.ahmadrezapc.photoframe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Ahmadreza pc on 14/07/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c){
        mContext =c;
    }

    public int getCount() {
        return mThumsIda.length;

    }
    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
    }

    public View getView (int position, View convertView, ViewGroup parent){
        ImageView imageView= new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(140, 140));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8,8,8,8);

        imageView.setImageResource(mThumsIda[position]);
        return imageView;
    }

    public Integer[] mThumsIda = {
            R.drawable.frame_0,
            R.drawable.frame_1,
            R.drawable.frame_2,
            R.drawable.frame_3,
            R.drawable.frame_4,
            R.drawable.frame_0,
            R.drawable.frame_1,
            R.drawable.frame_2,
            R.drawable.frame_3,
            R.drawable.frame_4,
            R.drawable.frame_0,
            R.drawable.frame_1,
            R.drawable.frame_2,
            R.drawable.frame_3,
            R.drawable.frame_4,
            R.drawable.frame_0,
            R.drawable.frame_1,
            R.drawable.frame_2,
            R.drawable.frame_3,
            R.drawable.frame_4,
            R.drawable.frame_0,
            R.drawable.frame_1,
            R.drawable.frame_2,
            R.drawable.frame_3,
            R.drawable.frame_4

    };
}