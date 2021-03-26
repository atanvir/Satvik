package com.satvick.utils;

import android.content.Context;
import androidx.annotation.IdRes;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.satvick.R;

public class BottomMenuHelper {
    public static void showBadge(Context context, BottomNavigationView bottomNavigationView, @IdRes int itemId, String value) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.unread_message_layout, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.notification_badge);
        text.setText(value);
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }
}
