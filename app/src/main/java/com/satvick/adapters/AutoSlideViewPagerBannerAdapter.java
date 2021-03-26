package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.satvick.R;
import com.satvick.activities.ProductListActivity;
import com.satvick.model.HomeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class AutoSlideViewPagerBannerAdapter extends PagerAdapter {

    private Context context;
    List<HomeModel.Banner> bannersList;

    public AutoSlideViewPagerBannerAdapter(Context context, List<HomeModel.Banner> bannersList) {
        this.context = context;
        this.bannersList = bannersList;
    }

    @Override
    public int getCount() {
        return bannersList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);
        ImageView linearLayout = view.findViewById(R.id.image_view_slider);

        String imgUrl = bannersList.get(position).getImage();
        if (imgUrl != null && imgUrl.length() > 0) {
            Picasso.with(context).load(imgUrl).into(linearLayout);
        }

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent=new Intent(context,ProductListActivity.class);
                intent.putExtra("from","AutoSlideViewPagerBannerAdapter");
                intent.putExtra(GlobalVariables.subsubcatid,bannersList.get(position).getFilterData());
                intent.putExtra(GlobalVariables.section_name,"Items");
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