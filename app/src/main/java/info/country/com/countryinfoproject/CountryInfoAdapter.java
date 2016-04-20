package info.country.com.countryinfoproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JP on 20-04-2016.
 */
public class CountryInfoAdapter extends ArrayAdapter<CountryInfo> {
    ArrayList<CountryInfo> countryInfo;
    int inflateLayout;
    LayoutInflater rowInflater;
    ViewHolder holder;

    public CountryInfoAdapter(Context context, int inflateLayout, ArrayList<CountryInfo> countryInfo) {
        super(context, inflateLayout, countryInfo);
        rowInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflateLayout = inflateLayout;
        this.countryInfo = countryInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            view = rowInflater.inflate(inflateLayout, null);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView txtTitle;
        public TextView txtDescription;
        public ImageView imgImage;

    }
}
