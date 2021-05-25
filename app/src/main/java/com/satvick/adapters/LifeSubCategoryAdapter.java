package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.satvick.R;
import com.satvick.activities.LifeDescriptionActivity;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.AdapterLifeSubCategoryBinding;
import com.satvick.model.LifeTabBean;
import com.satvick.model.TabModel;

import java.util.List;

public class LifeSubCategoryAdapter  extends RecyclerView.Adapter<LifeSubCategoryAdapter.MyViewHolder> {
    private Context context;
    private List<LifeTabBean> list;

    public LifeSubCategoryAdapter(Context context,List<LifeTabBean> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public LifeSubCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LifeSubCategoryAdapter.MyViewHolder(AdapterLifeSubCategoryBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LifeSubCategoryAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.binding.ivProfile);
        holder.binding.tvTitle.setText(list.get(position).getTitle());
        holder.binding.tvDesc.setText(Html.fromHtml(list.get(position).getShort_desc()));
        if(list.get(position).getPayment_mode().equalsIgnoreCase("Paid")) holder.binding.btnPaymentMode.setText(SharedPreferenceWriter.getInstance(context).getString("symbol") +" "+Math.round(list.get(position).getPrice()));
        else holder.binding.btnPaymentMode.setText(list.get(position).getPayment_mode());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterLifeSubCategoryBinding binding;
        public MyViewHolder(@NonNull AdapterLifeSubCategoryBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

            binding.cvMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cvMain:
                Intent intent=new Intent(context, LifeDescriptionActivity.class);
                intent.putExtra("_id",list.get(getAdapterPosition()).getId());
                intent.putExtra("title",list.get(getAdapterPosition()).getTitle());
                context.startActivity(intent);
                break;
            }
        }
    }
}
