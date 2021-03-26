package com.satvick.adapters;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.databinding.ItemNotificationListBinding;
import com.satvick.model.Notficationlist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<Notficationlist> notificationList;
    private OnNotificationListItemClickListener listener;


    public NotificationAdapter(Context context, List<Notficationlist> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        ItemNotificationListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_notification_list, parent, false);
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.binding.tvTitle.setText(notificationList.get(position).getTitle());
        holder.binding.tvContent.setText(notificationList.get(position).getContent());
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat outputFormat=new SimpleDateFormat("HH:mm a");
        String desireDate="";
        try
        {
            Date date=dateFormat.parse(notificationList.get(position).getCreatedAt().split(" ")[1]);
            desireDate=outputFormat.format(date);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        holder.binding.tvTime.setText(desireDate);



    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationListBinding binding;


        public ViewHolder(ItemNotificationListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public interface OnNotificationListItemClickListener {
        void onItemClick(View view, int pos);
    }

    public void setOnNotificationListClickListener(OnNotificationListItemClickListener listener) {
        this.listener = listener;
    }
}
