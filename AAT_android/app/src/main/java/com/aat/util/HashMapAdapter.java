package com.aat.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aat.datastore.Group;
import com.ase.aat_android.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Yesika on 1/20/2017.
 */

public class HashMapAdapter extends BaseAdapter {

    private final ArrayList mData;

    public HashMapAdapter(Map<String, Group> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, Group> getItem(int i) {
        return (Map.Entry) mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = null;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adaptor_hashmap, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, Group> item = getItem(position);
        // TODO replace findViewById by ViewHolder
       ((TextView) result.findViewById(R.id.course_group)).setText(item.getKey());

        return result;
    }
}
