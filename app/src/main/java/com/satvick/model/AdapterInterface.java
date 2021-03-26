package com.satvick.model;

import android.content.Context;

import java.util.List;

public interface AdapterInterface {

    void onClickListner(Context context, int postion, String status, List<MyOrderDetailsModel.Response> myOrderDetailsModelList);

}
