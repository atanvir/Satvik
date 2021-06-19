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

    private Context context;
    private List<Moreproductlist> list;
    private ProductListItemClickListener listener;
    private String symbol;
    private Double convertedAmount;

    public ProductListingAdapter(Context context, List<Moreproductlist> list,ProductListItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
        this.convertedAmount = Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount"));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemProductListingBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.binding.img);

        holder.binding.tvDesc.setText(list.get(position).getName());
        holder.binding.tvName.setText(list.get(position).getBrand());
        holder.binding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(""+list.get(position).getSp())*convertedAmount));

        if(list.get(position).getPercentage().equalsIgnoreCase("0")){
            holder.binding.tvCuttedText.setVisibility(View.GONE);
            holder.binding.tvOff.setVisibility(View.GONE);
        }else {
            holder.binding.tvOff.setText(MessageFormat.format("({0}% OFF)",list.get(position).getPercentage()));
            holder.binding.tvCuttedText.setText(symbol+" "+Math.round(Double.parseDouble(""+list.get(position).getMrp())*convertedAmount));
            holder.binding.tvCuttedText.setPaintFlags(holder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


         if(list.get(position).getLikeStatus().equalsIgnoreCase("1")) holder.binding.ivAddRemoveFromWishlist.setImageResource(R.drawable.bitmap_saved_salected);
         else holder.binding.ivAddRemoveFromWishlist.setImageResource(R.drawable.save_icon);
    }

    @Override
    public int getItemCount() { return list!=null?list.size():0; }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemProductListingBinding binding;

        public ViewHolder(final ItemProductListingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
            binding.ivAddRemoveFromWishlist.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ivAddRemoveFromWishlist : if (listener != null) listener.onIvItemClick(view, getBindingAdapterPosition()); break;
                default: if (listener != null) listener.onProductItemClick(view, getBindingAdapterPosition()); break;
            }
        }
    }

    public interface ProductListItemClickListener {
        void onProductItemClick(View view,int pos);
        void onIvItemClick(View view,int pos);
    }

}
