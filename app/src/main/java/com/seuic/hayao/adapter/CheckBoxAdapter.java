package com.seuic.hayao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.seuic.hayao.R;

import java.util.ArrayList;

public class CheckBoxAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<CheckBoxItmeData> mData;

    public CheckBoxAdapter(Context context, ArrayList<CheckBoxItmeData> data) {
        this.mContext = context;
        this.mData = data;
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
        CheckBoxTextViewHolder holder ;
        if (convertView == null) {
            holder = new CheckBoxTextViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_check_box_list_item, null);
            CheckBox box = (CheckBox) convertView.findViewById(R.id.list_item_checkbox);
            TextView tv = (TextView) convertView.findViewById(R.id.list_item_textview);
            holder.checkBox = box;
            holder.textView = tv;
            convertView.setTag(holder);
        } else {
            holder = (CheckBoxTextViewHolder) convertView.getTag();
        }
        holder.textView.setText(mData.get(position).getBarCode());
        holder.checkBox.setChecked(mData.get(position).isChecked());
        return convertView;
    }

    public class CheckBoxItmeData {

        private boolean isChecked;
        private String barCode;

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setIsChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }
    }


    public static class CheckBoxTextViewHolder {
        public CheckBox checkBox;
        public TextView textView;
    }

}
