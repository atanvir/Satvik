package com.satvick.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.satvick.fragments.more.ReferralFragment;
import com.satvick.fragments.more.TermConditionFragment;
import com.satvick.model.ReferalListModel;

public class TablyoutFragmentsAdapter extends  FragmentPagerAdapter{

    private String[] tabTitles = new String[]{"T & C", "My Referrals"};
    private Context context;
    private ReferalListModel.ReferalList referlist;

    public TablyoutFragmentsAdapter(FragmentManager fm, Context context,ReferalListModel.ReferalList referlist) {
        super(fm);
        this.context = context;
        this.referlist=referlist;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new TermConditionFragment();
            case 1:return new ReferralFragment(referlist);
            default: break;
        }

        return new TermConditionFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}
