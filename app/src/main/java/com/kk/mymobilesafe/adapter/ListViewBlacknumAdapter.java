package com.kk.mymobilesafe.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.bean.BlackNumBean;
import com.kk.mymobilesafe.dao.BlackNumDao;

import java.util.ArrayList;

/**
 * 黑名单列表适配器
 * Created by Administrator on 2016/9/26.
 */

public class ListViewBlacknumAdapter extends BaseAdapter {
    ArrayList<BlackNumBean> blackNumBeanList;
    Context mContext;
    String[] blackType = {"拦截短信", "拦截来电", "拦截来电短信"};


    public ListViewBlacknumAdapter(ArrayList<BlackNumBean> blackNumBeanList, Context mContext) {
        this.blackNumBeanList = blackNumBeanList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return blackNumBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return blackNumBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.blacknum_item, null);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_blacknum_item_title);
            holder.tvSubTitle = (TextView) convertView.findViewById(R.id.tv_blacknum_item_subtitle);
            holder.ivDel = (ImageView) convertView.findViewById(R.id.iv_blacknum_item_del);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(blackNumBeanList.get(position).phone);
        holder.tvSubTitle.setText(blackType[blackNumBeanList.get(position).blockType - 1]);
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("删除");
                builder.setMessage("确定删除此黑名单？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "删除操作" + blackNumBeanList.get(position).toString(), Toast.LENGTH_SHORT).show();
                        new BlackNumDao(mContext).delete(blackNumBeanList.get(position));
                        blackNumBeanList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.create().show();
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvSubTitle;
        ImageView ivDel;
    }

}
