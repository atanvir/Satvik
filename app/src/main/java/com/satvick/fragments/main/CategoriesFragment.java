package com.satvick.fragments.main;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesFragment extends Fragment implements View.OnClickListener, ExpandableListView.OnGroupExpandListener, Callback<CategoryModel> {

    private FragmentCategoriesBinding binding;
    private Map<CategoryModel.Categorylist,List<CategoryModel.Categorylist.SubCategoryModel>> allData=new HashMap<>();
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
    int previousGroup = -1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        init();
        initCtrl();
        if (HelperClass.showInternetAlert(getActivity())) categoriesApi();
    }
    private void init(){

    }
    private void initCtrl() {
        binding.llSearch.setOnClickListener(this);
        binding.expandibleListview.setOnGroupExpandListener(this);
    }


    private void categoriesApi() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<CategoryModel> call = apiInterface.getCategories();
        call.enqueue(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.llSearch: startActivity(new Intent(getActivity(), SearchScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK)); break;
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
        if(getActivity()!=null){
        binding.progressBar.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            if (response.body().getStatus().equals(GlobalVariables.SUCCESS)) settingAdapter(response.body());
            else if (response.body().getStatus().equals(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getStatus(), requireActivity());
        } else CommonUtil.setUpSnackbarMessage(binding.getRoot(), "Internal Server Error", requireActivity());
        }
    }

    @Override
    public void onFailure(Call<CategoryModel> call, Throwable t) {
        if(getActivity()!=null) {
            binding.progressBar.setVisibility(View.GONE);
            CommonUtil.setUpSnackbarMessage(binding.getRoot(), t.getMessage(), requireActivity());
        }
    }

    private void settingAdapter(CategoryModel model) {
        binding.expandibleListview.setVisibility(View.VISIBLE);
        binding.expandibleListview.setAdapter(new ThreeLevelListAdapter(getActivity(), getAllData(model)));
    }

    private Map<CategoryModel.Categorylist, List<CategoryModel.Categorylist.SubCategoryModel>> getAllData(CategoryModel model) {
        allData.clear();
        for(int i=0;i<model.getCategory_name().size();i++) {
            if(model.getCategory_name().get(i).getCategory_name().equalsIgnoreCase("Natural & Homemade")) model.getCategory_name().get(i).setCat_image(""+R.drawable.natural);
            if(model.getCategory_name().get(i).getCategory_name().equalsIgnoreCase("Ayurvedic")) model.getCategory_name().get(i).setCat_image(""+R.drawable.ayurvedi);
            if(model.getCategory_name().get(i).getCategory_name().equalsIgnoreCase("Organic")) model.getCategory_name().get(i).setCat_image(""+R.drawable.organic);
            allData.put(model.getCategory_name().get(i),model.getCategory_name().get(i).getSubcategory());
        }
        return allData;
    }
}