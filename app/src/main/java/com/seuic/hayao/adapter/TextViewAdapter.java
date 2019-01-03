package com.seuic.hayao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seuic.hayao.R;
import com.seuic.hayao.data.bean.SmartCorpInfo;

import java.util.ArrayList;

public class TextViewAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<SmartCorpInfo> mData;

    public TextViewAdapter(Context context, ArrayList<SmartCorpInfo> data) {
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
        TextViewHolder holder = null;
        if (convertView == null) {
            holder = new TextViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_text_sub_view_list_item, null);
            TextView tv1 = (TextView) convertView.findViewById(R.id.main_title);
            TextView tv2 = (TextView) convertView.findViewById(R.id.sub_title);
            holder.mainTitle = tv1;
            holder.subTitle = tv2;
            convertView.setTag(holder);
        } else {
            holder = (TextViewHolder) convertView.getTag();
        }
        String cropCode = mData.get(position).getCorpCode();
        String cropName = mData.get(position).getCorpName();
//        SpannableStringBuilder builder = new SpannableStringBuilder(cropName + "|" + cropCode);
//        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
//        builder.setSpan(redSpan, cropName.length(), cropName.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mainTitle.setText(cropName);
        holder.subTitle.setText("编号：" + cropCode);
        return convertView;
    }

    public static class TextViewHolder {
        public TextView mainTitle;
        public TextView subTitle;
    }

}
