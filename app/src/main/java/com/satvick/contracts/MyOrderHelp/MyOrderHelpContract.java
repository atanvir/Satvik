package com.satvick.contracts.MyOrderHelp;

import android.content.Context;

import com.satvick.model.MyOrderHelp;

public interface MyOrderHelpContract {

    interface Model {

        interface OnFinishedListener {
            void onSucess(MyOrderHelp body);

            void onFailure(String message);

        }
        void getMyOrderHelp(Context context,OnFinishedListener listner);
    }

    interface View
    {
        void showProgress();

        void hideProgress();

        void setDataToView(MyOrderHelp data);

        void onResponseFailure(String message);



    }

    interface Presenter
    {
        void onDestroy();

        void requestMyOderHelpData();

    }



}
