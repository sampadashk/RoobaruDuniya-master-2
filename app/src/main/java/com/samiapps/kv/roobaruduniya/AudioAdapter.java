package com.samiapps.kv.roobaruduniya;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KV on 14/12/17.
 */

public class AudioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
   List<SectionAudio> audios;

    private static AudioAdapter.ClickListener clickListener;

    AudioAdapter(Context ct, List<SectionAudio> audios) {
        this.context = ct;
        this.audios = audios;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View vew = LayoutInflater.from(context).inflate(R.layout.list_audio, parent, false);

            AudioAdapter.ViewHolder viewholder = new AudioAdapter.ViewHolder(vew);

            return viewholder;



    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        Log.d("positionnn",""+position);
        final String sectionName = audios.get(position).getHeaderTitle();

        ArrayList singleSectionItems = audios.get(position).getAllStories();
        AudioAdapter.ViewHolder holder1 = (AudioAdapter.ViewHolder) holder;
        holder1.textHeader.setText(sectionName);




       AudioHorizontalAdapter itemListDataAdapter = new AudioHorizontalAdapter(context, singleSectionItems);

        holder1.recycler_view_list.setHasFixedSize(true);
        holder1.recycler_view_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder1.recycler_view_list.setAdapter(itemListDataAdapter);


       /* itemListDataAdapter.setOnItemClickListener(new AudioHorizontalAdapter.ClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                Log.d("checkpp",""+pos);






            }
        });
        */




    }



        //  ((AudioAdapter.ViewHolder) holder).imageView.setContentDescription(category.getText());




    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(AudioAdapter.ClickListener clickListener) {
        AudioAdapter.clickListener = clickListener;
    }


    @Override
    public int getItemCount() {
        Log.d("checksize",""+audios.size());
        //  Log.d("cksize", "" + audios.size());
        return audios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        TextView textHeader;
        protected RecyclerView recycler_view_list;

        public ViewHolder(View itemView) {
            super(itemView);
            recycler_view_list = (RecyclerView) itemView.findViewById(R.id.recycler_view_list);
            textHeader = (TextView) itemView.findViewById(R.id.itemTitle);

        }


    }

}