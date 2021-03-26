package com.satvick.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.satvick.R;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ItemMyWishlistBinding;

import java.util.List;

public class MyWishListAdapter extends RecyclerView.Adapter<MyWishListAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    private Double convertedPrice;
    private String symbol;
    //int[] image;

    List<com.satvick.model.List> wishlistproductList;

    private MyWishListItemClickListener listener;


    public MyWishListAdapter(Context context,  List<com.satvick.model.List> wishlistproductList) {
        this.context = context;
        this.wishlistproductList = wishlistproductList;
        setHasStableIds(true);
        this.convertedPrice = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        ItemMyWishlistBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_my_wishlist, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

       // viewHolder.itemMywishlistBinding.imgMywishlist.setImageResource(image[i]);
        String imgUrl = wishlistproductList.get(position).getImage();
        if (imgUrl != null && imgUrl.length() > 0) {
            Glide.with(context).load(imgUrl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    viewHolder.itemMywishlistBinding.progressbar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    viewHolder.itemMywishlistBinding.progressbar.setVisibility(View.GONE);
                    return false;
                }
            }).into(viewHolder.itemMywishlistBinding.imgMywishlist);
        }

        viewHolder.itemMywishlistBinding.tvName.setText(wishlistproductList.get(position).getBrand());
        viewHolder.itemMywishlistBinding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(wishlistproductList.get(position).getSp())*convertedPrice));
        viewHolder.itemMywishlistBinding.tvOff.setText(wishlistproductList.get(position).getPercentage()+"% OFF");
        viewHolder.itemMywishlistBinding.textDisc.setText(wishlistproductList.get(position).getName());

        viewHolder.itemMywishlistBinding.tvCuttedText.setText(symbol+" "+Math.round(Double.parseDouble(wishlistproductList.get(position).getMrp())*convertedPrice));
        viewHolder.itemMywishlistBinding.tvCuttedText.setPaintFlags( viewHolder.itemMywishlistBinding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }


    @Override
    public int getItemCount() {
        return wishlistproductList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemMyWishlistBinding itemMywishlistBinding;

        ViewHolder(ItemMyWishlistBinding binding) {
            super(binding.getRoot());
            this.itemMywishlistBinding = binding;


            binding.llMoveToBag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener != null)
                        listener.onMoveToBagItemClick(view, getAdapterPosition());
                }
            });

            binding.ivCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener != null)
                        listener.onIvCrossItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public interface MyWishListItemClickListener {
        void onMoveToBagItemClick(View view, int pos);
        void onIvCrossItemClick(View view,int pos);
    }

    public void setListener(MyWishListItemClickListener listener) {
        this.listener = listener;
    }
}
