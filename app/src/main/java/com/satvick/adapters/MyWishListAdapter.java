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
    private Context context;
    private List<com.satvick.model.List> list;
    private MyWishListItemClickListener listener;


    public MyWishListAdapter(Context context,  List<com.satvick.model.List> list,MyWishListItemClickListener listner) {
        this.context = context;
        this.list = list;
        this.listener=listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(ItemMyWishlistBinding.inflate(LayoutInflater.from(context),viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Glide.with(context).load(list.get(position).getImage()).into(viewHolder.binding.imgMywishlist);
        viewHolder.binding.tvPrice.setText("₹ "+Math.round(Double.parseDouble(list.get(position).getSp())));
        viewHolder.binding.tvOff.setText(list.get(position).getPercentage()+"% OFF");
        viewHolder.binding.textDisc.setText(list.get(position).getName());
        viewHolder.binding.tvCuttedText.setText("₹ "+Math.round(Double.parseDouble(list.get(position).getMrp())));
        viewHolder.binding.tvCuttedText.setPaintFlags( viewHolder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }


    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemMyWishlistBinding binding;

        ViewHolder(ItemMyWishlistBinding binding) {
            super(binding.getRoot());
            init(binding);
            initCtrl();
        }

        private void init (ItemMyWishlistBinding binding) {
            this.binding = binding;
        }

        private void initCtrl(){
            binding.llMoveToBag.setOnClickListener(this);
            binding.ivCross.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.llMoveToBag:  if (listener != null) listener.onMoveToBagItemClick(view, getAbsoluteAdapterPosition()); break;
                case R.id.ivCross:   if (listener != null) listener.onIvCrossItemClick(view, getAbsoluteAdapterPosition()); break;
            }
        }
    }

    public interface MyWishListItemClickListener {
        void onMoveToBagItemClick(View view, int pos);
        void onIvCrossItemClick(View view,int pos);
    }

}
