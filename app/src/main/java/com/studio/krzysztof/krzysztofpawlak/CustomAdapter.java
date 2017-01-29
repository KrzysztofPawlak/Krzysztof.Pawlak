package com.studio.krzysztof.krzysztofpawlak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Krzysiek on 2017-01-19.
 */

public class CustomAdapter extends BaseAdapter {
    private List<Response.ArrayBean> mCustomList;
    private Context mContext;
    private ImageView picture;
    private TextView title;
    private TextView desc;
    private String imgUrl;
    private Response.ArrayBean item;
    private LayoutInflater inflater;
    private View rowView;

    public CustomAdapter(Context mContext, List<Response.ArrayBean> mCustomList) {
        this.mContext = mContext;
        this.mCustomList = mCustomList;
    }

    @Override
    public int getCount() {
        return mCustomList.size();
    }

    @Override
    public Object getItem(int i) {
        return mCustomList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.list_item, viewGroup, false);

        item = (Response.ArrayBean) getItem(i);

        picture = (ImageView) rowView.findViewById(R.id.imageIv);
        title = (TextView) rowView.findViewById(R.id.titleTv);
        desc = (TextView) rowView.findViewById(R.id.descTv);

        imgUrl = item.getUrl();
        Picasso.with(mContext)
                .load(imgUrl)
                .error(R.drawable.error)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .placeholder(R.drawable.sample)
                .into(picture);
        title.setText(item.getTitle());
        desc.setText(item.getDesc());

        return rowView;
    }

    public void addListItemToAdapter(List<Response.ArrayBean> list) {
        mCustomList.addAll(list);
        this.notifyDataSetChanged();
    }
}
