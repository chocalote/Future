package com.kunxun.future.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

    private Context context;
    private List<T> data;
    private int layoutId;

    public CommonAdapter(Context context, List<T> data, int layoutId) {
        this.context = context;
        this.data = data;
        this.layoutId = layoutId;
    }


    public void notifyDataSetChanged(ListView listView, int position) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();

        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View view = listView.getChildAt(position - firstVisiblePosition);
            getView(position, view, listView);
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            Log.d("Lily", "----LayoutInflater()----" + position);
            convertView = LayoutInflater.from(context).inflate(layoutId, null);
        }

        Log.d("Lily", "----getView()----" + position);

        T t = (T) getItem(position);
        convertView(convertView, t);
        return convertView;

    }

    protected abstract void convertView(View item, T t);

}
