package com.luda.testvolley.testvolley;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class PhotoList extends Activity {

    private final String TAG = this.getClass().getName();
    private final String RequestURLGet = "http://px.eput.com/api/photo_list";
    private RequestQueue requestQueue = null;
    private ListView listView = null;
    private PhotoViewAdapter photoViewAdapter = null;
    private Map<String, Object> convertMap = null;
    private List<Map<String, Object>> convertList = null;
    private final String BASEURL = "http://px.eput.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        listView = (ListView)findViewById(R.id.imagelist);



        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest getJsonObjectRequest = new JsonObjectRequest(RequestURLGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(TAG, jsonObject.toString());
                        try {
                            Map<String, Object> map = JsonUtils.jsonObject2Map(jsonObject);
                            Log.d(TAG + "#data", map.get("data").toString());
                            List<Map<String, Object>> jsonList = JsonUtils.jsonArray2List(map.get("data").toString());
                            List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
                            for (int i = 0; i < jsonList.size(); i++) {
//                                convertMap = new HashMap<String, Object>();
                                listItems.add(new HashMap<String, Object>());
                                /**
                                 * get JsonArray "path" and convert it to ArrayList.
                                 */
                                convertList = JsonUtils.jsonArray2List(jsonList.get(i).get("path").toString());
                                /**
                                 * get JsonObject of first photo in "path" and convert it to HashMap.
                                 */
                                convertMap = convertList.get(0);
                                /**
                                 * add the "limb" to the specified index in listItems.
                                 */
                                listItems.get(i).put("limb", BASEURL + convertMap.get("limb"));

                                /**
                                 * get JsonObject "user" and convert it to HashMap.
                                 */
                                convertMap = JsonUtils.jsonObject2Map(jsonList.get(i).get("user").toString());
                                /**
                                 * add the "name" to the specified index in listItems.
                                 */
                                listItems.get(i).put("name", convertMap.get("name"));
                            }
                            Log.d(TAG + "#listItem", JsonUtils.list2JsonArrayString(listItems));
                            photoViewAdapter = new PhotoViewAdapter(PhotoList.this, listItems);
                            listView.setAdapter(photoViewAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage(), volleyError);
            }
        }
        );

        requestQueue.add(getJsonObjectRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_list, menu);
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
