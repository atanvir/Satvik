package com.satvick.expandablelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.satvick.R;

import java.util.List;


public class SecondLevelAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<String[]> data;
    String[] headers;

    private Animation animationUp;
    private Animation animationDown;

    public SecondLevelAdapter(Context context, String[] headers, List<String[]> data) {
        this.context = context;
        this.data = data;
        this.headers = headers;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return headers.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_second, null);

        TextView text = (TextView) convertView.findViewById(R.id.rowSecondText);
        String groupText = getGroup(groupPosition).toString();
        text.setText(groupText);

        ImageView ivDropDown = (ImageView) convertView.findViewById(R.id.ivDropDown);

        if (!isExpanded) {
            ivDropDown.setBackgroundResource(R.drawable.down_arrow_two);
        } else {
            ivDropDown.setBackgroundResource(R.drawable.up_arrow_one);
        }

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        String[] childData;
        childData = data.get(groupPosition);

        return childData[childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_third, null);


        animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

        TextView textView = (TextView) convertView.findViewById(R.id.rowThirdText);

        String[] childArray = data.get(groupPosition);

        String text = childArray[childPosition];

        String[] afterSplit=text.split(","); //"shorts,46"

        textView.setText(afterSplit[0]);           //shorts

        textView.startAnimation(animationDown);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        String[] children = data.get(groupPosition);

        try {
            return children.length;
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return children.length;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}