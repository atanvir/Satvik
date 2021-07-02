package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.satvick.model.BannerBean;
import com.squareup.picasso.Picasso;
import com.satvick.R;
import com.satvick.activities.ProductListActivity;
import com.satvick.model.HomeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class AutoSlideViewPagerBannerAdapter extends PagerAdapter {
    private Context context;
    private List<BannerBean> list;
    public AutoSlideViewPagerBannerAdapter(Context context, List<BannerBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() { return list!=null?list.size():0; }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);
        ImageView linearLayout = view.findViewById(R.id.image_view_slider);
        String imgUrl = list.get(position).getImage();
        if (imgUrl != null && imgUrl.length() > 0) {
            Picasso.with(context).load(imgUrl).into(linearLayout);
        }

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(context,ProductListActivity.class);
                intent.putExtra(GlobalVariables.catid,"");
                intent.putExtra(GlobalVariables.subcatid,"");
                intent.putExtra(GlobalVariables.subsubcatid,list.get(position).getFilterData());
                intent.putExtra(GlobalVariables.section_name,"Banner");
                context.startActivity(intent);
            }
        });

        final ViewPager viewPager = (ViewPager) container;
        container.addView(view, 0);
        //viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

}