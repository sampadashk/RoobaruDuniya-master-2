package com.samiapps.kv.roobaruduniya;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by KV on 13/8/17.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<MainDisplay> categories;
    private static ClickListener clickListener;

    MainAdapter(Context ct, ArrayList<MainDisplay> categories) {
        this.context = ct;
        this.categories = categories;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false);
        ViewHolder myviewholder = new ViewHolder(view);
        return myviewholder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainAdapter.ViewHolder holder1 = (MainAdapter.ViewHolder) holder;
        MainDisplay category = categories.get(position);
        Context context = holder1.imageView.getContext();
        holder1.textImage.setText(category.getText());
      //  Log.d("getText", category.getText());
     //   Log.d("getImage", "" + category.getImage());
        int d = category.getImage();
        Glide.with(context).load(d).into(((ViewHolder) holder).imageView);
        ((ViewHolder) holder).imageView.setContentDescription(category.getText());


    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(MainAdapter.ClickListener clickListener) {
        MainAdapter.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
      //  Log.d("cksize", "" + categories.size());
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textImage;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.myImageView);
            textImage = (TextView) itemView.findViewById(R.id.myImageViewText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);

        }
    }
}
