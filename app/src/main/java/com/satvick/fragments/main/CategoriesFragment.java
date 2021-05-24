package com.satvick.fragments.main;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.satvick.R;
import com.satvick.activities.SearchScreenActivity;
import com.satvick.databinding.FragmentCategoriesBinding;
import com.satvick.expandablelistview.ThreeLevelListAdapter;
import com.satvick.model.CategoryModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.HelperClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoriesFragment extends Fragment implements View.OnClickListener, ExpandableListView.OnGroupExpandListener, Callback<CategoryModel> {
    private FragmentCategoriesBinding binding;
    private Map<CategoryModel.Categorylist,List<CategoryModel.Categorylist.SubCategoryModel>> allData=new HashMap<>();
    private MyDialog myDialog;
    int previousGroup = -1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false);
        initControl();
        if (HelperClass.showInternetAlert(getActivity())) {
            myDialog=new MyDialog(getActivity());
            getCategories();
        }

        return binding.getRoot();
    }

    private void getCategories() {
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<CategoryModel> call = apiInterface.getCategories();
        call.enqueue(this);
    }

    private void settingAdapter(CategoryModel model) {
        binding.expandibleListview.setVisibility(View.VISIBLE);
        binding.expandibleListview.setAdapter(new ThreeLevelListAdapter(getActivity(), getAllData(model)));
    }

    private Map<CategoryModel.Categorylist, List<CategoryModel.Categorylist.SubCategoryModel>> getAllData(CategoryModel model) {
        allData.clear();
        for(int i=0;i<model.getCategory_name().size();i++) {
            allData.put(model.getCategory_name().get(i),model.getCategory_name().get(i).getSubcategory());
        }
        return allData;
    }

    private void initControl() {
        binding.llSearch.setOnClickListener(this);
        binding.expandibleListview.setOnGroupExpandListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        case R.id.llSearch:
        Intent intent=new Intent(getActivity(), SearchScreenActivity.class);
        startActivity(intent);
        break;
        }
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        if (previousGroup != -1 && groupPosition != previousGroup) {
            binding.expandibleListview.collapseGroup(previousGroup);
        }
        previousGroup = groupPosition;

    }

    @Override
    public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
        if (response.body().getStatus().equals("SUCCESS")) { myDialog.hideDialog(); settingAdapter(response.body());}
        else { myDialog.hideDialog(); }

    }

    @Override
    public void onFailure(Call<CategoryModel> call, Throwable t) {
        myDialog.hideDialog();
        Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}