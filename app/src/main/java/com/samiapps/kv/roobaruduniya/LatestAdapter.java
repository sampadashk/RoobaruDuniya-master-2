package com.samiapps.kv.roobaruduniya;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by KV on 25/10/17.
 */

public class LatestAdapter extends RecyclerView.Adapter<LatestAdapter.ViewHolder> {
    Context ct;
   private ArrayList<RoobaruList> roobaruLists;
   // private ArrayList<RoobaruDuniya> latestArticles;



    LatestAdapter(Context ct,ArrayList<RoobaruList> roobaruLists)
    {
        this.ct=ct;
        this.roobaruLists=roobaruLists;
    }
    @Override
    public LatestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ct).inflate(R.layout.latest_page,parent,false);
        LatestAdapter.ViewHolder viewHolder=new LatestAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        RoobaruList  latest = roobaruLists.get(position);

        holder.latestTitl.setText(latest.getRoobaru().getTitle());
        String image=latest.getRoobaru().getPhoto();
        final String key=latest.getKey();
        if (image != null) {
            Glide.with(ct)
                    .load(image)
                    .into(holder.vlatest);
        }
        /*
        else {
            //TODO try firebase storage
            FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
            StorageReference storageReference=firebaseStorage.getReference().child("default").child("0.jpg");
            //Drawable drawable=ct.getResources().getDrawable(R.drawable.defaultpp);
          //  Glide.with(ct).load(R.drawable.defaultpp).into(holder.vlatest);
            String path=storageReference.getPath();
            Log.d("ckkpath",path);
            Glide.with(ct).load(path) .into(holder.vlatest);









        }
        */

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ct, ArticleDetail.class);
                // intent.putExtra("position", position);
                intent.putExtra("keySelected", key);
                // Bundle b = new Bundle();
                //  b.putSerializable(ArticleDetail.TAG, item);
                //  intent.putExtras(b);
                ct.startActivity(intent);
            }
        });



    }



    @Override
    public int getItemCount() {
        return roobaruLists.size();
    }
   public  class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView latestTitl;
        public ImageView vlatest;
        public ImageButton leftNav;
        public ImageButton rightNav;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            latestTitl=(TextView)itemView.findViewById(R.id.latest_title);
            vlatest=(ImageView)itemView.findViewById(R.id.latest_img);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.relative_lay);


        }




        }
    }

