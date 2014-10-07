package com.example.chaos.alltestinone.ViewElems;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chaos on 9/28/14.
 */
public class CapsulationView {

    private View thisView = null;
    private List<Map<String, Object>> listItems = null;
    private Map<String, Object> map = null;
    private ListView listView = null;

    public View getView(Context context) {
        thisView = new View(context);
        listView = new ListView(context);
        listView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        setData();
        listView.setAdapter(new MyListVewAdapter(context, listItems));
        return listView;
    }

    public void createView(Activity activity, int id) {
        ((LinearLayout)activity.findViewById(id)).addView(getView(activity));
    }

    private void setData() {
        listItems = new ArrayList<Map<String, Object>>();
        String str = null;
        for (int i = 1; i <= 50; i++) {
            str = "text" + i + ": ";
            for (int j = 0; j < i; j++) {
                str += i + " ";
            }
            map = new HashMap<String, Object>();
            map.put("text1", str);
            map.put("text2", str);
            map.put("text3", str);
            listItems.add(map);
        }
    }
}
