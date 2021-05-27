package com.satvick.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.satvick.databinding.AdapterCategoriesBinding;
import com.satvick.databinding.AdapterCommonBannerBinding;
import com.satvick.databinding.AdapterCommonSubCategoryBinding;
import com.satvick.databinding.AdapterLifeBannerBinding;
import com.satvick.databinding.AdapterLifeSubCategoryBinding;
import com.satvick.model.LifeTabBean;
import com.satvick.model.TabModel;
import java.util.List;

public class LifeCommonCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TabModel> list;
    private final int BANNER=0;
    private final int CATEGORY=1;

    public LifeCommonCategoryAdapter(Context context, List<TabModel> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==BANNER) return new LifeBannerViewHolder(AdapterCommonBannerBinding.inflate(LayoutInflater.from(context),parent,false));
        else return new LifeCategoryViewHolder(AdapterCommonSubCategoryBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getBanner()!=null)  return BANNER;
        else return CATEGORY;
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case BANNER :
            ((LifeBannerViewHolder) holder).binding.rvBanner.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            ((LifeBannerViewHolder) holder).binding.rvBanner.setAdapter(new LifeBannerAdapter(context,list.get(position).getBanner()));
            new PagerSnapHelper().attachToRecyclerView(((LifeBannerViewHolder) holder).binding.rvBanner);
            break;

            case CATEGORY:
            if(!list.get(position).getLifeTab().isEmpty()) {
                if (!list.get(position).getName().equalsIgnoreCase("")) {
                    ((LifeCategoryViewHolder) holder).binding.tvHeader.setVisibility(View.VISIBLE);
                    ((LifeCategoryViewHolder) holder).binding.belowView.setVisibility(View.VISIBLE);
                    ((LifeCategoryViewHolder) holder).binding.aboveView.setVisibility(View.VISIBLE);
                }
                ((LifeCategoryViewHolder) holder).binding.tvHeader.setText(list.get(position).getName());
                ((LifeCategoryViewHolder) holder).binding.rvCategory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                ((LifeCategoryViewHolder) holder).binding.rvCategory.setAdapter(new LifeSubCategoryAdapter(context, list.get(position).getLifeTab()));
            }else{

                ((LifeCategoryViewHolder) holder).binding.tvHeader.setVisibility(View.GONE);
                ((LifeCategoryViewHolder) holder).binding.rvCategory.setVisibility(View.GONE);
            }
            break;
        }

    }

    @Override
    public int getItemCount() {
      return list!=null?list.size():0;
    }

    public class LifeCategoryViewHolder extends RecyclerView.ViewHolder {
         AdapterCommonSubCategoryBinding binding;
        public LifeCategoryViewHolder(@NonNull AdapterCommonSubCategoryBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
    }

    public class LifeBannerViewHolder extends RecyclerView.ViewHolder{
         AdapterCommonBannerBinding binding;
        public LifeBannerViewHolder(@NonNull AdapterCommonBannerBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
