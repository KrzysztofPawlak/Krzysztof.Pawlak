package com.studio.krzysztof.krzysztofpawlak;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Krzysiek on 2017-01-28.
 */

public class GridAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<Response.ArrayBean> mCustomList;
    private Context mContext;

    public GridAdapter(Context mContext, List<Response.ArrayBean> mCustomList) {
        this.mContext = mContext;
        this.mCustomList = mCustomList;
    }

    @Override
    public int getItemViewType(int position) {
        return mCustomList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.grid_item, viewGroup, false);
            vh = new ViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.progressbar_item, viewGroup, false);
            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder)holder).title.setText(mCustomList.get(position).getTitle());
            ((ViewHolder)holder).desc.setText(mCustomList.get(position).getDesc());
            Picasso.with(mContext)
                    .load(mCustomList.get(position).getUrl())
                    .error(R.drawable.error)
                    .fit()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.sample)
                    .into(((ViewHolder)holder).picture);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void addListItemToAdapter(List<Response.ArrayBean> list) {
//        mCustomList.remove(mCustomList.size() - 1);
//        this.notifyItemRemoved(mCustomList.size());
//        this.notifyAll();
//        mCustomList.notifyAll();
        mCustomList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addProgressBar() {
        mCustomList.add(null);
        this.notifyItemInserted(mCustomList.size());
    }

    public void removeProgressBar() {
        mCustomList.remove(mCustomList.size() - 1);
        this.notifyItemRemoved(mCustomList.size());
    }

    public int getItemCount() {
        return mCustomList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}