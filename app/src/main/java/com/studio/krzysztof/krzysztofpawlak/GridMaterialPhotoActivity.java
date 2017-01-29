package com.studio.krzysztof.krzysztofpawlak;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class GridMaterialPhotoActivity extends Activity {
    RecyclerView mRecyclerView;
//    RecyclerView.LayoutManager mLayoutManager;
    GridLayoutManager mLayoutManager;
//    RecyclerView.Adapter mAdapter;
    GridAdapter mAdapter;

    ListView listView;
    GridViewWithHeaderAndFooter gridView;
    Response responseObject;
    CustomAdapter adapter;
    //    static String BASE_SERVER_URL = "http://127.0.0.1:8080";
//    static String BASE_SERVER_URL = "http://10.0.2.2:8080";
    static String BASE_SERVER_URL = "http://sunpatrol.pe.hu";

    public Handler mHandler;
    public View ftView;
//    public boolean loading = false;
    int nrJSONFile = 0;

    Gson gson;
    AsyncHttpClient client;

    private int previousTotal = 0;
    private boolean loading = false;
    private int visibleThreshold = 0;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_material_grid);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.findLastVisibleItemPosition();

        client = new AsyncHttpClient();
        takeData();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    loading = true;
                    nrJSONFile++;
                    takeData();
                }
            }
        });

        mHandler = new GridMaterialPhotoActivity.MyHandler();
    }


    private Response takeData() {
        String url = BASE_SERVER_URL + "/page_" + nrJSONFile + ".json";
        client.get(GridMaterialPhotoActivity.this, url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);

//                Toast.makeText(getApplicationContext(),"tu" + responseString,
//                        Toast.LENGTH_LONG).show();

                if (!responseString.contains("Error 404 - Page Not Found")) {
                    gson = new Gson();
                    responseObject = gson.fromJson(responseString, Response.class);
                    if (nrJSONFile == 0) {
//                        adapter = new CustomAdapter(PhotoActivity.this, responseObject.getArray());
//                        gridView.addFooterView(ftView);
//                        gridView.setAdapter(adapter);
                        mAdapter = new GridAdapter(GridMaterialPhotoActivity.this, responseObject.getArray());
                        mRecyclerView.setAdapter(mAdapter);
//                        Toast.makeText(getApplicationContext(),"dodaj" + mAdapter.getItemCount(),
//                                Toast.LENGTH_LONG).show();
                    } else {
                        Message msg = mHandler.obtainMessage(1, responseObject.getArray());
                        mHandler.sendMessage(msg);
//                Toast.makeText(getApplicationContext(),"dodaj" + mAdapter.getItemCount(),
//                        Toast.LENGTH_LONG).show();
                    }
                } else {
//                    mHandler.sendEmptyMessage(2);
                }

            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
//                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"nie poszlo",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
//                progressBar.setVisibility(View.GONE);
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
                    mAdapter.addListItemToAdapter((ArrayList<Response.ArrayBean>) msg.obj);
                    loading = false;
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


