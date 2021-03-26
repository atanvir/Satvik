package com.satvick.model.SubCategoryContract;

import android.content.Context;
import android.widget.Toast;

import com.satvick.model.HomeModel;

public class SubCategoryPresenter implements SubCategoryContract.Presenter, SubCategoryContract.Model.OnFinishedListener  {

    private Context context;
    private  SubCategoryContract.View subCategoryView;
    private  SubCategoryContract.Model modelsubCategory;

    public SubCategoryPresenter(Context context, SubCategoryContract.View subCategoryView)
    {
        this.context=context;
        this.subCategoryView=subCategoryView;
        this.modelsubCategory=new SubCategoryModel(context);

    }


    @Override
    public void onSucess(HomeModel.Homeapi model) {
        if(subCategoryView!=null)
        {

            subCategoryView.hideProgress();
        }
        subCategoryView.setDataToView(model);





    }

    @Override
    public void onFailure(String message) {
        if(subCategoryView!=null)
        {
            subCategoryView.hideProgress();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDestroy() {
        subCategoryView=null;

    }

    @Override
    public void requestSubCategoryData() {
        if(subCategoryView!=null)
        {
            subCategoryView.showProgress();
            modelsubCategory.getSubCategoryList(this);
        }
    }
}
