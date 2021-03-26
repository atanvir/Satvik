package com.satvick.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.databinding.ItemCategoriesListBinding;
import com.satvick.model.CategoriesListResponse;

import java.util.List;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<CategoriesListResponse> categoriesListResponseList;
    OnCategoriesListItemClickListener listener;
    private static final String TAG = "CategoriesListAdapter";

    private int[] image = {
            R.drawable.girl_img_a,
            R.drawable.men_style,
            R.drawable.cute_baby,
            R.drawable.pillow_red,
            R.drawable.gadgets_hit,
    };

    public CategoriesListAdapter(Context context, List<CategoriesListResponse> categoriesListResponseList) {
        this.context = context;
        this.categoriesListResponseList = categoriesListResponseList;
        setHasStableIds(true);
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
    public CategoriesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemCategoriesListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_categories_list, parent, false);
        return new CategoriesListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CategoriesListAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + categoriesListResponseList.size());

        holder.binding.tvWomen.setText(categoriesListResponseList.get(position).getName());
        holder.binding.productDesc.setText(categoriesListResponseList.get(position).getProductName());
        holder.binding.imageView.setImageResource(image[position]);

        /*String imgUrl = homeSlidingProductResponseList.get(position).getImage();
        if (imgUrl != null && imgUrl.length() > 0) {
            Picasso.with(context).load(imgUrl).into(holder.binding.image);
        }*/
    }

    @Override
    public int getItemCount() {
        return categoriesListResponseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoriesListBinding binding;

        ViewHolder(ItemCategoriesListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onItemClick(view, getAdapterPosition());
                }
            });

        }
    }

    public interface OnCategoriesListItemClickListener {
        void onItemClick(View view, int pos);
    }

    public void setOnCategoriesListItemClickListener(OnCategoriesListItemClickListener listener) {
        this.listener = listener;
    }
}
