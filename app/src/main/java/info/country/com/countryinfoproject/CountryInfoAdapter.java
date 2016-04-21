package info.country.com.countryinfoproject;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by JP on 20-04-2016.
 * Controller to handle Data and ListView for application
 */
public class CountryInfoAdapter extends ArrayAdapter<CountryInfo> {
    ArrayList<CountryInfo> countryInfo;
    int inflateLayout;
    LayoutInflater rowInflater;
    ViewHolder holder;

    private LruCache<String, Bitmap> mLruCache;

    public CountryInfoAdapter(Context context, int inflateLayout, ArrayList<CountryInfo> countryInfo) {
        super(context, inflateLayout, countryInfo);
        rowInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflateLayout = inflateLayout;
        this.countryInfo = countryInfo;

        final int memClass = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int cacheSize = 1024 * 1024 * memClass / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            view = rowInflater.inflate(inflateLayout, null);
            holder.txtTitle = (TextView) view.findViewById(R.id.txt_title);
            holder.txtDescription = (TextView) view.findViewById(R.id.txt_description);
            holder.imgImage = (ImageView) view.findViewById(R.id.img_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtTitle.setText(countryInfo.get(position).getTitle());
        holder.txtDescription.setText(countryInfo.get(position).getDescription());
        holder.imgImage.setImageResource(R.drawable.android);

        Bitmap bm = getBitmapFromMemCache(countryInfo.get(position).getTitle());
        if (bm == null){
            new ImageDownloadAsyncTask(holder.imgImage, countryInfo.get(position).getTitle()).execute(countryInfo.get(position).getImageUrl());
        }

        return view;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return (Bitmap) mLruCache.get(key);
    }

    /**
     * AsyncTask to download images to smooth scroll
     * String : URL to download image
     * Bitmap : To display in ImageView
     */
    private class ImageDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmapImage;
        String title;

        public ImageDownloadAsyncTask(ImageView bitmapImage, String title) {
            this.bitmapImage = bitmapImage;
            this.title = title;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(params[0]).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (params[0] != null && bitmap != null) {
                addBitmapToMemoryCache(title, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bitmapImage.setImageBitmap(bitmap);
        }
    }

    /**
     * View holder for optimize ListView
     */
    static class ViewHolder {
        public TextView txtTitle;
        public TextView txtDescription;
        public ImageView imgImage;

    }
}
