package com.satvick.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.databinding.ItemAddNewAddressBinding;
import com.satvick.model.ViewAddressModel;

import java.util.List;

public class AddNewAddressAdapter extends RecyclerView.Adapter<AddNewAddressAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    //int[] image;

    List<ViewAddressModel.Viewaddress> viewAddressModelList;

    AddressItemClickListener listener;

    public AddNewAddressAdapter(Context context, List<ViewAddressModel.Viewaddress> viewAddressModelList) {
        this.context = context;
        this.viewAddressModelList = viewAddressModelList;
        setHasStableIds(true);
    }

    public void deleteItem(int pos) {
        viewAddressModelList.remove(pos);
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AddNewAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        ItemAddNewAddressBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_add_new_address, viewGroup, false);
        return new AddNewAddressAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddNewAddressAdapter.ViewHolder viewHolder, int position) {


               if(viewAddressModelList.get(position).getRemark().equalsIgnoreCase("1")){

                   viewHolder.binding.tvDefaultAddress.setText("Default Address");
                   viewHolder.binding.tvRemoveDefault.setText("REMOVE");

                   viewHolder.binding.tvNameDefault.setText(viewAddressModelList.get(position).getName());
                   viewHolder.binding.tvAddressDefault.setText(viewAddressModelList.get(position).getAddress());
                   viewHolder.binding.tvPhoneDefault.setText(viewAddressModelList.get(position).getPhone());

               } else if(viewAddressModelList.get(position).getRemark().equalsIgnoreCase("2")){

                   viewHolder.binding.tvDefaultAddress.setText("Other Address");

                   viewHolder.binding.tvRemoveDefault.setText("SET AS DEFAULT");
                   viewHolder.binding.tvNameDefault.setText(viewAddressModelList.get(position).getName());
                   viewHolder.binding.tvAddressDefault.setText(viewAddressModelList.get(position).getAddress());
                   viewHolder.binding.tvPhoneDefault.setText(viewAddressModelList.get(position).getPhone());
               }


    }


    @Override
    public int getItemCount() {
        return viewAddressModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemAddNewAddressBinding binding;

        ViewHolder(ItemAddNewAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


            binding.tvRemoveDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener != null)
                        listener.onRemoveItemClick(view, getAdapterPosition());
                }
            });

            binding.tvEditDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener != null)
                        listener.onEditItemClick(view, getAdapterPosition());
                }
            });


        }

    }


    public interface AddressItemClickListener {
        void onRemoveItemClick(View view, int pos);
        void onEditItemClick(View view, int pos);
    }


    public void setListener(AddressItemClickListener listener) {
        this.listener = listener;
    }
}
