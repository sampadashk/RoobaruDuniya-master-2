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
 * Created by KV on 22/12/17.
 */

public class AudioHorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<AudioStories> audioList;


   // private static AudioHorizontalAdapter.ClickListener clickListener;

    AudioHorizontalAdapter(Context ct, List<AudioStories> audioList) {
        this.context = ct;
        this.audioList = audioList;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false);
            AudioHorizontalAdapter.ViewHolder myviewholder = new AudioHorizontalAdapter.ViewHolder(view);
            return myviewholder;



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                AudioHorizontalAdapter.ViewHolder holder1 = (AudioHorizontalAdapter.ViewHolder) holder;
        final AudioStories categorys = audioList.get(position);
                final AudioStory category =categorys.getAu();
               final String key=categorys.getKey();

                final Context context = holder1.imageView.getContext();
                if(category!=null) {
                    holder1.textImage.setText(category.getTitle());
                    //  Log.d("getText", category.getText());
                    //   Log.d("getImage", "" + category.getImage());
                    String d = category.getPhoto();

                    Glide.with(context).load(d).into(((AudioHorizontalAdapter.ViewHolder) holder).imageView);
                }
                holder1.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context,PlayAudioActivity.class);
                        intent.putExtra("AudioObj",category);
                        intent.putExtra("keysend",key);
                        context.startActivity(intent);

                    }
                });

               holder1.textImage.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(context,PlayAudioActivity.class);
                       intent.putExtra("AudioObj",category);
                       intent.putExtra("keysend",key);
                       context.startActivity(intent);

                   }
               });




            }






    //  ((AudioHorizontalAdapter.ViewHolder) holder).imageView.setContentDescription(category.getText());




  /*  public interface ClickListener {
        void onItemClick(int position, View v);
    }
    */


  /*  public void setOnItemClickListener(AudioHorizontalAdapter.ClickListener clickListener) {
        AudioHorizontalAdapter.clickListener = clickListener;
    }
    */


    @Override
    public int getItemCount() {
        //  Log.d("cksize", "" + audios.size());
        return audioList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder   {
        ImageView imageView;
        TextView textImage;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.myImageView);
            textImage = (TextView) itemView.findViewById(R.id.myImageViewText);
           // itemView.setOnClickListener(this);

        }

/*
        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);

        }
        */
    }

}