package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.satvick.R;
import com.satvick.activities.ProductDetailsActivityFinal;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ItemMyOrdersBinding;
import com.satvick.model.AdapterImplementation;
import com.satvick.model.AdapterInterface;
import com.satvick.model.MyOrderDetailsModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    private Context context;
    private List<MyOrderDetailsModel.Response> myOrderDetailsModelList;
    private ItemMyOrdersBinding binding;
    LayoutInflater inflater;
    AdapterInterface anInterface;
    private Double convertedPrice;
    private String symbol;

    public MyOrderAdapter(Context context, List<MyOrderDetailsModel.Response> myOrderDetailsModelList) {
        this.context = context;
        this.myOrderDetailsModelList = myOrderDetailsModelList;
        anInterface = new AdapterImplementation();
        this.convertedPrice = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_my_orders, viewGroup, false);
        return new MyOrderAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        String imgURl = myOrderDetailsModelList.get(i).getImage();
        if (!imgURl.isEmpty()) {
            Glide.with(context).load(imgURl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    viewHolder.binding.progressbar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    viewHolder.binding.progressbar.setVisibility(View.GONE);
                    return false;
                }
            }).into(viewHolder.binding.ivProduct);
        }
        viewHolder.binding.tvProductName.setText(myOrderDetailsModelList.get(i).getBrand());
        viewHolder.binding.tvProductDescription.setText(myOrderDetailsModelList.get(i).getProductName());
        viewHolder.binding.tvSizeContent.setText(myOrderDetailsModelList.get(i).getSize());
        viewHolder.binding.tvQuantityContent.setText("" + myOrderDetailsModelList.get(i).getQuantity());
        if (myOrderDetailsModelList.get(i).getCoupanCode().equalsIgnoreCase("")) {
            viewHolder.binding.llOffer.setVisibility(View.GONE);
        } else {
            viewHolder.binding.llOffer.setVisibility(View.VISIBLE);
            viewHolder.binding.tvOfferContent.setText(myOrderDetailsModelList.get(i).getCoupanCode());
        }

        viewHolder.binding.tvRupee.setText(symbol + " " + Math.round(Double.parseDouble(myOrderDetailsModelList.get(i).getAmount()) * convertedPrice));

        viewHolder.binding.tvOrderNumber.setText(myOrderDetailsModelList.get(i).getOrderNumber());


        if (myOrderDetailsModelList.get(i).getPercentage() == 0) {
            viewHolder.binding.tvCuttedText.setVisibility(View.GONE);
            viewHolder.binding.tvOff.setVisibility(View.GONE);
        } else {
            viewHolder.binding.tvCuttedText.setVisibility(View.VISIBLE);
            viewHolder.binding.tvOff.setVisibility(View.VISIBLE);
            viewHolder.binding.tvCuttedText.setText(symbol + " " + Math.round(Double.parseDouble(myOrderDetailsModelList.get(i).getMrp()) * convertedPrice));
            viewHolder.binding.tvOff.setText(myOrderDetailsModelList.get(i).getPercentage() + "% OFF");

        }

        viewHolder.binding.tvCuttedText.setPaintFlags(viewHolder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        // viewHolder.binding.tvOrderStatusContent.setText(myOrderDetailsModelList.get(i).getStatus());
        viewHolder.binding.tvOrderDateContent.setText(myOrderDetailsModelList.get(i).getOrderDate());
        viewHolder.binding.tvAddress.setText(myOrderDetailsModelList.get(i).getLocation());

        if (myOrderDetailsModelList.get(i).getStatus().equalsIgnoreCase(GlobalVariables.new_order)) {
            viewHolder.binding.tvRequestForReturn.setText("Cancel");
               viewHolder.binding.tvRequestForExchange.setText("Track Order");
        } else if (myOrderDetailsModelList.get(i).getStatus().equalsIgnoreCase(GlobalVariables.delivred)) {
            viewHolder.binding.tvRequestForReturn.setText("Return");
            viewHolder.binding.tvRequestForExchange.setText("Exchange");
        } else if (myOrderDetailsModelList.get(i).getStatus().equalsIgnoreCase(GlobalVariables.cancelled)) {
            viewHolder.binding.tvRequestForReturn.setText("Cancelled");
            viewHolder.binding.tvRequestForExchange.setVisibility(View.GONE);
        } else if (myOrderDetailsModelList.get(i).getStatus().equalsIgnoreCase(GlobalVariables.dispatched)) {
            viewHolder.binding.tvRequestForReturn.setText("Cancel");
            viewHolder.binding.tvRequestForExchange.setText("Track Order");
        } else if (myOrderDetailsModelList.get(i).getStatus().equalsIgnoreCase(GlobalVariables.exchange)) {
            viewHolder.binding.tvRequestForReturn.setVisibility(View.GONE);
            viewHolder.binding.tvRequestForExchange.setText("Track Order");
        } else if (myOrderDetailsModelList.get(i).getStatus().equalsIgnoreCase(GlobalVariables.Return)) {
            viewHolder.binding.tvRequestForReturn.setText("Retunred");
            viewHolder.binding.tvRequestForExchange.setVisibility(View.GONE);

        } else {
            viewHolder.binding.tvRequestForReturn.setText("Cancel");
            viewHolder.binding.tvRequestForExchange.setText("Track Order");

        }


    }

    @Override
    public int getItemCount() {
        return myOrderDetailsModelList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ItemMyOrdersBinding binding;

        ViewHolder(@NonNull final ItemMyOrdersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.tvRequestForReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!binding.tvRequestForReturn.getText().toString().equalsIgnoreCase("Cancelled") &&
                            !binding.tvRequestForReturn.getText().toString().equalsIgnoreCase("Retunred")) {
                        anInterface.onClickListner(context, getAdapterPosition(), binding.tvRequestForReturn.getText().toString(), myOrderDetailsModelList);
                    }

                }
            });
            binding.tvRequestForExchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    anInterface.onClickListner(context, getAdapterPosition(), binding.tvRequestForExchange.getText().toString(), myOrderDetailsModelList);
                }
            });

            binding.tvRequestForOrderAgan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    anInterface.onClickListner(context, getAdapterPosition(), "OrderAgain", myOrderDetailsModelList);
                }
            });


            binding.llTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ProductDetailsActivityFinal.class).putExtra(GlobalVariables.product_id, "" + myOrderDetailsModelList.get(getAdapterPosition()).getProductId()));
                }
            });

        }
    }
}

