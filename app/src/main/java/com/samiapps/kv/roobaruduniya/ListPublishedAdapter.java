package com.samiapps.kv.roobaruduniya;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KV on 17/8/17.
 */

public class ListPublishedAdapter extends RecyclerView.Adapter<ListPublishedAdapter.ViewHolder> {
    Context context;

    private ArrayList<RoobaruDuniya> articles;
    private static ListPublishedAdapter.ClickListener clickListener;

    public ListPublishedAdapter(ArrayList<RoobaruDuniya> articles, Context context) {
        this.articles = articles;
        this.context = context;

    }

    @Override
    public ListPublishedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_published, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListPublishedAdapter.ViewHolder holder, int position) {
        if (articles.size() > 0) {
            RoobaruDuniya rb = articles.get(position);
            String title = rb.getTitle();
            holder.publishedTitle.setText(title);
        }

    }

    public void setOnItemClickListener(ListPublishedAdapter.ClickListener clickListener) {
        ListPublishedAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @Override
    public int getItemCount() {
       // Log.d("pubs", "" + articles.size());
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView publishedTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            publishedTitle = (TextView) itemView.findViewById(R.id.publishedtitleId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}
