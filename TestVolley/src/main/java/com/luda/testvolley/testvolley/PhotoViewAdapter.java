package com.luda.testvolley.testvolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.List;
import java.util.Map;

/**
 * Created by chaos on 14-10-9.
 */
public class PhotoViewAdapter extends BaseAdapter {


    private Context context = null;
    private LayoutInflater listContainer = null;
    private List<Map<String, Object>> listItems = null;
    private RequestQueue requestQueue = null;
    private ImageLoader imageLoader = null;
    private BitmapCache bitmapCache = null;

    public class PhotoViewItem {
        TextView textView;
        ImageView imageView;
        ImageLoader.ImageListener imageListener;
    }

    public PhotoViewAdapter(Context context, List<Map<String, Object>> listItems) {
        this.context = context;
        this.listItems = listItems;
        listContainer = LayoutInflater.from(this.context);
        requestQueue = Volley.newRequestQueue(this.context);
        bitmapCache = new BitmapCache();
        imageLoader = new ImageLoader(requestQueue, bitmapCache);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return listItems.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoViewItem photoViewItem = null;
        if (null == convertView) {
            convertView = listContainer.inflate(R.layout.photo_item, null);
            photoViewItem = new PhotoViewItem();
            photoViewItem.textView = (TextView) convertView.findViewById(R.id.photo_item_textview);
            photoViewItem.imageView = (ImageView) convertView.findViewById(R.id.photo_item_imageview);
            photoViewItem.imageListener = ImageLoader.getImageListener(photoViewItem.imageView, R.drawable.refreshing, R.drawable.ic_launcher);
//            photoViewItem.imageListener = ImageLoader.getImageListener(photoViewItem.imageView, R.drawable.refreshing, R.drawable.ic_launcher);
//            photoViewItem.imageListener = ImageLoader.getImageListener()
            convertView.setTag(photoViewItem);
        } else {
            photoViewItem = (PhotoViewItem) convertView.getTag();
        }



        photoViewItem.textView.setText(listItems.get(position).get("name").toString());
//        imageLoader.get(listItems.get(position).get("limb").toString(), photoViewItem.imageListener, 50, 50);
        imageLoader.get(listItems.get(position).get("limb").toString(), photoViewItem.imageListener);
//        new DownLoadImageWithCache(photoViewItem.imageView).execute(listItems.get(position).get("limb").toString());
        return convertView;
    }
}
