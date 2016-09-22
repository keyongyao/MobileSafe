package com.kk.mymobilesafe.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.mymobilesafe.R;

/**
 * Created by Administrator on 2016/9/20.
 */
public class NineGridsAdapter extends BaseAdapter {
    int[] icons;
    String[] descs;
    Activity activity;

    public NineGridsAdapter(int[] icons, String[] descs, Activity activity) {
        this.icons = icons;
        this.descs = descs;
        this.activity = activity;
    }

    @Override
    public int getCount() {

        return descs.length;
    }

    @Override
    public Object getItem(int position) {
        return descs[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View inflate;
        if (convertView == null) {
            inflate = View.inflate(activity.getApplicationContext(), R.layout.grid_item, null);

        } else {
            inflate = convertView;
        }

        ImageView icon = (ImageView) inflate.findViewById(R.id.iv_icon);
        icon.setImageResource(icons[position]);
        TextView desc = (TextView) inflate.findViewById(R.id.tv_desc);
        desc.setText(descs[position]);
        return inflate;
    }
}
