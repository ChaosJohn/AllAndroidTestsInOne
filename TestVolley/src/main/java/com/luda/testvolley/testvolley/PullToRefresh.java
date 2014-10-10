package com.luda.testvolley.testvolley;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PullToRefresh extends Activity {

    private PullToRefreshListView pullToRefreshListView = null;
    private ListView actualListView = null;
    private final String TAG = this.getClass().getName();
    private final String RequestURLGet = "http://px.eput.com/api/photo_list";
    private RequestQueue requestQueue = null;
    private PhotoViewAdapter photoViewAdapter = null;
    private Map<String, Object> convertMap = null;
    private List<Map<String, Object>> convertList = null;
    private List<Map<String, Object>> listItems = null;
    private final String BASEURL = "http://px.eput.com/";

    private List<Map<String, Object>> deltaRefreshList = null;
    private List<Map<String, Object>> deltaLoadMoreList = null;

    private String newestDateline = null;
    private String oldestDateline = null;

    private int originSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh);

        listItems = new ArrayList<Map<String, Object>>();
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);

                new PullToRefreshTask().execute(newestDateline);
//                new PullTask(PullToRefresh.this, requestQueue, listItems, oldestDateline, newestDateline, photoViewAdapter, pullToRefreshListView).execute(RequestURLGet, "direc=1", "dline=" + newestDateline);
            }
        });

        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Toast.makeText(PullToRefresh.this,
                        "Loading More!", Toast.LENGTH_SHORT).show();
                new PullToLoadMoreTask().execute(oldestDateline);
//                new PullTask(PullToRefresh.this, requestQueue, listItems, oldestDateline, newestDateline, photoViewAdapter, pullToRefreshListView).execute(RequestURLGet, "dline=" + oldestDateline);
            }
        });


        actualListView = pullToRefreshListView.getRefreshableView();
        registerForContextMenu(actualListView);
        setDefaultAdapter();

    }

    private void setDefaultAdapter() {
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest getJsonObjectRequest = new JsonObjectRequest(RequestURLGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(TAG, jsonObject.toString());
                        Map<String, Object> map = JsonUtils.jsonObject2Map(jsonObject);
                        Log.d(TAG + "#data", map.get("data").toString());
                        List<Map<String, Object>> jsonList = JsonUtils.jsonArray2List(map.get("data").toString());
                        /**
                         * get the newest and the oldest dateline.
                         */
                        newestDateline = jsonList.get(0).get("dateline").toString();
                        oldestDateline = jsonList.get(jsonList.size() - 1).get("dateline").toString();

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
                        originSize = listItems.size();
                        photoViewAdapter = new PhotoViewAdapter(PullToRefresh.this, listItems);
                        actualListView.setAdapter(photoViewAdapter);
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


    private class PullToRefreshTask extends AsyncTask<String, Void, List<Map<String, Object>>> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected List<Map<String, Object>> doInBackground(String... params) {
            String dateline = params[0];
            String requestUrl = RequestURLGet + "?direc=1&dline=" + dateline;
            JsonObjectRequest getRefreshObjectRequest = new JsonObjectRequest(requestUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d(TAG, jsonObject.toString());
                            originSize = listItems.size();
//                                Toast.makeText(getApplicationContext(), "" + listItems.size(), Toast.LENGTH_LONG).show();
//                                Log.d("#originSize", "" + originSize);
//                                Log.d("#listItems.size()", "" + listItems.size());
                            Map<String, Object> map = JsonUtils.jsonObject2Map(jsonObject);
                            Log.d(TAG + "#refreshdata", map.get("data").toString());
                            List<Map<String, Object>> jsonList = JsonUtils.jsonArray2List(map.get("data").toString());
                            /**
                             * get the newest and the oldest dateline.
                             */
//                                newestDateline = jsonList.get(0).get("dateline").toString();
                            if (0 == jsonList.size()) {
                                return;
                            }
                            newestDateline = jsonList.get(0).get("dateline").toString();


                            for (int i = 0; i < jsonList.size(); i++) {
//                                convertMap = new HashMap<String, Object>();
                                listItems.add(i, new HashMap<String, Object>());
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
//                                Log.d(TAG + "#listItem", JsonUtils.list2JsonArrayString(listItems));
//                                photoViewAdapter = new PhotoViewAdapter(PullToRefresh.this, listItems);
//                                actualListView.setAdapter(photoViewAdapter);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e(TAG, volleyError.getMessage(), volleyError);
                }
            });

            requestQueue.add(getRefreshObjectRequest);

            return listItems;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> list) {
//            Log.d("originSize", "" + originSize);
//            Log.d("listItems.size()", "" + listItems.size());
            if (originSize == list.size()) {
                Toast.makeText(getApplicationContext(), "Already the newest", Toast.LENGTH_LONG).show();
            } else {
                photoViewAdapter.notifyDataSetChanged();
            }

            pullToRefreshListView.onRefreshComplete();

            super.onPostExecute(list);
        }
    }


    private class PullToLoadMoreTask extends AsyncTask<String, Void, List<Map<String, Object>>> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected List<Map<String, Object>> doInBackground(String... params) {
            String dateline = params[0];
            String requestUrl = RequestURLGet + "?dline=" + dateline;
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
//                                newestDateline = jsonList.get(0).get("dateline").toString();
                            oldestDateline = jsonList.get(jsonList.size() - 1).get("dateline").toString();
                            int originSize = listItems.size();
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
                                listItems.get(i + originSize).put("limb", BASEURL + convertMap.get("limb"));

                                /**
                                 * get JsonObject "user" and convert it to HashMap.
                                 */
                                convertMap = JsonUtils.jsonObject2Map(jsonList.get(i).get("user").toString());
                                /**
                                 * add the "name" to the specified index in listItems.
                                 */
                                listItems.get(i + originSize).put("name", convertMap.get("name"));
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
            photoViewAdapter.notifyDataSetChanged();
//            pullToRefreshListView.onRefreshComplete();

            super.onPostExecute(list);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pull_to_refresh, menu);
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
