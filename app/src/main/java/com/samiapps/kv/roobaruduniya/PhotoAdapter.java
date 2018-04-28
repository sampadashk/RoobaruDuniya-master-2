package com.samiapps.kv.roobaruduniya;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by KV on 5/1/18.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    List<PhotoStories> photos;
    Context context;
    ClickPhotoListener clickPhotoListener;
    public interface ClickPhotoListener
    {
        void onItemClick(int position, View v);

    }
    public void setOnClickListener(PhotoAdapter.ClickPhotoListener clickPhotoListener)
    {
        this.clickPhotoListener=clickPhotoListener;

    }


    PhotoAdapter(Context context,List<PhotoStories> photos)
    {
        this.context=context;
        this.photos=photos;
    }
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.main_item,parent,false);

        return new PhotoAdapter.ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.ViewHolder holder, int position) {
       final PhotoStories p=photos.get(position);
       Glide.with(context).load(p.getPhoto().getPhoto()).into(holder.imgView);
        holder.title.setText(p.getPhoto().getTitle());
       holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,DisplayPhoto.class);
                intent.putExtra("sendPhoto",p.getPhoto());

                intent.putExtra("keysend",p.getKey());
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgView;
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            imgView=(ImageView)itemView.findViewById(R.id.myImageView);
            title=(TextView)itemView.findViewById(R.id.myImageViewText);
        }

        @Override
        public void onClick(View view) {
            clickPhotoListener.onItemClick(getAdapterPosition(), view);
        }
    }
}
