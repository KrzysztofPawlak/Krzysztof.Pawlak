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

class HFAdapter extends HFRecyclerViewAdapter<List<Response.ArrayBean>, HFAdapter.DataViewHolder> {

    boolean isLoading;
    private List<Response.ArrayBean> mCustomList;
    private Context mContext;

    public HFAdapter(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public void footerOnVisibleItem() {
        if (!isLoading) {

            isLoading = true;
        }
    }

    @Override
    public HFAdapter.DataViewHolder onCreateDataItemViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        return new DataViewHolder(v);
    }

    @Override
    public void onBindDataItemViewHolder(HFAdapter.DataViewHolder holder, int position) {
        holder.title.setText(getData().get(position).getTitle());
        holder.desc.setText(getData().get(position).getDesc());
        Picasso.with(mContext)
                .load(getData().get(position).getUrl())
                .error(R.drawable.error)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .placeholder(R.drawable.sample)
                .into(holder.picture);
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView title;
        public TextView desc;

        public DataViewHolder(View itemView) {
            super(itemView);

            picture = (ImageView) itemView.findViewById(R.id.imageIv);
            title = (TextView) itemView.findViewById(R.id.titleTv);
            desc = (TextView) itemView.findViewById(R.id.descTv);
        }
    }
}