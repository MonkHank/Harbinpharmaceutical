package com.seuic.hayao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seuic.hayao.R;
import com.seuic.hayao.data.bean.StoreTypeInfo;

import java.util.ArrayList;

public class StoreTypeAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<StoreTypeInfo> mData;

    public StoreTypeAdapter(Context context, ArrayList<StoreTypeInfo> infos) {
        this.mContext = context;
        this.mData = infos;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextViewHolder holder = null;
        if (convertView == null) {
            holder = new TextViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_text_view_list_item, null);
            TextView tv = (TextView) convertView.findViewById(R.id.text1);
            holder.textView = tv;
            convertView.setTag(holder);
        } else {
            holder = (TextViewHolder) convertView.getTag();
        }
        holder.textView.setText(mData.get(position).getStoreTypeText());
        return convertView;
    }

    public static class TextViewHolder {
        public TextView textView;
    }

}
