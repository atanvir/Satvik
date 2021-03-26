package com.satvick.model.SubCategoryContract;

import com.satvick.model.HomeModel;

public class SubCategoryContract {

    interface Model
    {
        interface OnFinishedListener
        {
            void onSucess(HomeModel.Homeapi model);

            void onFailure(String message);

        }
        void getSubCategoryList(OnFinishedListener listener);

    }

    interface View
    {
        void showProgress();

        void hideProgress();

        void setDataToView(HomeModel.Homeapi model);

        void onResponseFailure(Throwable throwable);

    }

    interface Presenter
    {
        void onDestroy();

        void requestSubCategoryData();

    }
}
