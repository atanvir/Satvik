package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.activities.ProductListActivity;
import com.satvick.databinding.ItemSearchListBinding;
import com.satvick.model.Searchlist;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private Context context;
    LayoutInflater inflater;

    List<Searchlist> searchListModelList;


    public SearchAdapter(Context context,List<Searchlist> searchListModelList) {
        this.context = context;
        this.searchListModelList = searchListModelList;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemSearchListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_search_list, viewGroup, false);
        return new SearchAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.binding.tvName.setText(searchListModelList.get(position).getName());

        viewHolder.binding.llWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductListActivity.class);
                intent.putExtra("from","SearchAdapter");
                intent.putExtra(GlobalVariables.section_name,searchListModelList.get(position).getName());
                intent.putExtra(GlobalVariables.subsubcatid,""+searchListModelList.get(position).getSubsubcatid());
                intent.putExtra(GlobalVariables.catid,searchListModelList.get(position).getCatid());
                intent.putExtra(GlobalVariables.filter_data,"");
                intent.putExtra(GlobalVariables.search,"");
                intent.putExtra(GlobalVariables.type,"");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchListModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemSearchListBinding binding;

        ViewHolder(ItemSearchListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
