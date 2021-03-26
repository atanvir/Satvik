package com.satvick.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.satvick.R;
import com.satvick.databinding.AdapterPriceRangeBinding;

public class PriceRangeAdapter extends RecyclerView.Adapter<PriceRangeAdapter.MyViewHolder> {
    private Context context;
    private  PriceRangerClickListner listner;


    public PriceRangeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterPriceRangeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_price_range, parent, false);
        return new PriceRangeAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnRangeSeekbarChangeListener, OnRangeSeekbarFinalValueListener {
        AdapterPriceRangeBinding binding;

        public MyViewHolder(AdapterPriceRangeBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            binding.rangeSeekbar.setOnRangeSeekbarChangeListener(this);
            binding.rangeSeekbar.setOnRangeSeekbarFinalValueListener(this);

        }


        @Override
        public void valueChanged(Number minValue, Number maxValue) {
            binding.minValueText.setText("" + minValue);
            if(listner!=null)
            {
             listner.getPriceRange((long)minValue,(long)maxValue);
            }


            long max = (long) maxValue;

            if (max == 10000) {
                binding.maxValueText.setText("" + max + "+");
            } else {
                binding.maxValueText.setText("" + max);
            }
        }


        @Override
        public void finalValue(Number minValue, Number maxValue) {

            long max = (long) maxValue;
            if (max == 10000) {
                Log.e("final_range:", minValue + ":" + maxValue + "+");
            } else {

                Log.e("final_range:", minValue + ":" + maxValue);
            }


        }
    }


   public interface PriceRangerClickListner {
        void getPriceRange(long minValue, long maxValue);

    }

    public void setListner(PriceRangerClickListner listner)
    {
            this.listner=listner;
    }

}









