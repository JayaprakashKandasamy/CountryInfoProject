package info.country.com.countryinfoproject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by JP on 20-04-2016.
 * This is the main class for Application to display Main Screen
 */
public class CountryInfoMainScreen extends Activity {

    private final String JSON_URL = "https://dl.dropboxusercontent.com/u/746330/facts.json";
    private CountryInfoAdapter countryInfoAdapter;
    private ArrayList<CountryInfo> countryInfoList;
    private ActionBar actionBar;
    private String projectTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_info_main_screen);

        projectTitle = getString(R.string.app_name);
        actionBar = getActionBar();
        actionBar.setTitle(projectTitle);

        countryInfoList = new ArrayList<CountryInfo>();
        countryInfoAdapter = new CountryInfoAdapter(getApplicationContext(), R.layout.row_layout, countryInfoList);
        ListView infoList = (ListView) findViewById(R.id.country_info_list);
        infoList.setAdapter(countryInfoAdapter);

        new CountryInfoJSONAsyncTask().execute(JSON_URL);
    }

    private class CountryInfoJSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog downloadDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downloadDialog = new ProgressDialog(CountryInfoMainScreen.this);
            downloadDialog.setTitle("Country Information");
            downloadDialog.setMessage("Downloading, Please wait...");
            downloadDialog.show();
            downloadDialog.setCancelable(true);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                System.out.println("Response Code: " + conn.getResponseCode());
                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                    StringBuilder responseStrBuilder = new StringBuilder();
                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);

                    JSONObject object = new JSONObject(responseStrBuilder.toString());
                    projectTitle = object.getString("title");

                    JSONArray jsonarray = object.getJSONArray("rows");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        CountryInfo countryInfo = new CountryInfo();
                        countryInfo.setTitle(jsonobject.getString("title"));
                        countryInfo.setDescription(jsonobject.getString("description"));
                        countryInfo.setImageUrl(jsonobject.getString("imageHref"));
                        countryInfoList.add(countryInfo);
                    }

                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                actionBar.setTitle(projectTitle);
            else
                System.out.println("Download Fails");

            downloadDialog.cancel();
            countryInfoAdapter.notifyDataSetChanged();
        }
    }
}
