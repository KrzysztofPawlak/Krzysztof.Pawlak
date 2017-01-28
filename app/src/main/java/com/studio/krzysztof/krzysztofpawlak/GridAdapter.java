package com.studio.krzysztof.krzysztofpawlak;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Krzysiek on 2017-01-28.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private List<Response.ArrayBean> mCustomList;
    private Context mContext;

    public GridAdapter(Context mContext, List<Response.ArrayBean> mCustomList) {
        this.mContext = mContext;
        this.mCustomList = mCustomList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.title.setText(mCustomList.get(position).getTitle());
        viewHolder.desc.setText(mCustomList.get(position).getDesc());
        Picasso.with(mContext)
                .load(mCustomList.get(position).getUrl())
                .error(R.drawable.error)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .placeholder(R.drawable.sample)
                .into(viewHolder.picture);
    }

    public int getItemCount() {
        return mCustomList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView picture;
        public TextView title;
        public TextView desc;

        public ViewHolder(View itemView) {
            super(itemView);

            picture = (ImageView) itemView.findViewById(R.id.imageIv);
            title = (TextView) itemView.findViewById(R.id.titleTv);
            desc = (TextView) itemView.findViewById(R.id.descTv);
        }
    }
}
