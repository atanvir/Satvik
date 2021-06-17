package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.satvick.R;
import com.satvick.activities.ProductDetailActivity;
import com.satvick.databinding.AdapterProductBinding;
import com.satvick.model.ProductBean;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private List<ProductBean> list;
    private String type;

    public ProductAdapter(Context context,String type, List<ProductBean> list){
        this.context=context;
        this.list=list;
        this.type=type;
    }


    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductAdapter.MyViewHolder(AdapterProductBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductAdapter.MyViewHolder holder, int position) {
        holder.binding.tvName.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getImage()).listener(new RequestListener<Drawable>() {
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
        }).into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterProductBinding binding;
        public MyViewHolder(@NonNull AdapterProductBinding itemView) {
            super(itemView.getRoot());
            this.binding=itemView;
            binding.clMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.clMain :
                Intent intent=new Intent(context, ProductDetailActivity.class);
                intent.putExtra("from","HomeFragmentAfterLogin");
                intent.putExtra("product_id", list.get(getAdapterPosition()).getProduct_id());
                context.startActivity(intent);
                break;
            }
        }
    }
}
