package com.studio.krzysztof.krzysztofpawlak;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class GridMaterialPhotoActivity extends Activity {
    RecyclerView mRecyclerView;
    GridLayoutManager mLayoutManager;
    GridAdapter mAdapter;
    Response responseObject;
    //    static String BASE_SERVER_URL = "http://127.0.0.1:8080";
//    static String BASE_SERVER_URL = "http://10.0.2.2:8080";
    static String BASE_SERVER_URL = "http://sunpatrol.pe.hu";

    public Handler mHandler;
    int nrJSONFile = 0;

    Gson gson;
    AsyncHttpClient client;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, lastVisibleItem, visibleItemCount, totalItemCount;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.findLastVisibleItemPosition();

        mHandler = new GridMaterialPhotoActivity.MyHandler();
        client = new AsyncHttpClient();
        takeData();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {

                    nrJSONFile++;
//                    mAdapter.addProgressBar();
//                    mHandler.sendEmptyMessage(0);
//                    mHandler.sendEmptyMessage(2);
//                    mAdapter.addProgressBar();

                    takeData();
                    loading = true;
//                    Toast.makeText(getApplicationContext(),
//                            "dodaj" + nrJSONFile + "//" +
//                                    "dodaj" + totalItemCount + "//" +
//                                    "dodaj" + totalItemCount + "//" +
//                                    "dodaj" + visibleItemCount + "//"
//                            , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private Response takeData() {
        String url = BASE_SERVER_URL + "/page_" + nrJSONFile + ".json";

        client.get(GridMaterialPhotoActivity.this, url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);

                if (!responseString.contains("Error 404 - Page Not Found")) {
                    gson = new Gson();
                    responseObject = gson.fromJson(responseString, Response.class);
                    if (nrJSONFile == 0) {
//                        gridView.addFooterView(ftView);
//                        gridView.setAdapter(adapter);

                        mAdapter = new GridAdapter(GridMaterialPhotoActivity.this, responseObject.getArray());
                        mRecyclerView.setAdapter(mAdapter);
                    } else {

//                        mAdapter.removeProgressBar();
                        Message msg = mHandler.obtainMessage(1, responseObject.getArray());
                        mHandler.sendMessage(msg);
//                                mHandler.sendEmptyMessage(2);
                    }
                } else {
//                    mHandler.sendEmptyMessage(2);
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Toast.makeText(getApplicationContext(), "nie poszlo",
//                        Toast.LENGTH_LONG).show();
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
                    mAdapter.addProgressBar();
                    break;
                case 1:
                    mAdapter.addListItemToAdapter((ArrayList<Response.ArrayBean>) msg.obj);
                    loading = false;
                    break;
                case 2:
//                    mAdapter.removeProgressBar();
                    break;
                default:
                    break;
            }
        }
    }
}


