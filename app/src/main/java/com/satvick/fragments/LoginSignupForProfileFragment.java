package com.satvick.fragments;

import android.content.Intent;
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


public class LoginSignupForProfileFragment extends Fragment implements View.OnClickListener {

    View rootView;
    FragmentLoginSignupForProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_signup_for_profile, container, false);
        rootView = binding.getRoot();

        binding.loginButton.setOnClickListener(this);
        binding.signupButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginButton:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;

            case R.id.signupButton:
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                break;
        }
    }
}
