package com.samiapps.kv.roobaruduniya;

import android.content.Context;
import android.content.Intent;
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
 * Created by KV on 12/7/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    private static CommentAdapter.ClickListener clickListener;
    private ArrayList<Comment> comments;

    CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment_layout, parent, false);
        CommentAdapter.ViewHolder myviewholder = new CommentAdapter.ViewHolder(v);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Comment cmt = comments.get(position);
        String cmtName = cmt.getCommentorName();
        String commnt = cmt.getComment();
        String date = cmt.getDate();
        String imgCommentor = cmt.getuPhoto();
        //   Log.d("commentname",cmtName);
        holder.cName.setText(cmtName);
        holder.cmnt.setText(commnt);
        holder.dateTextView.setText(date);
        Glide.with(context)
                .load(imgCommentor)
                .into(holder.uPhotoView);
        holder.cName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment cmt = comments.get(position);
                String name = cmt.commentorName;

               // Log.d("commentorname", name);
                String uid = cmt.getUid();
              //  Log.d("getuid", uid);
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("senuid", uid);

                context.startActivity(intent);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ListsDatabaseList theRemovedItem = list.get(position);
                // remove your item from data base
                Comment cmt = comments.get(position);
                comments.remove(cmt);  // remove the item from list
                notifyItemRemoved(position); // notify the adapter about the removed item
            }
        });

    }

    public void setOnItemClickListener(CommentAdapter.ClickListener clickListener) {
        CommentAdapter.clickListener = clickListener;
    }


    @Override
    public int getItemCount() {
        // Log.d("checkcosize",""+comments.size());
        return comments.size();

    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView cName;
        public TextView cmnt;
        public TextView dateTextView;
        public ImageView uPhotoView;
        public ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            cName = (TextView) itemView.findViewById(R.id.uc_name);
            cmnt = (TextView) itemView.findViewById(R.id.uc_comment);
            dateTextView = (TextView) itemView.findViewById(R.id.cmt_data);
            uPhotoView = (ImageView) itemView.findViewById(R.id.img_profile);
            deleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);

        }
    }
}
