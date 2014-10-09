package com.luda.testvolley.testvolley;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    private final String TAG = this.getClass().getName();
    private String TOKEN = "";
    //    private final String TAG = "TestVolley";
//    private final String RequestURLGet = "http://m.weather.com.cn/data/101010100.html";
    private final String RequestURLGet = "http://px.eput.com/api/user?id=p18262281177";
    private final String IMAGEURL = "http://developer.android.com/images/home/aw_dac.png";

    private ImageView imageView = null;
    private NetworkImageView networkImageView = null;

    private LinearLayout linearLayout = null;

    private ImageLoader imageLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = (ImageView) findViewById(R.id.default_imageview);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

//        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        Log.d("TAG", "Max memory is " + maxMemory + "KB");
//        Toast.makeText(this, "Max memory is " + maxMemory + "KB", Toast.LENGTH_LONG).show();


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.refreshing, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
        Toast.makeText(this, imageHeight + "_" + imageWidth + "_" + imageType, Toast.LENGTH_LONG).show();

        ((TextView) findViewById(R.id.default_textview)).setText(imageHeight + "_" + imageWidth + "_" + imageType);
//        ((TextView) findViewById(R.id.default_textview)).setText(TAG);
//        Toast.makeText(this, this.getClass().getName(), Toast.LENGTH_LONG).show();

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {

            }
        });

        StringRequest getStringRequest = new StringRequest("http://px.eput.com/api/user?id=p18262281177",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d(TAG, s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage(), volleyError);
            }
        }
        );

        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://px.eput.com/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d(TAG, s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage(), volleyError);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user", "p18262281177");
                map.put("pass", "fighting");
                map.put("token", TOKEN);
                return map;
            }
        };

        JsonObjectRequest getJsonObjectRequest = new JsonObjectRequest(RequestURLGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(TAG, jsonObject.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage(), volleyError);
            }
        }
        );


        final ImageRequest imageRequest = new ImageRequest(
                IMAGEURL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(R.drawable.ic_launcher);
            }
        }
        );


        requestQueue.add(getStringRequest);
        requestQueue.add(postStringRequest);
        requestQueue.add(getJsonObjectRequest);
//        requestQueue.add(imageRequest);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue.add(imageRequest);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
                    @Override
                    public Bitmap getBitmap(String s) {
                        return null;
                    }

                    @Override
                    public void putBitmap(String s, Bitmap bitmap) {

                    }
                });

                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.drawable.refreshing, R.drawable.ic_launcher);

                imageLoader.get(IMAGEURL, imageListener);
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkImageView = (NetworkImageView) findViewById(R.id.networkimageview);
                networkImageView.setDefaultImageResId(R.drawable.refreshing);
                networkImageView.setErrorImageResId(R.drawable.ic_launcher);
                networkImageView.setImageUrl(IMAGEURL, imageLoader);
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkImageView = new NetworkImageView(MainActivity.this);
                networkImageView.setDefaultImageResId(R.drawable.refreshing);
                networkImageView.setErrorImageResId(R.drawable.ic_launcher);
//                networkImageView.setLayoutParams(new AbsListView.LayoutParams(100, 100));
                networkImageView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(networkImageView);
                networkImageView.setImageUrl(IMAGEURL, imageLoader);
            }
        });

        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PhotoList.class));
            }
        });

        findViewById(R.id.btn_jump2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PullToRefresh.class));
            }
        });
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
