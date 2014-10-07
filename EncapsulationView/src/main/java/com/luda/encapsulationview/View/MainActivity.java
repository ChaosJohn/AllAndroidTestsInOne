package com.luda.encapsulationview.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.luda.encapsulationview.R;
import com.luda.encapsulationview.ViewElems.MyListVewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private List<Map<String, Object>> listItems;
    private Map<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setData();
        ((ListView)findViewById(R.id.a_listview)).setAdapter(new MyListVewAdapter(this, listItems));

    }

    private void setData() {
        listItems = new ArrayList<Map<String, Object>>();
        String str = null;
        for (int i = 1; i <= 50; i++) {
            str = "text1: ";
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
