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
    private List<Searchlist> list;


    public SearchAdapter(Context context,List<Searchlist> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SearchAdapter.ViewHolder(ItemSearchListBinding.inflate(LayoutInflater.from(context),viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.binding.tvName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemSearchListBinding binding;

        ViewHolder(ItemSearchListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.llWomen.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.llWomen:
                Intent intent=new Intent(context, ProductListActivity.class);
                intent.putExtra(GlobalVariables.catid,list.get(getAbsoluteAdapterPosition()).getCatid());
                intent.putExtra(GlobalVariables.subcatid,"");
                intent.putExtra(GlobalVariables.subsubcatid,""+list.get(getAbsoluteAdapterPosition()).getSubsubcatid());
                intent.putExtra(GlobalVariables.section_name,list.get(getAbsoluteAdapterPosition()).getName());
                context.startActivity(intent);    
                break;
            }
        }
    }
}
