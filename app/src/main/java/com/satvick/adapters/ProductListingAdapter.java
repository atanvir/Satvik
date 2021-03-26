package com.satvick.adapters;

import android.content.Context;
import android.graphics.Paint;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
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
import com.satvick.databinding.ItemProductListingBinding;
import com.satvick.model.Moreproductlist;

import java.text.MessageFormat;
import java.util.List;

public class ProductListingAdapter extends RecyclerView.Adapter<ProductListingAdapter.ViewHolder> {
    private int[] image = {
            R.drawable._img54,
            R.drawable._img98,
            R.drawable._img_77,
            R.drawable._img_44
    };
    private Double convertedAmount;
    private String symbol;

    private Context context;
    private LayoutInflater inflater;
    private List<Moreproductlist> productListingResponseList;

    private ProductListItemClickListener listener;

    private boolean isChecked=true;

    private int tempPosition=0;
    private String productId="";



    public ProductListingAdapter(Context context, List<Moreproductlist> productListingResponseList, String productId) {
        this.context = context;
        this.productListingResponseList = productListingResponseList;
        this.productId=productId;
        setHasStableIds(true);
        this.convertedAmount = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemProductListingBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_product_listing, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String imgUrl = productListingResponseList.get(position).getImage();
        if (imgUrl != null && imgUrl.length() > 0) {
            Glide.with(context).load(imgUrl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.binding.progressbar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.binding.progressbar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.binding.img);
        }

        holder.binding.tvDesc.setText(productListingResponseList.get(position).getName());
        holder.binding.tvName.setText(productListingResponseList.get(position).getBrand());
        holder.binding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(""+productListingResponseList.get(position).getSp())*convertedAmount));

        if(productListingResponseList.get(position).getPercentage().equalsIgnoreCase("0")){
            holder.binding.tvCuttedText.setVisibility(View.GONE);
            holder.binding.tvOff.setVisibility(View.GONE);
        }else {
            holder.binding.tvOff.setText(MessageFormat.format("({0}% OFF)",productListingResponseList.get(position).getPercentage()));
            holder.binding.tvCuttedText.setText(symbol+" "+Math.round(Double.parseDouble(""+productListingResponseList.get(position).getMrp())*convertedAmount));
            holder.binding.tvCuttedText.setPaintFlags(holder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


         if(productListingResponseList.get(position).getLikeStatus().equalsIgnoreCase("1"))
         {
                    holder.binding.ivAddRemoveFromWishlist.setImageResource(R.drawable._saved_salected);
         }
         else
         {
                    holder.binding.ivAddRemoveFromWishlist.setImageResource(R.drawable.save_icon);
         }

    }

    @Override
    public int getItemCount() {
        return productListingResponseList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ItemProductListingBinding binding;

        public ViewHolder(final ItemProductListingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onProductItemClick(view, getAdapterPosition());
                }
            });


            binding.ivAddRemoveFromWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener != null)
                        listener.onIvItemClick(view, getAdapterPosition());
                }
            });
        }

    }

    public interface ProductListItemClickListener {
        void onProductItemClick(View view,int pos);
        void onIvItemClick(View view,int pos);
    }

    public void setListener(ProductListItemClickListener listener) {
        this.listener = listener;
    }
}
