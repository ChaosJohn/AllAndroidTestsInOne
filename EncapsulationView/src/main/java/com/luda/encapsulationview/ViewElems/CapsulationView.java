package com.luda.encapsulationview.ViewElems;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chaos on 9/28/14.
 */
public class CapsulationView extends View {

    private View thisView = null;
    private List<Map<String, Object>> listItems = null;
    private Map<String, Object> map = null;
    private ListView listView = null;

    public CapsulationView(Context context) {
        super(context);
    }

    public View getView(Context context) {
        thisView = new View(context);
        listView = new ListView(context);
        listView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        setData();
        listView.setAdapter(new MyListVewAdapter(context, listItems));
        return listView;
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
