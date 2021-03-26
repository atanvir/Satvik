package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.activities.ProductListActivity;
import com.satvick.databinding.CategoryLayoutBinding;
import com.satvick.model.CategoryItemResponse;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    LayoutInflater inflater;
    List<CategoryItemResponse> categoryList;
    OnCategoryListItemClickListener listener;


    private int[] image = {
            R.drawable.mens_style,
            R.drawable.cute_kids,
            R.drawable.cute_kids,
    };

    public CategoryAdapter(Context context, List<CategoryItemResponse> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        /*LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.category_layout,viewGroup,false);
        return new ViewHolder(view);*/

        // for binding, first we inflate layout here and below ViewHolder write
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        CategoryLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_layout, parent, false);
        return new CategoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder viewHolder, int i) {

        viewHolder.binding.tvName.setText(categoryList.get(i).getTitleName());
        viewHolder.binding.image.setImageResource(image[i]);

        viewHolder.binding.tvItem1.setText(categoryList.get(i).getItemName1());
        viewHolder.binding.tvItem2.setText(categoryList.get(i).getItemName2());
        viewHolder.binding.tvItem3.setText(categoryList.get(i).getItemName3());
        viewHolder.binding.tvItem4.setText(categoryList.get(i).getItemName4());
        viewHolder.binding.tvItem5.setText(categoryList.get(i).getItemName5());
        viewHolder.binding.tvItem6.setText(categoryList.get(i).getItemName6());

       /* //Set Category Item List

        categoryList.add(new HomeSlidingProductResponse("KURTA & KURTIS", R.drawable.image_a));
        categoryList.add(new HomeSlidingProductResponse("TOPS & TEES", R.drawable.image_b));
        categoryList.add(new HomeSlidingProductResponse("DRESSES", R.drawable.image_c));
        categoryList.add(new HomeSlidingProductResponse("SHOES", R.drawable.image_c));
        categoryList.add(new HomeSlidingProductResponse("WATCHES", R.drawable.image_c));
        categoryList.add(new HomeSlidingProductResponse("HANDBAGS", R.drawable.image_c));

        viewHolder.binding.categoryItemRecyclerView.setLayoutManager(new LinearLayoutManager
                (context, LinearLayout.VERTICAL, true));

        CategoryItemAdapter adapter = new CategoryItemAdapter(context,categoryList);
        viewHolder.binding.categoryItemRecyclerView.setAdapter(adapter);*/

        viewHolder.binding.llCategoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProductListActivity.class).putExtra("from","CategoryAdapter"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CategoryLayoutBinding binding;

        /* RecyclerView categoryItemRecyclerView;
         public ViewHolder(@NonNull View itemView)
         {
             super(itemView);

             categoryItemRecyclerView=itemView.findViewById(R.id.categoryItemRecyclerView);
         }*/

        ViewHolder(CategoryLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

    public interface OnCategoryListItemClickListener {
        void onItemClick(View view, int pos);
    }
}
