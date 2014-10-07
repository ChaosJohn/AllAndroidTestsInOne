package com.luda.encapsulationview.ViewElems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luda.encapsulationview.R;

import java.util.List;
import java.util.Map;

/**
 * Created by chaos on 9/28/14.
 */
public class MyListVewAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> listItems;
    private LayoutInflater listContainer;

    public MyListVewAdapter(Context context, List<Map<String, Object>> listItems) {
        this.context = context;
        listContainer = LayoutInflater.from(this.context);
        this.listItems = listItems;
    }

    public final class ListViewItem {
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListViewItem listViewItem = null;
        if (null == view) {
            listViewItem = new ListViewItem();
            view = listContainer.inflate(R.layout.a_item, null);
            listViewItem.textView1 = (TextView)view.findViewById(R.id.a_text_1);
            listViewItem.textView2 = (TextView)view.findViewById(R.id.a_text_2);
            listViewItem.textView3 = (TextView)view.findViewById(R.id.a_text_3);
            view.setTag(listViewItem);
        } else {
            listViewItem = (ListViewItem) view.getTag();
        }

        listViewItem.textView1.setText((String)listItems.get(i).get("text1"));
        listViewItem.textView2.setText((String) listItems.get(i).get("text2"));
        listViewItem.textView3.setText((String) listItems.get(i).get("text3"));

        return view;
    }
}
