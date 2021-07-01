package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.satvick.R;
import com.satvick.activities.AddressActivity;
import com.satvick.databinding.AdapterAddressBinding;
import com.satvick.model.ViewAddressModel;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {
    private AppCompatActivity context;
    private List<ViewAddressModel.Viewaddress> list;
    private setOnAddressClick listner;
    private Boolean isRemove;


    public AddressAdapter(AppCompatActivity context, List<ViewAddressModel.Viewaddress> list, setOnAddressClick listner, Boolean isRemove){
        this.context=context;
        this.list=list;
        this.listner=listner;
        this.isRemove=isRemove;
    }

    @NonNull
    @Override
    public AddressAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressAdapter.MyViewHolder(AdapterAddressBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.MyViewHolder holder, int position) {
        if(isRemove) {
            if(list.get(position).getRemark().equals("0")) holder.binding.tvAddNewAddress.setText("Remove");
            else holder.binding.tvAddNewAddress.setText("Remove");
        }else{
            if(list.get(position).getRemark().equals("0")) holder.binding.cbDefaultAddress.setVisibility(View.VISIBLE);
            else holder.binding.cbDefaultAddress.setVisibility(View.GONE);
        }

        holder.binding.tvName.setText(list.get(position).getName());
        holder.binding.tvAddress.setText(list.get(position).getAddress());
        holder.binding.tvContactNumber.setText(list.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AdapterAddressBinding binding;
        public MyViewHolder(@NonNull AdapterAddressBinding binding) {
            super(binding.getRoot());
            init(binding);
            intCtrl();
        }

        private void init(AdapterAddressBinding binding) {
            this.binding=binding;
        }

        private void intCtrl() {
            binding.rlMain.setOnClickListener(this);
            binding.tvEdit.setOnClickListener(this);
            binding.tvAddNewAddress.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
            case R.id.rlMain: listner.onAddressClick(getAbsoluteAdapterPosition()); break;
            case R.id.tvEdit: laucherIntent(); break;
            case R.id.tvAddNewAddress:
            if(isRemove) listner.onAddressRemove(getBindingAdapterPosition());
            else {
                Intent intent = new Intent(context, AddressActivity.class);
                context.startActivityForResult(intent, 12);
            }
            break;
            }
        }


        private void laucherIntent() {
            Intent intent= new Intent(context, AddressActivity.class);
            intent.putExtra("data",list.get(getBindingAdapterPosition()));
            context.startActivityForResult(intent,21);
        }
    }



    public interface setOnAddressClick{
        void onAddressClick(int pos);
        void onAddressRemove(int pos);
    }

}
