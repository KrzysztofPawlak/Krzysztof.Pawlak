package com.studio.krzysztof.krzysztofpawlak;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class GridMaterialPhotoActivity extends Activity {

//    static String BASE_SERVER_URL = "http://127.0.0.1:8080";
//    static String BASE_SERVER_URL = "http://10.0.2.2:8080";
    static String BASE_SERVER_URL = "http://sunpatrol.pe.hu";
    private String url;

    private RecyclerView mRecyclerView;
    final HFAdapter hfAdapter = new HFAdapter(this);
    private GridLayoutManager manager;
    private View footerView;

    private Gson gson;
    private AsyncHttpClient client;
    private Response responseObject;

    private int nrJSONFile = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItem, lastVisibleItem, visibleItemCount, totalItemCount;
    private long startTime, stopTime, difference;
    private double sec;

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
        setContentView(R.layout.activity_photo_material_grid);

        client = new AsyncHttpClient();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(hfAdapter);
        manager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((hfAdapter.hasHeader() && hfAdapter.isHeader(position)) ||
                        hfAdapter.hasFooter() && hfAdapter.isFooter(position))
                    return manager.getSpanCount();
                return 1;
            }
        });

        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view, mRecyclerView, false);

        takeData();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = manager.getItemCount();
                firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    nrJSONFile++;
                    takeData();
                    loading = true;
                }
            }
        });
    }

    private Response takeData() {
        url = BASE_SERVER_URL + "/page_" + nrJSONFile + ".json";

        client.get(GridMaterialPhotoActivity.this, url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);

                if (!responseString.contains("Error 404 - Page Not Found")) {
                    gson = new Gson();
                    responseObject = gson.fromJson(responseString, Response.class);

                    if (nrJSONFile == 0) {
                        hfAdapter.setData(responseObject.getArray());
                    } else {
                        stopTime = System.nanoTime();
                        difference = stopTime - startTime;
                        sec = (double) difference / 1000000000.0;

                        if (sec < 1.0) { // only for show progress bar when too fast
                            Handler handlerTimer = new Handler();
                            handlerTimer.postDelayed(new Runnable() {
                                public void run() {
                                    hfAdapter.removeFooter();
                                    hfAdapter.addData(responseObject.getArray());
                                }
                            }, 2000);
                        } else {
                            hfAdapter.removeFooter();
                            hfAdapter.addData(responseObject.getArray());
                        }
                    }
                } else {

                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                hfAdapter.setFooterView(footerView);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onStart() {
                super.onStart();
                startTime = System.nanoTime();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

        return responseObject;
    }

}


