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

import com.satvick.R;
import com.satvick.activities.LoginActivity;
import com.satvick.activities.SignUpActivity;
import com.satvick.databinding.FragmentLoginSignupForProfileBinding;
import com.satvick.utils.CommonUtil;

import org.jetbrains.annotations.NotNull;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private FragmentLoginSignupForProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginSignupForProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        init();
        initCtrl();
    }

    private void init(){ }

    private void initCtrl(){
        binding.loginButton.setOnClickListener(this);
        binding.signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginButton: CommonUtil.startNewActivity(requireActivity(), LoginActivity.class); break;
            case R.id.signupButton: CommonUtil.startNewActivity(requireActivity(), SignUpActivity.class); break;
        }
    }
}
