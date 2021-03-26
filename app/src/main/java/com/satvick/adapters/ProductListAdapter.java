package com.satvick.adapters;

import android.content.Context;
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
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ProductListAdapterBinding;
import com.satvick.model.MyOrderHelp;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private Context context;
    private List<MyOrderHelp.Response> list;
    private String activityName;
    private Double convertedPrice;
    private String symbol;

    public ProductListAdapter(Context context, List<MyOrderHelp.Response> list,String activityName)
    {
        this.context=context;
        this.list=list;
        this.activityName=activityName;
        setHasStableIds(true);
        this.convertedPrice = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       ProductListAdapterBinding bind = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.product_list_adapter,parent,false);
        return new ProductListAdapter.MyViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {


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
            }).into(holder.binding.imageIv);


            holder.binding.tvProductName.setText(list.get(position).getBrand());
            holder.binding.tvProductDesc.setText(list.get(position).getProductName());
            holder.binding.tvSizeQty.setText(MessageFormat.format("Size:{0} | Qty {1}", list.get(position).getSize(), list.get(position).getQuantity()));
            holder.binding.tvTotalMrp.setText(MessageFormat.format(symbol+" {0}", Math.round(Double.parseDouble(list.get(position).getAmount())*convertedPrice)));

            holder.binding.tvOrderStatus.setText(list.get(position).getStatus().toUpperCase());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            String ouputDate = "";
            try {
                Date server_date = dateFormat.parse(list.get(position).getOrderDate());
                ouputDate = new SimpleDateFormat("dd MMM yyyy").format(server_date);

            } catch (Exception e) {
                System.out.println(e.getStackTrace());

            }

            holder.binding.tvDate.setText(ouputDate);
        }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProductListAdapterBinding binding;
        public MyViewHolder(ProductListAdapterBinding bind) {
            super(bind.getRoot());
            this.binding=bind;






        }
    }
}
