package com.luda.testvolley.testvolley;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chaos on 14-10-10.
 */
public class PullTask extends AsyncTask<String, Void, List<Map<String, Object>>> {

    private Context context = null;
    private String TAG = "PullTask";
    private String BASEURL = "http://px.eput.com/";
    private RequestQueue requestQueue = null;
    private Map<String, Object> convertMap = null;
    private List<Map<String, Object>> convertList = null;
    private List<Map<String, Object>> listItems = null;
    private String oldestDateline = null;
    private String newestDateline = null;
    private PhotoViewAdapter photoViewAdapter = null;
    private PullToRefreshListView pullToRefreshListView = null;
    private int toRefresh = 0;
    private int originSize = -1;


    public PullTask(Context context, RequestQueue requestQueue, List<Map<String, Object>> listItems, String oldestDateline, String newestDateline, PhotoViewAdapter photoViewAdapter, PullToRefreshListView pullToRefreshListView) {
        this.context = context;
        this.requestQueue = requestQueue;
        this.listItems = listItems;
        this.oldestDateline = oldestDateline;
        this.newestDateline = newestDateline;
        this.photoViewAdapter = photoViewAdapter;
        this.pullToRefreshListView = pullToRefreshListView;
    }


    @Override
    protected List<Map<String, Object>> doInBackground(String... params) {
//        int numOfParams = params.length;
        String requestUrl = OtherUtils.concatenateRequestUrlFromBaseUrlAndParams(params);
        toRefresh = requestUrl.contains("direc=1") ? 1 : 0;
        JsonObjectRequest getMoreJsonObjectRequest = new JsonObjectRequest(requestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(TAG, jsonObject.toString());

                        Map<String, Object> map = JsonUtils.jsonObject2Map(jsonObject);
                        Log.d(TAG + "#moredata", map.get("data").toString());
                        List<Map<String, Object>> jsonList = JsonUtils.jsonArray2List(map.get("data").toString());
                        /**
                         * get the newest and the oldest dateline.
                         */
                        if (1 == toRefresh) {
                            newestDateline = jsonList.get(0).get("dateline").toString();
                        } else {
                            oldestDateline = jsonList.get(jsonList.size() - 1).get("dateline").toString();
                        }
                        originSize = listItems.size();
                        for (int i = 0; i < jsonList.size(); i++) {
//                                convertMap = new HashMap<String, Object>();
                            if (1 == toRefresh) {
                                listItems.add(i, new HashMap<String, Object>());
                            } else {
                                listItems.add(new HashMap<String, Object>());
                            }
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
                            listItems.get(i + (1 == toRefresh ? 0 : originSize)).put("limb", BASEURL + convertMap.get("limb"));

                            /**
                             * get JsonObject "user" and convert it to HashMap.
                             */
                            convertMap = JsonUtils.jsonObject2Map(jsonList.get(i).get("user").toString());
                            /**
                             * add the "name" to the specified index in listItems.
                             */
                            listItems.get(i + (1 == toRefresh ? 0 : originSize)).put("name", convertMap.get("name"));
                        }
                        Log.d(TAG + "#listItem", JsonUtils.list2JsonArrayString(listItems));
//                                photoViewAdapter = new PhotoViewAdapter(PullToRefresh.this, listItems);
//                                actualListView.setAdapter(photoViewAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage(), volleyError);
            }
        });

        requestQueue.add(getMoreJsonObjectRequest);

        return listItems;
    }

    @Override
    protected void onPostExecute(List<Map<String, Object>> list) {
        if (originSize == list.size()) {
            Toast.makeText(context, "Already the newest", Toast.LENGTH_LONG).show();
        } else {
            photoViewAdapter.notifyDataSetChanged();
        }

        if (1 == toRefresh) {
            pullToRefreshListView.onRefreshComplete();
        }

        super.onPostExecute(list);
    }
}
