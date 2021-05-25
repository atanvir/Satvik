package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.satvick.R;
import com.satvick.activities.LifeCategoryActivity;
import com.satvick.databinding.AdapterLifeCategoryTabBinding;
import com.satvick.model.LifeResponseModel;

import java.util.List;

public class LifeCategoryTabAdapter extends RecyclerView.Adapter<LifeCategoryTabAdapter.MyViewHolder> {
    private Context context;
    private List<LifeResponseModel.Category> list;


    public LifeCategoryTabAdapter(Context context, List<LifeResponseModel.Category> list){
        this.context=context;
        this.list=list;
    }


    @NonNull
    @Override
    public LifeCategoryTabAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AdapterLifeCategoryTabBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LifeCategoryTabAdapter.MyViewHolder holder, int position) {
        holder.binding.tvName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterLifeCategoryTabBinding binding;

        public MyViewHolder(@NonNull AdapterLifeCategoryTabBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

            binding.clCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.clCardView:
                Intent intent=new Intent(context, LifeCategoryActivity.class);
                intent.putExtra("_id",list.get(getAdapterPosition()).getId());
                intent.putExtra("title",list.get(getAdapterPosition()).getName());
                context.startActivity(intent);
                break;
            }

        }
    }
}
