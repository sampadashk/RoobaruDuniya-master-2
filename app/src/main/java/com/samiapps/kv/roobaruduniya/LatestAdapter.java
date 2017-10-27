package com.samiapps.kv.roobaruduniya;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by KV on 25/10/17.
 */

public class LatestAdapter extends RecyclerView.Adapter<LatestAdapter.ViewHolder> {
    Context ct;

    private ArrayList<RoobaruDuniya> latestArticles;
    private static LatestListener latestListener;
    LatestAdapter(Context ct,ArrayList<RoobaruDuniya> latestArticles)
    {
        this.ct=ct;
        this.latestArticles=latestArticles;
    }
    @Override
    public LatestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ct).inflate(R.layout.latest_page,parent,false);
        LatestAdapter.ViewHolder viewHolder=new LatestAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(  LatestAdapter.ViewHolder holder, int position) {



        RoobaruDuniya  latest = latestArticles.get(position);

        holder.latestTitl.setText(latest.getTitle());
        String image = latest.getPhoto();

        if (image != null) {


            Glide.with(ct)
                    .load(image)
                    .into(holder.vlatest);
        }



    }
    public interface LatestListener {
         void onItemClick(int position, View v);


    }
    public void setOnItemClickListener(LatestAdapter.LatestListener clickListener) {
        LatestAdapter.latestListener = clickListener;
    }



    @Override
    public int getItemCount() {
        return latestArticles.size();
    }
   public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView latestTitl;
        public ImageView vlatest;
        public ImageButton leftNav;
        public ImageButton rightNav;

        public ViewHolder(View itemView) {
            super(itemView);
            latestTitl=(TextView)itemView.findViewById(R.id.latest_title);
            vlatest=(ImageView)itemView.findViewById(R.id.latest_img);
            leftNav = (ImageButton) itemView.findViewById(R.id.left_nav);
            rightNav = (ImageButton) itemView.findViewById(R.id.right_nav);
            rightNav.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


                latestListener.onItemClick(getAdapterPosition(), v);
            }


        }
    }

