package com.studio.krzysztof.krzysztofpawlak;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class GridMaterialPhotoActivity extends Activity {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    ListView listView;
    GridViewWithHeaderAndFooter gridView;
    Response responseObject;
    CustomAdapter adapter;
    //    static String BASE_SERVER_URL = "http://127.0.0.1:8080";
//    static String BASE_SERVER_URL = "http://10.0.2.2:8080";
    static String BASE_SERVER_URL = "http://sunpatrol.pe.hu";

    public Handler mHandler;
    public View ftView;
    public boolean isLoading = false;
    int nrJSONFile = 0;

    Gson gson;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_material_grid);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        client = new AsyncHttpClient();
        takeData();

//        Toast.makeText(getApplicationContext(),"dziala",
//                Toast.LENGTH_LONG).show();
//        responseObject = takeData();
//        takeData();
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


                    } else {
//                        Message msg = mHandler.obtainMessage(1, responseObject.getArray());
//                        mHandler.sendMessage(msg);
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
}


