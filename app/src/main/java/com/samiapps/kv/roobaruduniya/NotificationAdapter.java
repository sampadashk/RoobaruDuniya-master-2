package com.samiapps.kv.roobaruduniya;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KV on 21/7/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    ArrayList<Notification> notifications;
    private static NotificationAdapter.ClickListener clickListener;

    NotificationAdapter(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }


    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(v);
    }

    public void setOnItemClickListener(NotificationAdapter.ClickListener clickListener) {
        NotificationAdapter.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification n = notifications.get(position);

        /*String type=n.getType();
        String articleTitle=n.getArticle();
        String by=n.getByPerson();
        holder.displaynotification.setText("New "+type+"on"+"article "+articleTitle+"by"+by);
        */
        String message = n.getMessage();
        holder.displaynotification.setText(message);


    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView displaynotification;

        public ViewHolder(View itemView) {
            super(itemView);
            displaynotification = (TextView) itemView.findViewById(R.id.text_notification);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}
