package com.seuic.hayao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seuic.hayao.R;
import com.seuic.hayao.modelbean.EditCommonUseShow;

import java.util.ArrayList;

public class TextViewCheckBoxAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<EditCommonUseShow> mData;
    private String mType;

    public TextViewCheckBoxAdapter(Context context, ArrayList<EditCommonUseShow> data, String type) {
        this.mContext = context;
        this.mData = data;
        this.mType = type;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_text_sub_view_list_item_checkbox, null);
            TextView tv1 = (TextView) convertView.findViewById(R.id.main_title);
            TextView tv2 = (TextView) convertView.findViewById(R.id.sub_title);
            AppCompatCheckBox checkBox = (AppCompatCheckBox) convertView.findViewById(R.id.select_check_box);
            holder.mainTitle = tv1;
            holder.subTitle = tv2;
            holder.checkbox = checkBox;
            convertView.setTag(holder);
        } else {
            holder = (TextViewHolder) convertView.getTag();
        }
        String cropCode = mData.get(position).getCorpInfo().getCorpCode();
        String cropName = mData.get(position).getCorpInfo().getCorpName();
        holder.mainTitle.setText(cropName);
        holder.subTitle.setText("编号：" + cropCode);
        char[] ss = mData.get(position).getCheckStatInfo().getCommonUse().toCharArray();
        holder.checkbox.setChecked((ss["in".equals(mType) ? 0 : 1] + "").equals("1"));
        return convertView;
    }

    public static class TextViewHolder {
        public TextView mainTitle;
        public TextView subTitle;
        public AppCompatCheckBox checkbox;
    }

}
