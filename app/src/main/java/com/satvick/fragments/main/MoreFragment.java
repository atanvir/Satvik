package com.satvick.fragments.main;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.Interfaces.IHelpCenter;
import com.satvick.Interfaces.IHelpCenterImplementation;
import com.satvick.R;
import com.satvick.activities.AboutActivity;
import com.satvick.activities.FaqsActivity;
import com.satvick.activities.TermsOfUseActivity;
import com.satvick.databinding.FragmentMoreBinding;
import com.satvick.utils.CommonUtil;

import org.jetbrains.annotations.NotNull;

public class MoreFragment extends Fragment implements View.OnClickListener {

    private FragmentMoreBinding binding;
    private IHelpCenter center;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentMoreBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        init();
        initCtrl();
    }

    private void init(){
        center=new IHelpCenterImplementation();
    }

    private void initCtrl(){
        binding.tvFAQs.setOnClickListener(this);
        binding.tvAboutUs.setOnClickListener(this);
        binding.tvTerms.setOnClickListener(this);
        binding.tvContactUs.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvContactUs: center.showBottomSheet(getActivity(),""); break;
            case R.id.tvFAQs: CommonUtil.startNewActivity(requireActivity(),FaqsActivity.class); break;
            case R.id.tvAboutUs: CommonUtil.startNewActivity(requireActivity(),AboutActivity.class); break;
            case R.id.tvTerms: CommonUtil.startNewActivity(requireActivity(),TermsOfUseActivity.class); break;
        }
    }
}
