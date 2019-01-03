package com.seuic.listgrid;

import java.util.ArrayList;
import java.util.List;

import com.seuic.listgrid.DataTable.DataRow;
import com.seuic.listgrid.ListGridStyle.HeaderStyle;
import com.seuic.listgrid.ListGridStyle.ItemStyle;
import com.seuic.hayao.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListGrid extends LinearLayout {

    private List<GridColumn> mColumns;
    private ListView mListView;
    private Context mContext;
    private DataAdapter mAdapter;
    private ColumnType mColumnType = ColumnType.WIDTH;
    private Resources mResources;
    private int mCellTextSize = ItemStyle.TEXT_SIZE;
    private LinearLayout mHeader;
    private OnGridItemLongClickListener mLongClickListener;
    private List<ListRow> mRows;
    private boolean mFullRowSelect = true;

    private boolean mHasCheckBox = false;
    private static final int CHECK_BOX_ID = 0x3867365;

    public ListGrid(Context context, List<GridColumn> columns, boolean hasCheckbox) {
        super(context);
        mColumns = columns;
        mContext = context;
        mResources = context.getResources();
        mHasCheckBox = hasCheckbox;
        initView();
    }

    public ListGrid(Context context, List<GridColumn> columns) {
        super(context);

        mColumns = columns;
        mContext = context;
        mResources = context.getResources();

        initView();

    }

    public ListGrid(Context context, List<GridColumn> columns, ColumnType columnType, boolean hasCheckbox) {
        super(context);
        mHasCheckBox = hasCheckbox;

        mColumns = columns;
        mContext = context;
        mColumnType = columnType;
        mResources = context.getResources();

        initView();
    }

    public ListGrid(Context context, List<GridColumn> columns, ColumnType columnType) {
        super(context);

        mColumns = columns;
        mContext = context;
        mColumnType = columnType;
        mResources = context.getResources();

        initView();
    }

    private void initView() {

        setOrientation(VERTICAL);
        setBackground(mResources.getDrawable(R.drawable.listgrid_style));

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(VERTICAL);
        layout.addView(initHeader());
        layout.addView(initData());
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if (mColumnType == ColumnType.WIDTH) {
            HorizontalScrollView horScroll = new HorizontalScrollView(mContext);
            horScroll.addView(layout);
            horScroll.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            addView(horScroll);
        } else {
            LinearLayout layoutContent = new LinearLayout(mContext);
            layoutContent.addView(layout);
            layoutContent.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            addView(layoutContent);
        }
    }

    private LinearLayout initHeader() {
        mHeader = new LinearLayout(mContext);
        if (mColumnType == ColumnType.WEIGHT) {
            mHeader.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        } else {
            mHeader.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
        }
        // mHeader.setBackgroundColor(HeaderStyle.BACKGROUND_COLOR);
        mHeader.setOrientation(HORIZONTAL);
        mHeader.setBackground(mResources.getDrawable(R.drawable.listgrid_header_style));

        if (mHasCheckBox) {
            View view = new CheckBox(mContext);
            view.setVisibility(View.INVISIBLE);
            mHeader.addView(view);
        }

        mHeader.addView(getSpliter());
        for (GridColumn column : mColumns) {
            TextView tv = new TextView(mContext);
            tv.setText(column.getName());
            if (mColumnType == ColumnType.WEIGHT) {
                LayoutParams lp = new LayoutParams(0,
                        LayoutParams.WRAP_CONTENT, column.getWeight());
                tv.setLayoutParams(lp);
            } else {
                tv.setWidth(column.getWidth());
            }
            tv.setTextColor(HeaderStyle.TEXT_COLOR);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(HeaderStyle.TEXT_SIZE);
            tv.setBackgroundColor(Color.TRANSPARENT);
            tv.setPadding(0, 5, 0, 5);
            tv.setVisibility(column.getVisible());
            mHeader.addView(tv);
            if (mColumns.iterator().hasNext()) {
                mHeader.addView(getSpliter());
            }
        }
        return mHeader;
    }

    private LinearLayout initData() {

        mRows = new ArrayList<ListRow>();

        mAdapter = new DataAdapter();

        mListView = new ListView(mContext);
        mListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mListView.setOnItemClickListener(new GridItemClickListener());
        mListView.setOnItemSelectedListener(new GridItemSelectedListener());
        mListView.setOnItemLongClickListener(new GridItemLongClickListener());
        mListView.setDivider(mResources.getDrawable(getResourceId(mContext, "drawable", "color_black")));
        mListView.setClickable(false);
        mListView.setDividerHeight(1);
        mListView.setAdapter(mAdapter);

        LinearLayout layout = new LinearLayout(mContext);
        layout.addView(mListView);
        layout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        return layout;
    }

    public ListGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void refresh() {
        addView(mHeader);
    }

    public void removeRow(int index) {
        mRows.remove(index);
        mAdapter.notifyDataSetChanged();
    }

    public void clearRows() {
        mRows.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void setDataSource(DataTable dt) {
        mRows = new ArrayList<ListRow>();
        List<DataRow> dataRows = dt.getAllRows();
        int dtColSize = dt.getColumnSize();
        for (DataRow dr : dataRows) {
            ListRow row = new ListRow(mColumns);
            for (int i = 0, length = mColumns.size(); i < length; i++) {
                if (i == dtColSize) {
                    break;
                }
                row.setValue(i, dr.get(i));
            }
            mRows.add(row);
        }

        mAdapter = new DataAdapter();
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void insertRow(ListRow row) {
        mRows.add(row);
        mAdapter.notifyDataSetChanged();
    }

    public void insertRows(List<ListRow> rows) {
        for (ListRow row : rows) {
            mRows.add(row);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void insertRow(int index, ListRow row) {
        mRows.add(index, row);
        mAdapter.notifyDataSetChanged();
    }

    public List<GridColumn> getColumns() {
        return mColumns;
    }

    public List<ListRow> getAllRows() {
        return mRows;
    }

    public int getRowsCount() {
        return mRows.size();
    }

    public ArrayList<ListRow> getSelectRows() {
        ArrayList<ListRow> rows = new ArrayList<ListRow>();
        for (ListRow row : mRows) {
            if(row.isRowSelect()) {
                rows.add(row);
            }
        }
        return rows;
    }

    public int getSelectIndexs() {
//        return mCurID;
        return 0;
    }

    public void setCellTextSize(int size) {
        mCellTextSize = size;
    }

    public void setOnItemLongClickListener(OnGridItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    public void setFullRowSelect(boolean fullRow) {
        mFullRowSelect = fullRow;
    }

    public void setHeaderTextSize(int size) {
        for (int i = 0, count = mHeader.getChildCount(); i < count; i++) {
            View view = mHeader.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(size);
            }
        }
    }

    public void setHeaderShown(boolean shown) {
        if (shown) {
            mHeader.setVisibility(View.VISIBLE);
        } else {
            mHeader.setVisibility(View.GONE);
        }
    }

    public ListRow newRow() {
        ListRow item = new ListRow(mColumns);
        return item;
    }

    public List<ListRow> newRows(int size) {
        List<ListRow> rows = new ArrayList<ListRow>();
        for (int i = 0; i < size; i++) {
            rows.add(newRow());
        }
        return rows;
    }

    public class ListRow {
        List<GridColumn> mColumns;
        List<String> mValues;
        private List<Integer> mTextColors;
        private int backgroundColor = ItemStyle.NORMAL_BACKGROUND_COLOR;

        private boolean mIsRowSelect;

        public boolean isRowSelect() {
            return mIsRowSelect;
        }

        public void setRowSelect(boolean rowSelect) {
            this.mIsRowSelect = rowSelect;
        }

        private ListRow(List<GridColumn> columns) {
            mColumns = columns;
            mValues = new ArrayList<String>();
            mTextColors = new ArrayList<Integer>();
            for (int i = 0, size = columns.size(); i < size; i++) {
                mValues.add("");
                mTextColors.add(mColumns.get(i).getTextColor());
            }
        }

        public void addColumn(GridColumn column) {
            mColumns.add(column);
            mTextColors.add(column.getTextColor());
            mValues.add("");
        }

        public int getColumnsSize() {
            return mColumns.size();
        }

        public String getValue(int index) {
            return mValues.get(index);
        }

        public void setValue(int index, String value) {
            int size = mValues.size();
            if (size == 0) {
                return;
            }
            mValues.set(index, value);

            if (mRows.contains(this)) {
                mAdapter.notifyDataSetChanged();
            }
        }

        public void setBackgroundColor(int color) {
            this.backgroundColor = color;

            if (mRows.contains(this)) {
                mAdapter.notifyDataSetChanged();
            }
        }

        public int getBackgroundColor() {
            return this.backgroundColor;
        }

        public void setCellTextColor(int cellIndex, int color) {
            mTextColors.set(cellIndex, color);
        }

        public int getCellTextColor(int cellIndex) {
            return mTextColors.get(cellIndex);
        }
    }

    class DataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mRows.size();
        }

        @Override
        public Object getItem(int position) {
            return mRows.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                LinearLayout layout = new LinearLayout(mContext);
                layout.setOrientation(HORIZONTAL);
                CheckBox box = null;
                if (mHasCheckBox) {
                    box = new CheckBox(mContext);
                    box.setFocusable(false);
                    box.setClickable(false);
                    box.setId(CHECK_BOX_ID);
                    layout.addView(box);
                }
                layout.addView(getSpliter());
                for (int i = 0, size = mColumns.size(); i < size; i++) {
                    TextView tv = new TextView(mContext);
                    if (mColumnType == ColumnType.WEIGHT) {
                        LayoutParams lp = new LayoutParams(0,
                                LayoutParams.WRAP_CONTENT, mColumns.get(i)
                                .getWeight());
                        lp.gravity = Gravity.CENTER_VERTICAL;
                        tv.setLayoutParams(lp);
                    } else {
                        tv.setWidth(mColumns.get(i).getWidth());
                    }
                    tv.setVisibility(mColumns.get(i).getVisible());
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(mCellTextSize);
                    tv.setPadding(0, 5, 0, 5);
                    tv.setBackgroundColor(Color.TRANSPARENT);
                    layout.addView(tv);
                    holder.addView(tv);
                    if (i != size - 1) {
                        layout.addView(getSpliter());
                    }
                }

                holder.mCheckBox = box;
                layout.addView(getSpliter());
                convertView = layout;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            for (int i = 0, size = mColumns.size(); i < size; i++) {
                holder.getTextList().get(i)
                        .setText(mRows.get(position).getValue(i));
                holder.getTextList().get(i)
                        .setTextColor(mRows.get(position).getCellTextColor(i));
            }

//            if (position != mCurID) {
//                convertView.setBackgroundColor(mRows.get(position).getBackgroundColor());
//            } else {
//            mCurView = convertView;
            if (mFullRowSelect) {
                convertView.setBackgroundColor(mRows.get(position).isRowSelect() ? ItemStyle.SELECTED_BACKGROUND_COLOR : mRows.get(position).getBackgroundColor());
            }
//            }

            if (mHasCheckBox && holder.mCheckBox != null) {
                holder.mCheckBox.setChecked(mRows.get(position).isRowSelect());
            }
            return convertView;
        }
    }

    public List<ListRow> getSelectedRows() {
        List<ListRow> selectRows = new ArrayList<ListRow>();
        for (ListRow row : mRows) {
            if (row.isRowSelect()) {
                selectRows.add(row);
            }
        }
        return selectRows;
    }

    public void setAllRowsSelect(boolean isSelect) {
        for (ListRow row : mRows) {
            row.setRowSelect(isSelect);
        }
        mAdapter.notifyDataSetChanged();
    }

    private View getSpliter() {
        View spliter = new View(mContext);
        spliter.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
        spliter.setBackgroundColor(Color.BLACK);
        return spliter;
    }

    private void setSelected(View convertView, int position) {

        boolean state = mRows.get(position).isRowSelect();
        mRows.get(position).setRowSelect(!state);

        if (mHasCheckBox) {
            CheckBox box = (CheckBox) convertView.findViewById(CHECK_BOX_ID);
            if (box != null) {
                box.setChecked(!state);
            }
        }
        if (mFullRowSelect) {
            convertView.setBackgroundColor(mRows.get(position).isRowSelect() ? ItemStyle.SELECTED_BACKGROUND_COLOR : mRows.get(position).getBackgroundColor());
        }
    }

    private class GridItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View convertView,
                                int position, long id) {
            setSelected(convertView, position);
        }
    }

    private class GridItemSelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View convertView,
                                   int position, long arg3) {
            // setSelected(convertView,position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // setSelected(null, -1);
        }
    }

    private class GridItemLongClickListener implements OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                       int index, long id) {
            if (mLongClickListener == null) {
                return false;
            }

            mLongClickListener.onLongClick(view, index, id);

            return false;
        }
    }

    public interface OnGridItemLongClickListener {
        void onLongClick(View view, int index, long id);
    }

    public enum ColumnType {
        WEIGHT, WIDTH
    }

    private int getResourceId(Context context, String defType, String name) {
        return context.getResources().getIdentifier(name, defType,
                context.getPackageName());
    }
}
