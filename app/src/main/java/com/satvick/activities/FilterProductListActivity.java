package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.adapters.FilterProductListByBrandAdapter;
import com.satvick.adapters.FilterProductListByColorAdapter;
import com.satvick.adapters.FilterProductListBySizeAdapter;
import com.satvick.adapters.PriceRangeAdapter;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityFilterProductListBinding;
import com.satvick.fcm.MyFirebaseMessagingService;
import com.satvick.model.BrandCheckedListModel;
import com.satvick.model.ColorCheckedListModel;
import com.satvick.model.FilterProductListModel;
import com.satvick.model.SizeCheckedListModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FilterProductListActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityFilterProductListBinding binding;
    LinearLayoutManager linearLayoutManager;
    FilterProductListBySizeAdapter filterProductListAdapter;
    FilterProductListByColorAdapter filterProductListByColorAdapter;
    FilterProductListByBrandAdapter filterProductListByBrandAdapter;

    List<String> sizeList = new ArrayList<>();
    List<String> colorList = new ArrayList<>();
    List<String> brandList = new ArrayList<>();
    List<String> hexcodeList = new ArrayList<>();

    FilterProductListModel data;

    List<SizeCheckedListModel> sizeNameCheckedList = new ArrayList<>();
    List<ColorCheckedListModel> colorNameCheckedList = new ArrayList<>();
    List<BrandCheckedListModel> brandNameCheckedList = new ArrayList<>();
    HashMap<List<String>,List<String>> colorHM=new HashMap<>();

    String commaSeparatedSizeId = "";
    String commaSeparatedColorId = "";
    String commaSeparatedBrandId = "";
    String maxValue="",minValue="";

    String comeFrom = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter_product_list);


        comeFrom = getIntent().getStringExtra("from");

        //get the selected CheckBox values.
        commaSeparatedSizeId = SharedPreferenceWriter.getInstance(this).getString("SizeIds");
        commaSeparatedColorId = SharedPreferenceWriter.getInstance(this).getString("ColorIds");
        commaSeparatedBrandId = SharedPreferenceWriter.getInstance(this).getString("BrandIds");


        binding.llSize.setOnClickListener(this);
        binding.llColor.setOnClickListener(this);
        binding.llBrand.setOnClickListener(this);
        binding.llFabric.setOnClickListener(this);
        binding.llPrice.setOnClickListener(this);
        binding.llClose.setOnClickListener(this);
        binding.llApply.setOnClickListener(this);

        binding.tvClearAll.setOnClickListener(this);
        binding.llPrice.setOnClickListener(this);
        filterByColorSizeBrandApi(binding.mainRl);//hit filter api


    }

    private void filterByColorSizeBrandApi(final View view) {
        String type=getIntent().getStringExtra(GlobalVariables.type);
        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<FilterProductListModel> call = apiInterface.getFilterListResult(getIntent().getStringExtra(GlobalVariables.subcatid),type);
        call.enqueue(new Callback<FilterProductListModel>() {
            @Override
            public void onResponse(Call<FilterProductListModel> call, Response<FilterProductListModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                     data = response.body();

                    if (response.body().getStatus().equals("SUCCESS")) {

                        getData(data);

                    } else {
                        Toast.makeText(FilterProductListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(FilterProductListActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            filterByColorSizeBrandApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(FilterProductListActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(FilterProductListActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<FilterProductListModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void getData(FilterProductListModel data) {


        if(data!=null) {
            sizeList = data.getFilterSection().getSize();
            colorList = data.getFilterSection().getColor();
            brandList = data.getFilterSection().getBrand();
            hexcodeList = data.getFilterSection().getHexcode();
            colorHM.put(colorList, hexcodeList);
            if(comeFrom.equalsIgnoreCase("BrandInFocusAdapter") || comeFrom.equalsIgnoreCase(GlobalVariables.ProductDetailsActivityFinal) || comeFrom.equalsIgnoreCase("ShopByThemeAdapter"))
            {
                binding.llBrand.setVisibility(View.GONE);
                binding.llSize.setVisibility(View.GONE);
//                setColorAdapter(colorList, hexcodeList);
                setPriceRange();
            }
            else {

                if (!getIntent().getStringExtra(GlobalVariables.type).equalsIgnoreCase(GlobalVariables.flashSale)
                        && !getIntent().getStringExtra(GlobalVariables.type).equalsIgnoreCase(GlobalVariables.search)
                        ) {
                    binding.llSize.setVisibility(View.VISIBLE);
                    setSizeAdapter(sizeList);
                } else {
                    binding.llSize.setVisibility(View.GONE);
//                    setColorAdapter(colorList, hexcodeList);
                    setPriceRange();
                }
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSize:

                setSizeAdapter(sizeList);

                break;

            case R.id.llColor:

                setColorAdapter(colorList,hexcodeList);

                break;

            case R.id.llBrand:

                setBrandAdapter(brandList);

                break;

            case R.id.llPrice:
                setPriceRange();

                break;

            case R.id.llClose:

                sizeNameCheckedList.clear();
                colorNameCheckedList.clear();
                brandNameCheckedList.clear();


                if(filterProductListAdapter!=null) {
                    filterProductListAdapter.notifyDataSetChanged();
                }
                if(filterProductListByColorAdapter!=null) {
                    filterProductListByColorAdapter.notifyDataSetChanged();
                }

                if(filterProductListByBrandAdapter!=null)
                 {
                     filterProductListByBrandAdapter.notifyDataSetChanged();
                 }



                startActivity(new Intent(this, ProductListActivity.class)
                        .putExtra("from","FilterProductListActivity")
                        .putExtra(GlobalVariables.subcatid, getIntent().getStringExtra(GlobalVariables.subcatid))
                        .putExtra(GlobalVariables.section_name, getIntent().getStringExtra(GlobalVariables.section_name))
                        .putExtra(GlobalVariables.minValue,minValue)
                        .putExtra(GlobalVariables.maxValue,maxValue)
                        .putExtra(GlobalVariables.type,getIntent().getStringExtra(GlobalVariables.type)));



                break;

            case R.id.llApply:

//             <-----------Size ---------->
                String checkedIdSizeName="";
                List<String> sizeNameList=new ArrayList<>();

                for(int i=0;i<sizeNameCheckedList.size();i++){
                    if(sizeNameCheckedList.get(i).isSizeChecked()){
                        if(sizeNameCheckedList.get(i).isSizeChecked()){
                            sizeNameList.add(sizeNameCheckedList.get(i).getSizeName());
                        }
                   }
                }
                checkedIdSizeName=TextUtils.join(",",sizeNameList);



//              <-------- color ------->
                String checkedIdColorName="";
                List<String> colorNameList=new ArrayList<>();
                for(int i=0;i<colorNameCheckedList.size();i++){
                    if(colorNameCheckedList.get(i).isColorChecked()){
                        colorNameList.add(colorNameCheckedList.get(i).getHexCode());
                    }
                }
                checkedIdColorName=TextUtils.join(",",colorNameList);



//              <-------- Brand ------->
                String checkedIdBrandName="";
                List<String> brandNameList=new ArrayList<>();
                for(int i=0;i<brandNameCheckedList.size();i++){
                    if(brandNameCheckedList.get(i).isBrandChecked()){
                        brandNameList.add(brandNameCheckedList.get(i).getBrandName());
                    }
                }
                checkedIdBrandName=TextUtils.join(",",brandNameList);


//                <---------- Filter Applied -------->


                if(brandNameCheckedList.size()>0 || colorNameCheckedList.size()>0 || sizeNameCheckedList.size()>0) {

                    Log.e("size",checkedIdSizeName);
                    Log.e("brand",checkedIdBrandName);
                    Log.e("color:",checkedIdColorName);


                    if(comeFrom.equalsIgnoreCase("ThreeLevelListAdapter"))
                    {
                        callingIntent("ThreeLevelListAdapter",checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                       getIntent().getStringExtra(GlobalVariables.section_name),
                                       getIntent().getStringExtra(GlobalVariables.subcatid),
                                       minValue,maxValue,getIntent().getStringExtra(GlobalVariables.type));


                    }
                    else if(comeFrom.equalsIgnoreCase("SearchAdapter"))
                    {

                        callingIntent("SearchAdapter",checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),
                                getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,getIntent().getStringExtra(GlobalVariables.type));
                    }else if(comeFrom.equalsIgnoreCase("BrandInFocusAdapter"))
                    {
                        callingIntent("FilteredBrandInFocus","",checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),"",
                                minValue,maxValue,"");
                    }
                    else if(comeFrom.equalsIgnoreCase("ShopByThemeAdapter"))
                    {
                        callingIntent("ShopByThemeAdapter",checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                        getIntent().getStringExtra(GlobalVariables.section_name),getIntent().getStringExtra(GlobalVariables.subcatid),
                                        minValue,maxValue,"");
                    }
                    else if(comeFrom.equalsIgnoreCase("MenSectionShopByOccassionAdapter"))
                    {
                        callingIntent(comeFrom,checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),
                                getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,getIntent().getStringExtra(GlobalVariables.type));

                    }else if(comeFrom.equalsIgnoreCase("AutoSlideViewPagerBannerAdapter"))
                    {
                        callingIntent(comeFrom,checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),
                                getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,getIntent().getStringExtra(GlobalVariables.type));
                    }
                    else if(comeFrom.equalsIgnoreCase(MyFirebaseMessagingService.class.getSimpleName()))
                    {
                        callingIntent(comeFrom,checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),
                                getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,getIntent().getStringExtra(GlobalVariables.type));
                    }

                    else
                    {
                        callingIntent("FilterProductListActivity",checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),
                                getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,getIntent().getStringExtra(GlobalVariables.type));

                    }




                }
//                <---------- Filter Not Applied -------->

                else
                {


                    if(comeFrom.equalsIgnoreCase("ThreeLevelListAdapter"))
                    {
                        callingIntent("ThreeLevelListAdapter",checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),
                                getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,getIntent().getStringExtra(GlobalVariables.type));

                    }
                    else if(comeFrom.equalsIgnoreCase("SearchAdapter"))
                    {

                        callingIntent("SearchAdapter",checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),
                                getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,getIntent().getStringExtra(GlobalVariables.type));
                    }
                    else if(comeFrom.equalsIgnoreCase("BrandInFocusAdapter"))
                    {
                        callingIntent("FilteredBrandInFocus",checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),"",
                                minValue,maxValue,"");
                    }
                    else if(comeFrom.equalsIgnoreCase("ShopByThemeAdapter"))
                    {
                        callingIntent("ShopByThemeAdapter",checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,"");
                    } else if(comeFrom.equalsIgnoreCase("MenSectionShopByOccassionAdapter"))
                    {
                        callingIntent(comeFrom,checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,"");
                    }
                    else if(comeFrom.equalsIgnoreCase("AutoSlideViewPagerBannerAdapter"))
                    {
                        callingIntent(comeFrom,checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,"");
                    }  else if(comeFrom.equalsIgnoreCase(MyFirebaseMessagingService.class.getSimpleName()))
                    {
                        callingIntent(comeFrom,checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,"");
                    }

                    else {
                        callingIntent("FilterProductListActivity",checkedIdSizeName,checkedIdColorName,checkedIdBrandName,
                                getIntent().getStringExtra(GlobalVariables.section_name),
                                getIntent().getStringExtra(GlobalVariables.subcatid),
                                minValue,maxValue,getIntent().getStringExtra(GlobalVariables.type));
                    }


                }
                break;

            case R.id.tvClearAll:

                sizeNameCheckedList.clear();
                colorNameCheckedList.clear();
                brandNameCheckedList.clear();
                if(filterProductListAdapter!=null) {
                    filterProductListAdapter.notifyDataSetChanged();
                }
                if(filterProductListByColorAdapter!=null) {
                    filterProductListByColorAdapter.notifyDataSetChanged();
                }

                if(filterProductListByBrandAdapter!=null)
                {
                    filterProductListByBrandAdapter.notifyDataSetChanged();
                }
                getData(data);

                break;
        }
    }

    private void callingIntent(String comeFrom, String sizeName, String colorName, String BrandName,
                               String sectionName, String subcatid, String minValue, String maxValue, String type) {

        Intent intent=new Intent(this,ProductListActivity.class);
        intent.putExtra("from", comeFrom);
        intent.putExtra("commaSeparatedSizeNameId", sizeName);
        intent.putExtra("commaSeparatedColorNameId", colorName);
        intent.putExtra("commaSeparatedBrandNameId", BrandName);
        intent.putExtra(GlobalVariables.section_name,sectionName);
        intent.putExtra(GlobalVariables.minValue,minValue);
        intent.putExtra(GlobalVariables.maxValue,maxValue);
        intent.putExtra(GlobalVariables.type,type);

        if(comeFrom.equalsIgnoreCase("ThreeLevelListAdapter"))
        {
         intent.putExtra(GlobalVariables.subsubcatid,subcatid);
        }
        else if(comeFrom.equalsIgnoreCase("FilterProductListActivity"))
        {
            intent.putExtra(GlobalVariables.subcatid,subcatid);
        }
        else if(comeFrom.equalsIgnoreCase("SearchAdapter"))
        {
            intent.putExtra(GlobalVariables.catid,getIntent().getStringExtra(GlobalVariables.catid));
            intent.putExtra(GlobalVariables.subsubcatid,subcatid);
            intent.putExtra(GlobalVariables.filter_data,getIntent().getStringExtra(GlobalVariables.filter_data));
            intent.putExtra(GlobalVariables.type,"");
        }else if(comeFrom.equalsIgnoreCase("ShopByThemeAdapter"))
        {
            intent.putExtra(GlobalVariables.subcatid,subcatid);
            intent.putExtra(GlobalVariables.theme,getIntent().getStringExtra(GlobalVariables.theme));
            intent.putExtra(GlobalVariables.filter_data,"filter");
        }else if(comeFrom.equalsIgnoreCase("MenSectionShopByOccassionAdapter"))
        {
            intent.putExtra(GlobalVariables.subsubcatid,getIntent().getStringExtra(GlobalVariables.subsubcatid));
            intent.putExtra(GlobalVariables.filter_data,"filter");
        }else if(comeFrom.equalsIgnoreCase("AutoSlideViewPagerBannerAdapter"))
        {
            intent.putExtra(GlobalVariables.subsubcatid,getIntent().getStringExtra(GlobalVariables.subsubcatid));
            intent.putExtra(GlobalVariables.filter_data,"filter");
        }else if(comeFrom.equalsIgnoreCase(MyFirebaseMessagingService.class.getSimpleName()))
        {
            intent.putExtra(GlobalVariables.subsubcatid,getIntent().getStringExtra(GlobalVariables.subsubcatid));
            intent.putExtra(GlobalVariables.filter_data,"filter");
        }else if(comeFrom.equalsIgnoreCase("FilteredBrandInFocus"))
        {
            intent.putExtra("commaSeparatedBrandNameId",getIntent().getStringExtra(GlobalVariables.section_name));

        }


        startActivity(intent);







    }

    private void setPriceRange() {
        binding.llSize.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvSize.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llColor.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvColor.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llBrand.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvBrand.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llFabric.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvFabric.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llPrice.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        binding.tvPrice.setTextColor(getResources().getColor(R.color.colorLogOut));


        PriceRangeAdapter adapter=new PriceRangeAdapter(this);
        binding.filterRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.filterRecyclerview.setAdapter(adapter);
        adapter.setListner(new PriceRangeAdapter.PriceRangerClickListner() {
            @Override
            public void getPriceRange(long min, long max) {

                maxValue= String.valueOf(max);
                minValue= String.valueOf(min);



            }
        });



    }

    private void setSizeAdapter(List<String> sizeList) {
        binding.llSize.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        binding.tvSize.setTextColor(getResources().getColor(R.color.colorLogOut));

        binding.llColor.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvColor.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llBrand.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvBrand.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llFabric.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvFabric.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llPrice.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvPrice.setTextColor(getResources().getColor(R.color.colorBlack));

        sizeNameCheckedList.clear();

        for (int i = 0; i < sizeList.size(); i++) {
            SizeCheckedListModel sizeCheckedListModel = new SizeCheckedListModel(sizeList.get(i));
            sizeNameCheckedList.add(sizeCheckedListModel);
        }


        filterProductListAdapter = new FilterProductListBySizeAdapter(this, sizeNameCheckedList);
        linearLayoutManager = new LinearLayoutManager(FilterProductListActivity.this);

        binding.filterRecyclerview.setLayoutManager(linearLayoutManager);
        binding.filterRecyclerview.setAdapter(filterProductListAdapter);

    }

    private void setColorAdapter(List<String> colorList,List<String> hexcodeList) {

        binding.llColor.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        binding.tvColor.setTextColor(getResources().getColor(R.color.colorLogOut));

        binding.llSize.setBackgroundColor(getResources().getColor(R.color.stroke));
        binding.tvSize.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llFabric.setBackgroundColor(getResources().getColor(R.color.stroke));
        binding.tvFabric.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llBrand.setBackgroundColor(getResources().getColor(R.color.stroke));
        binding.tvBrand.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llPrice.setBackgroundColor(getResources().getColor(R.color.stroke));
        binding.tvPrice.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.containerLl.setBackgroundColor(ContextCompat.getColor(this,R.color.stroke));

        colorNameCheckedList.clear();

        for (int i = 0; i < colorList.size(); i++) {
            ColorCheckedListModel colorCheckedListModel = new ColorCheckedListModel(colorList.get(i),hexcodeList.get(i));
            colorNameCheckedList.add(colorCheckedListModel);
        }


        filterProductListByColorAdapter = new FilterProductListByColorAdapter(this, colorNameCheckedList);

        linearLayoutManager = new LinearLayoutManager(this);
        binding.filterRecyclerview.setLayoutManager(linearLayoutManager);
        binding.filterRecyclerview.setAdapter(filterProductListByColorAdapter);
    }

    private void setBrandAdapter(List<String> brandList) {

        binding.llBrand.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        binding.tvBrand.setTextColor(getResources().getColor(R.color.colorLogOut));

        binding.llSize.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvSize.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llColor.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvColor.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llFabric.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvFabric.setTextColor(getResources().getColor(R.color.colorBlack));

        binding.llPrice.setBackgroundColor(getResources().getColor(R.color.colorView));
        binding.tvPrice.setTextColor(getResources().getColor(R.color.colorBlack));

        brandNameCheckedList.clear();

        for (int i = 0; i < brandList.size(); i++) {
            BrandCheckedListModel brandCheckedListModel = new BrandCheckedListModel(brandList.get(i));
            brandNameCheckedList.add(brandCheckedListModel);
        }

        filterProductListByBrandAdapter = new FilterProductListByBrandAdapter(this, brandNameCheckedList);

        linearLayoutManager = new LinearLayoutManager(FilterProductListActivity.this);

        binding.filterRecyclerview.setLayoutManager(linearLayoutManager);
        binding.filterRecyclerview.setAdapter(filterProductListByBrandAdapter);
    }

}
