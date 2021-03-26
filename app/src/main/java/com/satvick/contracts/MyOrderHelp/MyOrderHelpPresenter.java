package com.satvick.contracts.MyOrderHelp;

import android.content.Context;

import com.satvick.model.MyOrderHelp;

public class MyOrderHelpPresenter implements MyOrderHelpContract.Presenter,MyOrderHelpContract.Model.OnFinishedListener {
   private MyOrderHelpContract.View view;
   private MyOrderHelpContract.Model model;
   private Context context;

   public MyOrderHelpPresenter(Context context,MyOrderHelpContract.View view)
   {
       this.view=view;
       this.context=context;
       this.model=new MyOrderHelpModel();
   }



    @Override
    public void onSucess(MyOrderHelp body) {

        if(view != null){
            view.hideProgress();
        }
        view.setDataToView(body);

    }

    @Override
    public void onFailure(String message) {
        if(view != null){
            view.hideProgress();
        }
        view.onResponseFailure(message);
    }

    @Override
    public void onDestroy() {
       view=null;
    }

    @Override
    public void requestMyOderHelpData() {

        if(view != null){
            view.showProgress();
        }
        model.getMyOrderHelp(context, this);


    }
}
