package com.example.chaos.alltestinone.Views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.chaos.alltestinone.R;
import com.example.chaos.alltestinone.ViewElems.CapsulationView;

public class TestCapsulation extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsulation);
//        View view = ((View)LayoutInflater.from(this).inflate(R.layout.activity_capsulation, null));
//        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.capsulation_linearlayout);
//        CapsulationView capsulationView = new CapsulationView(this);
//        linearLayout.addView(capsulationView.getView(this));
//        ((LinearLayout)findViewById(R.id.capsulation_linearlayout)).addView((new CapsulationView(this)).getView(this));
//        createView(this, R.id.capsulation_linearlayout);
        //
        (new CapsulationView()).createView(this, R.id.capsulation_linearlayout);
    }

    private void createView(Context context, int id) {
        ((LinearLayout)findViewById(id)).addView((new CapsulationView()).getView(context));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.capsulation, menu);
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
