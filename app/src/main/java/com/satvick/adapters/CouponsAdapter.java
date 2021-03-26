package com.satvick.adapters;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.satvick.R;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ItemCouponsListingBinding;
import com.satvick.model.CouponsListModel;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<CouponsListModel.Response> couponsList;
    CouponsListItemClickListener listener;
    private String symbol;
    private Double convertedPrice;


    public CouponsAdapter(Context context,List<CouponsListModel.Response> couponsList) {
        this.context = context;
        this.couponsList = couponsList;
        this.convertedPrice = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public CouponsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        ItemCouponsListingBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_coupons_listing, parent, false);
        return new CouponsAdapter.ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(CouponsAdapter.ViewHolder holder, int position) {
        if(couponsList.get(position).getDiscountType().equalsIgnoreCase("price"))
        {
            holder.binding.tvOff.setText(""+Math.round(Double.parseDouble(couponsList.get(position).getDiscount())*convertedPrice));
        }else if(couponsList.get(position).getDiscountType().equalsIgnoreCase("percent"))
        {
            holder.binding.tvOff.setText(Math.round(Double.parseDouble(couponsList.get(position).getDiscount())*convertedPrice)+"%");
        }
        holder.binding.tvCouponsCode.setText(couponsList.get(position).getCupon_code());
        holder.binding.tvMinimumPurchase.setText(symbol+" "+Math.round(Double.parseDouble(couponsList.get(position).getMin_price())*convertedPrice));
        holder.binding.tvDate.setText(couponsList.get(position).getExpiry_date());
    }

    @Override
    public int getItemCount() {
        return couponsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCouponsListingBinding binding;


        public ViewHolder(ItemCouponsListingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onItemClick(view, getAdapterPosition());
                }
            });

            binding.copyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", couponsList.get(getAdapterPosition()).getCupon_code());
                    clipboard.setPrimaryClip(clip);
                    CommonMessagePopup("Coupon code copied");
                }
            });
        }
    }

    private void CommonMessagePopup(String message) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_mesage_popup);
        ImageView closeiv=dialog.findViewById(R.id.closeiv);
        TextView messagetxt=dialog.findViewById(R.id.messagetxt);
        LottieAnimationView lottieAnimationView=dialog.findViewById(R.id.lottieAnimationView);
        lottieAnimationView.setAnimation("done.json");
        messagetxt.setText(message);

        Button okbtn=dialog.findViewById(R.id.okbtn);

        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });



        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public interface CouponsListItemClickListener {
        void onItemClick(View view, int pos);
    }

    public void setCouponsListItemClickListener(CouponsListItemClickListener listener) {
        this.listener = listener;
    }
}
