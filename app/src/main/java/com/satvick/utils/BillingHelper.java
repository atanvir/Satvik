package com.satvick.utils;

import com.satvick.model.BillingModel;

public class BillingHelper {
    private static BillingHelper billingHelper;
    private BillingModel model;

    private BillingHelper(){

    }

    public static BillingHelper getInstance(){
        if(billingHelper ==null){
            billingHelper =new BillingHelper();
        }
        return billingHelper;
    }

    public void saveBillingData(BillingModel model){
       this.model=model;
    }

    public BillingModel getBillingData(){
        return model;
    }

    public void removeData(){
        this.model=null;
    }

}
