package com.satvick.fragments.main;

import android.content.Intent;

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

public class MoreFragment extends Fragment implements View.OnClickListener {

    View rootView;
    FragmentMoreBinding binding;
    IHelpCenter center;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        rootView = binding.getRoot();
        binding.tvFAQs.setOnClickListener(this);
        binding.tvAboutUs.setOnClickListener(this);
        binding.tvTerms.setOnClickListener(this);
        binding.tvContactUs.setOnClickListener(this);
        center=new IHelpCenterImplementation();

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvContactUs:
                center.showBottomSheet(getActivity(),"");
                break;

            case R.id.tvFAQs:
               startActivity(new Intent(getActivity(), FaqsActivity.class));
               break;

            case R.id.tvAboutUs:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;

            case R.id.tvTerms:
                startActivity(new Intent(getActivity(), TermsOfUseActivity.class));
                break;

        }

    }

}
