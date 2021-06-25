package com.satvick.expandablelistview;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.satvick.R;
import com.satvick.activities.ProductListActivity;
import com.satvick.model.CategoryModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;
import java.util.Map;

public class ThreeLevelListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private Map<CategoryModel.Categorylist, List<CategoryModel.Categorylist.SubCategoryModel>> data;


    public ThreeLevelListAdapter(Context context, Map<CategoryModel.Categorylist, List<CategoryModel.Categorylist.SubCategoryModel>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return data.keySet().toArray().length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(data.keySet().toArray()[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int group, int child) {
        return child;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_first, null);

        // init
        ImageView ivDropDown = convertView.findViewById(R.id.ivDropDown);
        RelativeLayout rlMain = convertView.findViewById(R.id.rlMain);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        ImageView imageViewCategoriesItem = convertView.findViewById(R.id.imageViewCategoriesItem);
        TextView text = convertView.findViewById(R.id.rowParentText);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rlMain.setBackgroundColor(groupPosition % 2 == 0 ? context.getColor(R.color.colorLogOut) : context.getColor(R.color.odd_cat_color));
        }
        if (!isExpanded) ivDropDown.setBackgroundResource(R.drawable.down_arrow_two);
        else ivDropDown.setBackgroundResource(R.drawable.up_arrow_one);

        if (data.keySet().toArray()[groupPosition] instanceof CategoryModel.Categorylist) {
            text.setText("" + ((CategoryModel.Categorylist) data.keySet().toArray()[groupPosition]).getCategory_name());
            if(!((CategoryModel.Categorylist) data.keySet().toArray()[groupPosition]).getCategory_name().equalsIgnoreCase("Herbal")){
                imageViewCategoriesItem.setImageResource(Integer.parseInt(((CategoryModel.Categorylist) data.keySet().toArray()[groupPosition]).getCat_image()));
            }else{
                Glide.with(context).load(((CategoryModel.Categorylist) data.keySet().toArray()[groupPosition]).getCat_image()).into(imageViewCategoriesItem);
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_third, null);
        TextView textView = (TextView) convertView.findViewById(R.id.rowThirdText);
        textView.setText(data.get(data.keySet().toArray()[groupPosition]).get(childPosition).getSubcategory_name());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductListActivity.class);
                intent.putExtra("from", "HomeFragmentAfterLogin");
                intent.putExtra(GlobalVariables.subcatid, data.get(data.keySet().toArray()[groupPosition]).get(childPosition).getId());
                intent.putExtra(GlobalVariables.subcatname, data.get(data.keySet().toArray()[groupPosition]).get(childPosition).getCategory_id());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
