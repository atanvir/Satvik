package com.satvick.fragments.more;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.activities.MyReferralsListAdapter;
import com.satvick.databinding.FragmentMyreferralsBinding;
import com.satvick.model.ReferalListModel;

public class ReferralFragment extends Fragment {

    View rootView;
    FragmentMyreferralsBinding binding;
    private ReferalListModel.ReferalList referlist;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myreferrals, container, false);
        rootView = binding.getRoot();
        binding.recyclerView.setAdapter(new MyReferralsListAdapter(getActivity(),referlist.getList()));


        return rootView;
    }


    public ReferralFragment(ReferalListModel.ReferalList referlist)
    {
        this.referlist=referlist;
    }

}
