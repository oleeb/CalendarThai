package com.oleeb.calendarthai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oleeb.calendarthai.R;

/**
 * Created by HackerOne on 12/8/2015.
 */
public class CustomListViewAdapter  extends BaseAdapter {
    Context mContext;
    String[] strName;
    int[] resId;

    public CustomListViewAdapter(Context context, String[] strName, int[] resId) {
        this.mContext = context;
        this.strName = strName;
        this.resId = resId;
    }

    public int getCount() {
        return strName.length;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        View row = mInflater.inflate(R.layout.row_event, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.textView1);
        textView.setText(strName[position]);

        ImageView imageView = (ImageView) row.findViewById(R.id.imageView1);
        imageView.setBackgroundResource(resId[position]);

        return row;
    }
}

/**************************************************************
int[] resId = {
        R.drawable.ic_wanpra_xx
        , R.drawable.ic_wanpra_xx
        , R.drawable.ic_wanpra_xx
        , R.drawable.ic_wanpra_xx
        , R.drawable.ic_wanpra_xx
        , R.drawable.ic_wanpra_xx
        , R.drawable.ic_wanpra_xx
        , R.drawable.ic_wanpra_xx
        , R.drawable.ic_wanpra_xx
        , R.drawable.ic_wanpra_xx
        , R.drawable.ic_wanpra_xx
};

String[] list = {
        "Aerith Gainsborough"
        , "Barret Wallace"
        , "Cait Sith"
        , "Cid Highwind"
        , "Cloud Strife"
        , "RedXIII"
        , "Sephiroth"
        , "Tifa Lockhart"
        , "Vincent Valentine"
        , "Yuffie Kisaragi"
        , "ZackFair"
};

LayoutInflater mInflater = (LayoutInflater) context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE);

View v_row_listview = mInflater.inflate(R.layout.row_event, null, false);


CustomListViewAdapter adapter = new CustomListViewAdapter(context, list, resId);

ListView listView = (ListView)v_row_listview.findViewById(R.id.listView1);
listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//
//            }
//        });
/**************************************************************/
