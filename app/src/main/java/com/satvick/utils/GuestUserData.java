package com.satvick.utils;

import com.satvick.application.YODApplication;
import com.satvick.model.ProductDetails;

import java.util.ArrayList;
import java.util.List;

public class GuestUserData {
    private static GuestUserData guestUserData;
    private static List<ProductDetails> list;

    private GuestUserData(){ }

    public static GuestUserData getInstance() {
        if(guestUserData==null){
            guestUserData=new GuestUserData();
            list=new ArrayList<>();
        }
        return guestUserData;
    }

    public List<ProductDetails> getHugeData() {
        return list;
    }



    public List<ProductDetails> removeData(int pos) {
        list.remove(pos);
        return this.list;
    }


    public void setHugeData(List<ProductDetails> list) {
        this.list = list;
    }

}
