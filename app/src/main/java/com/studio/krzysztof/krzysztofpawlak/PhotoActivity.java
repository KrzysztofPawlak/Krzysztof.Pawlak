package com.studio.krzysztof.krzysztofpawlak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class PhotoActivity extends AppCompatActivity {

    //    static String BASE_SERVER_URL = "http://127.0.0.1:8080";
//    static String BASE_SERVER_URL = "http://10.0.2.2:8080";
    static String BASE_SERVER_URL = "http://sunpatrol.pe.hu";

    private static Handler mHandler;
    public View ftView;
    private boolean isLoading = false;
    private int nrJSONFile = 0;

    private GridViewWithHeaderAndFooter gridView;
    private Response responseObject;
    private CustomAdapter adapter;
    private Gson gson;
    private AsyncHttpClient client;
    private LayoutInflater li;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                final SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                getPrefs.edit().putBoolean("checkbox", false).commit();

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);

                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view);

        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridview2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(PhotoActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (absListView.getLastVisiblePosition() == i2 - 1 && gridView.getCount() >= 10 && isLoading == false) {
                    isLoading = true;
                    nrJSONFile++;
                    takeData();
                }
            }
        });

        li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.footer_view, null);
        mHandler = new MyHandler();

        client = new AsyncHttpClient();
        responseObject = takeData();
    }

    private Response takeData() {
        String url = BASE_SERVER_URL + "/page_" + nrJSONFile + ".json";
        client.get(PhotoActivity.this, url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);

                if (!responseString.contains("Error 404 - Page Not Found")) {
                    gson = new Gson();
                    responseObject = gson.fromJson(responseString, Response.class);
                    if (nrJSONFile == 0) {
                        adapter = new CustomAdapter(PhotoActivity.this, responseObject.getArray());
                        gridView.addFooterView(ftView);
                        gridView.setAdapter(adapter);
                    } else {
                        Message msg = mHandler.obtainMessage(1, responseObject.getArray());
                        mHandler.sendMessage(msg);
                    }
                } else {
                    mHandler.sendEmptyMessage(2);
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
        return responseObject;
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    gridView.addFooterView(ftView); // now isn't working
                    break;
                case 1:
                    adapter.addListItemToAdapter((ArrayList<Response.ArrayBean>) msg.obj);
                    isLoading = false;
                    break;
                case 2:
                    gridView.removeFooterView(ftView);
                    break;
                default:
                    break;
            }
        }
    }
}
