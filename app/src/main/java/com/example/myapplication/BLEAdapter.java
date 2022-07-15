package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.model.BLEDevice;

import java.util.ArrayList;
import java.util.List;

public class BLEAdapter extends BaseAdapter {
    private static final String TAG = "BLEAdapter";
    private Context context;

    private List<BLEDevice> datas;

    public BLEAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<BLEDevice> datas1) {
        if (datas1 == null)
            return;

        Log.w(TAG, "setDatas: " );
        datas.clear();
        datas.addAll(datas1);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold;
        if (view == null) {
            view = View.inflate(context, R.layout.layout, null);
            viewHold = new ViewHold(view);
            view.setTag(viewHold);
        } else {
            viewHold = (ViewHold) view.getTag();
        }

        viewHold.textView.setText(datas.get(i).getName());

        return view;
    }


    private class ViewHold {
        public final TextView textView;

        public ViewHold(View view) {
            textView = view.findViewById(R.id.name);
        }
    }
}
